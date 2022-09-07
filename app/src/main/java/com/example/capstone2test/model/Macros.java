package com.example.capstone2test.model;

import com.google.gson.annotations.SerializedName;

public class Macros {

    @SerializedName("carb")
    private double carb;
    @SerializedName("sugar")
    private double sugar;
    @SerializedName("fructose")
    private double fructose;
    @SerializedName("lactose")
    private double lactose;
    @SerializedName("protein")
    private double protein;
    @SerializedName("amino")
    private double amino;
    @SerializedName("fat")
    private double fat;
    @SerializedName("unsaturated")
    private double unsaturated;
    @SerializedName("monounsaturated")
    private double monounsaturated;
    @SerializedName("polyunsaturated")
    private double polyunsaturated;
    @SerializedName("saturated")
    private double saturated;
    @SerializedName("fiber")
    private double fiber;
    @SerializedName("trans")
    private double trans;

    public Macros() {
    }

    public Macros(double carb, double sugar, double fructose, double lactose, double protein, double amino, double fat, double unsaturated, double monounsaturated, double polyunsaturated, double saturated, double fiber, double trans) {
        this.carb = carb;
        this.sugar = sugar;
        this.fructose = fructose;
        this.lactose = lactose;
        this.protein = protein;
        this.amino = amino;
        this.fat = fat;
        this.unsaturated = unsaturated;
        this.monounsaturated = monounsaturated;
        this.polyunsaturated = polyunsaturated;
        this.saturated = saturated;
        this.fiber = fiber;
        this.trans = trans;
    }

    public double getCarb() {
        return carb;
    }

    public void setCarb(double carb) {
        this.carb = carb;
    }

    public double getSugar() {
        return sugar;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    public double getFructose() {
        return fructose;
    }

    public void setFructose(double fructose) {
        this.fructose = fructose;
    }

    public double getLactose() {
        return lactose;
    }

    public void setLactose(double lactose) {
        this.lactose = lactose;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getAmino() {
        return amino;
    }

    public void setAmino(double amino) {
        this.amino = amino;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getUnsaturated() {
        return unsaturated;
    }

    public void setUnsaturated(double unsaturated) {
        this.unsaturated = unsaturated;
    }

    public double getMonounsaturated() {
        return monounsaturated;
    }

    public void setMonounsaturated(double monounsaturated) {
        this.monounsaturated = monounsaturated;
    }

    public double getPolyunsaturated() {
        return polyunsaturated;
    }

    public void setPolyunsaturated(double polyunsaturated) {
        this.polyunsaturated = polyunsaturated;
    }

    public double getSaturated() {
        return saturated;
    }

    public void setSaturated(double saturated) {
        this.saturated = saturated;
    }

    public double getFiber() {
        return fiber;
    }

    public void setFiber(double fiber) {
        this.fiber = fiber;
    }

    public double getTrans() {
        return trans;
    }

    public void setTrans(double trans) {
        this.trans = trans;
    }
}
