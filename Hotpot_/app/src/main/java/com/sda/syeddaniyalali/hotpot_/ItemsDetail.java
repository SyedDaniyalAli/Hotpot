package com.sda.syeddaniyalali.hotpot_;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ItemsDetail {

    public String sda_FoodName, sda_ChiefName, sda_FoodPrice, sda_DeliveryPrice,sda_Rating, sda_ImageUri;

    public ItemsDetail()
    {

    }

    public ItemsDetail(String sda_FoodName, String sda_ChiefName, String sda_FoodPrice, String sda_DeliveryPrice,String sda_Rating, String sda_ImageUri)
    {
        this.sda_FoodName = sda_FoodName;
        this.sda_ChiefName = sda_ChiefName;
        this.sda_FoodPrice = sda_FoodPrice;
        this.sda_DeliveryPrice = sda_DeliveryPrice;
        this.sda_ImageUri = sda_ImageUri;
        this.sda_Rating = sda_Rating;
    }
}
