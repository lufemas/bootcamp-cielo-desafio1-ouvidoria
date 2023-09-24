package com.ouvidoria.bootcampcieloouvidoria.dto;

import com.ouvidoria.bootcampcieloouvidoria.dto.enums.FeedbackStatus;
import com.ouvidoria.bootcampcieloouvidoria.dto.enums.FeedbackType;
import com.ouvidoria.bootcampcieloouvidoria.models.CustomerFeedbackModel;
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
    private String type;

    @NotBlank
    private String message;

    @NotBlank
    private String status;

    public CustomerFeedbackResponseDTO(CustomerFeedbackModel customer) {
        this.message = customer.getMessage();
        this.type = customer.getType();
        this.id = String.valueOf(customer.getIdFeedback());
        this.status = customer.getStatus();
    }


}
