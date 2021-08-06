package com.gautham.chatapp.Model;

public class Chats {
    private String sender;
    private String Receiver;
    private String message;
    private boolean isseen;

    public Chats(String sender, String receiver, String message) {
        this.sender = sender;
        this.Receiver = Receiver;
        this.message = message;
        this.isseen=isseen;
    }
    public Chats(){

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setReceiver(String Receiver) {
        this.Receiver = Receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }
}
