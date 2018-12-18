package com.reallyinvincible.veto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;

import Twofish.Twofish_Algorithm;

public class HomeActivity extends AppCompatActivity {

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    Object twofishKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        String key = "ThisIsASecretKey";
        String message = "This is Veto. The Cypto Application";
        int numberOfBlocks = (int) Math.ceil(((double)message.length())/16);

        encyptBlock(message, key);
    }

    void createKey(String Key){

    }

    void encyptBlock(String message, String key){

        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] messageBytes =  message.getBytes(StandardCharsets.UTF_8);

        try {
            Object twofishKey = Twofish_Algorithm.makeKey(keyBytes);
            byte[] cipherBytes = Twofish_Algorithm.blockEncrypt(messageBytes, 0, twofishKey);
            String cipherHexText = bytesToHex(cipherBytes);
            Log.i("Ye dekho", String.valueOf(cipherBytes.length));
            ((TextView)findViewById(R.id.tv_encrypted_text)).setText(cipherHexText);
            decryptBlock(cipherBytes, key);

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    void decryptBlock(byte[] cipher, String key){

        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

        try {
            Object twofishKey = Twofish_Algorithm.makeKey(keyBytes);
            byte[] messageBytes = Twofish_Algorithm.blockDecrypt(cipher, 0, twofishKey);
            String messageText = new String(messageBytes, StandardCharsets.UTF_8);
            ((TextView)findViewById(R.id.tv_decrypted_text)).setText(messageText);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    void encrypt(){

    }

}
