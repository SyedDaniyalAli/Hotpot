package com.sda.syeddaniyalali.hotpot_;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Order_Item_Class {

    public String sda_Name, sda_Title, sda_Food_Price, sda_Delivery_Price, sda_Rating, imagePath;

    public Order_Item_Class()
    {

    }

    public Order_Item_Class(String imagePath,String sda_Name,String sda_Title,String sda_Food_Price,String sda_Delivery_Price,String sda_Rating)
    {
        this.sda_Name = sda_Name;
        this.sda_Title = sda_Title;
        this.sda_Food_Price = sda_Food_Price;
        this.sda_Delivery_Price = sda_Delivery_Price;
        this.sda_Rating = sda_Rating;
        this.imagePath=imagePath;
    }
}
