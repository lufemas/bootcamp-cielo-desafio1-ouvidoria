package com.ouvidoria.bootcampcieloouvidoria.dto;


import com.ouvidoria.bootcampcieloouvidoria.dto.enums.FeedbackType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CustomerFeedbackRequestDTO {
    @NotBlank
    private FeedbackType type;

    @NotBlank
    private String message;
}
