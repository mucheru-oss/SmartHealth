package com.mysasse.afyasmart.ui.fragments.diseases.details.measures;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.models.DiseaseMeasure;

import java.util.List;

public class DiseaseMeasuresAdapter extends RecyclerView.Adapter<DiseaseMeasuresAdapter.MeasureHolder> {

    private List<DiseaseMeasure> measures;

    public DiseaseMeasuresAdapter(List<DiseaseMeasure> measures) {
        this.measures = measures;
    }

    @NonNull
    @Override
    public MeasureHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MeasureHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.single_measure_item,
                                parent,
                                false
                        )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MeasureHolder holder, int position) {
        holder.bodyTextView.setText(measures.get(position).getBody());
        holder.typeTextView.setText(measures.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return measures.size();
    }

    static public class MeasureHolder extends RecyclerView.ViewHolder {

        TextView bodyTextView;
        TextView typeTextView;

        public MeasureHolder(@NonNull View itemView) {
            super(itemView);

            bodyTextView = itemView.findViewById(R.id.measure_body_text_view);
            typeTextView = itemView.findViewById(R.id.measure_type_text_view);
        }
    }
}
