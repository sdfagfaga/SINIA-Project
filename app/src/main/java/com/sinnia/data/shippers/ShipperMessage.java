package com.sinnia.data.shippers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ShipperMessage implements Serializable {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("text")
    @Expose
    private String text;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
