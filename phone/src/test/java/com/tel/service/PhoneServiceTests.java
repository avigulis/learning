package com.tel.service;

import com.tel.domain.CountryCodeProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PhoneServiceTests {

    @Autowired
    CountryCodeProvider phoneService;

    @Test
    public void testGetExactNumber() {
        // when given number 371
        Optional<String> country = phoneService.get(BigInteger.valueOf(371));

        // then should return Latvia
        assertThat(country.get(), notNullValue());
        assertThat(country.get(), is("Latvia"));

        // when given number 7
        country = phoneService.get(BigInteger.valueOf(7));

        // then should return Russia
        assertThat(country.get(), notNullValue());
        assertThat(country.get(), is("Russia"));
    }


    @Test
    public void testGetApproximateNumber() {
        // when given valid number 37129905454
        Optional<String> country = phoneService.get(BigInteger.valueOf(37129905454L));

        // then should return Latvia
        assertThat(country.get(), notNullValue());
        assertThat(country.get(), is("Latvia"));

        // when given valid number 74957397000
        country = phoneService.get(BigInteger.valueOf(74957397000L));

        // then should return Russia
        assertThat(country.get(), notNullValue());
        assertThat(country.get(), is("Russia"));
    }
}
