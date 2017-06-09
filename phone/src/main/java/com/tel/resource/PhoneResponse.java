package com.tel.resource;

import java.util.LinkedList;
import java.util.List;

public class PhoneResponse {

    private final String phone;

    private final String country;

    private List<PhoneError> errors = new LinkedList<>();

    public PhoneResponse(String phone, String country) {
        this.phone = phone;
        this.country = country;
    }

    public void addError(String code, String message) {
        this.errors.add(new PhoneError(code, message));
    }

    private class PhoneError {
        private final String code;
        private final String message;

        public PhoneError(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
