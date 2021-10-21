package com.sinnia.data.banner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Banner {



    @SerializedName("productId")
    @Expose
    private String productId;

    @SerializedName("StartTime")
    @Expose
    private String startTime;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("UserType")
    @Expose
    private String userType;
    @SerializedName("URL")
    @Expose
    private String uRL;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getURL() {
        return uRL;
    }

    public void setURL(String uRL) {
        this.uRL = uRL;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }


}
