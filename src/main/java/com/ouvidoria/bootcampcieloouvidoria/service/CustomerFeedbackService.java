package com.ouvidoria.bootcampcieloouvidoria.service;

import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.ouvidoria.bootcampcieloouvidoria.dto.CustomerFeedbackRequestDTO;
import com.ouvidoria.bootcampcieloouvidoria.dto.CustomerFeedbackResponseDTO;
import com.ouvidoria.bootcampcieloouvidoria.dto.FeedbackQueueSizeResponseDTO;
import com.ouvidoria.bootcampcieloouvidoria.models.CustomerFeedbackModel;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CustomerFeedbackService {
    String sendFeedback(CustomerFeedbackRequestDTO feedback);

    List<FeedbackQueueSizeResponseDTO> getQueueSize();

    List<CustomerFeedbackResponseDTO> getQueuedFeedbackByType(String type);

    CustomerFeedbackResponseDTO createFeedbackDatabase(CustomerFeedbackRequestDTO feedback);

    String getMessage(String type);
    ReceiveMessageRequest receiveMessageRequest(String queueUrl);
}
