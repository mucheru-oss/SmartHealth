package com.mysasse.afyasmart.data.repositories;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mysasse.afyasmart.data.models.Disease;

import java.util.List;

public class DiseaseRepository {

    private DiseaseTaskListener diseaseTaskListener;

    private static final String TAG = "DiseaseRepository";

    private FirebaseFirestore mDb;

    public DiseaseRepository(DiseaseTaskListener diseaseTaskListener) {

        mDb = FirebaseFirestore.getInstance();

        this.diseaseTaskListener = diseaseTaskListener;
    }

    public void getAllDiseases() {

        mDb.collection("diseases")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.e(TAG, "getAllDiseases: Failed:", e);
                        diseaseTaskListener.showError(e);
                        return;
                    }
                    assert queryDocumentSnapshots != null;

                    Log.d(TAG, "getAllDiseases: Fetched: count => " + queryDocumentSnapshots.size());
                    List<Disease> diseaseList = queryDocumentSnapshots.toObjects(Disease.class);
                    diseaseTaskListener.showDiseases(diseaseList);
                });
    }


    public interface DiseaseTaskListener {
        void showDiseases(List<Disease> diseases);

        void showError(Exception exception);
    }
}
