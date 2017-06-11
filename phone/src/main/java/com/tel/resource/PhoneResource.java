package com.tel.resource;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tel.service.PhoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@RestController
@RequestMapping("/phones")
public class PhoneResource {

    private static final Logger log = LoggerFactory.getLogger(PhoneResource.class);

    private final PhoneService phoneService;

    @Autowired
    public PhoneResource(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @GetMapping(value = "/{phone}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public PhoneResponse phone(@NotNull @Size(min = 1, max = 15) @Pattern(regexp = "^[0-9]+$")
                                                @PathVariable(value="phone") String phone) {
        log.debug("phone => {}", phone);
        return new PhoneResponse(phone, phoneService.countryCodeFromPhoneNumber(phone));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ObjectNode> empty() {
        ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
        node.put("number", "empty");
        return new ResponseEntity(node, HttpStatus.UNPROCESSABLE_ENTITY);

    }
}
