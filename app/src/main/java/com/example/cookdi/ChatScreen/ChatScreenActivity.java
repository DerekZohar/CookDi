package com.example.cookdi.ChatScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.cookdi.R;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.example.cookdi.ChatScreen.User;

import java.util.Date;
import java.util.HashMap;

public class ChatScreenActivity extends AppCompatActivity  implements MessagesListAdapter.SelectionListener,
        MessagesListAdapter.OnLoadMoreListener, MessagesListAdapter.OnMessageClickListener,MessageInput.InputListener,
        MessageInput.AttachmentsListener,
        MessageInput.TypingListener  {


    MessagesList messagesList;
    MessageInput input;
    MessagesListAdapter<Message> messageAdapter;

    public static void open(Context context) {
        Intent actMessages = new Intent(context, ChatScreenActivity.class);
        context.startActivity(actMessages);
////        actMessages.putExtra("remoteUserId", user.uuid);
////        actMessages.putExtra("remoteUserName", user.name);
////        context.startActivity(actMessages);
//        if (!ChatApplication.getInstance().getUserPeerConnections().containsKey(user.uuid)) {
//            RTCPeerConnectionWrapper wrapper = new RTCPeerConnectionWrapper(user.uuid, context);
//            wrapper.startDataChannel();
//            ChatApplication.getInstance().getUserPeerConnections().put(user.uuid, wrapper);
//            wrapper.state = ActivityState.IN;
//            wrapper.createOffer();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        messagesList=findViewById(R.id.chatMessagesList);
        input=findViewById(R.id.chatMessageInput);

        messageAdapter = new MessagesListAdapter<>("123", null);
        messagesList.setAdapter(messageAdapter);

        input.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                User usr= new User("123","Example Name","",false);
                Message msg=  new Message("123",usr,input.toString(),new Date());
                messageAdapter.addToStart(msg, true);
                return true;
            }
        });
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {

    }

    @Override
    public void onMessageClick(IMessage message) {

    }

    @Override
    public void onSelectionChanged(int count) {

    }

    @Override
    public void onAddAttachments() {

    }

    @Override
    public void onStartTyping() {

    }

    @Override
    public void onStopTyping() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onSubmit(CharSequence input) {
        return false;
    }
}