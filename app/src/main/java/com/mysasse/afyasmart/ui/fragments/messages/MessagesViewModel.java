package com.mysasse.afyasmart.ui.fragments.messages;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mysasse.afyasmart.data.models.Chat;
import com.mysasse.afyasmart.data.repositories.ChatRepository;

import java.util.List;

public class MessagesViewModel extends ViewModel implements ChatRepository.ChatTaskListener {
    private static final String TAG = "MessagesViewModel";

    private MutableLiveData<List<Chat>> _chats;
    private ChatRepository chatRepository;

    public MessagesViewModel() {
        _chats = new MutableLiveData<>();
        chatRepository = new ChatRepository(this);
    }

    public LiveData<List<Chat>> getChats() {

        chatRepository.getChatsFromFirebase();

        return _chats;

    }

    @Override
    public void showChats(List<Chat> chatList) {
        _chats.setValue(chatList);
    }

    @Override
    public void showError(Exception exception) {
        Log.e(TAG, "showError: ", exception);
    }
}
