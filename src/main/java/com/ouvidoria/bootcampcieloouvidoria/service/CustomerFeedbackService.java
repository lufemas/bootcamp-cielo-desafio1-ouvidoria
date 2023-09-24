package com.ouvidoria.bootcampcieloouvidoria.service;

import com.ouvidoria.bootcampcieloouvidoria.dto.CustomerFeedbackRequestDTO;
import com.ouvidoria.bootcampcieloouvidoria.dto.CustomerFeedbackResponseDTO;
import com.ouvidoria.bootcampcieloouvidoria.dto.FeedbackQueueSizeResponseDTO;
import com.ouvidoria.bootcampcieloouvidoria.models.CustomerFeedbackModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerFeedbackService {
    String sendFeedback(CustomerFeedbackRequestDTO feedback);

    List<FeedbackQueueSizeResponseDTO> getQueueSize();

    List<CustomerFeedbackResponseDTO> getQueuedFeedbackByType(String type);

    CustomerFeedbackResponseDTO createFeedbackDatabase(CustomerFeedbackRequestDTO feedback);
}
