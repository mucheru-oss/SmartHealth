package com.mysasse.afyasmart.ui.fragments.home;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.models.Disease;
import com.mysasse.afyasmart.ui.fragments.diseases.DiseaseAdapter;
import com.mysasse.afyasmart.utils.UIHelpers;

public class HomeFragment extends Fragment implements DiseaseAdapter.DiseaseItemListener {

    private RecyclerView diseasesRecyclerView;
    private NavController mNavController;

    private static final String TAG = "HomeFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNavController = Navigation.findNavController(view);

        diseasesRecyclerView = view.findViewById(R.id.diseases_recycler_view);
        diseasesRecyclerView.setHasFixedSize(true);
        diseasesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        //Set a vertical divider
        diseasesRecyclerView.addItemDecoration(
                new DividerItemDecoration(
                        requireContext(),
                        LinearLayoutManager.VERTICAL
                )
        );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        homeViewModel.getDiseases().observe(getViewLifecycleOwner(), diseases -> {
            DiseaseAdapter adapter = new DiseaseAdapter(diseases, this);
            diseasesRecyclerView.setAdapter(adapter);
        });


        homeViewModel.getException().observe(getViewLifecycleOwner(), exception -> {
            Log.e(TAG, "onActivityCreated: getting diseases", exception);
            UIHelpers.toast(exception.getLocalizedMessage());
        });
    }

    @Override
    public void onClick(Disease disease) {

        HomeFragmentDirections.ActionReadDisease action = HomeFragmentDirections.actionReadDisease(disease);
        mNavController.navigate(action);

    }
}
