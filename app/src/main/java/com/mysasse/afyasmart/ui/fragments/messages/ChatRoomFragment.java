package com.mysasse.afyasmart.ui.fragments.messages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.Constants;
import com.mysasse.afyasmart.data.models.Chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRoomFragment extends Fragment {
    private static final String TAG = "ChatRoomFragment";
    private String mReceiver = null;
    private String mSender = null;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;
    private EditText messageEt;
    private RecyclerView chatsRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getArguments() != null;
        mReceiver = ChatRoomFragmentArgs.fromBundle(getArguments()).getReceiver();

        //Get the current user
        mAuth = FirebaseAuth.getInstance();
        assert mAuth.getCurrentUser() != null;
        mSender = mAuth.getCurrentUser().getUid();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chat_room_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //Init fire-base instances
        mDb = FirebaseFirestore.getInstance();

        //Register the necessary views
        chatsRecyclerView = view.findViewById(R.id.chats_recycler_view);
        chatsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatsRecyclerView.setHasFixedSize(true);

        messageEt = view.findViewById(R.id.message_et);
        ImageButton sendMessageBtn = view.findViewById(R.id.send_message_ib);

        sendMessageBtn.setOnClickListener(v -> {
            String message = String.valueOf(messageEt.getText());

            if (TextUtils.isEmpty(message)) {
                messageEt.setError("You can not send an empty message");
                messageEt.requestFocus();
                return;
            }

            Map<String, Object> chatMap = new HashMap<>();

            chatMap.put("sender", mSender);
            chatMap.put("receiver", mReceiver);
            chatMap.put("message", message);
            chatMap.put("timestamp", FieldValue.serverTimestamp());

            pushChat(chatMap);
        });
    }

    private void pushChat(Map<String, Object> chatMap) {

        mDb.collection("chats")
                .add(chatMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Message sent", Toast.LENGTH_SHORT).show();
                        messageEt.setText("");
                        return;
                    }

                    Log.e(TAG, "pushChat: error", task.getException());
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDb.collection(Constants.PROFILES_NODE)
                .document(mReceiver)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        Log.e(TAG, "onActivityCreated: ", e);
                        return;
                    }

                    if (documentSnapshot != null) {
                        if (((AppCompatActivity) requireActivity()).getSupportActionBar() != null) {
                            ((AppCompatActivity) requireActivity()).getSupportActionBar().setSubtitle(documentSnapshot.getString("name"));
                        }
                    }
                });


        ChatRoomViewModel mViewModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        mViewModel.getChatListData().observe(getViewLifecycleOwner(), chatList -> showChatList(chatList));
    }

    private void showChatList(List<Chat> chatList) {

        List<Chat> twoUsersChatList = new ArrayList<>();
        for (Chat chat : chatList) {
            if (chat.getReceiver().equals(mReceiver) && chat.getSender().equals(mSender)) {
                twoUsersChatList.add(chat);
            }
            if (chat.getReceiver().equals(mSender) && chat.getSender().equals(mReceiver)) {
                twoUsersChatList.add(chat);
            }
        }

        ChatAdapter adapter = new ChatAdapter(twoUsersChatList);

        chatsRecyclerView.setAdapter(adapter);


    }

    @Override
    public void onPause() {
        super.onPause();


        if (((AppCompatActivity) requireActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setSubtitle(null);
        }

    }
}
