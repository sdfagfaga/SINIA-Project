package com.sinnia.data.shippers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ShipperData implements Serializable {

  /*  @SerializedName("productId")
    @Expose
    private Integer productId;
    @SerializedName("rates")
    @Expose
    private List<ShipperRate> rates = null;


    @SerializedName("messages")
    @Expose
    private List<ShipperMessage> messages = null;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public List<ShipperRate> getRates() {
        return rates;
    }

    public void setRates(List<ShipperRate> rates) {
        this.rates = rates;
    }

    public List<ShipperMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ShipperMessage> messages) {
        this.messages = messages;
    }*/


    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("shipperContact")
    @Expose
    private String shipperContact;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("shipperName")
    @Expose
    private String shipperName;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("shipperLocation")
    @Expose
    private String shipperLocation;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getShipperLocation() {
        return shipperLocation;
    }

    public void setShipperLocation(String shipperLocation) {
        this.shipperLocation = shipperLocation;
    }


}
