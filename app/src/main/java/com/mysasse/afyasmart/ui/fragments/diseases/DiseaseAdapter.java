package com.mysasse.afyasmart.ui.fragments.diseases;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.Constants;
import com.mysasse.afyasmart.data.models.Disease;

import java.util.List;

import static com.mysasse.afyasmart.data.Constants.MEASURES_NODE;

public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder> {
    private static final String TAG = "DiseaseAdapter";

    private List<Disease> diseaseList;
    private DiseaseItemListener listener;
    private FirebaseFirestore mDb;

    public DiseaseAdapter(List<Disease> diseaseList, DiseaseItemListener listener) {
        this.diseaseList = diseaseList;
        this.listener = listener;

        mDb = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public DiseaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DiseaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_disease, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DiseaseViewHolder holder, int position) {
        holder.nameTv.setText(trimToSizeIfLong(diseaseList.get(position).getName(), 15));
        holder.descriptionTv.setText(trimToSizeIfLong(diseaseList.get(position).getDescription(), 72));

        mDb.collection(Constants.DISEASES_NODE).document(diseaseList.get(position).getId())
                .collection(Constants.SYMPTOMS_NODE)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.e(TAG, "onBindViewHolder: ", e);
                        return;
                    }

                    if (queryDocumentSnapshots != null) {
                        holder.symptomsCountTv.setText(String.format("%s symptoms", queryDocumentSnapshots.size()));
                    } else {
                        holder.symptomsCountTv.setText(String.format("%s symptoms", 0));
                    }
                });

        mDb.collection(Constants.DISEASES_NODE).document(diseaseList.get(position).getId())
                .collection(MEASURES_NODE)
                .addSnapshotListener(((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.e(TAG, "onBindViewHolder: ", e);
                        return;
                    }

                    if (queryDocumentSnapshots != null) {
                        holder.measuresCountTv.setText(String.format("%s measures", queryDocumentSnapshots.size()));
                    } else {
                        holder.measuresCountTv.setText(String.format("%s measures", 0));
                    }
                }));

        holder.mView.setOnClickListener(view -> listener.onClick(diseaseList.get(position)));
    }

    private String trimToSizeIfLong(String content, int size) {
        return (content.length() > size) ? content.substring(0, size) + "..." : content;
    }

    @Override
    public int getItemCount() {
        return diseaseList.size();
    }

    static class DiseaseViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv, descriptionTv;

        TextView symptomsCountTv, measuresCountTv;

        View mView;

        DiseaseViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

            nameTv = itemView.findViewById(R.id.name_tv);
            descriptionTv = itemView.findViewById(R.id.description_tv);

            symptomsCountTv = itemView.findViewById(R.id.symptoms_count_tv);
            measuresCountTv = itemView.findViewById(R.id.measures_count_tv);
        }
    }

    public interface DiseaseItemListener {

        void onClick(Disease disease);

    }
}
