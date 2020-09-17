package com.mysasse.afyasmart.ui.fragments.posts;


import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.models.Post;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPostFragment extends Fragment {
    private static final String TAG = "AddPostFragment";

    private static final int SELECT_IMAGE_RC = 78;
    private ImageView postImageView;

    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private TextInputEditText titleTxt;
    private TextInputEditText bodyTxt;
    private Uri postImageUri;
    private ProgressBar addPostProgressBar;

    public AddPostFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Initialize fire-base instances
        mDatabase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();

        //Register views
        postImageView = view.findViewById(R.id.post_image_view);

        postImageView.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            assert getActivity() != null;

            ComponentName componentName = intent.resolveActivity(getActivity().getPackageManager());

            if (componentName != null) {
                startActivityForResult(intent, SELECT_IMAGE_RC);
            }
        });

        titleTxt = view.findViewById(R.id.title_txt);
        bodyTxt = view.findViewById(R.id.body_txt);

        addPostProgressBar = view.findViewById(R.id.add_post_progress_bar);

        Button addPostButton = view.findViewById(R.id.add_post_button);

        addPostButton.setOnClickListener(v -> {
            String title = String.valueOf(titleTxt.getText());
            String body = String.valueOf(bodyTxt.getText());

            String userUid = mAuth.getUid();

            if (hasInvalidInputs(title, body)) return;

            Post post = new Post(userUid, title, body);

            addPostProgressBar.setVisibility(View.VISIBLE);
            if (postImageUri != null) {

                //Upload the post image
                StorageReference postImageRef = mStorage.getReference("posts").child(UUID.randomUUID().toString());

                UploadTask postImageUploadTask = postImageRef.putFile(postImageUri);

                postImageUploadTask.addOnSuccessListener(taskSnapshot -> postImageRef.getDownloadUrl().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri remotePostImageUri = task.getResult();
                        assert remotePostImageUri != null;
                        post.setImage(remotePostImageUri.toString());

                        uploadPost(post);

                    }
                })).addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onViewCreated: ", e);
                });

            } else {
                uploadPost(post);
            }


        });
    }

    private void uploadPost(Post post) {
        mDatabase.collection("posts").add(post)
                .addOnCompleteListener(task -> {

                    addPostProgressBar.setVisibility(View.GONE);

                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Post Added", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "uploadPost: Post Added Successfully");
                        assert getActivity() != null;
                        getActivity().onBackPressed();
                    } else {

                        Toast.makeText(getContext(), "Post upload failed", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "uploadPost: Post Addition Failed", task.getException());

                    }
                });
    }

    private boolean hasInvalidInputs(String title, String body) {

        if (TextUtils.isEmpty(title)) {
            titleTxt.setError("Title is required");
            titleTxt.requestFocus();
            return true;
        }

        if (TextUtils.isEmpty(body)) {
            bodyTxt.setError("Body is required");
            bodyTxt.requestFocus();
            return true;
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE_RC) {
            assert data != null;
            postImageUri = data.getData();
            postImageView.setImageURI(postImageUri);
        }
    }
}
