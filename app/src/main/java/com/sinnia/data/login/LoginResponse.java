package com.sinnia.data.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("Status")
    @Expose
    private String Status;

    @SerializedName("Description")
    @Expose
    private String Description;

    @SerializedName("otp")
    @Expose
    private Integer otp;

    @SerializedName("user")
    @Expose
    private UserDataList userData;


    public UserDataList getUserData() {
        return userData;
    }

    public void setUserData(UserDataList userData) {
        this.userData = userData;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }
}
