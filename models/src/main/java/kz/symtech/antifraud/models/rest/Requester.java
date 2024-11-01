package kz.symtech.antifraud.models.rest;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;

@Component
public class Requester {
    private Requester(){}
    public static <T> T get(String uri, String auth, @Nullable Map<String, String> params) throws RuntimeException {
        byte[] encodedAuth = Base64.getEncoder().encode(
                auth.getBytes(StandardCharsets.US_ASCII));

        String authHeader = "Basic " + new String( encodedAuth );
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        ArrayList<Charset> charsets = new ArrayList<>();
        charsets.add(StandardCharsets.UTF_8);
        headers.setAcceptCharset(charsets);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authHeader);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(uri);
        if (params != null){
            params.forEach(uriBuilder::queryParam);
        }
        String buildedURL = uriBuilder.toUriString();
        ResponseEntity<T> response = template.exchange(
                buildedURL,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<T>() {}
        );
        return response.getBody();
    }

    public static  <T, R> T post(String uri, R requestBody, String auth) throws RuntimeException{
        byte[] encodedAuth = Base64.getEncoder().encode(
                auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        ArrayList<Charset> charsets = new ArrayList<>();
        charsets.add(StandardCharsets.UTF_8);
        headers.setAcceptCharset(charsets);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authHeader);
        HttpEntity<R> requestEntity = new HttpEntity<>(requestBody, headers);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(uri);
        String buildedURL = uriBuilder.toUriString();
        ResponseEntity<T> response = template.exchange(
                buildedURL,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<T>() {}
        );
        return response.getBody();
    }
}
