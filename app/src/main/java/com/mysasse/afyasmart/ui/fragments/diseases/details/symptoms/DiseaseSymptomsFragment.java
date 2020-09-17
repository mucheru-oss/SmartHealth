package com.mysasse.afyasmart.ui.fragments.diseases.details.symptoms;

import android.os.Bundle;
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
import com.mysasse.afyasmart.data.models.Disease;
import com.mysasse.afyasmart.utils.UIHelpers;

public class DiseaseSymptomsFragment extends Fragment {
    private static final String DISEASE_ARGS_KEY = "disease";

    private Disease mDisease;
    private RecyclerView diseaseSymptomsRecyclerView;

    public static DiseaseSymptomsFragment newInstance(Disease disease) {

        Bundle args = new Bundle();
        args.putSerializable(DISEASE_ARGS_KEY, disease);

        DiseaseSymptomsFragment fragment = new DiseaseSymptomsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDisease = (Disease) requireArguments().get(DISEASE_ARGS_KEY);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.disease_symptoms_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        diseaseSymptomsRecyclerView = view.findViewById(R.id.disease_symptoms_recycler_view);
        diseaseSymptomsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        diseaseSymptomsRecyclerView.setHasFixedSize(true);
        diseaseSymptomsRecyclerView.addItemDecoration(
                new DividerItemDecoration(
                        requireContext(),
                        LinearLayoutManager.VERTICAL
                )
        );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DiseaseSymptomsViewModelFactory factory = new DiseaseSymptomsViewModelFactory(mDisease);
        DiseaseSymptomsViewModel mViewModel = new ViewModelProvider(this, factory).get(DiseaseSymptomsViewModel.class);

        mViewModel.getDiseaseSymptoms().observe(getViewLifecycleOwner(), diseaseSymptoms -> {
            DiseaseSymptomsAdapter adapter = new DiseaseSymptomsAdapter(diseaseSymptoms);

            diseaseSymptomsRecyclerView.setAdapter(adapter);

        });

        mViewModel.getException().observe(getViewLifecycleOwner(), e -> UIHelpers.toast(e.getLocalizedMessage()));

    }

}