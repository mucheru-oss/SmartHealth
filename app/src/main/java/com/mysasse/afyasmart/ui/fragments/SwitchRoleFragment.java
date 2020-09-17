package com.mysasse.afyasmart.ui.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.models.Profile;
import com.mysasse.afyasmart.utils.UIHelpers;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SwitchRoleFragment extends Fragment {
    private static final String TAG = "SwitchRoleFragment";
    private String userUid;

    private FirebaseFirestore mDatabase;
    private ProgressBar switchRoleProgressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userUid = SwitchRoleFragmentArgs.fromBundle(requireArguments()).getUserId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_switch_role, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Init firebase instances
        mDatabase = FirebaseFirestore.getInstance();

        //Init the roles autocomplete text view
        AutoCompleteTextView rolesAtv = view.findViewById(R.id.role_atv);
        String[] roles = getResources().getStringArray(R.array.roles);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.simple_dropdown_menu_popup_item, roles);
        rolesAtv.setAdapter(adapter);

        Button changeRoleButton = view.findViewById(R.id.change_role_btn);
        switchRoleProgressBar = view.findViewById(R.id.switch_role_progress_bar);

        changeRoleButton.setOnClickListener(v -> {

            String role = String.valueOf(rolesAtv.getText());

            //Validate the role

            if (role.trim().equals("Patient") || role.trim().equals("Doctor") || role.trim().equals("Admin")) {

                //Get the name of the user
                mDatabase.collection("profiles")
                        .document(userUid)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            Profile profile = documentSnapshot.toObject(Profile.class);
                            assert profile != null;
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
                            alertDialog.setTitle("Switching a users role");
                            alertDialog.setMessage("You are to about to switch " + profile.getName() + " to " + role);
                            alertDialog.setPositiveButton("Continue", ((dialog, which) -> {

                                Map<String, Object> profileUpdateMap = new HashMap<>();

                                profileUpdateMap.put("role", role);

                                updateUserProfile(profileUpdateMap);

                            }));
                            alertDialog.setNegativeButton("Cancel", ((dialog, which) -> UIHelpers.toast("Operation cancelled")));
                            alertDialog.show();
                        })
                        .addOnFailureListener(exception -> {
                            UIHelpers.toast("Failed to get the users profile, try again later");
                            Log.e(TAG, "onViewCreated: getting user", exception);
                        });

            } else {
                rolesAtv.setError("Please select a valid role from the available options");
                rolesAtv.requestFocus();
            }

        });

    }

    private void updateUserProfile(Map<String, Object> profileUpdateMap) {

        switchRoleProgressBar.setVisibility(View.VISIBLE);
        mDatabase.collection("profiles").document(userUid)
                .update(profileUpdateMap)
                .addOnCompleteListener(task -> {
                    switchRoleProgressBar.setVisibility(View.GONE);

                    if (task.isSuccessful()) {

                        UIHelpers.toast("Role successfully switched");
                        requireActivity().onBackPressed();
                        return;

                    }

                    Log.e(TAG, "updateUserProfile: failed", task.getException());
                    UIHelpers.toast("Changing the users role failed");
                });
    }
}
