package com.mysasse.afyasmart.ui.fragments.diseases;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.models.Disease;

import static com.mysasse.afyasmart.data.Constants.DISEASES_NODE;

public class AddDiseaseFragment extends Fragment {
    private static final String TAG = "AddDiseaseFragment";

    private FirebaseFirestore mDatabase;

    private TextInputEditText nameTxt;
    private TextInputEditText descriptionTxt;
    private Group addDiseaseGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_disease, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameTxt = view.findViewById(R.id.name_txt);
        descriptionTxt = view.findViewById(R.id.description_txt);

        //Init fire-base instance
        mDatabase = FirebaseFirestore.getInstance();

        Button addDiseaseButton = view.findViewById(R.id.add_disease_button);

        addDiseaseGroup = view.findViewById(R.id.add_disease_group);

        addDiseaseButton.setOnClickListener(v -> {

            String name = String.valueOf(nameTxt.getText());
            String description = String.valueOf(descriptionTxt.getText());

            if (hasInvalidInputs(name, description)) return;

            addDiseaseGroup.setVisibility(View.VISIBLE);
            mDatabase.collection(DISEASES_NODE).add(new Disease(name, description)).addOnCompleteListener(
                    task -> {
                        addDiseaseGroup.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Product Added", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onViewCreated: Product Added");
                            assert getActivity() != null;
                            getActivity().onBackPressed();
                        } else {
                            Toast.makeText(getContext(), "Operation Failed", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onViewCreated: ", task.getException());
                        }
                    }
            );
        });
    }

    private boolean hasInvalidInputs(String name, String description) {

        //Check whether the name is empty
        if (TextUtils.isEmpty(name)) {
            nameTxt.setError("Name is required");
            nameTxt.requestFocus();
            return true;
        }

        //Check the length of the diseases name
        if (name.length() < 3) {
            nameTxt.setError("Name should be at least 3 characters");
            nameTxt.requestFocus();
            return true;
        }

        if (TextUtils.isEmpty(description)) {
            descriptionTxt.setError("Description is required");
            descriptionTxt.requestFocus();
            return true;
        }

        //Check the length of the diseases name
        if (description.length() < 100) {
            nameTxt.setError("Description should be at least 100 characters");
            nameTxt.requestFocus();
            return true;
        }

        return false;
    }
}
