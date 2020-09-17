package com.mysasse.afyasmart.ui.fragments.diseases.details.info;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.models.Disease;

public class DiseaseInfoFragment extends Fragment {

    private static final String DISEASE_ARGS_KEY = "disease";

    private Disease mDisease;

    private DiseaseInfoViewModel mViewModel;

    public static DiseaseInfoFragment newInstance(Disease disease) {
        DiseaseInfoFragment instance = new DiseaseInfoFragment();

        Bundle args = new Bundle();
        args.putSerializable(DISEASE_ARGS_KEY, disease);

        instance.setArguments(args);

        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDisease = (Disease) requireArguments().get(DISEASE_ARGS_KEY);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.disease_info_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView diseaseNameTextView = view.findViewById(R.id.disease_name_text_view);
        diseaseNameTextView.setText(mDisease.getName());

        TextView diseaseDescriptionTextView = view.findViewById(R.id.disease_description_text_view);
        diseaseDescriptionTextView.setText(mDisease.getDescription());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DiseaseInfoViewModel.class);
    }

}