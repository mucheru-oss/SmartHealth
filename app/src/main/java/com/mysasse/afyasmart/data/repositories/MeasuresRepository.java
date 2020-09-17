package com.mysasse.afyasmart.data.repositories;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mysasse.afyasmart.data.Constants;
import com.mysasse.afyasmart.data.models.Disease;
import com.mysasse.afyasmart.data.models.DiseaseMeasure;

import java.util.List;

import static com.mysasse.afyasmart.data.Constants.MEASURES_NODE;

public class MeasuresRepository {

    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private MeasuresTaskListener mListener;

    public MeasuresRepository(MeasuresTaskListener listener) {
        this.mListener = listener;
    }

    public void getDiseaseMeasures(Disease disease) {

        mDb.collection(Constants.DISEASES_NODE).document(disease.getId())
                .collection(MEASURES_NODE)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        mListener.onError(e);
                        return;
                    }

                    mListener.onBrowse(queryDocumentSnapshots.toObjects(DiseaseMeasure.class));
                });

    }


    public interface MeasuresTaskListener {
        void onBrowse(List<DiseaseMeasure> diseaseMeasures);

        void onError(Exception e);
    }
}
