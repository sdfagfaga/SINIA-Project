package com.sinnia.listeners;

import com.sinnia.data.products.Products;

import java.util.Map;

public interface PriceListner {

    public void getTotalPrice(Map<String, Products> stringLongMap);
    public void deleteCartItem(String id);
}
