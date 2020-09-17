package com.mysasse.afyasmart.ui.fragments.diseases.details.symptoms;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.Constants;
import com.mysasse.afyasmart.data.models.Disease;
import com.mysasse.afyasmart.data.models.DiseaseSymptom;
import com.mysasse.afyasmart.utils.UIHelpers;

public class AddDiseaseSymptomFragment extends Fragment {
    private static final String TAG = "AddDiseaseSymptomFrag";

    private TextInputEditText periodField, bodyField;
    private Group addSymptomGroup;
    private Button addSymptomButton;

    private Disease mDisease;

    private FirebaseUser mCurrentUser;
    private FirebaseFirestore mDb;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDisease = AddDiseaseSymptomFragmentArgs.fromBundle(requireArguments()).getDisease();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_disease_symptom, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDb = FirebaseFirestore.getInstance();

        periodField = view.findViewById(R.id.period_field);
        bodyField = view.findViewById(R.id.body_field);
        addSymptomGroup = view.findViewById(R.id.add_symptom_group);
        addSymptomButton = view.findViewById(R.id.add_symptom_button);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (((AppCompatActivity) requireActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setSubtitle(mDisease.getName());
        }

        addSymptomButton.setOnClickListener(view -> {
            String period = String.valueOf(periodField.getText());
            String body = String.valueOf(bodyField.getText());

            //Validate The Input Fields
            if (!valid(period, body)) return;

            if (mCurrentUser == null) return;

            DiseaseSymptom diseaseSymptom = new DiseaseSymptom(mCurrentUser.getUid(), period, body);

            addSymptom(diseaseSymptom);

        });
    }

    private void addSymptom(DiseaseSymptom diseaseSymptom) {
        addSymptomGroup.setVisibility(View.VISIBLE);
        mDb.collection(Constants.DISEASES_NODE).document(mDisease.getId())
                .collection(Constants.SYMPTOMS_NODE).add(diseaseSymptom)
                .addOnSuccessListener(documentReference -> {
                    addSymptomGroup.setVisibility(View.INVISIBLE);

                    UIHelpers.toast(mDisease.getName() + " symptom successfully added");

                    requireActivity().onBackPressed();

                })
                .addOnFailureListener(exception -> {
                    addSymptomGroup.setVisibility(View.INVISIBLE);
                    Log.e(TAG, "addSymptom: ", exception);
                    UIHelpers.toast(exception.getLocalizedMessage());
                });
    }

    private boolean valid(String period, String body) {

        if (period.isEmpty()) {
            periodField.setError("Period is required");
            periodField.requestFocus();
            return false;
        }

        if (body.isEmpty()) {
            bodyField.setError("Body is required");
            bodyField.requestFocus();
            return false;
        }

        if (body.length() < 50) {
            bodyField.setError("I don't think you can explain a symptom in less that 50 words...");
            bodyField.requestFocus();
            return false;
        }

        return true;

    }

    @Override
    public void onPause() {
        super.onPause();


        if (((AppCompatActivity) requireActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setSubtitle(null);
        }

    }
}