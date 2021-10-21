package com.sinnia.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddPayPalId {

    @SerializedName("payPalAccountId")
    @Expose
    private String payPalAccountId;

    @SerializedName("userId")
    @Expose
    private String userId;

    public String getPayPalAccountId() {
        return payPalAccountId;
    }

    public void setPayPalAccountId(String payPalAccountId) {
        this.payPalAccountId = payPalAccountId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
