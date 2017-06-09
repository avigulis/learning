package com.tel.model;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.neovisionaries.i18n.CountryCode;
import com.tel.service.PhoneService;
import com.tel.util.SpringContext;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.util.Optional;

public class PhoneValidator implements Validator {

    private static final Logger log = LoggerFactory.getLogger(PhoneValidator.class);
    private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    @NotNull(message = "empty")
    @NotBlank(message = "blank")
    @Size(max = 15, message = "too.long") //E.164, which specifies that the entire number should be 15 digits or shorter
    private String number;

    private String country;

    @Override
    public void validate(Object target, Errors errors) {
        Assert.notNull(target, "Target must not be null");
        Assert.notNull(errors, "Errors must not be null");

        // Validate default constraints first
        if (errors.hasErrors()) return;
        PhoneValidator phoneValidator = (PhoneValidator) target;
        log.info("Begin custom validation: " + phoneValidator.toString());
        PhoneService phoneService = SpringContext.get().getBean(PhoneService.class);

        // Validate for number
        BigInteger number;
        try {
            number = new BigInteger(phoneValidator.getNumber().replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            log.info("Number is not a string: " + phoneValidator.getNumber());
            errors.rejectValue("number", "", "number.invalid");
            return;
        }

        // Validate for country
        Optional<String> country = phoneService.get(number);
        if (country.isPresent()) phoneValidator.setCountry(country.get());
        else {
            errors.rejectValue("number", "", "country.invalid");
            return;
        }

        log.info("Number has country: " + country.get());

        // Validate for phone
        boolean countryValid = CountryCode.findByName(country.get()).size() > 0;
        log.info("Country code available? " + countryValid);
        Phonenumber.PhoneNumber phoneNumber;
        try {
            if (countryValid) {
                CountryCode countryCode = CountryCode.findByName(country.get()).get(0);
                phoneNumber = phoneUtil.parse(phoneValidator.getNumber(), countryCode.getAlpha2());
            } else {
                phoneNumber = phoneUtil.parse(phoneValidator.getNumber(), country.get());
            }
        } catch (NumberParseException e) {
            errors.rejectValue("number", "", "number.invalid");
            return;
        }

        if (!phoneUtil.isValidNumber(phoneNumber)) {
            errors.rejectValue("number", "", "number.invalid");
            return;
        }

        log.info("Number valid");
    }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    @Override
    public boolean supports(Class<?> aClass) { return aClass.equals(PhoneValidator.class); }

    @Override
    public String toString() {
        return "PhoneValidator{" +
                "number='" + number + '\'' +
                '}';
    }
}
