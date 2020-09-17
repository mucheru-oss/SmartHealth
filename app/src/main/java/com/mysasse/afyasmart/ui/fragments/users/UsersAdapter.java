package com.mysasse.afyasmart.ui.fragments.users;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.models.Profile;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private UserItemClicked mUserItemClicked;
    private List<Profile> profileList;

    public UsersAdapter(UserItemClicked userItemClicked, List<Profile> profileList) {
        this.mUserItemClicked = userItemClicked;
        this.profileList = profileList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.userNameTv.setText(profileList.get(position).getName());
        holder.userRoleTv.setText(profileList.get(position).getRole());

        Glide.with(holder.userAvatarCiv)
                .load(profileList.get(position).getAvatar())
                .centerCrop()
                .placeholder(R.drawable.ic_account_circle_black_48dp)
                .into(holder.userAvatarCiv);

        holder.mView.setOnClickListener(v -> mUserItemClicked.onClick(profileList.get(position)));
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;
        CircleImageView userAvatarCiv;
        TextView userNameTv, userRoleTv;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            userAvatarCiv = itemView.findViewById(R.id.user_avatar_civ);
            userNameTv = itemView.findViewById(R.id.user_name_tv);
            userRoleTv = itemView.findViewById(R.id.user_role_tv);
        }
    }

    public interface UserItemClicked {
        void onClick(Profile profile);
    }
}
