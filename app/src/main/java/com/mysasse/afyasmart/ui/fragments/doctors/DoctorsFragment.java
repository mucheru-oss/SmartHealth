package com.mysasse.afyasmart.ui.fragments.doctors;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.utils.UIHelpers;

public class DoctorsFragment extends Fragment {

    private RecyclerView recyclerView;
    private static final String TAG = "DoctorsFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.doctors_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //Register necessary views
        recyclerView = view.findViewById(R.id.doctors_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        //Adding a horizontal separation
        recyclerView.addItemDecoration(
                new DividerItemDecoration(
                        requireContext(),
                        LinearLayoutManager.VERTICAL
                )
        );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DoctorsViewModel mViewModel = new ViewModelProvider(this).get(DoctorsViewModel.class);

        mViewModel.getDoctors().observe(getViewLifecycleOwner(), profiles -> {

            DoctorsAdapter adapter = new DoctorsAdapter(profiles);
            recyclerView.setAdapter(adapter);
        });

        mViewModel.getException().observe(getViewLifecycleOwner(), exception -> {
            Log.e(TAG, "onActivityCreated: getting doctors", exception);
            UIHelpers.toast(exception.getLocalizedMessage());
        });

    }

}
