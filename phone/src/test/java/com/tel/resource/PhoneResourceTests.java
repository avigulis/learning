package com.tel.resource;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tel.domain.PhoneValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PhoneResourceTests {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    WebApplicationContext webApplicationContext;

    PhoneValidator phoneValidator;

    @Before
    public void setup() {
        assertThat(this.webApplicationContext, notNullValue());
        phoneValidator = new PhoneValidator();
    }

    @Test
    public void testEmptyRequest() {
        // when empty request
        ResponseEntity<ObjectNode> responseEntity = testRestTemplate.postForEntity("/phones", phoneValidator, ObjectNode.class);

        // then should return error
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.UNPROCESSABLE_ENTITY));
    }

    @Test
    public void testInvalidPhone() {
        // when empty request
        phoneValidator.setNumber("+418379123847");
        ResponseEntity<ObjectNode> responseEntity = testRestTemplate.postForEntity("/phones", phoneValidator, ObjectNode.class);

        // then should return error
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.UNPROCESSABLE_ENTITY));
    }

    @Test
    public void testValidPhone() {
        // given a request with a valid countryCode
        phoneValidator.setNumber("+15005550006");
        ResponseEntity<ObjectNode> responseEntity =
                testRestTemplate.postForEntity("/phones", phoneValidator, ObjectNode.class);

        // then should be OK
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));

    }
}
