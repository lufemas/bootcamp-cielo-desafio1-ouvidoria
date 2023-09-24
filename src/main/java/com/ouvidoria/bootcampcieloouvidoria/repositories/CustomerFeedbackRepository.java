package com.ouvidoria.bootcampcieloouvidoria.repositories;

import com.ouvidoria.bootcampcieloouvidoria.models.CustomerFeedbackModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerFeedbackRepository extends JpaRepository<CustomerFeedbackModel, UUID> {
}
