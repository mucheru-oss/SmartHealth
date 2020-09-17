package com.mysasse.afyasmart.data.models;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class DiseaseMeasure implements Serializable {

    @DocumentId
    private String id;

    private String userId;
    private String body;
    private String type;

    public DiseaseMeasure() {
    }

    public DiseaseMeasure(String userId, String body, String type) {
        this.userId = userId;
        this.body = body;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
