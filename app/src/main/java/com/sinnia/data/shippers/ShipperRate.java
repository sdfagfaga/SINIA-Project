package com.sinnia.data.shippers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ShipperRate implements Serializable {

    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("duration_terms")
    @Expose
    private String durationTerms;
    @SerializedName("provider")
    @Expose
    private String provider;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("estimated_days")
    @Expose
    private String estimatedDays;
    @SerializedName("object_id")
    @Expose
    private String objectId;
    @SerializedName("carrier_account")
    @Expose
    private String carrierAccount;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDurationTerms() {
        return durationTerms;
    }

    public void setDurationTerms(String durationTerms) {
        this.durationTerms = durationTerms;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getEstimatedDays() {
        return estimatedDays;
    }

    public void setEstimatedDays(String estimatedDays) {
        this.estimatedDays = estimatedDays;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCarrierAccount() {
        return carrierAccount;
    }

    public void setCarrierAccount(String carrierAccount) {
        this.carrierAccount = carrierAccount;
    }
}
