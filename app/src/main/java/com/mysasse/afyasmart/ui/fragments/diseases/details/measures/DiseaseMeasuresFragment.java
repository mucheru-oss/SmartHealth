package com.mysasse.afyasmart.ui.fragments.diseases.details.measures;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.models.Disease;
import com.mysasse.afyasmart.utils.UIHelpers;

public class DiseaseMeasuresFragment extends Fragment {

    private static final String DISEASE_ARGS_KEY = "disease";

    private Disease mDisease;

    private RecyclerView diseaseMeasuresRecyclerView;

    public static DiseaseMeasuresFragment newInstance(Disease disease) {

        Bundle args = new Bundle();
        args.putSerializable(DISEASE_ARGS_KEY, disease);

        DiseaseMeasuresFragment fragment = new DiseaseMeasuresFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDisease = (Disease) requireArguments().get(DISEASE_ARGS_KEY);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        diseaseMeasuresRecyclerView = view.findViewById(R.id.disease_measures_recycler_view);

        diseaseMeasuresRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        diseaseMeasuresRecyclerView.setHasFixedSize(true);
        diseaseMeasuresRecyclerView.addItemDecoration(
                new DividerItemDecoration(
                        requireContext(),
                        LinearLayoutManager.VERTICAL
                )
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.disease_measures_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DiseaseMeasuresViewModelFactory factory = new DiseaseMeasuresViewModelFactory(mDisease);

        DiseaseMeasuresViewModel mViewModel = new ViewModelProvider(this, factory).get(DiseaseMeasuresViewModel.class);

        mViewModel.getDiseaseMeasures().observe(getViewLifecycleOwner(), diseaseMeasures -> {

            DiseaseMeasuresAdapter adapter = new DiseaseMeasuresAdapter(diseaseMeasures);
            diseaseMeasuresRecyclerView.setAdapter(adapter);

        });

        mViewModel.getException().observe(getViewLifecycleOwner(), e -> UIHelpers.toast(e.getLocalizedMessage()));
    }

}