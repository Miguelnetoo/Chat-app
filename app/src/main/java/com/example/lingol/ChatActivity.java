package com.example.lingol;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    private EditText messageInput;
    private Button sendButton;
    private ListView chatListView;
    private ArrayList<String> chatMessages;
    private ArrayAdapter<String> chatAdapter;

    private DatabaseReference mDatabase;
    private String senderId = "user1"; // Usar ID de usuário autenticado, como FirebaseAuth.getUid()
    private String receiverId = "user2"; // ID do outro usuário no chat

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Inicializa os componentes da tela
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        chatListView = findViewById(R.id.chatListView);

        // Inicializa a lista de mensagens
        chatMessages = new ArrayList<>();

        // Adapter para exibir mensagens na ListView
        chatAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chatMessages);
        chatListView.setAdapter(chatAdapter);

        // Gera um identificador único para o chat entre os dois usuários
        String chatId = generateChatId(senderId, receiverId);

        // Referência ao Firebase Realtime Database, para a conversa específica
        mDatabase = FirebaseDatabase.getInstance().getReference("chats").child(chatId);

        // Configura o botão de envio
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageInput.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    // Criar a mensagem com o timestamp atual
                    String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    Message message = new Message(senderId, messageText, timestamp);

                    // Enviar a mensagem para o Firebase
                    mDatabase.push().setValue(message);

                    // Limpa o campo de entrada de mensagem
                    messageInput.setText("");

                    // Exibe uma confirmação
                    Toast.makeText(ChatActivity.this, "Mensagem enviada", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Listener para ler mensagens em tempo real
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                // Recupera a mensagem e a adiciona à lista
                Message message = dataSnapshot.getValue(Message.class);
                if (message != null) {
                    chatMessages.add(message.getSenderId() + ": " + message.getMessageText());
                    chatAdapter.notifyDataSetChanged();
                    chatListView.setSelection(chatMessages.size() - 1); // Rola para a última mensagem
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    // Função para gerar um identificador único para o chat entre dois usuários
    private String generateChatId(String senderId, String receiverId) {
        // Garante que o ID do chat será sempre o mesmo, independentemente da ordem dos usuários
        return senderId.compareTo(receiverId) < 0 ? senderId + "_" + receiverId : receiverId + "_" + senderId;
    }
}






