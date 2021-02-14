package com.sda.syeddaniyalali.hotpot_;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Ordered_final_items_collection
{
    public static ArrayList<Ordered_final_items_Class> items_collection =new ArrayList<>();
    public static int totalAmount;
    public static String foodNames="";


    public Ordered_final_items_collection()
    {

    }

}