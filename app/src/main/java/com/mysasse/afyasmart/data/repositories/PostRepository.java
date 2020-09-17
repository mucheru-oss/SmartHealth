package com.mysasse.afyasmart.data.repositories;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mysasse.afyasmart.data.models.Post;

import java.util.List;

public class PostRepository {

    private FirebaseFirestore mDatabase;
    private static final String TAG = "PostRepository";
    private PostTaskListener listener;

    public PostRepository(PostTaskListener listener) {
        this.listener = listener;
        mDatabase = FirebaseFirestore.getInstance();
    }

    public void getPostsFromFirebase() {
        mDatabase.collection("posts")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {

                    if (e != null) {
                        Log.e(TAG, "getPostsFromFirebase: ", e);
                        return;
                    }
                    assert queryDocumentSnapshots != null;
                    listener.yieldPosts(queryDocumentSnapshots.toObjects(Post.class));

                });
    }

    public interface PostTaskListener {
        void yieldPosts(List<Post> posts);
    }
}
