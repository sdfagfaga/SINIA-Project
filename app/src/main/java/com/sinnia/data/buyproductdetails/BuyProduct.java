package com.sinnia.data.buyproductdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sinnia.data.cart.UserAddress;

public class BuyProduct {



    @SerializedName("address")
    @Expose
    private UserAddress address;

    @SerializedName("productOwnerName")
    @Expose
    private String productOwenerName;

    @SerializedName("quantityType")
    @Expose
    private String quantityType;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("highlights")
    @Expose
    private String highlights;
    @SerializedName("minQuantity")
    @Expose
    private Integer minQuantity;
    @SerializedName("quantityPrice")
    @Expose
    private Integer quantityPrice;
    @SerializedName("quantityPerUnit")
    @Expose
    private Integer quantityPerUnit;
    @SerializedName("productGrade")
    @Expose
    private String productGrade;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("productType")
    @Expose
    private String productType;
    @SerializedName("deliveryStatus")
    @Expose
    private String deliveryStatus;
    @SerializedName("thumbImageURL")
    @Expose
    private String thumbImageURL;
    @SerializedName("pricePerUnit")
    @Expose
    private Integer pricePerUnit;

    public String getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getHighlights() {
        return highlights;
    }

    public void setHighlights(String highlights) {
        this.highlights = highlights;
    }

    public Integer getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(Integer minQuantity) {
        this.minQuantity = minQuantity;
    }

    public Integer getQuantityPrice() {
        return quantityPrice;
    }

    public void setQuantityPrice(Integer quantityPrice) {
        this.quantityPrice = quantityPrice;
    }

    public Integer getQuantityPerUnit() {
        return quantityPerUnit;
    }

    public void setQuantityPerUnit(Integer quantityPerUnit) {
        this.quantityPerUnit = quantityPerUnit;
    }

    public String getProductGrade() {
        return productGrade;
    }

    public void setProductGrade(String productGrade) {
        this.productGrade = productGrade;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getThumbImageURL() {
        return thumbImageURL;
    }

    public void setThumbImageURL(String thumbImageURL) {
        this.thumbImageURL = thumbImageURL;
    }

    public Integer getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Integer pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public String getProductOwenerName() {
        return productOwenerName;
    }

    public void setProductOwenerName(String productOwenerName) {
        this.productOwenerName = productOwenerName;
    }

    public UserAddress getAddress() {
        return address;
    }

    public void setAddress(UserAddress address) {
        this.address = address;
    }
}
