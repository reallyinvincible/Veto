package com.reallyinvincible.veto;

import android.content.Context;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

import Twofish.Twofish_Algorithm;

public class Utils {

    private static Object twoFishkey;
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static List<String> textToBlocks(String text, int blockSize) {

        String s = ".";
        if (blockSize == 32){
            s = "0";
        }

        List<String> blocks = new ArrayList<String>();
        int index = 0;
        while (index < text.length()) {
            blocks.add(text.substring(index, Math.min(index + blockSize, text.length())));
            index += blockSize;
        }

        String block = blocks.get(blocks.size()-1);
        if (block.length() % blockSize != 0){
            int remainder = block.length() % blockSize;
            String repeated = new String(new char[blockSize - remainder]).replace("\0", s);
            block += repeated;
            blocks.set(blocks.size()-1, block);
        }
        return  blocks;
    }

    public static void createKey(String key, Context context){

        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        try {
            Object cryptKey = Twofish_Algorithm.makeKey(keyBytes);
            Utils.setTwoFishkey(cryptKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
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

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String encyptBlock(String message){
        byte[] messageBytes =  message.getBytes(StandardCharsets.UTF_8);
        byte[] cipherBytes = Twofish_Algorithm.blockEncrypt(messageBytes, 0, Utils.getTwoFishkey());
        return Utils.bytesToHex(cipherBytes);
    }

    public static String decryptBlock(byte[] cipher){
        byte[] messageBytes = Twofish_Algorithm.blockDecrypt(cipher, 0, Utils.getTwoFishkey());
        return new String(messageBytes, StandardCharsets.UTF_8);
    }

    public static Object getTwoFishkey() {
        return twoFishkey;
    }

    public static void setTwoFishkey(Object twoFishkey) {
        Utils.twoFishkey = twoFishkey;
    }

}
