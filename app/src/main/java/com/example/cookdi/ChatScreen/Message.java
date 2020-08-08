package com.example.cookdi.ChatScreen;

import com.stfalcon.chatkit.commons.models.IMessage;

import java.util.Date;

public class Message implements IMessage {

    String id;
    String text;
    User author;
    Date createdAt;

    public Message(String id, User user, String text, Date createdAt) {
        this.id = id;
        this.text = text;
        this.author = user;
        this.createdAt = createdAt;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public User getUser() {
        return author;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }
}
