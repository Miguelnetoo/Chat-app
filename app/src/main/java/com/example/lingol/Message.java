package com.example.lingol;

public class Message {
    private String senderId;
    private String messageText;
    private String timestamp;

    public Message() {
        // Construtor padrão necessário para o Firebase
    }

    public Message(String senderId, String messageText, String timestamp) {
        this.senderId = senderId;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getText() {
        return 0;
    }
}




