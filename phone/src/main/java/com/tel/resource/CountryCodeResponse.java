package com.tel.resource;


public class CountryCodeResponse {

    private String phone;
    private String country;

    public CountryCodeResponse() {
    }

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

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
