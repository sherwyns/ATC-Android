package com.enqos.atc.data.response;

import com.google.gson.annotations.SerializedName;

public class Neighbourhood {
    @SerializedName("neighbourhood")
    private String neighbourhood;
    private boolean isSelected;

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
