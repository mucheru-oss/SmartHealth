package com.mysasse.afyasmart.ui.fragments.diseases.details;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.models.Disease;
import com.mysasse.afyasmart.ui.fragments.diseases.details.info.DiseaseInfoFragment;
import com.mysasse.afyasmart.ui.fragments.diseases.details.measures.DiseaseMeasuresFragment;
import com.mysasse.afyasmart.ui.fragments.diseases.details.symptoms.DiseaseSymptomsFragment;

public class DiseaseDetailsFragment extends Fragment {

    private static final String TAG = "DiseaseDetailsFragment";

    private Disease disease;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        disease = DiseaseDetailsFragmentArgs.fromBundle(requireArguments()).getDisease();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_disease_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, disease.getName());

        if (((AppCompatActivity) requireActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(disease.getName());
        }

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        ViewPager2 diseaseViewPager2 = view.findViewById(R.id.disease_view_pager);

        DiseaseStateAdapter diseaseStateAdapter = new DiseaseStateAdapter(this);

        diseaseStateAdapter.addFragments(
                DiseaseInfoFragment.newInstance(disease),
                DiseaseSymptomsFragment.newInstance(disease),
                DiseaseMeasuresFragment.newInstance(disease)
        );

        diseaseViewPager2.setAdapter(diseaseStateAdapter);

        new TabLayoutMediator(tabLayout, diseaseViewPager2, ((tab, position) -> {

            String title;

            switch (position) {
                case 0:
                    title = "Info";
                    break;
                case 1:
                    title = "Symptoms";
                    break;
                case 2:
                    title = "Measures";
                    break;
                default:
                    title = "None";
            }

            tab.setText(title);

        })).attach();

    }
}