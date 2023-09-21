package com.ouvidoria.bootcampcieloouvidoria.dtos;

import com.ouvidoria.bootcampcieloouvidoria.dtos.enums.Status;
import com.ouvidoria.bootcampcieloouvidoria.dtos.enums.Type;
import jakarta.validation.constraints.NotBlank;

public record CustomerFeedbackResponseDto(Type type, String message, Status status) {
}
