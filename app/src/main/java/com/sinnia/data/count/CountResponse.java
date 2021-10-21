package com.sinnia.data.count;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountResponse {

    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("counts")
    @Expose
    private Counts counts;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Counts getCounts() {
        return counts;
    }

    public void setCounts(Counts counts) {
        this.counts = counts;
    }

}
