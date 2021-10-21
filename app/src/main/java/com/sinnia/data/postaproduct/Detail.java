package com.sinnia.data.postaproduct;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detail {

    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("productUnits")
    @Expose
    private String productUnits;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("productGrade")
    @Expose
    private String productGrade;
    @SerializedName("productType")
    @Expose
    private String productType;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("productCategory")
    @Expose
    private String productCategory;

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getProductUnits() {
        return productUnits;
    }

    public void setProductUnits(String productUnits) {
        this.productUnits = productUnits;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductGrade() {
        return productGrade;
    }

    public void setProductGrade(String productGrade) {
        this.productGrade = productGrade;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    @Override
    public String toString() {
        return productName;
    }
}
