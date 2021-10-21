package com.sinnia.data.shippers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shipper {

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("shipperLocation")
    @Expose
    private String shipperLocation;


    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("shipperContact")
    @Expose
    private String shipperContact;

    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("ShipperDetails")
    @Expose
    private String shipperDetails;

    @SerializedName("shipperName")
    @Expose
    private String shipperName;

    @SerializedName("id")
    @Expose
    private Integer id;

    public String getShipperContact() {
        return shipperContact;
    }

    public void setShipperContact(String shipperContact) {
        this.shipperContact = shipperContact;
    }



    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getShipperDetails() {
        return shipperDetails;
    }

    public void setShipperDetails(String shipperDetails) {
        this.shipperDetails = shipperDetails;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShipperLocation() {
        return shipperLocation;
    }

    public void setShipperLocation(String shipperLocation) {
        this.shipperLocation = shipperLocation;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
