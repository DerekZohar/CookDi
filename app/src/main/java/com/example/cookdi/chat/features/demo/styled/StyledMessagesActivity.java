package com.example.cookdi.chat.features.demo.styled;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.example.cookdi.chat.common.data.model.Message;
import com.example.cookdi.config.Config;
import com.example.cookdi.retrofit2.entities.User;
import com.example.cookdi.sharepref.SharePref;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.example.cookdi.R;
import com.example.cookdi.chat.common.data.fixtures.MessagesFixtures;
import com.example.cookdi.chat.features.demo.DemoMessagesActivity;
import com.stfalcon.chatkit.utils.DateFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.socket.emitter.Emitter;

public class StyledMessagesActivity extends DemoMessagesActivity
        implements MessageInput.InputListener,
        MessageInput.AttachmentsListener,
        DateFormatter.Formatter {

    UUID UUID;
//    Button submit;
//    EditText editText;
    ImageView imageView;

    Uri filePath;
    final int PICK_IMAGE_REQUEST = 71;
    final String TAG = "Attachment";
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    String imgURL;
    ImageButton backBtn;

    public static final String RECEIVE_MESSAGE="receiveMessage";
    public static final String MESSAGE_CONTENT="messageContent";
    public static void open(Context context, User user) {
        Intent intent =  new Intent(context, StyledMessagesActivity.class);
        intent.putExtra("userId",user.getId());
        intent.putExtra("userName",user.getName());
        intent.putExtra("userAvatar",user.getAvatar());

        context.startActivity(intent);
    }

    private MessagesList messagesList;
    public String senderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_styled_messages);

        Intent intent = getIntent();
        Integer a = intent.getIntExtra("userId",0);
        senderId = a.toString();

        messagesList = (MessagesList) findViewById(R.id.messagesList);
        initAdapter();
        imageView = new ImageView(getApplicationContext());
        MessageInput input = (MessageInput) findViewById(R.id.input);
        input.setInputListener(this);
        input.setAttachmentsListener(this);
        backBtn = findViewById(R.id.back_button);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if(senderId == null)
            return;

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ArrayList<Message> messages = Config.IOSocketChatConnector.GetUnloadedMessages(senderId);
        if(messages != null){
            for (int i = 0; i < messages.size(); i++) {
                Message m = messages.get(i);
                m.setText(m.getText().substring(4));
                messagesAdapter.addToStart(m,false);
            }
            Config.IOSocketChatConnector.ClearUnloadedMessages(senderId);

        }

        Config.IOSocketChatConnector.SetMessageActivity(this);
    }

    @Override
    public boolean onSubmit(CharSequence input) {
        final String[] _message = {input.toString()};
        String uuid= SharePref.getInstance(getApplicationContext()).getUuid();

        Log.d(TAG, senderId);

        Config.IOSocketChatConnector.SendMessage(senderId, "msg:" + _message[0]);


        messagesAdapter.addToStart(MessagesFixtures.getTextMessage(_message[0], "0"), true);

        return true;
    }

    public void ReceiveMessage(final Message message){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                System.out.println("____________" + isImgMsg(message.getText()));
                if(isImgMsg(message.getText()))
                {
                    messagesAdapter.addToStart(MessagesFixtures.getImageMessage(message.getText().substring(4), "1"), true);
                }
                else
                    messagesAdapter.addToStart(MessagesFixtures.getTextMessage(message.getText().substring(4), "1"), true);

            }
        });

    }
    private boolean isImgMsg(String txt){
        return txt.substring(0, 4).equals("img:");
    }
    @Override
    public void onAddAttachments() {
        chooseImage();
    }

    @Override
    public String format(Date date) {
        if (DateFormatter.isToday(date)) {
            return getString(R.string.date_header_today);
        } else if (DateFormatter.isYesterday(date)) {
            return getString(R.string.date_header_yesterday);
        } else {
            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
        }
    }

    private void initAdapter() {
        super.messagesAdapter = new MessagesListAdapter<>(super.senderId, super.imageLoader);
        super.messagesAdapter.enableSelectionMode(this);
        super.messagesAdapter.setLoadMoreListener(this);
        super.messagesAdapter.setDateHeadersFormatter(this);
        messagesList.setAdapter(super.messagesAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(1223);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                uploadImage();

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private void uploadImage() {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            UUID = UUID.randomUUID();
            final StorageReference ref = storageReference.child("images/"+ UUID.toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
//                            Toast.makeText(getApplicationContext(), "Sending ...", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imgURL = String.valueOf(uri);
                                    messagesAdapter.addToStart(MessagesFixtures.getImageMessage(imgURL, "0"), true);

                                    //send by socket
                                    String uuid= SharePref.getInstance(getApplicationContext()).getUuid();
                                    Log.d(TAG, senderId);
                                    String img = "img:" + imgURL;
                                    Config.IOSocketChatConnector.SendMessage(senderId, img);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Please check your internet connection and try again!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });

        }

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //Permission memory
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {
                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        Config.IOSocketChatConnector.UnsetMessageActivity();
    }

    @Override
    public void onStop() {
        super.onStop();
        Config.IOSocketChatConnector.UnsetMessageActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        Config.IOSocketChatConnector.SetMessageActivity(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Config.IOSocketChatConnector.SetMessageActivity(this);
    }
}
