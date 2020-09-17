package com.mysasse.afyasmart.data.models;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class Post implements Serializable {

    @DocumentId
    private String id;
    private String userUid;

    private String title;
    private String body;
    private String image;

    public Post() {
    }

    public Post(String userUid, String title, String body) {
        this.title = title;
        this.body = body;
        this.userUid = userUid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
