package com.sinnia.data.products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostAProduct {

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
    @SerializedName("minQuantity")
    @Expose
    private Integer minQuantity;
    @SerializedName("productOwenerID")
    @Expose
    private Integer productOwenerID;
    @SerializedName("productDescription")
    @Expose
    private String productDescription;
    @SerializedName("productType")
    @Expose
    private String productType;
    @SerializedName("productSubName")
    @Expose
    private String productSubName;

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
}
