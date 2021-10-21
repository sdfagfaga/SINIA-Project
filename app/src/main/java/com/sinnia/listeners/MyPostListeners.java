package com.sinnia.listeners;

import com.sinnia.data.products.Products;

public interface MyPostListeners {

    void clickOnActiveOrInactiveButton(int status,String productId);
    void clickOnEdit(Products productId);
}
