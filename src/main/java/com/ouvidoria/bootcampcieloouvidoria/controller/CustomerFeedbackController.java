package com.ouvidoria.bootcampcieloouvidoria.controller;


import com.ouvidoria.bootcampcieloouvidoria.dto.CustomerFeedbackRequestDTO;
import com.ouvidoria.bootcampcieloouvidoria.dto.CustomerFeedbackResponseDTO;
import com.ouvidoria.bootcampcieloouvidoria.dto.FeedbackQueueSizeResponseDTO;
import com.ouvidoria.bootcampcieloouvidoria.models.CustomerFeedbackModel;
import com.ouvidoria.bootcampcieloouvidoria.service.CustomerFeedbackService;
import com.ouvidoria.bootcampcieloouvidoria.threads.GetPraiseMessageThread;
import com.ouvidoria.bootcampcieloouvidoria.threads.GetSuggestionMessageThread;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class CustomerFeedbackController {

    private final CustomerFeedbackService customerFeedbackService;
    public GetSuggestionMessageThread suggestionMessage;


    @PostMapping
    public ResponseEntity<String> sendFeedback(@RequestBody CustomerFeedbackRequestDTO feedback) {
        //customerFeedbackService.createFeedbackDatabase(feedback);
        return new ResponseEntity<>(customerFeedbackService.sendFeedback(feedback), HttpStatus.CREATED);
    }

    @GetMapping("/size")
    public ResponseEntity<List<FeedbackQueueSizeResponseDTO>> getQueueSize() {
        return new ResponseEntity<>(customerFeedbackService.getQueueSize(), HttpStatus.OK);
    }

    @GetMapping("/{type}")
    public ResponseEntity<List<CustomerFeedbackResponseDTO>> getQueuedFeedbackByType(@PathVariable String type) {
        return new ResponseEntity<>(customerFeedbackService.getQueuedFeedbackByType(type), HttpStatus.OK);
    }


    @GetMapping("/message/{type}")
    public ResponseEntity<String> getMessage(@PathVariable String type ) {
        return new ResponseEntity<>(customerFeedbackService.getMessage(type), HttpStatus.OK);
    }
}
