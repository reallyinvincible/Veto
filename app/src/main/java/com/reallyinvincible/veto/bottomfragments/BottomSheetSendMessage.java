package com.reallyinvincible.veto.bottomfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.reallyinvincible.veto.R;
import com.reallyinvincible.veto.SendMessageInterface;
import com.reallyinvincible.veto.activities.HomeActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BottomSheetSendMessage extends BottomSheetDialogFragment {

    SendMessageInterface sendMessageInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_send_message, container, false);
        sendMessageInterface = HomeActivity.getSendMessageInterface();
        final EditText messageEditText = view.findViewById(R.id.et_message_input);
        view.findViewById(R.id.btn_encrypt_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageEditText.getText().toString();
                sendMessageInterface.sendMessage(message);
            }
        });
        return view;
    }
}
