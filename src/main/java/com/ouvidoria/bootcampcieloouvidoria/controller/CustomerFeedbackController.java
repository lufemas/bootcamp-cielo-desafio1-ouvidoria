package com.ouvidoria.bootcampcieloouvidoria.controller;


import com.ouvidoria.bootcampcieloouvidoria.dto.CustomerFeedbackRequestDTO;
import com.ouvidoria.bootcampcieloouvidoria.dto.CustomerFeedbackResponseDTO;
import com.ouvidoria.bootcampcieloouvidoria.dto.FeedbackQueueSizeResponseDTO;
import com.ouvidoria.bootcampcieloouvidoria.service.CustomerFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CustomerFeedbackController {

    private final CustomerFeedbackService customerFeedbackService;

    @PostMapping
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> sendFeedback(@RequestBody CustomerFeedbackRequestDTO feedback) {
        return new ResponseEntity<>(customerFeedbackService.sendFeedback(feedback), HttpStatus.CREATED);
    }

    @GetMapping("/size")
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<FeedbackQueueSizeResponseDTO>> getQueueSize() {
        return new ResponseEntity<>(customerFeedbackService.getQueueSize(), HttpStatus.OK);
    }

    @GetMapping("/{type}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<CustomerFeedbackResponseDTO>> getQueuedFeedbackByType(@PathVariable String type) {
        return new ResponseEntity<>(customerFeedbackService.getQueuedFeedbackByType(type), HttpStatus.OK);
    }
}
