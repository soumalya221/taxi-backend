package com.soumalya.taxi_backend.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RideRatingRequest {

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be more than 5")
    private Integer rating;

    @Size(
            max = 500,
            message = "Comment cannot exceed 500 characters"
    )
    private String comment;
}