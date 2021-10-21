package com.sinnia.utils;


import com.sinnia.data.products.Products;

import java.util.List;

/**
 * Created by AXIOM on 02-04-2018.
 */

public class SectionModel {

    private String sectionLabel;
    private List<Products> itemArrayList;

    public SectionModel(String sectionLabel, List<Products> itemArrayList) {
        this.sectionLabel = sectionLabel;
        this.itemArrayList = itemArrayList;
    }


    public String getSectionLabel() {
        return sectionLabel;
    }

    public List<Products> getItemArrayList() {
        return itemArrayList;
    }
}
