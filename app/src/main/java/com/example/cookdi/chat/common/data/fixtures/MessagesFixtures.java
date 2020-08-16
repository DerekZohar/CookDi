package com.example.cookdi.chat.common.data.fixtures;

import com.example.cookdi.chat.common.data.model.Message;
import com.example.cookdi.chat.common.data.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/*
 * Created by troy379 on 12.12.16.
 */
public final class MessagesFixtures extends FixturesData {
    private MessagesFixtures() {
        throw new AssertionError();
    }

    public static Message getImageMessage(String url, String uuid) {
        Message message = new Message(getRandomId(), getUser(uuid), null);
        message.setImage(new Message.Image(url));
        return message;
    }

    public static Message getTextMessage(String text, String uuid) {
        return new Message(uuid, getUser(uuid), text);
    }

    private static User getUser(String id) {
        return new User(
                id,
                names.get(0),
                null,
                true);
        //return userList.getID(id);
    }
}
