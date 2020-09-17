package com.mysasse.afyasmart.ui.fragments.posts;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.models.Post;
import com.mysasse.afyasmart.data.models.Profile;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private static final String TAG = "PostAdapter";
    private List<Post> posts;
    private FirebaseFirestore mDatabase;

    public PostAdapter(List<Post> posts) {

        mDatabase = FirebaseFirestore.getInstance();
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_post, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.postTitleTv.setText(post.getTitle());
        holder.postExcerptTv.setText(post.getBody());

        //Set the post image using glide
        Glide.with(holder.postImageView)
                .load(post.getImage())
                .centerCrop()
                .placeholder(R.mipmap.noimage)
                .into(holder.postImageView);

        //Setting the Author related views
        mDatabase.collection("profiles").document(post.getUserUid())
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        Log.e(TAG, "onBindViewHolder: ", e);
                        return;
                    }

                    assert documentSnapshot != null;

                    Profile profile = documentSnapshot.toObject(Profile.class);
                    assert profile != null;

                    holder.postAuthorNameTv.setText(profile.getName());

                    Glide.with(holder.postAuthorAvatarCiv)
                            .load(profile.getAvatar())
                            .centerCrop()
                            .placeholder(R.drawable.ic_account_circle_black_48dp)
                            .into(holder.postAuthorAvatarCiv);

                });


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        CircleImageView postAuthorAvatarCiv;
        TextView postAuthorNameTv;
        ImageView postImageView;
        TextView postTitleTv;
        TextView postExcerptTv;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);

            postAuthorAvatarCiv = itemView.findViewById(R.id.post_author_avatar_civ);
            postAuthorNameTv = itemView.findViewById(R.id.post_author_name_tv);
            postImageView = itemView.findViewById(R.id.post_image_view);
            postTitleTv = itemView.findViewById(R.id.post_title_tv);
            postExcerptTv = itemView.findViewById(R.id.post_excerpt_tv);

        }
    }
}
