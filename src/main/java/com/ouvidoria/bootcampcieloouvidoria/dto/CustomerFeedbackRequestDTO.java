package com.ouvidoria.bootcampcieloouvidoria.dto;


import com.ouvidoria.bootcampcieloouvidoria.dto.enums.FeedbackType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class CustomerFeedbackRequestDTO {
    @NotBlank
    private FeedbackType type;

    @NotBlank
    private String message;

    private UUID idMessage;
}
