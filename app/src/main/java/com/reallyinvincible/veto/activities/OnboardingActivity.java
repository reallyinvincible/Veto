package com.reallyinvincible.veto.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.reallyinvincible.veto.R;
import com.reallyinvincible.veto.SendMessageInterface;
import com.reallyinvincible.veto.models.EncryptedMessage;
import com.reallyinvincible.veto.models.SecureRoom;

import java.util.ArrayList;

public class OnboardingActivity extends AppCompatActivity {

    private EditText usernameEditText, roomIdEditText;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRoomIdReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        mDatabase = FirebaseDatabase.getInstance();
        mRoomIdReference = mDatabase.getReference().child("Rooms");
        usernameEditText = findViewById(R.id.et_user_name);
        roomIdEditText = findViewById(R.id.et_room_id);
        findViewById(R.id.btn_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterRoom();
            }
        });
    }

    void enterRoom(){
        final String username = usernameEditText.getText().toString();
        final String roomId = roomIdEditText.getText().toString();
        mRoomIdReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(roomId)){
                    SecureRoom secureRoom = dataSnapshot.child(roomId).getValue(SecureRoom.class);
                    Toast.makeText(OnboardingActivity.this, "Entering " + roomId + "!", Toast.LENGTH_SHORT).show();
                    saveDetails(username, secureRoom);
                } else {
                    SecureRoom secureRoom = new SecureRoom(roomId, roomId, new ArrayList<EncryptedMessage>());
                    mRoomIdReference.child(roomId).setValue(secureRoom);
                    Toast.makeText(OnboardingActivity.this, "Creating a new Room For You!", Toast.LENGTH_SHORT).show();
                    saveDetails(username, secureRoom);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void saveDetails(String userName, SecureRoom secureRoom){
        Gson gson = new Gson();
        String secureRoomString = gson.toJson(secureRoom);
        SharedPreferences sharedPreferences = getSharedPreferences("Details", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Username", userName);
        editor.putString("SecureRoom", secureRoomString);
        editor.apply();
        launchHome();
    }

    void launchHome(){
        Intent intent = new Intent(OnboardingActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}
