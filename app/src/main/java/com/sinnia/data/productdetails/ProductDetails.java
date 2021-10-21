package com.sinnia.data.productdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sinnia.data.products.Products;

import java.util.ArrayList;
import java.util.List;

public class ProductDetails {

    @SerializedName("Status")
    @Expose
    private Integer status;

    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("products")
    @Expose
    private ArrayList<Products> products = null;

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

    public ArrayList<Products> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Products> products) {
        this.products = products;
    }
}

