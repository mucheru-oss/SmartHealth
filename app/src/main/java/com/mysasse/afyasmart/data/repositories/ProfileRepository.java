package com.mysasse.afyasmart.data.repositories;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mysasse.afyasmart.data.models.Profile;

public class ProfileRepository {
    private static final String TAG = "ProfileRepository";

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;
    private UserProfileListener listener;

    public ProfileRepository(UserProfileListener listener) {
        this.listener = listener;

        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
    }

    public void getAuthenticatedUserProfile() {

        String userUid = mAuth.getUid();
        assert userUid != null;

        mDb.collection("profiles")
                .document(userUid)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        Log.e(TAG, "getAuthenticatedUserProfile: ", e);
                        listener.showError(e);
                        return;
                    }

                    assert documentSnapshot != null;
                    Profile profile = documentSnapshot.toObject(Profile.class);
                    Log.d(TAG, "getAuthenticatedUserProfile: documentSnapshot: " + documentSnapshot);
                    listener.showProfile(profile);
                });
    }

    public interface UserProfileListener {

        void showProfile(Profile profile);

        void showError(Exception e);

    }
}
