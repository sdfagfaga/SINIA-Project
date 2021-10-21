package com.sinnia.data.products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sinnia.data.cart.UserAddress;

import java.util.List;

public class AddressDetails {

    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("data")
    @Expose
    private List<UserAddress> data = null;

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

    public List<UserAddress> getData() {
        return data;
    }

    public void setData(List<UserAddress> data) {
        this.data = data;
    }
}
