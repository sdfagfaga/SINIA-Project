package com.sinnia.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddDonation {



    @SerializedName("paymentDetails")
    @Expose
    private String paymentDetails;

    @SerializedName("paymentAmount")
    @Expose
    private Double paymentAmount;

    @SerializedName("userId")
    @Expose
    private String userId;

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
