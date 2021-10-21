package com.sinnia.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddShipper {

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("shipperName")
    @Expose
    private String shipperName;

    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("shipperContact")
    @Expose
    private String shipperContact;


    @SerializedName("shipperLocation")
    @Expose
    private String shipperLocation;

    @SerializedName("userId")
    @Expose
    private String userId;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getShipperContact() {
        return shipperContact;
    }

    public void setShipperContact(String shipperContact) {
        this.shipperContact = shipperContact;
    }

    public String getShipperLocation() {
        return shipperLocation;
    }

    public void setShipperLocation(String shipperLocation) {
        this.shipperLocation = shipperLocation;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
