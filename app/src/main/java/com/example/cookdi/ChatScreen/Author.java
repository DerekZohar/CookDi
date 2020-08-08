package com.example.cookdi.ChatScreen;

import com.stfalcon.chatkit.commons.models.IUser;

public class Author implements IUser {

    String id;
    String name;
    String avatar;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }
}
