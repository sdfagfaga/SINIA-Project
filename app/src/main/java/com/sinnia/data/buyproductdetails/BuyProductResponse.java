package com.sinnia.data.buyproductdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BuyProductResponse {

    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("data")
    @Expose
    private BuyProduct data;

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

    public BuyProduct getData() {
        return data;
    }

    public void setData(BuyProduct data) {
        this.data = data;
    }
}
