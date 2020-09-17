package com.mysasse.afyasmart.data.models;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class Doctor implements Serializable {

    @DocumentId
    private String id;

    private String areaOfExpertise;
    private String topMostAward;
    private String currentlyWorkingWhere;
    private String trainingInstitution;
    private String professionHistory;

    public Doctor() {
    }

    public Doctor(String areaOfExpertise, String topMostAward, String currentlyWorkingWhere, String trainingInstitution, String professionHistory) {
        this.areaOfExpertise = areaOfExpertise;
        this.topMostAward = topMostAward;
        this.currentlyWorkingWhere = currentlyWorkingWhere;
        this.trainingInstitution = trainingInstitution;
        this.professionHistory = professionHistory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAreaOfExpertise() {
        return areaOfExpertise;
    }

    public void setAreaOfExpertise(String areaOfExpertise) {
        this.areaOfExpertise = areaOfExpertise;
    }

    public String getTopMostAward() {
        return topMostAward;
    }

    public void setTopMostAward(String topMostAward) {
        this.topMostAward = topMostAward;
    }

    public String getCurrentlyWorkingWhere() {
        return currentlyWorkingWhere;
    }

    public void setCurrentlyWorkingWhere(String currentlyWorkingWhere) {
        this.currentlyWorkingWhere = currentlyWorkingWhere;
    }

    public String getTrainingInstitution() {
        return trainingInstitution;
    }

    public void setTrainingInstitution(String trainingInstitution) {
        this.trainingInstitution = trainingInstitution;
    }

    public String getProfessionHistory() {
        return professionHistory;
    }

    public void setProfessionHistory(String professionHistory) {
        this.professionHistory = professionHistory;
    }
}
