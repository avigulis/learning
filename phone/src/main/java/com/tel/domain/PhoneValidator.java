package com.tel.domain;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.neovisionaries.i18n.CountryCode;
import com.tel.service.PhoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Objects;


@Component
public class PhoneValidator {
    private static final Logger log = LoggerFactory.getLogger(PhoneValidator.class);
    private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();


    private final PhoneService phoneService;

    @Autowired
    public PhoneValidator(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    public String validate(String phone) {

        Objects.nonNull(phone);

        log.info("Begin custom validation: {}", phone);


        // Validate for number
        BigInteger number = null;
        try {
            number = new BigInteger(phone.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            log.error("Number is not a string: {}", phone);
            //errors.rejectValue("number", "", "number.invalid");
            throw new RuntimeException("number.invalid");
        }

        // Validate for country
        String country = phoneService.get(number).orElseThrow(() -> {
            //errors.rejectValue("number", "", "country.invalid");
            return new RuntimeException("country.invalid");
        });
        log.debug("Number has country: {}", country);

        // Validate for phone
        boolean countryValid = CountryCode.findByName(country).size() > 0;
        log.debug("Country code available? {}", countryValid);
        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            if (countryValid) {
                CountryCode countryCode = CountryCode.findByName(country).get(0);
                phoneNumber = phoneUtil.parse(phone, countryCode.getAlpha2());
            } else {
                phoneNumber = phoneUtil.parse(phone, country);
            }
        } catch (NumberParseException e) {
            //errors.rejectValue("number", "", "number.invalid");
            throw new RuntimeException("number.invalid");
        }

        if (!phoneUtil.isValidNumber(phoneNumber)) {
            //errors.rejectValue("number", "", "number.invalid");
            throw new RuntimeException("number.invalid");
        }

        log.info("Number valid");
        return country;
    }

}
