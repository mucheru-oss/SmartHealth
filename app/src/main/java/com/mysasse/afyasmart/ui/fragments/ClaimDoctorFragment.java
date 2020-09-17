package com.mysasse.afyasmart.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.models.Doctor;
import com.mysasse.afyasmart.data.models.Notification;
import com.mysasse.afyasmart.utils.UIHelpers;

import static com.mysasse.afyasmart.data.Constants.DOCTORS_NODE;
import static com.mysasse.afyasmart.data.Constants.NOTIFICATIONS_NODE;

public class ClaimDoctorFragment extends Fragment {
    private static final String TAG = "ClaimDoctorFragment";

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;
    private Group sendingRequestGroup;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_claim_doctor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //Init fire-base instances
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();

        //Register necessary view
        TextInputEditText expertiseAreaField = view.findViewById(R.id.expertise_area_field);
        TextInputEditText topMostAwardField = view.findViewById(R.id.top_most_award_field);
        TextInputEditText trainedInstitutionField = view.findViewById(R.id.trained_institution_field);
        TextInputEditText currentlyWorkingWhereField = view.findViewById(R.id.currently_working_where_field);
        TextInputEditText professionHistoryField = view.findViewById(R.id.profession_history_field);

        Button sendRequestBtn = view.findViewById(R.id.send_request_btn);

        sendingRequestGroup = view.findViewById(R.id.sending_request_group);

        sendRequestBtn.setOnClickListener(v -> {

            String expertiseArea = String.valueOf(expertiseAreaField.getText());
            String topMostAward = String.valueOf(topMostAwardField.getText());
            String trainedInstitution = String.valueOf(trainedInstitutionField.getText());
            String currentlyWorkingWhere = String.valueOf(currentlyWorkingWhereField.getText());
            String professionHistory = String.valueOf(professionHistoryField.getText());

            //Field Validations

            if (TextUtils.isEmpty(expertiseArea)) {
                expertiseAreaField.setError("You must specify you area of expertise");
                expertiseAreaField.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(topMostAward)) {
                topMostAwardField.setError("Your top most achievement is required");
                topMostAwardField.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(trainedInstitution)) {
                trainedInstitutionField.setError("You must specify award institution");
                trainedInstitutionField.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(currentlyWorkingWhere)) {
                currentlyWorkingWhereField.setError("Which hospital are current working with or at?");
                currentlyWorkingWhereField.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(professionHistory)) {
                professionHistoryField.setError("Tell us a little about your professional life");
                professionHistoryField.requestFocus();
                return;
            }

            //Create a new doctor instance
            Doctor doctor = new Doctor(expertiseArea, topMostAward, currentlyWorkingWhere, trainedInstitution, professionHistory);

            //Get current user details
            assert mAuth.getCurrentUser() != null;

            sendingRequestGroup.setVisibility(View.VISIBLE);
            mDatabase.collection(DOCTORS_NODE).document(mAuth.getCurrentUser().getUid())
                    .set(doctor)
                    .addOnSuccessListener(aVoid -> {

                        Notification notification = new Notification(mAuth.getCurrentUser().getUid(), "Claim a doctor", "I am a doctor with expertise in " + expertiseArea + ", I need elevations please", "role_change");

                        addNotification(notification);
                    })
                    .addOnFailureListener(exception -> {
                        sendingRequestGroup.setVisibility(View.INVISIBLE);
                        UIHelpers.toast("Error while sending request");
                        Log.e(TAG, "onViewCreated: ", exception);
                    });

        });
    }

    private void addNotification(Notification notification) {
        mDatabase.collection(NOTIFICATIONS_NODE)
                .add(notification)
                .addOnCompleteListener(task -> {
                    sendingRequestGroup.setVisibility(View.GONE);
                    if (task.isSuccessful()) {

                        UIHelpers.toast("Request sent successfully");
                        requireActivity().onBackPressed();

                    } else {
                        Log.e(TAG, "addNotification: failed:", task.getException());
                        UIHelpers.toast("Request sending failed try again later");
                    }
                });
    }
}
