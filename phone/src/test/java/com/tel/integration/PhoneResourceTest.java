package com.tel.integration;

import com.tel.domain.PhoneValidator;
import com.tel.resource.CountryCodeResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.List;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PhoneResourceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void validPhoneNumber() {
        ResponseEntity<CountryCodeResponse> response = countryCode("37165432176");

        assertThat(response.getStatusCodeValue(), is(200));
        assertTrue(response.hasBody());
        assertThat(response.getBody().getCountry(), is("Latvia"));
        assertThat(response.getBody().getPhone(), is("37165432176"));
    }

    @Test
    public void invalidPhoneNumber() {
        ResponseEntity<List<PhoneValidator.PhoneValidationError>> response = countryCodeErrors("3716543L2176");

        assertThat(response.getStatusCodeValue(), is(400));
        assertTrue(response.hasBody());
        assertTrue(response.getBody().size() == 1);
        assertThat(response.getBody().get(0).getCode(), is("phone.onlyDigitsAllowed"));
        assertThat(response.getBody().get(0).getMessage(), is("Phone number contains forbidden symbols"));
    }

    private ResponseEntity<CountryCodeResponse> countryCode(String phone) {
        return restTemplate.getForEntity("/phones/{phone}/country-code", CountryCodeResponse.class, phone);
    }

    private ResponseEntity<List<PhoneValidator.PhoneValidationError>> countryCodeErrors(String phone) {
        RequestEntity request = RequestEntity.get(URI.create(format("/phones/%s/country-code", phone))).build();
        ParameterizedTypeReference<List<PhoneValidator.PhoneValidationError>> responseType = new ParameterizedTypeReference<List<PhoneValidator.PhoneValidationError>>() {};
        return restTemplate.exchange(request, responseType);
    }

}
