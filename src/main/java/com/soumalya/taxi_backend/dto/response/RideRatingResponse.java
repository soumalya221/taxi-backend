package com.soumalya.taxi_backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RideRatingResponse {

    private Long id;

    private Long rideId;

    private String customer;

    private String driver;

    private Integer rating;

    private String comment;
}