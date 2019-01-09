package com.reallyinvincible.veto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DemoActivity extends AppCompatActivity {

    private static final String TAG = "DemoActivity";
    private final String KEY = "ThisIsASecretKey";
    private Context context;
    EditText keyEdiText, messageEditText;
    TextView encyptedTextView, decryptedTextView;
    Button generateKeyButton, encryptButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        keyEdiText = findViewById(R.id.et_key);
        messageEditText = findViewById(R.id.et_message);
        encyptedTextView = findViewById(R.id.tv_encrypted_text);
        decryptedTextView = findViewById(R.id.tv_decrypted_text);
        generateKeyButton = findViewById(R.id.btn_generate_key);
        encryptButton = findViewById(R.id.btn_encrypt);

        context = this;

        generateKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyEdiText.setText(KEY);
                Utils.createKey(KEY, context);
            }
        });

        encryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageEditText.getText().toString();
                String key = keyEdiText.getText().toString();
                int keyLength = key.length();
                if (keyLength == 16 || keyLength == 8 || keyLength == 12) {
                    Utils.createKey(key, getApplicationContext());
                    encrypt(message);
                }
                else {
                    Toast.makeText(DemoActivity.this, "Please Enter A Valid Key", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    void encrypt(String message) {
        
        List<String> messageBlocks = Utils.textToBlocks(message, 16);
        List<String> cipherBlocks = new ArrayList<String>();

        StringBuilder encryptedText = new StringBuilder();

        for (int i = 0; i < messageBlocks.size(); i++){
            String block = messageBlocks.get(i);
            cipherBlocks.add(Utils.encyptBlock(block));
            encryptedText.append(cipherBlocks.get(i));
        }

        ((TextView)findViewById(R.id.tv_encrypted_text)).setText(encryptedText.toString());
        decrypt(encryptedText.toString());

    }
    
    void decrypt(String cipher) {

        List<String> cipherBlocks = Utils.textToBlocks(cipher, 32);
        List<String> messageBlocks = new ArrayList<String>();

        StringBuilder decryptedText = new StringBuilder();

        for (int i = 0; i < cipherBlocks.size(); i++){
            String block = cipherBlocks.get(i);
            messageBlocks.add(Utils.decryptBlock(Utils.hexStringToByteArray(block)));
            decryptedText.append(messageBlocks.get(i));
        }

        ((TextView)findViewById(R.id.tv_decrypted_text)).setText(decryptedText.toString());

    }
    


}
