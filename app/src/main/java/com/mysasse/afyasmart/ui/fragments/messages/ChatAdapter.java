package com.mysasse.afyasmart.ui.fragments.messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.models.Chat;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private static final int CHAT_LEFT_ITEM = 0, CHAT_RIGHT_ITEM = 1;

    private List<Chat> chatList;
    private FirebaseAuth mAuth;

    public ChatAdapter(List<Chat> chatList) {

        this.chatList = chatList;
        mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;

        if (viewType == CHAT_LEFT_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_left_message_item, parent, false);
        } else if (viewType == CHAT_RIGHT_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_right_message_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_right_message_item, parent, false);
        }


        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

        holder.messageTv.setText(chatList.get(position).getMessage());

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {

        assert mAuth.getCurrentUser() != null;

        String currentUser = mAuth.getCurrentUser().getUid();
        Chat chat = chatList.get(position);

        if (chat.getSender().equals(currentUser)) return CHAT_RIGHT_ITEM;
        if (chat.getReceiver().equals(currentUser)) return CHAT_LEFT_ITEM;

        return super.getItemViewType(position);
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView messageTv;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            messageTv = itemView.findViewById(R.id.message_tv);
        }
    }
}
