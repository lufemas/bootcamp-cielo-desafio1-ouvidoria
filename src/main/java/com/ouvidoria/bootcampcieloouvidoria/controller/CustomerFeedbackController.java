package com.ouvidoria.bootcampcieloouvidoria.controller;


import com.ouvidoria.bootcampcieloouvidoria.dto.CustomerFeedbackRequestDTO;
import com.ouvidoria.bootcampcieloouvidoria.dto.CustomerFeedbackResponseDTO;
import com.ouvidoria.bootcampcieloouvidoria.dto.FeedbackQueueSizeResponseDTO;
import com.ouvidoria.bootcampcieloouvidoria.models.CustomerFeedbackModel;
import com.ouvidoria.bootcampcieloouvidoria.service.CustomerFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.cache.SpringCacheBasedUserCache;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class CustomerFeedbackController {

    private final CustomerFeedbackService customerFeedbackService;

    @PostMapping
    public ResponseEntity<CustomerFeedbackResponseDTO> sendFeedback(@RequestBody CustomerFeedbackRequestDTO feedback) {
        customerFeedbackService.sendFeedback(feedback);
        return new ResponseEntity<>(customerFeedbackService.createFeedbackDatabase(feedback), HttpStatus.CREATED);
    }

    @GetMapping("/size")
    public ResponseEntity<List<FeedbackQueueSizeResponseDTO>> getQueueSize() {
        return new ResponseEntity<>(customerFeedbackService.getQueueSize(), HttpStatus.OK);
    }

    @GetMapping("/{type}")
    public ResponseEntity<List<CustomerFeedbackResponseDTO>> getQueuedFeedbackByType(@PathVariable String type) {
        return new ResponseEntity<>(customerFeedbackService.getQueuedFeedbackByType(type), HttpStatus.OK);
    }

    @GetMapping("/message")
    public ResponseEntity<List<CustomerFeedbackResponseDTO>> getMessage() {
        return new ResponseEntity<>(customerFeedbackService.getMessage(), HttpStatus.OK);
    }
}
