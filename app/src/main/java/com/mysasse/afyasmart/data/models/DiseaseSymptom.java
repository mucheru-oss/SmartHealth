package com.mysasse.afyasmart.data.models;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class DiseaseSymptom implements Serializable {

    @DocumentId
    private String id;

    private String userId;
    private String period;
    private String body;

    public DiseaseSymptom() {

    }

    public DiseaseSymptom(String userId, String period, String body) {
        this.userId = userId;
        this.period = period;
        this.body = body;
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

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
