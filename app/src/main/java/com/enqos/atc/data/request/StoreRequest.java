package com.enqos.atc.data.request;

import com.google.gson.annotations.SerializedName;

public class StoreRequest extends BaseRequest {

    public StoreRequest(String[] neighbourhood, int[] category, double latitude, double longitude) {
        this.neighbourhood = neighbourhood;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public StoreRequest(String[] neighbourhood, int[] category) {
        this.neighbourhood = neighbourhood;
        this.category = category;
    }

    public String[] getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String[] neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public int[] getCategory() {
        return category;
    }

    public void setCategory(int[] category) {
        this.category = category;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @SerializedName("neighbourhood")
    private String[] neighbourhood;
    @SerializedName("category")
    private int[] category;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;

}
