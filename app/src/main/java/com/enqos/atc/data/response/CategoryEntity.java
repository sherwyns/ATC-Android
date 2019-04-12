package com.enqos.atc.data.response;

import com.google.gson.annotations.SerializedName;

public class CategoryEntity {

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

    public String getImage_url() {
        return image_url;
    }

    private boolean isSelected;

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("image_url")
    private String image_url;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
