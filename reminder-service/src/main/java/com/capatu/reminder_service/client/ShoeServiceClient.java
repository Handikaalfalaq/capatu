package com.capatu.reminder_service.client;

import com.capatu.reminder_service.client.dto.RotationSnapshot;
import com.capatu.reminder_service.client.dto.ShoeSnapshot;
import com.capatu.reminder_service.constant.Constants;
import com.capatu.reminder_service.exception.ShoeServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
public class ShoeServiceClient {

    private final RestClient restClient;

    public ShoeServiceClient(RestClient.Builder restClientBuilder, @Value("${shoe-service.base-url}") String baseUrl) {
        this.restClient = restClientBuilder.baseUrl(baseUrl).build();
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
