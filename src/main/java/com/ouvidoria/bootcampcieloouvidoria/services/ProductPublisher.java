package com.ouvidoria.bootcampcieloouvidoria.services;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class ProductPublisher {
    private static final Logger LOG = LoggerFactory.getLogger(
            ProductPublisher.class);

    private AmazonSNS snsClient;
    private Topic productEventsTopic;

    public ProductPublisher(AmazonSNS snsClient,
                            @Qualifier("productEventsTopic")Topic productEventsTopic) {
        this.snsClient = snsClient;
        this.productEventsTopic = productEventsTopic;
    }

    public void publishProductEvent(String message) {
        PublishResult publishResult = snsClient.publish(
                productEventsTopic.getTopicArn(),
                message);

        LOG.info("MessageId: {}", publishResult.getMessageId());
    }
}
