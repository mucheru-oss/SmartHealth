package com.mysasse.afyasmart.ui.fragments.posts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mysasse.afyasmart.data.models.Post;
import com.mysasse.afyasmart.data.repositories.PostRepository;

import java.util.List;

public class PostViewModel extends ViewModel implements PostRepository.PostTaskListener {

    private MutableLiveData<List<Post>> _posts;
    private PostRepository postRepository;

    public PostViewModel() {
        _posts = new MutableLiveData<>();

        postRepository = new PostRepository(this);
    }

    public LiveData<List<Post>> getAllPost() {
        postRepository.getPostsFromFirebase();

        return _posts;
    }

    @Override
    public void yieldPosts(List<Post> posts) {
        _posts.setValue(posts);
    }
}
