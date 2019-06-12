package com.example.chatbot.Model;

public class Chat {
    private String sender;
    private String message;

    public Chat(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public Chat() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
