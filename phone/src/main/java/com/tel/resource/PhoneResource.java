package com.tel.resource;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tel.model.PhoneValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.tel.util.ControllerUtils.getErrorMap;

@RestController
@RequestMapping("/phones")
public class PhoneResource {

    private static final Logger log = LoggerFactory.getLogger(PhoneResource.class);

    @GetMapping(value = "/{phone}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ObjectNode> phone(@NotNull @Size(min = 1)
                                                @PathVariable(value="phone") String phone) {
        log.debug("phone => {}", phone);
/*        if (errors.hasErrors())  {
            return ResponseEntity.unprocessableEntity().body(getErrorMap(errors));
        } else {
            return ResponseEntity.ok(phoneValidator.toJsonNode());
        }*/
        return null;
    }

    @InitBinder("phoneValidator")
    protected void initGetBinder(WebDataBinder binder) { binder.addValidators(new PhoneValidator());}

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ObjectNode> empty() {
        ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
        node.put("number", "empty");
        return new ResponseEntity(node, HttpStatus.UNPROCESSABLE_ENTITY);

    }
}
