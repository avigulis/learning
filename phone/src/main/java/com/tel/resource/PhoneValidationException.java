package com.tel.resource;


import com.tel.domain.PhoneValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PhoneValidationException extends RuntimeException {

    private final List<PhoneValidator.PhoneValidationError> errors = new ArrayList<>();
    private final String phone;

    public PhoneValidationException(String phone, List<PhoneValidator.PhoneValidationError> errors) {
        super();
        this.phone = phone;
        this.errors.addAll(errors);
    }

    public PhoneValidationException(String phone, PhoneValidator.PhoneValidationError error) {
        super();
        this.phone = phone;
        this.errors.add(error);
    }

    public List<PhoneValidator.PhoneValidationError> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public String getPhone() {
        return phone;
    }
}
