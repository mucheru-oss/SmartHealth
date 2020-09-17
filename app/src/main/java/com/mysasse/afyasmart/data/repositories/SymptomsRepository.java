package com.mysasse.afyasmart.data.repositories;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mysasse.afyasmart.data.Constants;
import com.mysasse.afyasmart.data.models.Disease;
import com.mysasse.afyasmart.data.models.DiseaseSymptom;

import java.util.List;

public class SymptomsRepository {

    private FirebaseFirestore mDb;
    private SymptomsTaskListener mListener;

    public SymptomsRepository(SymptomsTaskListener listener) {
        mListener = listener;
        mDb = FirebaseFirestore.getInstance();
    }

    public void getDiseaseSymptoms(Disease disease) {
        mDb.collection(Constants.DISEASES_NODE).document(disease.getId())
                .collection(Constants.SYMPTOMS_NODE)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        mListener.onError(e);
                        return;
                    }

                    mListener.onBrowse(queryDocumentSnapshots.toObjects(DiseaseSymptom.class));

                });
    }

    public interface SymptomsTaskListener {

        void onBrowse(List<DiseaseSymptom> diseaseSymptoms);

        void onError(Exception e);

    }

}
