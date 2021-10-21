package com.sinnia.data.cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserAddress implements Serializable {

    @SerializedName("address2")
    @Expose
    private String address2;
    @SerializedName("address1")
    @Expose
    private String address1;
    @SerializedName("landmark")
    @Expose
    private String landmark;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("addressId")
    @Expose
    private Integer addressId;
    @SerializedName("pinCode")
    @Expose
    private String pincode;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("addressLong")
    @Expose
    private String addressLong;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("addressLat")
    @Expose
    private String addressLat;

    @SerializedName("state")
    @Expose
    private String state;

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getPinCode() {
        return pincode;
    }

    public void setPinode(String pincode) {
        this.pincode = pincode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddressLong() {
        return addressLong;
    }

    public void setAddressLong(String addressLong) {
        this.addressLong = addressLong;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddressLat() {
        return addressLat;
    }

    public void setAddressLat(String addressLat) {
        this.addressLat = addressLat;
    }

    @Override
    public String toString() {
        return address1 + ", " + address2 + ", " + landmark + "," + pincode;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
