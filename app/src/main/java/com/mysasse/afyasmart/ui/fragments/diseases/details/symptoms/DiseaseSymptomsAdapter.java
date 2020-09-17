package com.mysasse.afyasmart.ui.fragments.diseases.details.symptoms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.models.DiseaseSymptom;

import java.util.List;

public class DiseaseSymptomsAdapter extends RecyclerView.Adapter<DiseaseSymptomsAdapter.SymptomViewHolder> {

    private List<DiseaseSymptom> symptoms;

    public DiseaseSymptomsAdapter(List<DiseaseSymptom> symptoms) {
        this.symptoms = symptoms;
    }

    @NonNull
    @Override
    public SymptomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SymptomViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.single_symptom_item,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SymptomViewHolder holder, int position) {
        holder.bodyTextView.setText(symptoms.get(position).getBody());
        holder.periodTextView.setText(symptoms.get(position).getPeriod());
    }

    @Override
    public int getItemCount() {
        return symptoms.size();
    }

    static public class SymptomViewHolder extends RecyclerView.ViewHolder {
        TextView bodyTextView;
        TextView periodTextView;

        public SymptomViewHolder(@NonNull View itemView) {
            super(itemView);

            bodyTextView = itemView.findViewById(R.id.symptom_body_text_view);
            periodTextView = itemView.findViewById(R.id.symptom_period_text_view);
        }
    }
}
