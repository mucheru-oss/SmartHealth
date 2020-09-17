package com.mysasse.afyasmart.ui.fragments.doctors;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.models.Profile;
import com.mysasse.afyasmart.utils.UIHelpers;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.mysasse.afyasmart.data.Constants.DOCTORS_NODE;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.DoctorViewHolder> {
    private static final String TAG = "DoctorsAdapter";

    private List<Profile> profiles;
    private FirebaseFirestore mDb;

    public DoctorsAdapter(List<Profile> profiles) {

        mDb = FirebaseFirestore.getInstance();
        this.profiles = profiles;

    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DoctorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_doctor_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Profile profile = profiles.get(position);

        holder.doctorNameTv.setText(profile.getName());

        //Get the correct expertise from the doctors model
        mDb.collection(DOCTORS_NODE).document(profile.getId())
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        Log.e(TAG, "onBindViewHolder: ", e);
                        holder.doctorExpertiseTv.setText("Unknown Expert area");
                        return;
                    }

                    if (documentSnapshot != null) {
                        holder.doctorExpertiseTv.setText(documentSnapshot.getString("areaOfExpertise"));
                    } else {
                        holder.doctorExpertiseTv.setText("Unknown Expert area");
                    }
                });

        Glide.with(holder.doctorAvatarCiv)
                .load(profile.getAvatar())
                .centerCrop()
                .placeholder(R.drawable.ic_account_circle_black_48dp)
                .into(holder.doctorAvatarCiv);

    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    static class DoctorViewHolder extends RecyclerView.ViewHolder {

        CircleImageView doctorAvatarCiv;
        TextView doctorNameTv;
        TextView doctorExpertiseTv;

        DoctorViewHolder(@NonNull View itemView) {
            super(itemView);

            doctorAvatarCiv = itemView.findViewById(R.id.doctor_avatar_civ);
            doctorNameTv = itemView.findViewById(R.id.doctor_name_tv);
            doctorExpertiseTv = itemView.findViewById(R.id.doctor_area_of_expertise_tv);
        }
    }
}
