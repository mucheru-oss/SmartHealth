package com.mysasse.afyasmart.data.models;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class Profile implements Serializable {

    @DocumentId
    private String id;

    private String name;
    private String phone;
    private String role;
    private String bio;
    private String expertise;

    private String avatar;

    public Profile() {
    }

    public Profile(String name, String phone, String role, String bio) {
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.bio = bio;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }
}
