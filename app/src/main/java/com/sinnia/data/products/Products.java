package com.sinnia.data.products;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sinnia.data.cart.UserAddress;
import com.sinnia.data.login.User;

import java.io.Serializable;


public class Products implements Serializable {

    @SerializedName("length")
    @Expose
    private String length;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("city")
    @Expose
    private String city;



    @SerializedName("weight")
    @Expose
    private String weight;

    @SerializedName("height")
    @Expose
    private String height;

    @SerializedName("width")
    @Expose
    private String width;

    @SerializedName("address1")
    @Expose
    private String address1;

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    @SerializedName("address")
    @Expose
    private UserAddress address;

    @SerializedName("orderId")
    @Expose
    private Integer orderId;

    @SerializedName("productStatus")
    @Expose
    private Integer productStatus;

    @SerializedName("radius")
    @Expose
    private String radius;

    @SerializedName("productId")
    @Expose
    private Integer productId;

    @SerializedName("quantity")
    @Expose
    private Integer selectedQuantity;


    @SerializedName("quantityPrice")
    @Expose
    private double selectedTotal;


    @SerializedName("quantityType")
    @Expose
    private String quantityType;
    @SerializedName("quantityAvailable")
    @Expose
    private Integer quantityAvailable;
    @SerializedName("quantityPerUnit")
    @Expose
    private Integer quantityPerUnit;
    @SerializedName("productOwnerName")
    @Expose
    private String productOwnerName;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("productGrade")
    @Expose
    private String productGrade;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("pricePerUnit")
    @Expose
    private Integer pricePerUnit;
    @SerializedName("availableAddressId")
    @Expose
    private Integer availableAddressId;
    @SerializedName("highlight")
    @Expose
    private String highlight;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("minQuantity")
    @Expose
    private Integer minQuantity;
    @SerializedName("productOwenerID")
    @Expose
    private Integer productOwenerID;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("productDescription")
    @Expose
    private String productDescription;
    @SerializedName("productType")
    @Expose
    private String productType;
    @SerializedName("productSubName")
    @Expose
    private String productSubName;

    @SerializedName("thumbImageURL")
    @Expose
    private String thumbImageURL;

    public String getThumbImageURL() {
        return thumbImageURL;
    }

    public void setThumbImageURL(String thumbImageURL) {
        this.thumbImageURL = thumbImageURL;
    }

    public String getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }

    public Integer getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(Integer quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public Integer getQuantityPerUnit() {
        return quantityPerUnit;
    }

    public void setQuantityPerUnit(Integer quantityPerUnit) {
        this.quantityPerUnit = quantityPerUnit;
    }

    public String getProductOwnerName() {
        return productOwnerName;
    }

    public void setProductOwnerName(String productOwnerName) {
        this.productOwnerName = productOwnerName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public Integer getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Integer pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public Integer getAvailableAddressId() {
        return availableAddressId;
    }

    public void setAvailableAddressId(Integer availableAddressId) {
        this.availableAddressId = availableAddressId;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(Integer minQuantity) {
        this.minQuantity = minQuantity;
    }

    public Integer getProductOwenerID() {
        return productOwenerID;
    }

    public void setProductOwenerID(Integer productOwenerID) {
        this.productOwenerID = productOwenerID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductSubName() {
        return productSubName;
    }

    public void setProductSubName(String productSubName) {
        this.productSubName = productSubName;
    }


    public Integer getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(Integer selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public double getSelectedTotal() {
        return selectedTotal;
    }

    public void setSelectedTotal(double selectedTotal) {
        this.selectedTotal = selectedTotal;
    }

    public String getRadius() {
        return radius;
    } 

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public Integer getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(Integer productStatus) {
        this.productStatus = productStatus;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public UserAddress getAddress() {
        return address;
    }

    public void setAddress(UserAddress address) {
        this.address = address;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
