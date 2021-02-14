package com.sda.syeddaniyalali.hotpot_;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User_Ordered_Details {

    public String sda_foodName,sda_amount, sda_address, sda_Phone;

    public User_Ordered_Details()
    {

    }

    public User_Ordered_Details(String sda_foodName, String sda_amount, String sda_address, String sda_Phone)
    {
        this.sda_foodName=sda_foodName;
        this.sda_amount=sda_amount;
        this.sda_address=sda_address;
        this.sda_Phone=sda_Phone;
    }
}
