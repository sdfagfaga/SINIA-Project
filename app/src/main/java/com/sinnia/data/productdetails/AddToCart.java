package com.sinnia.data.productdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddToCart {

    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("availableAddressId")
    @Expose
    private String availableAddressId;
    @SerializedName("deliveryAddressID")
    @Expose
    private String deliveryAddressID;
    @SerializedName("shipmentId")
    @Expose
    private String shipmentId;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("quantityTypeId")
    @Expose
    private String quantityTypeId;
    @SerializedName("quantityPrice")
    @Expose
    private Double quantityPrice;
    @SerializedName("deliveryStatus")
    @Expose
    private String deliveryStatus;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvailableAddressId() {
        return availableAddressId;
    }

    public void setAvailableAddressId(String availableAddressId) {
        this.availableAddressId = availableAddressId;
    }

    public String getDeliveryAddressID() {
        return deliveryAddressID;
    }

    public void setDeliveryAddressID(String deliveryAddressID) {
        this.deliveryAddressID = deliveryAddressID;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantityTypeId() {
        return quantityTypeId;
    }

    public void setQuantityTypeId(String quantityTypeId) {
        this.quantityTypeId = quantityTypeId;
    }

    public Double getQuantityPrice() {
        return quantityPrice;
    }

    public void setQuantityPrice(Double quantityPrice) {
        this.quantityPrice = quantityPrice;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
