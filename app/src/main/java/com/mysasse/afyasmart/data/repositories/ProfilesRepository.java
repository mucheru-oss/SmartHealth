package com.mysasse.afyasmart.data.repositories;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mysasse.afyasmart.data.Constants;
import com.mysasse.afyasmart.data.models.Profile;

import java.util.List;

public class ProfilesRepository {

    private static final String TAG = "ProfilesRepository";

    private FirebaseFirestore mDatabase;
    private ProfilesTaskListener listener;

    public ProfilesRepository(ProfilesTaskListener listener) {
        mDatabase = FirebaseFirestore.getInstance();
        this.listener = listener;
    }

    public void getAllProfiles() {
        mDatabase.collection(Constants.PROFILES_NODE).addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e(TAG, "getAllProfiles: failed: ", e);
                listener.showErrors(e);
                return;
            }

            listener.showProfiles(queryDocumentSnapshots.toObjects(Profile.class));
        });
    }

    public void getDoctors() {
        mDatabase.collection(Constants.PROFILES_NODE)
                .whereEqualTo("role", "Doctor")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.e(TAG, "getAllProfiles: failed: ", e);
                        listener.showErrors(e);
                        return;
                    }

                    listener.showProfiles(queryDocumentSnapshots.toObjects(Profile.class));
                });
    }


    public interface ProfilesTaskListener {
        void showProfiles(List<Profile> profileList);

        void showErrors(Exception error);
    }
}
