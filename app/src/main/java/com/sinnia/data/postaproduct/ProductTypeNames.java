package com.sinnia.data.postaproduct;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sinnia.data.cart.UserAddress;

import java.util.List;

public class ProductTypeNames {

    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("productCategories")
    @Expose
    private List<ProductCategory> productCategories = null;
    @SerializedName("userAddressData")
    @Expose
    private List<UserAddress> userAddressData = null;
    @SerializedName("Description")
    @Expose
    private String description;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<ProductCategory> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(List<ProductCategory> productCategories) {
        this.productCategories = productCategories;
    }

    public List<UserAddress> getUserAddressData() {
        return userAddressData;
    }

    public void setUserAddressData(List<UserAddress> userAddressData) {
        this.userAddressData = userAddressData;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
