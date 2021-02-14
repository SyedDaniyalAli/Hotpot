package com.sda.syeddaniyalali.hotpot_;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Ordered_final_items_Class {

    public String sda_item_name, sda_item_amount, sda_item_delivery_price;

    public Ordered_final_items_Class()
    {

    }

    public Ordered_final_items_Class(String sda_item_name, String sda_item_amount, String sda_item_delivery_price)
    {
        this.sda_item_name =sda_item_name;
        this.sda_item_amount =sda_item_amount;
        this.sda_item_delivery_price =sda_item_delivery_price;
    }

}



