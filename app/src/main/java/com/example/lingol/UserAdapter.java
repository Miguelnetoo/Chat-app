package com.example.lingol;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList != null ? userList : new ArrayList<>();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        holder.nameTextView.setText(user.getName() != null ? user.getName() : "Nome indisponível");
        holder.emailTextView.setText(user.getEmail() != null ? user.getEmail() : "E-mail indisponível");
        holder.languageTextView.setText(user.getLanguage() != null ? user.getLanguage() : "Idioma indisponível");

        holder.sendMessageButton.setOnClickListener(v -> {
            Log.d("UserAdapter", "Abrindo ChatActivity para: " + user.getName());

            Intent intent = new Intent(holder.itemView.getContext(),ChatActivity.class);
            intent.putExtra("userId", user.getId());
            intent.putExtra("userName", user.getName());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView emailTextView;
        public TextView languageTextView;
        public ImageButton sendMessageButton;

        public UserViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.userName);
            emailTextView = itemView.findViewById(R.id.userEmail);
            languageTextView = itemView.findViewById(R.id.userLanguage);
            sendMessageButton = itemView.findViewById(R.id.sendMessageButton);
        }
    }
}


