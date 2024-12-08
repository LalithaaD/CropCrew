package com.example.team09finalgroupproject.model;

public class Medication {
    private String id;
    private String name;
    private String dosage;
    private String time;
    private String date;
    private String adherenceStatus;

    public Medication(){

    }

    public Medication(String id, String name, String dosage, String time, String date, String adherenceStatus) {
        this.id = id;
        this.name = name;
        this.dosage = dosage;
        this.time = time;
        this.date = date;
        this.adherenceStatus = adherenceStatus;
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

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAdherenceStatus() {
        return adherenceStatus;
    }

    public void setAdherenceStatus(String adherenceStatus) {
        this.adherenceStatus = adherenceStatus;
    }
}
