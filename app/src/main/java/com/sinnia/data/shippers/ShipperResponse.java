package com.sinnia.data.shippers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShipperResponse {

    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Data")
    @Expose
    private List<Shipper> data = null;


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

    public List<Shipper> getData() {
        return data;
    }

    public void setData(List<Shipper> data) {
        this.data = data;
    }
}
