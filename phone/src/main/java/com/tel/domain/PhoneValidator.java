package com.tel.domain;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.neovisionaries.i18n.CountryCode;
import com.tel.service.PhoneService;
import com.tel.util.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

import java.math.BigInteger;
import java.util.Optional;

@Component
public class PhoneValidator {
    private static final Logger log = LoggerFactory.getLogger(PhoneValidator.class);
    private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    private String number;

    private String country;

    public String validate(Object target) {

        Errors errors = null;
        Assert.notNull(target, "Target must not be null");

        PhoneValidator phoneValidator = (PhoneValidator) target;
        log.info("Begin custom validation: " + phoneValidator.toString());
        PhoneService phoneService = SpringContext.get().getBean(PhoneService.class);

        // Validate for number
        BigInteger number = null;
        try {
            number = new BigInteger(phoneValidator.getNumber().replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            log.info("Number is not a string: " + phoneValidator.getNumber());
            errors.rejectValue("number", "", "number.invalid");
        }

        // Validate for country
        Optional<String> country = phoneService.get(number);
        if (country.isPresent()) phoneValidator.setCountry(country.get());
        else {
            errors.rejectValue("number", "", "country.invalid");
        }

        log.info("Number has country: " + country.get());

        // Validate for phone
        boolean countryValid = CountryCode.findByName(country.get()).size() > 0;
        log.info("Country code available? " + countryValid);
        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            if (countryValid) {
                CountryCode countryCode = CountryCode.findByName(country.get()).get(0);
                phoneNumber = phoneUtil.parse(phoneValidator.getNumber(), countryCode.getAlpha2());
            } else {
                phoneNumber = phoneUtil.parse(phoneValidator.getNumber(), country.get());
            }
        } catch (NumberParseException e) {
            errors.rejectValue("number", "", "number.invalid");
        }

        if (!phoneUtil.isValidNumber(phoneNumber)) {
            errors.rejectValue("number", "", "number.invalid");
        }

        log.info("Number valid");
        return null;
    }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    @Override
    public String toString() {
        return "PhoneValidator{" +
                "number='" + number + '\'' +
                '}';
    }
}
