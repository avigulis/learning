package com.tel.resource;


public class CountryCodeResponse {

    private final String phone;
    private final String country;

    public CountryCodeResponse(String phone, String country) {
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
