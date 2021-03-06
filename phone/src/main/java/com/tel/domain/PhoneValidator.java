package com.tel.domain;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.tel.domain.PhoneValidator.PhoneValidationError.errorWith;
import static java.lang.String.format;
import static java.util.Objects.isNull;


@Component
public class PhoneValidator {
    private static final Logger log = LoggerFactory.getLogger(PhoneValidator.class);
    private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    private final CountryCodeProvider countryCodeProvider;

    @Autowired
    public PhoneValidator(CountryCodeProvider countryCodeProvider) {
        this.countryCodeProvider = countryCodeProvider;
    }

    public List<PhoneValidationError> validate(String phone) {

        List<PhoneValidationError> errors = new ArrayList<>();

        if (isNull(phone) || phone.isEmpty()) {
            errors.add(errorWith("phone.empty", "Phone number empty"));
            return errors;
        }

        if (!phone.matches("^\\d+$")) {
            errors.add(errorWith("phone.onlyDigitsAllowed", "Phone number contains forbidden symbols"));
            return errors;
        }


        BigInteger number = new BigInteger(phone);
        String country = countryCodeProvider.get(number).orElse(null);
        if (isNull(country)) {
            errors.add(errorWith("phone.countryCodeNotExist", "Country code not exist for this countryCode number"));
            return errors;
        }

        Phonenumber.PhoneNumber phoneNumber = parsePhoneNumber(phone, country);
        if (isNull(phoneNumber)) {
            errors.add(errorWith("phone.incorrectFormat", "Phone number incorrect format"));
        }

        return errors;
    }

    private Phonenumber.PhoneNumber parsePhoneNumber(String phone, String country) {
        try {
            return phoneUtil.parse(format("+%s", phone) , country);
        } catch (NumberParseException e) {
            return null;
        }
    }

    public static class PhoneValidationError {

        private String code;
        private String message;

        public PhoneValidationError(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public PhoneValidationError() {
        }

        public static PhoneValidationError errorWith(String code, String message) {
            return new PhoneValidationError(code, message);
        }

        public static PhoneValidationError unknown() {
            return new PhoneValidationError("unknown", "unknown");
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
