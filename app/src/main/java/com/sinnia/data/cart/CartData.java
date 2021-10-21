package com.sinnia.data.cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sinnia.data.login.User;
import com.sinnia.data.products.Products;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartData implements Serializable {


    @SerializedName("userAddressData")
    @Expose
    private ArrayList<UserAddress> userAddressData = null;

    @SerializedName("productData")
    @Expose
    private ArrayList<Products> productData = null;

    public ArrayList<Products> getProductData() {
        return productData;
    }

    public void setProductData(ArrayList<Products> productData) {
        this.productData = productData;
    }

    public ArrayList<UserAddress> getUserAddressData() {
        return userAddressData;
    }

    public void setUserAddressData(ArrayList<UserAddress> userAddressData) {
        this.userAddressData = userAddressData;
    }
}
