package com.sda.syeddaniyalali.hotpot_;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String Role;
    public String Email;
    public String Password;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String Role,String Email, String Password) {
        this.Role= Role;
        this.Email = Email;
        this.Password = Password;
    }

}