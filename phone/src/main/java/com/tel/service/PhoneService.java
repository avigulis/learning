package com.tel.service;

import com.tel.domain.CountryCodeProvider;
import com.tel.domain.PhoneValidator;
import com.tel.resource.PhoneValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

import static com.tel.domain.PhoneValidator.PhoneValidationError.unknown;

@Service
public class PhoneService {

    private final CountryCodeProvider countryCodeProvider;
    private final PhoneValidator phoneValidator;

    @Autowired
    public PhoneService(CountryCodeProvider countryCodeProvider, PhoneValidator phoneValidator) {
        this.countryCodeProvider = countryCodeProvider;
        this.phoneValidator = phoneValidator;
    }

    public String countryCodeFromPhoneNumber(String phone) {
        List<PhoneValidator.PhoneValidationError> errors = phoneValidator.validate(phone);
        if (!errors.isEmpty()) {
            throw new PhoneValidationException(phone, errors);
        }

        return countryCodeProvider.get(new BigInteger(phone))
                .orElseThrow(() -> new PhoneValidationException(phone, unknown()));
    }

}
