package com.reallyinvincible.veto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reallyinvincible.veto.models.EncryptedMessage;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EncryptedMessageAdapter extends RecyclerView.Adapter<EncryptedMessageAdapter.EncryptedMessageViewHolder> {

    List<EncryptedMessage> messageList;

    public EncryptedMessageAdapter(List<EncryptedMessage> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public EncryptedMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_encrypted_message, parent, false);
        return new EncryptedMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EncryptedMessageViewHolder holder, int position) {
        EncryptedMessage encryptedMessage = messageList.get(position);
        holder.messageTextView.setText(encryptedMessage.getEncryptedText());
        holder.nameTextView.setText(encryptedMessage.getUserName());
        holder.dateTextView.setText(encryptedMessage.getMessageDate());
        holder.timeTextView.setText(encryptedMessage.getMessageTime());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class EncryptedMessageViewHolder extends RecyclerView.ViewHolder{

        TextView messageTextView, nameTextView, dateTextView, timeTextView;

        EncryptedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.tv_message_view);
            nameTextView = itemView.findViewById(R.id.tv_name);
            dateTextView = itemView.findViewById(R.id.tv_date);
            timeTextView = itemView.findViewById(R.id.tv_time);
        }
    }

}
