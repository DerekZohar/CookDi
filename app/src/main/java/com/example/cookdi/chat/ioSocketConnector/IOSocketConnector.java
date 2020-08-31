package com.example.cookdi.chat.ioSocketConnector;

import android.util.Log;

import com.example.cookdi.chat.common.data.model.Message;
import com.example.cookdi.chat.common.data.model.User;
import com.example.cookdi.chat.features.demo.styled.StyledMessagesActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class IOSocketConnector {
    public static final String START_CHAT="start";
    public static final String RECEIVE_MESSAGE="receiveMessage";
    public static final String SEND_MESSAGE="sendMessage";

    public static final String MESSAGE_SENDER_ID="senderId";
    public static final String MESSAGE_RECIPIENT_ID="recipientId";
    public static final String MESSAGE_CONTENT="messageContent";

    public String senderId;
    public Socket ioSocket;
    public HashMap<String, ArrayList<Message>> messages;
    StyledMessagesActivity currentActivity;

    public IOSocketConnector(String server, final String senderId){
        this.senderId=senderId;

        try{
            ioSocket= IO.socket(server);
        }
        catch (URISyntaxException e){

        }

        messages = new HashMap<>();

        ioSocket.connect();
        ioSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            //On connect, send a message to join room
            @Override
            public void call(Object... args) {
                SendStartMessage();
            }
        });

        ioSocket.on(RECEIVE_MESSAGE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject message = new JSONObject((String)args[0]);
                    Log.d("ReceiveMessage", message.getString(MESSAGE_CONTENT));
                    ReceiveMessageInActivity(message.getString(MESSAGE_CONTENT),message.getString(MESSAGE_SENDER_ID));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    public void ReceiveMessageInActivity(String message,String senderId){
        Message rcvMess = new Message("0",new User("","","",true),message);

        if(currentActivity!= null && currentActivity.senderId.equals(senderId)){
            currentActivity.ReceiveMessage(rcvMess);
        }
        else{
            if(messages.containsKey(senderId)){
                ArrayList<Message> msg = messages.get(senderId);

                if(msg == null){
                    msg = new ArrayList<>();
                }

                msg.add(rcvMess);
            }

            else{
                ArrayList<Message> msg = new ArrayList<>();

                msg.add(rcvMess);

                messages.put(senderId,msg);
            }


        }
    }

    public ArrayList<Message> GetUnloadedMessages(String senderId){
        ArrayList<Message> msg = messages.get(senderId);

        return msg;
    }

    public void ClearUnloadedMessages(String senderId){
        ArrayList<Message> msg = messages.get(senderId);

        if(msg!= null){
            msg.clear();
        }
    }

    public void SendStartMessage(){

        JSONObject message = new JSONObject();

        try {
            message.put(MESSAGE_SENDER_ID, senderId);
            ioSocket.emit(START_CHAT,message.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void SetMessageActivity(StyledMessagesActivity activity){
        currentActivity = activity;
    }

    public void UnsetMessageActivity(){
        currentActivity = null;
    }

    public void SendMessage(String receiverId,String content){
        JSONObject message = new JSONObject();

        try {
            message.put(MESSAGE_SENDER_ID, senderId);
            message.put(MESSAGE_RECIPIENT_ID, receiverId);
            message.put(MESSAGE_CONTENT, content);

            ioSocket.emit(SEND_MESSAGE,message.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void ReceiveMessage(){
        ioSocket.on(RECEIVE_MESSAGE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject message = new JSONObject((String)args[0]);
                    Log.d("ReceiveMessage", message.getString(MESSAGE_CONTENT));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public Socket getIoSocket(){ return ioSocket;}
}
