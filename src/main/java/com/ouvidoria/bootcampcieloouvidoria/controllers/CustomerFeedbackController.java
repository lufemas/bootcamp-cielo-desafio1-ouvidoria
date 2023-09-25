package com.ouvidoria.bootcampcieloouvidoria.controllers;

import com.ouvidoria.bootcampcieloouvidoria.dtos.CustomerFeedbackRequestDto;
import com.ouvidoria.bootcampcieloouvidoria.dtos.CustomerFeedbackResponseDto;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feedback")
public class CustomerFeedbackController {
    @PostMapping
    public ResponseEntity<Object> saveCustomerFeedback(@RequestBody CustomerFeedbackRequestDto customerFeedbackRequestDto) {
        CustomerFeedbackRequestDto customer = new CustomerFeedbackRequestDto(customerFeedbackRequestDto.type(), customerFeedbackRequestDto.message());
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }
}