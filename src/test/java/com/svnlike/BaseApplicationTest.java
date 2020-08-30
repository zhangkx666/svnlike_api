package com.svnlike;

import com.svnlike.api.ApiApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedHashMap;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BaseApplicationTest {

    @Autowired
    public TestRestTemplate testRestTemplate;

    /**
     * get json http entity
     * @param params HashMap
     * @return HttpEntity
     */
    protected HttpEntity<LinkedHashMap> getJsonHttpEntity(LinkedHashMap params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(params, headers);
    }

    @Test
    public void test() {
        //
    }
}
