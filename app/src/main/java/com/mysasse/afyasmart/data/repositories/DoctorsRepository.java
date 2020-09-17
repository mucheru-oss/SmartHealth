package com.mysasse.afyasmart.data.repositories;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mysasse.afyasmart.data.models.Doctor;
import com.mysasse.afyasmart.data.models.Profile;

import java.util.List;

import static com.mysasse.afyasmart.data.Constants.DOCTORS_NODE;

public class DoctorsRepository {

    private static final String TAG = "DoctorsRepository";

    private FirebaseFirestore mDb;
    private FirebaseAuth mAuth;
    private DoctorsTaskCompleteListener listener;

    //Currently authenticated user's uid
    private String uid;


    public DoctorsRepository(DoctorsTaskCompleteListener listener) {
        this.listener = listener;
        mDb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        assert mAuth.getCurrentUser() != null;

        uid = mAuth.getCurrentUser().getUid();
    }

    public void addDoctor(Doctor doctor) {
        mDb.collection(DOCTORS_NODE).document(uid).set(doctor)
                .addOnSuccessListener(documentSnapshot -> listener.onDoctorAdded())
                .addOnFailureListener(e -> listener.onError(e));
    }

    public void getDoctorsFromFirebase() {
        mDb.collection("profiles")
                .whereEqualTo("role", "Doctor")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.e(TAG, "getDoctorsFromFirebase: exception", e);
                        listener.onError(e);
                        return;
                    }

                    assert queryDocumentSnapshots != null;
                    listener.onComplete(queryDocumentSnapshots.toObjects(Profile.class));

                });
    }

    public interface DoctorsTaskCompleteListener {
        void onComplete(List<Profile> profiles);

        void onDoctorAdded();

        void onError(Exception error);
    }
}
