package com.mysasse.afyasmart.ui.fragments.messages;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mysasse.afyasmart.data.models.Chat;
import com.mysasse.afyasmart.data.repositories.ChatRepository;

import java.util.List;

public class ChatRoomViewModel extends ViewModel implements ChatRepository.ChatTaskListener {
    private static final String TAG = "ChatRoomViewModel";

    private MutableLiveData<List<Chat>> _chatListData;
    private ChatRepository chatRepository;

    public ChatRoomViewModel() {
        _chatListData = new MutableLiveData<>();
        chatRepository = new ChatRepository(this);
    }

    public LiveData<List<Chat>> getChatListData() {
        chatRepository.getChatsFromFirebase();

        return _chatListData;
    }

    @Override
    public void showChats(List<Chat> chatList) {
        _chatListData.postValue(chatList);
    }

    @Override
    public void showError(Exception exception) {
        Log.e(TAG, "showError: of chats", exception);
    }
}
