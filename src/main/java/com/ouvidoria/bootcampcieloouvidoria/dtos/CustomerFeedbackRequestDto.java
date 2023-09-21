package com.ouvidoria.bootcampcieloouvidoria.dtos;

import com.ouvidoria.bootcampcieloouvidoria.dtos.enums.Type;
import jakarta.validation.constraints.NotBlank;

public record CustomerFeedbackRequestDto(@NotBlank Type type, @NotBlank String message) {
}