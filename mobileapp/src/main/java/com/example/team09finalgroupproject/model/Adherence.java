package com.example.team09finalgroupproject.model;

public class Adherence {

    private String id;
    private String medicationId;
    private String name;
    private String date;
    private String time;
    private String adherenceStatus;

    public Adherence() {
        // Required for Firestore
    }

    public Adherence(String id, String medicationId, String name, String date, String time, String adherenceStatus) {
        this.id = id;
        this.medicationId = medicationId;
        this.name = name;
        this.date = date;
        this.time = time;
        this.adherenceStatus = adherenceStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(String medicationId) {
        this.medicationId = medicationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAdherenceStatus() {
        return adherenceStatus;
    }

    public void setAdherenceStatus(String adherenceStatus) {
        this.adherenceStatus = adherenceStatus;
    }
}
