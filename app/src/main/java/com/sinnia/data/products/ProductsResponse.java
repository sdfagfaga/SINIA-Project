package com.sinnia.data.products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductsResponse implements Serializable {

    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("products")
    @Expose
    private ProductsList productsList;

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

    public ProductsList getProductsList() {
        return productsList;
    }

    public void setProductsList(ProductsList productsList) {
        this.productsList = productsList;
    }
}
