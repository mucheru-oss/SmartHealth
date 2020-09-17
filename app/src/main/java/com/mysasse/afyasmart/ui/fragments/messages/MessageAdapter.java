package com.mysasse.afyasmart.ui.fragments.messages;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.UserHolder> {
    private static final String TAG = "MessageAdapter";

    private List<String> users;
    private FirebaseFirestore mDatabase;
    private UserItemClicked itemClicked;

    public MessageAdapter(Set<String> users, UserItemClicked itemClicked) {
        this.itemClicked = itemClicked;
        mDatabase = FirebaseFirestore.getInstance();
        this.users = new ArrayList<>(users);
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {

        mDatabase.collection("profiles").document(users.get(position))
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        Log.e(TAG, "onBindViewHolder: ", e);
                        return;
                    }

                    assert documentSnapshot != null;

                    Profile profile = documentSnapshot.toObject(Profile.class);
                    assert profile != null;

                    holder.nameTv.setText(profile.getName());
                    holder.roleTv.setText(profile.getRole());

                    Glide.with(holder.userAvatarCiv)
                            .load(profile.getAvatar())
                            .centerCrop()
                            .placeholder(R.drawable.ic_account_circle_black_48dp)
                            .into(holder.userAvatarCiv);

                    holder.mView.setOnClickListener(v -> itemClicked.showChatRoom(profile));

                });


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserHolder extends RecyclerView.ViewHolder {

        View mView;
        CircleImageView userAvatarCiv;
        TextView nameTv;
        TextView roleTv;

        UserHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            userAvatarCiv = itemView.findViewById(R.id.user_avatar_civ);
            nameTv = itemView.findViewById(R.id.user_name_tv);
            roleTv = itemView.findViewById(R.id.user_role_tv);
        }
    }

    public interface UserItemClicked {

        void showChatRoom(Profile profile);
    }
}
