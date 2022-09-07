package com.example.capstone2test.model;

import com.google.gson.annotations.SerializedName;

public class General {
    @SerializedName("food_name")
    private String foodName;
    @SerializedName("servings")
    private int servings;
    @SerializedName("servings_taken")
    private int servingsTaken;
    @SerializedName("total_cals")
    private int totalCals;
    @SerializedName("cal_per_serv")
    private int calPerServ;
    @SerializedName("brand_name")
    private String brandName;
    @SerializedName("ingredients")
    private String ingrediants;
    @SerializedName("serving_size_unit")
    private String servingSizeUnit;

    public General(String foodName) {
        this.foodName = foodName;
    }

    public General(String foodName, String brandName) {
        this.foodName = foodName;
        this.brandName = brandName;
    }

    public General(String foodName, String brandName, String ingrediants) {
        this.foodName = foodName;
        this.brandName = brandName;
        this.ingrediants = ingrediants;
    }


    public General(String foodName, int servings, int totalCals, String brandName, String ingrediants, String servingSizeUnit) {
        this.foodName = foodName;
        this.servings = servings;
        this.totalCals = totalCals;
        this.brandName = brandName;
        this.ingrediants = ingrediants;
        this.servingSizeUnit = servingSizeUnit;
    }

    public General(String foodName, int servings, int servingsTaken, int totalCals, int calPerServ, String brandName, String ingrediants, String servingSizeUnit) {
        this.foodName = foodName;
        this.servings = servings;
        this.servingsTaken = servingsTaken;
        this.totalCals = totalCals;
        this.calPerServ = calPerServ;
        this.brandName = brandName;
        this.ingrediants = ingrediants;
        this.servingSizeUnit = servingSizeUnit;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public int getServingsTaken() {
        return servingsTaken;
    }

    public void setServingsTaken(int servingsTaken) {
        this.servingsTaken = servingsTaken;
    }

    public int getTotalCals() {
        return totalCals;
    }

    public void setTotalCals(int totalCals) {
        this.totalCals = totalCals;
    }

    public int getCalPerServ() {
        return calPerServ;
    }

    public void setCalPerServ(int calPerServ) {
        this.calPerServ = calPerServ;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getIngrediants() {
        return ingrediants;
    }

    public void setIngrediants(String ingrediants) {
        this.ingrediants = ingrediants;
    }

    public String getServingSizeUnit() {
        return servingSizeUnit;
    }

    public void setServingSizeUnit(String servingSizeUnit) {
        this.servingSizeUnit = servingSizeUnit;
    }
}
