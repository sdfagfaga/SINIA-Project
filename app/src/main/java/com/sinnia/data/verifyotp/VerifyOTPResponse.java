package com.sinnia.data.verifyotp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sinnia.data.login.UserDataList;

public class VerifyOTPResponse {

    @SerializedName("Status")
    @Expose
    private String Status;

    @SerializedName("Verified")
    @Expose
    private String Verified;

    @SerializedName("id")
    @Expose
    private Integer id;

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

    public String getVerified() {
        return Verified;
    }

    public void setVerified(String verified) {
        Verified = verified;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
