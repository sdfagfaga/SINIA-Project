package com.sinnia.data.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User  implements Serializable {




    @SerializedName("profilePicUrl")
    @Expose
    private String profilePicUrl;

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    @SerializedName("pinCode")
    @Expose
    private String pincode;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("address2")
    @Expose
    private String address2;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("addressLat")
    @Expose
    private String addressLat;
    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("basketOrdersCount")
    @Expose
    private String basketOrdersCount;
    @SerializedName("address1")
    @Expose
    private String address1;
    @SerializedName("notificationsCount")
    @Expose
    private String notificationsCount;
    @SerializedName("otp")
    @Expose
    private Integer otp;
    @SerializedName("addressLong")
    @Expose
    private String addressLong;
    @SerializedName("mobileCountry")
    @Expose
    private String mobileCountry;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("landmark")
    @Expose
    private String landmark;
    @SerializedName("isOTPVerified")
    @Expose
    private Integer isOTPVerified;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("isProfileComplete")
    @Expose
    private Integer isProfileComplete;

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getBasketOrdersCount() {
        return basketOrdersCount;
    }

    public void setBasketOrdersCount(String basketOrdersCount) {
        this.basketOrdersCount = basketOrdersCount;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getNotificationsCount() {
        return notificationsCount;
    }

    public void setNotificationsCount(String notificationsCount) {
        this.notificationsCount = notificationsCount;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public String getAddressLong() {
        return addressLong;
    }

    public void setAddressLong(String addressLong) {
        this.addressLong = addressLong;
    }

    public String getMobileCountry() {
        return mobileCountry;
    }

    public void setMobileCountry(String mobileCountry) {
        this.mobileCountry = mobileCountry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public Integer getIsOTPVerified() {
        return isOTPVerified;
    }

    public void setIsOTPVerified(Integer isOTPVerified) {
        this.isOTPVerified = isOTPVerified;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getIsProfileComplete() {
        return isProfileComplete;
    }

    public void setIsProfileComplete(Integer isProfileComplete) {
        this.isProfileComplete = isProfileComplete;
    }

}
