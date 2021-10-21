package com.sinnia.data.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("password")
    @Expose
    private String password;


    @SerializedName("versionName")
    @Expose
    private String versionName;

    @SerializedName("versionCode")
    @Expose
    private Integer versionCode;

    @SerializedName("deviceType")
    @Expose
    private String device;

    @SerializedName("userId")
    @Expose
    private String userId;


    @SerializedName("mobileCountry")
    @Expose
    private String mobileCountry;

    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;

    @SerializedName("email")
    @Expose
    private String email;

    public String getMobileCountry() {
        return mobileCountry;
    }

    public void setMobileCountry(String mobileCountry) {
        this.mobileCountry = mobileCountry;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
