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

import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.models.Profile;
import com.mysasse.afyasmart.ui.fragments.users.UsersAdapter;

public class AllChatUsersFragment extends Fragment implements UsersAdapter.UserItemClicked {

    private static final String TAG = "AllChatUsersFragment";

    private RecyclerView usersRecyclerView;

    public AllChatUsersFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.all_chat_users_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AllChatUsersViewModel mViewModel = new ViewModelProvider(this).get(AllChatUsersViewModel.class);

        mViewModel.getProfiles().observe(getViewLifecycleOwner(), profiles -> {
            UsersAdapter adapter = new UsersAdapter(this, profiles);
            usersRecyclerView.setAdapter(adapter);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usersRecyclerView = view.findViewById(R.id.all_users_recycler_view);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        usersRecyclerView.setHasFixedSize(true);

    }

    @Override
    public void onClick(Profile profile) {
        Log.d(TAG, "openMessages: profile: " + profile.getId());
        AllChatUsersFragmentDirections.ActionAllChatUsersFragmentToChatRoomFragment action = AllChatUsersFragmentDirections.actionAllChatUsersFragmentToChatRoomFragment(profile.getId());
        Navigation.findNavController(usersRecyclerView).navigate(action);
    }
}
