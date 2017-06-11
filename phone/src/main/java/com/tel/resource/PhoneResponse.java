package com.tel.resource;


public class PhoneResponse {

    private final String phone;
    private final String country;

    public PhoneResponse(String phone, String country) {
        this.phone = phone;
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public String getCountry() {
        return country;
    }
}
