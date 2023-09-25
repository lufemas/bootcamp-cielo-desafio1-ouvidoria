package com.ouvidoria.bootcampcieloouvidoria.repositories;

import com.ouvidoria.bootcampcieloouvidoria.models.CustomerFeedbackModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CustomerFeedbackRepository extends JpaRepository<CustomerFeedbackModel, UUID> {
    Optional<CustomerFeedbackModel> findByMessageId(UUID messageId);
}
