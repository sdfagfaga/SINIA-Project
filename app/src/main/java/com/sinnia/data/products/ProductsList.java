package com.sinnia.data.products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProductsList  implements Serializable {


    @SerializedName("data")
    @Expose
    private List<Products> products = null;
    @SerializedName("noOfRecords")
    @Expose
    private Integer noOfRecords;

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    public Integer getNoOfRecords() {
        return noOfRecords;
    }

    public void setNoOfRecords(Integer noOfRecords) {
        this.noOfRecords = noOfRecords;
    }
}
