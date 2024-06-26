package com.fsad.bookservice.utils;

import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Objects;

@Component
public class BookUtil {

  RestTemplate restTemplate;

  private static final String TOKEN_VALIDATE_URL = "http://localhost:9000/token/validate";

  public ResponseEntity<Long> validateToken(String token) {
    restTemplate = new RestTemplateBuilder()
        .setReadTimeout(Duration.ofMinutes(2L))
        .build();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("token", token);
    HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), headers);
    try {
      ResponseEntity<String> response = restTemplate.postForEntity(TOKEN_VALIDATE_URL, request, String.class);
      if (response.getStatusCode().value() == HttpStatus.OK.value()) {
        return new ResponseEntity<>(Long.parseLong(Objects.requireNonNull(response.getBody())), HttpStatus.OK);
      }
    } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
      return new ResponseEntity<>(0L, httpClientOrServerExc.getStatusCode());
    } catch (RestClientException | NumberFormatException e) {
      return new ResponseEntity<>(0L, HttpStatus.UNAUTHORIZED);
    }

    return new ResponseEntity<>(0L, HttpStatus.UNAUTHORIZED);
  }
}
