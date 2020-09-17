package com.mysasse.afyasmart.ui.fragments.messages;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.models.Chat;
import com.mysasse.afyasmart.data.models.Profile;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessagesFragment extends Fragment implements MessageAdapter.UserItemClicked {

    private static final String TAG = "MessagesFragment";

    private FirebaseAuth mAuth;
    private RecyclerView messagesRecyclerView;

    public MessagesFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.messages_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MessagesViewModel mViewModel = new ViewModelProvider(this).get(MessagesViewModel.class);

        mViewModel.getChats().observe(getViewLifecycleOwner(), chatList -> processThreads(chatList));
    }

    private void processThreads(List<Chat> chatList) {
        Set<String> users = new HashSet<>();
        assert mAuth.getCurrentUser() != null;
        String currentUser = mAuth.getCurrentUser().getUid();

        for (Chat chat : chatList) {
            if (chat.getReceiver().equals(currentUser)) {
                users.add(chat.getSender());
            }
            if (chat.getSender().equals(currentUser)) {
                users.add(chat.getReceiver());
            }
        }

        MessageAdapter adapter = new MessageAdapter(users, this);
        messagesRecyclerView.setAdapter(adapter);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //Init fire-base instances
        mAuth = FirebaseAuth.getInstance();

        //Register the views
        messagesRecyclerView = view.findViewById(R.id.messages_threads_recycler_view);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        messagesRecyclerView.setHasFixedSize(true);

        FloatingActionButton startThreadFab = view.findViewById(R.id.start_thread_fab);

        startThreadFab.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.allChatUsersFragment));
    }

    @Override
    public void showChatRoom(Profile profile) {
        Log.d(TAG, "showChatRoom: profile id: " + profile.getId());
        MessagesFragmentDirections.ActionMessagesFragmentToChatRoomFragment action = MessagesFragmentDirections.actionMessagesFragmentToChatRoomFragment(profile.getId());
        Navigation.findNavController(messagesRecyclerView).navigate(action);

    }
}
