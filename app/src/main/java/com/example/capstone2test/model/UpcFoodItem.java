package com.example.capstone2test.model;

import com.google.gson.annotations.SerializedName;

public class UpcFoodItem {
    @SerializedName("general")
    private General general;
    @SerializedName("macros")
    private Macros macros;
    @SerializedName("minerals")
    private Minerals minerals;
    @SerializedName("vitamins")
    private Vitamins vitamins;
    @SerializedName("traces")
    private Traces traces;

    public UpcFoodItem(General general) {
        this.general = general;
    }

    public UpcFoodItem(Macros macros) {
        this.macros = macros;
    }

    public UpcFoodItem(Minerals minerals) {
        this.minerals = minerals;
    }

    public UpcFoodItem(Vitamins vitamins) {
        this.vitamins = vitamins;
    }

    public UpcFoodItem(Traces traces) {
        this.traces = traces;
    }

    public UpcFoodItem(General general, Macros macros, Minerals minerals, Vitamins vitamins, Traces traces) {
        this.general = general;
        this.macros = macros;
        this.minerals = minerals;
        this.vitamins = vitamins;
        this.traces = traces;
    }

    public General getGeneral() {
        return general;
    }

    public void setGeneral(General general) {
        this.general = general;
    }

    public Macros getMacros() {
        return macros;
    }

    public void setMacros(Macros macros) {
        this.macros = macros;
    }

    public Minerals getMinerals() {
        return minerals;
    }

    public void setMinerals(Minerals minerals) {
        this.minerals = minerals;
    }

    public Vitamins getVitamins() {
        return vitamins;
    }

    public void setVitamins(Vitamins vitamins) {
        this.vitamins = vitamins;
    }

    public Traces getTraces() {
        return traces;
    }

    public void setTraces(Traces traces) {
        this.traces = traces;
    }
}
