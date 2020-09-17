package com.mysasse.afyasmart.ui.fragments.diseases.details.measures;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

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
import com.mysasse.afyasmart.data.models.DiseaseMeasure;
import com.mysasse.afyasmart.utils.UIHelpers;

import static com.mysasse.afyasmart.data.Constants.MEASURES_NODE;

public class AddDiseaseMeasureFragment extends Fragment {

    private static final String TAG = "AddDiseaseMeasureFrag";

    private TextInputEditText bodyField;
    private Button addMeasureButton;
    private Group addMeasureGroup;

    private Disease mDisease;

    private FirebaseUser mCurrentUser;
    private FirebaseFirestore mDb;
    private String mType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDisease = AddDiseaseMeasureFragmentArgs.fromBundle(requireArguments()).getDisease();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_disease_measure, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDb = FirebaseFirestore.getInstance();

        RadioGroup measureTypeRadioGroup = view.findViewById(R.id.measure_type_radio_group);
        measureTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.curative_radio_button:
                    mType = "Curative";
                    break;
                case R.id.preventive_radio_button:
                    mType = "Preventive";
                    break;
                default:
                    mType = null;
            }
        });

        bodyField = view.findViewById(R.id.body_field);

        addMeasureButton = view.findViewById(R.id.add_measure_button);
        addMeasureGroup = view.findViewById(R.id.add_measure_group);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (((AppCompatActivity) requireActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setSubtitle(mDisease.getName());
        }

        addMeasureButton.setOnClickListener(view -> {
            String body = String.valueOf(bodyField.getText());

            if (!isValid(mType, body)) return;
            if (mCurrentUser == null) return;

            DiseaseMeasure diseaseMeasure = new DiseaseMeasure(mCurrentUser.getUid(), body, mType);

            addMeasure(diseaseMeasure);

        });
    }

    private void addMeasure(DiseaseMeasure diseaseMeasure) {
        addMeasureGroup.setVisibility(View.VISIBLE);
        mDb.collection(Constants.DISEASES_NODE).document(mDisease.getId())
                .collection(MEASURES_NODE).add(diseaseMeasure)
                .addOnSuccessListener(documentReference -> {
                    addMeasureGroup.setVisibility(View.INVISIBLE);
                    UIHelpers.toast(mDisease.getName() + " measure added");
                    requireActivity().onBackPressed();
                })
                .addOnFailureListener(e -> {
                    addMeasureGroup.setVisibility(View.INVISIBLE);
                    Log.e(TAG, "addMeasure: ", e);
                    UIHelpers.toast(e.getLocalizedMessage());
                });
    }

    private boolean isValid(String type, String body) {

        if (body.isEmpty()) {
            bodyField.setError("Body f the measure is required");
            bodyField.requestFocus();
            return false;
        }

        if (body.trim().length() < 50) {
            bodyField.setError("Write a verbose explanation of the measure in at least 50 characters");
            bodyField.requestFocus();
            return false;
        }

        if (type == null) {
            UIHelpers.toast("Select the type of the measure from the available options");
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