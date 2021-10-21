package com.sinnia.data.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sinnia.data.cart.UserAddress;
import com.sinnia.data.products.AddressDetails;

import java.io.Serializable;
import java.util.List;

public class ProfileResponse implements Serializable {

    @SerializedName("Status")
    @Expose
    private Integer Status;

    @SerializedName("Description")
    @Expose
    private String Description;


    @SerializedName("userData")
    @Expose
    private UserDataList userData;

    @SerializedName("userAddressData")
    @Expose
    private UserAddress userAddressData;


    public UserDataList getUserData() {
        return userData;
    }

    public void setUserData(UserDataList userData) {
        this.userData = userData;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public UserAddress getUserAddressData() {
        return userAddressData;
    }

    public void setUserAddressData(UserAddress userAddressData) {
        this.userAddressData = userAddressData;
    }


}
