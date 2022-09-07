package com.example.capstone2test.model;

import com.google.gson.annotations.SerializedName;

public class Minerals {
    @SerializedName("calcium")
    private double calcium;
    @SerializedName("phosphorus")
    private double phosphorus;
    @SerializedName("magnesium")
    private double magnesium;
    @SerializedName("sodium")
    private double sodium;
    @SerializedName("potassium")
    private double potassium;
    @SerializedName("iron")
    private double iron;
    @SerializedName("zinc")
    private double zinc;

    public Minerals() {
    }

    public Minerals(double calcium, double phosphorus, double magnesium, double sodium, double potassium, double iron, double zinc) {
        this.calcium = calcium;
        this.phosphorus = phosphorus;
        this.magnesium = magnesium;
        this.sodium = sodium;
        this.potassium = potassium;
        this.iron = iron;
        this.zinc = zinc;
    }

    public double getCalcium() {
        return calcium;
    }

    public void setCalcium(double calcium) {
        this.calcium = calcium;
    }

    public double getPhosphorus() {
        return phosphorus;
    }

    public void setPhosphorus(double phosphorus) {
        this.phosphorus = phosphorus;
    }

    public double getMagnesium() {
        return magnesium;
    }

    public void setMagnesium(double magnesium) {
        this.magnesium = magnesium;
    }

    public double getSodium() {
        return sodium;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }

    public double getPotassium() {
        return potassium;
    }

    public void setPotassium(double potassium) {
        this.potassium = potassium;
    }

    public double getIron() {
        return iron;
    }

    public void setIron(double iron) {
        this.iron = iron;
    }

    public double getZinc() {
        return zinc;
    }

    public void setZinc(double zinc) {
        this.zinc = zinc;
    }
}
