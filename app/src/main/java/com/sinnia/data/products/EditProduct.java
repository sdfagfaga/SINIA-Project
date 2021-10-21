package com.sinnia.data.products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditProduct {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("productId")
    @Expose
    private String productId;

    @SerializedName("minQuantity")
    @Expose
    private String minQuantity;
    @SerializedName("quantityAvailable")
    @Expose
    private String quantityAvailable;
    @SerializedName("pricePerUnit")
    @Expose
    private String pricePerUnit;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(String minQuantity) {
        this.minQuantity = minQuantity;
    }

    public String getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(String quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public String getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(String pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
}
