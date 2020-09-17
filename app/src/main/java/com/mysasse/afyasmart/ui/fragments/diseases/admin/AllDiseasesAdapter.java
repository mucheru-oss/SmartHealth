package com.mysasse.afyasmart.ui.fragments.diseases.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.models.Disease;

import java.util.List;

public class AllDiseasesAdapter extends RecyclerView.Adapter<AllDiseasesAdapter.DiseaseViewHolder> {

    private List<Disease> diseaseList;
    private DiseaseItemListener listener;

    public AllDiseasesAdapter(List<Disease> diseaseList, DiseaseItemListener listener) {
        this.diseaseList = diseaseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DiseaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DiseaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_disease_admin, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DiseaseViewHolder holder, int position) {
        holder.nameTv.setText(trimToSizeIfLong(diseaseList.get(position).getName(), 15));
        holder.descriptionTv.setText(trimToSizeIfLong(diseaseList.get(position).getDescription(), 72));

        holder.moreVerticalImageButton.setOnClickListener(view -> {
            if (listener != null) {
                listener.onMoreOptionClicked(view, diseaseList.get(position));
            }
        });
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
        ImageButton moreVerticalImageButton;

        DiseaseViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.name_tv);
            descriptionTv = itemView.findViewById(R.id.description_tv);

            moreVerticalImageButton = itemView.findViewById(R.id.more_image_btn);
        }
    }

    public interface DiseaseItemListener {

        void onItemClick(Disease disease);

        void onMoreOptionClicked(View view, Disease disease);

    }
}
