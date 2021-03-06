package com.tel.resource;

import com.tel.domain.PhoneValidator;
import com.tel.service.PhoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/phones")
public class PhoneResource {

    private static final Logger log = LoggerFactory.getLogger(PhoneResource.class);

    private final PhoneService phoneService;

    @Autowired
    public PhoneResource(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @GetMapping(value = "/{phone}/country-code", produces = {MediaType.APPLICATION_JSON_VALUE})
    public CountryCodeResponse countryCode(@PathVariable(value="phone") String phone) {
        log.debug("phone => {}", phone);
        return new CountryCodeResponse(phone, phoneService.countryCodeFromPhoneNumber(phone));
    }

    @ExceptionHandler(PhoneValidationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<PhoneValidator.PhoneValidationError> phoneValidationHandler(PhoneValidationException ex) {
        return ex.getErrors();
    }
}
