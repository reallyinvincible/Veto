package com.reallyinvincible.veto.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.reallyinvincible.veto.EncryptedMessageAdapter;
import com.reallyinvincible.veto.bottomfragments.BottomSheetSendMessage;
import com.reallyinvincible.veto.SendMessageInterface;
import com.reallyinvincible.veto.bottomfragments.BottomSheetKeyFragment;
import com.reallyinvincible.veto.R;
import com.reallyinvincible.veto.models.EncryptedMessage;
import com.reallyinvincible.veto.models.SecureRoom;
import com.reallyinvincible.veto.utilities.EncryptionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    BottomAppBar bottomAppBar;
    FirebaseDatabase mDatabase;
    DatabaseReference mRoomReference;
    public static SendMessageInterface sendMessageInterface;
    private SecureRoom secureRoom;
    private List<EncryptedMessage> messages;
    private String username;
    private BottomSheetSendMessage bottomSheetSendMessage;
    private RecyclerView messageRecyclerView;
    private ChildEventListener childEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("Details", MODE_PRIVATE);
        username = sharedPreferences.getString("Username", "Anonymous");
        secureRoom = gson.fromJson(sharedPreferences.getString("SecureRoom", null), SecureRoom.class);
        messages = secureRoom.getMessages();

        mDatabase = FirebaseDatabase.getInstance();
        mRoomReference = mDatabase.getReference().child("Rooms").child(secureRoom.getRoomId());

        messageRecyclerView = findViewById(R.id.rv_messages);
        if (messages.size() != 0){
            messageRecyclerView.setAdapter(new EncryptedMessageAdapter(messages));
        }

        bottomAppBar = findViewById(R.id.bottom_bar);
        bottomAppBar.replaceMenu(R.menu.bottom_bar_menu);
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetKeyFragment bottomSheetKeyFragment = new BottomSheetKeyFragment();
                bottomSheetKeyFragment.show(getSupportFragmentManager(), "KeyFragment");
            }
        });
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_item_share_key){
                    startActivity(new Intent(HomeActivity.this, SoundShareActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.menu_item_receive_key){
                    startActivity(new Intent(HomeActivity.this, DemoActivity.class));
                    return true;
                }
                return false;
            }
        });

        sendMessageInterface = new SendMessageInterface() {
            @Override
            public void sendMessage(String message) {
                bottomSheetSendMessage.dismiss();
                addMessage(message);
            }
        };

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMessageDialogue();
            }
        });

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                EncryptedMessage encryptedMessage = dataSnapshot.getValue(EncryptedMessage.class);
                messages.add(encryptedMessage);
                messageRecyclerView.setAdapter(new EncryptedMessageAdapter(messages));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mRoomReference.child("messages").addChildEventListener(childEventListener);

    }

    void addMessage(String message){
        EncryptionUtils.createKey("ThisIsASecretKey", getApplicationContext());
        String cipherText = EncryptionUtils.encrypt(message);
        String dateStamp = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String timeStamp = new SimpleDateFormat("HH:mm").format(new Date());
        EncryptedMessage encryptedMessage = new EncryptedMessage(username, cipherText, dateStamp, timeStamp);
        if (messages == null || messages.size() == 0){
            messages  = new ArrayList<EncryptedMessage>();
        }
        List<EncryptedMessage> messageList = messages;
        messageList.add(encryptedMessage);
        secureRoom.setMessages(messageList);
        mRoomReference.setValue(secureRoom);
    }

    void openMessageDialogue(){
        bottomSheetSendMessage = new BottomSheetSendMessage();
        bottomSheetSendMessage.show(getSupportFragmentManager(), "SendMessage");
    }

    public static SendMessageInterface getSendMessageInterface() {
        return sendMessageInterface;
    }

}
