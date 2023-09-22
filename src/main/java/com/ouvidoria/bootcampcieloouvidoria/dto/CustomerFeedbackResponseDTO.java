package com.ouvidoria.bootcampcieloouvidoria.dto;

import com.ouvidoria.bootcampcieloouvidoria.dto.enums.FeedbackStatus;
import com.ouvidoria.bootcampcieloouvidoria.dto.enums.FeedbackType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;


@Data
@RequiredArgsConstructor
public class CustomerFeedbackResponseDTO {

    @NotBlank
    private String id;

    @NotBlank
    private FeedbackType type;

    @NotBlank
    private String message;

    @NotBlank
    private FeedbackStatus status;
}
