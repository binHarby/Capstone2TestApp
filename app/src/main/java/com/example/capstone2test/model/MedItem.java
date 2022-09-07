package com.example.capstone2test.model;

import com.google.gson.annotations.SerializedName;

public class MedItem {
    @SerializedName("med_name")
    private String medName;
    @SerializedName("res_name")
    private String resName;
    @SerializedName("res_id")
    private  int resId;
    @SerializedName("daily_dose")
    private int dailyDose;
    @SerializedName("dose_quant_type")
    private String doseQuantType;
    @SerializedName("med_id")
    private int medId;

    public MedItem(String medName, String resName, int resId, int dailyDose, String doseQuantType, int medId) {
        this.medName = medName;
        this.resName = resName;
        this.resId = resId;
        this.dailyDose = dailyDose;
        this.doseQuantType = doseQuantType;
        this.medId = medId;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getDailyDose() {
        return dailyDose;
    }

    public void setDailyDose(int dailyDose) {
        this.dailyDose = dailyDose;
    }

    public String getDoseQuantType() {
        return doseQuantType;
    }

    public void setDoseQuantType(String doseQuantType) {
        this.doseQuantType = doseQuantType;
    }

    public int getMedId() {
        return medId;
    }

    public void setMedId(int medId) {
        this.medId = medId;
    }
}
