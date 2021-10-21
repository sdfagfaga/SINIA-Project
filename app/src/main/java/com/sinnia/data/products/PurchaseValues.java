package com.sinnia.data.products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PurchaseValues {

    @SerializedName("selectedQuantity")
    @Expose
    private Integer selectedQuantity;

    @SerializedName("selectedTotal")
    @Expose
    private Long selectedTotal;

    @SerializedName("id")
    @Expose
    private int id;


}
