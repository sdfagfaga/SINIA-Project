package com.sinnia.data.productdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sinnia.data.cart.UserAddress;

public class PlaceOrder {


    @SerializedName("shipmentObjectId")
    @Expose
    private String shipmentObjectId;

    @SerializedName("provider")
    @Expose
    private String provider;

    @SerializedName("address")
    @Expose
    private UserAddress userAddress;


    @SerializedName("paymentType")
    @Expose
    private String paymentType;

    @SerializedName("paymentDetails")
    @Expose
    private String paymentDetails;

    @SerializedName("paymentAmount")
    @Expose
    private String paymentAmount;

    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("deliveryAddressId")
    @Expose
    private String deliveryAddressId;
    @SerializedName("shipmentId")
    @Expose
    private String shipmentId;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("quantityPrice")
    @Expose
    private String quantityPrice;

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

    public String getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(String deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
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

    public String getQuantityPrice() {
        return quantityPrice;
    }

    public void setQuantityPrice(String quantityPrice) {
        this.quantityPrice = quantityPrice;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getShipmentObjectId() {
        return shipmentObjectId;
    }

    public void setShipmentObjectId(String shipmentObjectId) {
        this.shipmentObjectId = shipmentObjectId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
