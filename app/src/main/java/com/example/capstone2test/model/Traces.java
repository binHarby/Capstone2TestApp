package com.example.capstone2test.model;

import com.google.gson.annotations.SerializedName;

public class Traces {
    @SerializedName("boron")
    private double boron;
    @SerializedName("copper")
    private double copper;
    @SerializedName("selenium")
    private double selenium;
    @SerializedName("maganese")
    private double maganese;
    @SerializedName("fluorine")
    private double fluorine;
    @SerializedName("chromium")
    private double chromium;
    @SerializedName("cobalt")
    private double cobalt;
    @SerializedName("iodine")
    private double iodine;

    public Traces() {
    }

    public Traces(double boron, double copper, double selenium, double maganese, double fluorine, double chromium, double cobalt, double iodine) {
        this.boron = boron;
        this.copper = copper;
        this.selenium = selenium;
        this.maganese = maganese;
        this.fluorine = fluorine;
        this.chromium = chromium;
        this.cobalt = cobalt;
        this.iodine = iodine;
    }

    public double getBoron() {
        return boron;
    }

    public void setBoron(double boron) {
        this.boron = boron;
    }

    public double getCopper() {
        return copper;
    }

    public void setCopper(double copper) {
        this.copper = copper;
    }

    public double getSelenium() {
        return selenium;
    }

    public void setSelenium(double selenium) {
        this.selenium = selenium;
    }

    public double getMaganese() {
        return maganese;
    }

    public void setMaganese(double maganese) {
        this.maganese = maganese;
    }

    public double getFluorine() {
        return fluorine;
    }

    public void setFluorine(double fluorine) {
        this.fluorine = fluorine;
    }

    public double getChromium() {
        return chromium;
    }

    public void setChromium(double chromium) {
        this.chromium = chromium;
    }

    public double getCobalt() {
        return cobalt;
    }

    public void setCobalt(double cobalt) {
        this.cobalt = cobalt;
    }

    public double getIodine() {
        return iodine;
    }

    public void setIodine(double iodine) {
        this.iodine = iodine;
    }
}
