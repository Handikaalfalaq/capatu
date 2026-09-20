package com.capatu.shoe_service.dto.response;

public interface ShoeRanking {

    Long getShoeId();

    String getShoeName();

    Long getUsageCount();

    Long getMostUsedRank();

    Long getLeastUsedRank();
}
