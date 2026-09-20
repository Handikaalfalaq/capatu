package com.capatu.reminder_service.client;

import com.capatu.reminder_service.client.dto.RotationSnapshot;
import com.capatu.reminder_service.client.dto.ShoeSnapshot;
import com.capatu.reminder_service.constant.Constants;
import com.capatu.reminder_service.exception.ShoeServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;

@Component
public class ShoeServiceClient {

    private final RestClient restClient;

    public ShoeServiceClient(@Value("${shoe-service.base-url}") String baseUrl,
                             @Value("${shoe-service.connect-timeout-ms}") long connectTimeoutMs,
                             @Value("${shoe-service.read-timeout-ms}") long readTimeoutMs) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(connectTimeoutMs))
                .build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofMillis(readTimeoutMs));

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .build();
    }

    public List<ShoeSnapshot> fetchShoes() {
        try {
            ShoeSnapshot[] shoes = restClient.get()
                    .uri("/api/v1/shoe")
                    .retrieve()
                    .body(ShoeSnapshot[].class);
            return shoes == null ? List.of() : List.of(shoes);
        } catch (RestClientException e) {
            throw new ShoeServiceUnavailableException(Constants.SHOE_SERVICE_UNAVAILABLE, e);
        }
    }

    public RotationSnapshot fetchRotation(int days) {
        try {
            return restClient.get()
                    .uri("/api/v1/stats/rotation?days={days}", days)
                    .retrieve()
                    .body(RotationSnapshot.class);
        } catch (RestClientException e) {
            throw new ShoeServiceUnavailableException(Constants.SHOE_SERVICE_UNAVAILABLE, e);
        }
    }
}
