package com.mysasse.afyasmart.ui.fragments.posts;

import android.os.Bundle;
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
import com.mysasse.afyasmart.R;

public class PostFragment extends Fragment {

    private RecyclerView postsRecyclerView;

    public PostFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.post_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        PostViewModel mViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        mViewModel.getAllPost().observe(getViewLifecycleOwner(), posts -> {
            PostAdapter postAdapter = new PostAdapter(posts);

            postsRecyclerView.setAdapter(postAdapter);

        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Register Views
        postsRecyclerView = view.findViewById(R.id.post_recycler_view);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postsRecyclerView.setHasFixedSize(true);

        FloatingActionButton addPostFab = view.findViewById(R.id.add_post_fab);
        addPostFab.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.addPostFragment));
    }
}
