package com.mysasse.afyasmart.ui.fragments.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.models.Profile;

public class AllUsersFragment extends Fragment implements UsersAdapter.UserItemClicked {

    private RecyclerView usersRecyclerView;

    public AllUsersFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.all_users_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Register the necessary views
        usersRecyclerView = view.findViewById(R.id.users_recycler_view);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        usersRecyclerView.setHasFixedSize(true);

        usersRecyclerView.addItemDecoration(
                new DividerItemDecoration(
                        requireContext(),
                        LinearLayoutManager.VERTICAL
                )
        );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AllUsersViewModel mViewModel = new ViewModelProvider(this).get(AllUsersViewModel.class);

        mViewModel.getProfiles().observe(getViewLifecycleOwner(), profileList -> {
            //Create the adapter and populate the list of profiles
            UsersAdapter adapter = new UsersAdapter(this, profileList);
            //Set the the adapter on the recycler view
            usersRecyclerView.setAdapter(adapter);
        });
    }

    @Override
    public void onClick(Profile profile) {
        AllUsersFragmentDirections.ActionAllUsersFragmentToSwitchRoleFragment action =
                AllUsersFragmentDirections.actionAllUsersFragmentToSwitchRoleFragment(profile.getId());

        Navigation.findNavController(usersRecyclerView).navigate(action);
    }
}
