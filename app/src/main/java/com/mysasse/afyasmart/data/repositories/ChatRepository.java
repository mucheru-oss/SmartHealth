package com.mysasse.afyasmart.data.repositories;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mysasse.afyasmart.data.models.Chat;

import java.util.List;

public class ChatRepository {

    private static final String TAG = "ChatRepository";

    private FirebaseFirestore mDb;
    private ChatTaskListener listener;

    public ChatRepository(ChatTaskListener listener) {
        this.listener = listener;
        mDb = FirebaseFirestore.getInstance();
    }

    public void getChatsFromFirebase() {

        //Getting all chats from the fire-store
        mDb.collection("chats")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.e(TAG, "getChatsFromFirebase: failed", e);
                        listener.showError(e);
                        return;
                    }
                    assert queryDocumentSnapshots != null;
                    List<Chat> chats = queryDocumentSnapshots.toObjects(Chat.class);
                    listener.showChats(chats);
                });
    }

    public interface ChatTaskListener {

        void showChats(List<Chat> chatList);

        void showError(Exception exception);
    }
}
