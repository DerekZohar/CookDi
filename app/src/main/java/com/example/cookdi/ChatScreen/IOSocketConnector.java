package com.example.cookdi.ChatScreen;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class IOSocketConnector {
    String SERVER_NAME="https://cookdi.herokuapp.com/";

    String senderId;
    Socket ioSocket;

    IOSocketConnector(String server,String senderId){
        this.senderId=senderId;

        try{
            ioSocket= IO.socket(server);
        }
        catch (URISyntaxException e){

        }

        ioSocket.connect();
        ioSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            //On connect, send a message to join room
            @Override
            public void call(Object... args) {


                JSONObject message = new JSONObject();

                try {
                    message.put("senderId", "123");
                    message.put("recipientId", "123");
                    message.put("messageContent", "123");

                    ioSocket.emit("start",message.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        ioSocket.on("receiveMessage", new Emitter.Listener() {

            //On connect, send a message to join room
            @Override
            public void call(Object... args) {
                try {
                    JSONObject message = new JSONObject((String)args[0]);
                    Log.d("123", message.getString("messageContent"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    public void SendMessage(String receiverId,String content){
        JSONObject message = new JSONObject();

        try {
            message.put("senderId", senderId);
            message.put("recipientId", receiverId);
            message.put("messageContent", content);

            ioSocket.emit("sendMessage",message.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
