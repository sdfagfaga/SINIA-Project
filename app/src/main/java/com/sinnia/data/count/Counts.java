package com.sinnia.data.count;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Counts {

    @SerializedName("BasketOrdersCount")
    @Expose
    private String basketOrdersCount;
    @SerializedName("NotificationsCount")
    @Expose
    private String notificationsCount;

    public String getBasketOrdersCount() {
        return basketOrdersCount;
    }

    public void setBasketOrdersCount(String basketOrdersCount) {
        this.basketOrdersCount = basketOrdersCount;
    }

    public String getNotificationsCount() {
        return notificationsCount;
    }

    public void setNotificationsCount(String notificationsCount) {
        this.notificationsCount = notificationsCount;
    }
}
