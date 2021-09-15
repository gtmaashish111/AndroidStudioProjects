package com.example.aashish.shopmobile;

public class Customer {


    String fullName, email,address,phoneNumber;

    public Customer(){}

    public Customer(String fullName,String address, String phoneNumber, String email)
    {
        this.fullName = fullName;
        this.email = email;

        this.address = address;
        this.phoneNumber = phoneNumber;


    }
}
