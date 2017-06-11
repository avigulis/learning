package com.tel.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PhoneValidatorTests {

    @Autowired
    CountryCodeProvider phoneService;

    PhoneValidator phoneValidator;
    Errors errors;
    ValidatorFactory factory;
    Validator validator;

    @Before
    public void setup() {
        phoneValidator = new PhoneValidator();
        errors = new BeanPropertyBindingResult(phoneValidator, "phoneValidator");
        assertThat(phoneService.get(new BigInteger("7")).get(), notNullValue());
    }

    @Test
    public void testNumberValidationConstraint() {
        // when given random string
        phoneValidator.setNumber("fail");
        ValidationUtils.invokeValidator(phoneValidator, phoneValidator, errors);

        // then errors present
        assertThat(errors.hasErrors(), is(true));
        assertThat(errors.getFieldError("number"), notNullValue());

        // when given  valid number
        phoneValidator = new PhoneValidator();
        errors = new BeanPropertyBindingResult(phoneValidator, "phoneValidator");
        phoneValidator.setNumber("+37129901454");
        ValidationUtils.invokeValidator(phoneValidator, phoneValidator, errors);

        // then errors absent
        assertThat(errors.hasErrors(), is(false));
        assertThat(errors.getFieldError("number"), nullValue());
    }

}
