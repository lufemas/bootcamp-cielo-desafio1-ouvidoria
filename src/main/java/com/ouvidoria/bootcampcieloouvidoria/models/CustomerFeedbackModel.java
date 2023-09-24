package com.ouvidoria.bootcampcieloouvidoria.models;

import com.ouvidoria.bootcampcieloouvidoria.dto.CustomerFeedbackRequestDTO;
import com.ouvidoria.bootcampcieloouvidoria.dto.enums.FeedbackStatus;
import com.ouvidoria.bootcampcieloouvidoria.dto.enums.FeedbackType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "TB_CUSTOMER_FEEDBACKS")
public class CustomerFeedbackModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idFeedback;
    private String type;
    private String status;
    private String message;

    public CustomerFeedbackModel(CustomerFeedbackRequestDTO customer) {
        this.message = customer.getMessage();
        this.type = customer.getType().label;
        this.status = FeedbackStatus.RECEIVED.label;
    }
}