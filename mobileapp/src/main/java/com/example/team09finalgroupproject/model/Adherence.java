package com.example.team09finalgroupproject.model;

public class Adherence {

    private String medicationName;
    private String date;
    private String time;
    private String adherenceStatus;

    public Adherence(String medicationName, String date, String time, String adherenceStatus) {
        this.medicationName = medicationName;
        this.date = date;
        this.time = time;
        this.adherenceStatus = adherenceStatus;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
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
