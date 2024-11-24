package com.example.lingol;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameEditText, emailEditText, passwordEditText, languageEditText;
    private Button registerButton;
    private FirebaseAuth auth;
    private DatabaseReference database;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar FirebaseAuth e DatabaseReference
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("Users");

        // Mapear os elementos do layout
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        languageEditText = findViewById(R.id.languageEditText);
        registerButton = findViewById(R.id.registerButton);

        // Configurar ação do botão de registro
        registerButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String language = languageEditText.getText().toString().trim();

            // Validação de campos
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || language.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Criar usuário no Firebase Auth
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Obter UID do usuário autenticado
                            String userId = auth.getCurrentUser().getUid();

                            // Criar objeto User com as informações
                            User user = new User(userId, name, email, language);

                            // Salvar usuário no Realtime Database
                            database.child(userId).setValue(user)
                                    .addOnCompleteListener(saveTask -> {
                                        if (saveTask.isSuccessful()) {
                                            Toast.makeText(this, "Registro bem-sucedido!", Toast.LENGTH_SHORT).show();

                                            // Navegar para a tela de lista de usuários
                                            startActivity(new Intent(this, UsersActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(this, "Erro ao salvar dados no banco de dados.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(this, "Erro no registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}


