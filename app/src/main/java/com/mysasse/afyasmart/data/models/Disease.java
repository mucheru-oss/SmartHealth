package com.mysasse.afyasmart.data.models;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class Disease implements Serializable {

    @DocumentId
    private String id;

    private String name;
    private String description;

    public Disease() {
    }

    public Disease(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
