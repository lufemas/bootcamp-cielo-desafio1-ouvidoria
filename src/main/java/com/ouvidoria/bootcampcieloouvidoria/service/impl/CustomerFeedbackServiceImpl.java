package com.ouvidoria.bootcampcieloouvidoria.service.impl;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.Topic;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.*;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.ouvidoria.bootcampcieloouvidoria.dto.CustomerFeedbackResponseDTO;
import com.ouvidoria.bootcampcieloouvidoria.dto.FeedbackQueueSizeResponseDTO;
import com.ouvidoria.bootcampcieloouvidoria.dto.enums.FeedbackStatus;
import com.ouvidoria.bootcampcieloouvidoria.dto.enums.FeedbackType;
import com.ouvidoria.bootcampcieloouvidoria.service.exceptions.InvalidFeedbackTypeException;
import com.ouvidoria.bootcampcieloouvidoria.service.exceptions.TopicNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import com.ouvidoria.bootcampcieloouvidoria.dto.CustomerFeedbackRequestDTO;
import com.ouvidoria.bootcampcieloouvidoria.service.CustomerFeedbackService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerFeedbackServiceImpl implements CustomerFeedbackService {

    @Autowired
    private AmazonSNSClient amazonSNSClient;

    @Autowired
    private AmazonSQSClient amazonSQSClient;

    @Override
    public String sendFeedback(CustomerFeedbackRequestDTO feedback) {
        if (!feedback.getType().equals(FeedbackType.SUGGESTION) && !feedback.getType().equals(FeedbackType.PRAISE) && !feedback.getType().equals(FeedbackType.CRITICISM)) {
            throw new InvalidFeedbackTypeException(feedback.getType().toString());
        }

        // get topic ARN by feedback type
        String topicArn = amazonSNSClient.listTopics().getTopics().stream()
                .filter(t -> t.getTopicArn().endsWith(feedback.getType().label + ".fifo"))
                .findFirst()
                .map(Topic::getTopicArn)
                .orElse(null);
        // publish feedback message to SNS topic
        if (topicArn != null) {
            PublishRequest publishRequest = new PublishRequest(topicArn, feedback.getMessage(), feedback.getType().toString())
                                                    .withMessageDeduplicationId(UUID.randomUUID().toString())
                                                    .withMessageGroupId(feedback.getType().label);
            Map<String,MessageAttributeValue> messageAttributes = new HashMap<>();
            MessageAttributeValue type = new MessageAttributeValue();
            type.setDataType("String");
            type.setStringValue(feedback.getType().toString());
            messageAttributes.put("type", type);
            MessageAttributeValue status = new MessageAttributeValue();
            status.setDataType("String");
            status.setStringValue("RECEIVED");
            messageAttributes.put("status", status);
            publishRequest.setMessageAttributes(messageAttributes);
            amazonSNSClient.publish(publishRequest);
            return "Feedback sent successfully";
        } else {
            throw new TopicNotFoundException(feedback.getType().toString());
        }
    }

    @Override
    public List<FeedbackQueueSizeResponseDTO> getQueueSize() {
        // create a JSON object to store queue sizes
        List<FeedbackQueueSizeResponseDTO> queueSize = new ArrayList<FeedbackQueueSizeResponseDTO>();

        // get queue URLs by queue names
        String suggestionQueueUrl = amazonSQSClient.getQueueUrl("Suggestion.fifo").getQueueUrl();
        String praiseQueueUrl = amazonSQSClient.getQueueUrl("Praise.fifo").getQueueUrl();
        String criticismQueueUrl = amazonSQSClient.getQueueUrl("Criticism.fifo").getQueueUrl();

        // get queue attributes by queue URLs
        GetQueueAttributesRequest getQueueAttributesRequest = new GetQueueAttributesRequest()
                .withAttributeNames("ApproximateNumberOfMessages");
        GetQueueAttributesResult suggestionQueueAttributes = amazonSQSClient.getQueueAttributes(getQueueAttributesRequest.withQueueUrl(suggestionQueueUrl));
        GetQueueAttributesResult praiseQueueAttributes = amazonSQSClient.getQueueAttributes(getQueueAttributesRequest.withQueueUrl(praiseQueueUrl));
        GetQueueAttributesResult criticismQueueAttributes = amazonSQSClient.getQueueAttributes(getQueueAttributesRequest.withQueueUrl(criticismQueueUrl));

        // get approximate number of messages from queue attributes
        int suggestionQueueSize = Integer.parseInt(suggestionQueueAttributes.getAttributes().get("ApproximateNumberOfMessages"));
        int praiseQueueSize = Integer.parseInt(praiseQueueAttributes.getAttributes().get("ApproximateNumberOfMessages"));
        int criticismQueueSize = Integer.parseInt(criticismQueueAttributes.getAttributes().get("ApproximateNumberOfMessages"));

        FeedbackQueueSizeResponseDTO suggestionQueue = new FeedbackQueueSizeResponseDTO();
        suggestionQueue.setType(FeedbackType.SUGGESTION);
        suggestionQueue.setCount(suggestionQueueSize);
        queueSize.add(suggestionQueue);

        FeedbackQueueSizeResponseDTO criticismQueue = new FeedbackQueueSizeResponseDTO();
        criticismQueue.setType(FeedbackType.CRITICISM);
        criticismQueue.setCount(criticismQueueSize);
        queueSize.add(criticismQueue);

        FeedbackQueueSizeResponseDTO praiseQueue = new FeedbackQueueSizeResponseDTO();
        praiseQueue.setType(FeedbackType.PRAISE);
        praiseQueue.setCount(praiseQueueSize);
        queueSize.add(praiseQueue);


        // return JSON object as response body
        return queueSize;
    }

    @Override
    public List<CustomerFeedbackResponseDTO> getQueuedFeedbackByType(String type) {
        FeedbackType feedbackType;
        // validate feedback type
        if (!type.equalsIgnoreCase(FeedbackType.SUGGESTION.toString()) && !type.equalsIgnoreCase(FeedbackType.PRAISE.toString()) && !type.equalsIgnoreCase(FeedbackType.CRITICISM.toString())) {
            throw new InvalidFeedbackTypeException(type);
        } else {
            feedbackType = Arrays.stream(FeedbackType.values())
                    .filter(t -> t.name().equalsIgnoreCase(type)).findAny().orElseThrow(() -> new InvalidFeedbackTypeException(type));
        }

        // get queue URL by queue name
        System.out.println("FIFO QUEUE");
        System.out.println(feedbackType.label + ".fifo");
        String queueUrl = amazonSQSClient.getQueueUrl(feedbackType.label + ".fifo").getQueueUrl();

        // receive messages from queue without deleting them
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
                .withMaxNumberOfMessages(10) // maximum number of messages per request
                .withVisibilityTimeout(0) // make messages visible to other consumers immediately after receiving them
                .withWaitTimeSeconds(0) // do not wait for messages if queue is empty
                .withMessageAttributeNames("All"); // get all message attributes, including type and status
        ReceiveMessageResult receiveMessageResult = amazonSQSClient.receiveMessage(receiveMessageRequest);

        // convert messages to JSON array
        List<CustomerFeedbackResponseDTO> customerFeedbackResponseDTOS = new ArrayList<CustomerFeedbackResponseDTO>();
        for (Message message : receiveMessageResult.getMessages()) {
            System.out.println("MESSAGE");
            System.out.println(message.getAttributes());
            System.out.println(message.getBody());
            System.out.println(message.getMessageAttributes());

            CustomerFeedbackResponseDTO customerFeedbackResponseDTO = new CustomerFeedbackResponseDTO();
            customerFeedbackResponseDTO.setId(message.getMessageId());
            customerFeedbackResponseDTO.setType(FeedbackType.SUGGESTION);
            customerFeedbackResponseDTO.setMessage(message.getBody());
            customerFeedbackResponseDTO.setStatus(FeedbackStatus.IN_PROCESS);

            //jsonObject.put("type", message.getMessageAttributes().get("type").getStringValue());
            //jsonObject.put("status", message.getMessageAttributes().get("status").getStringValue());
            customerFeedbackResponseDTOS.add(customerFeedbackResponseDTO);
        }
        // return JSON array as response body
        return customerFeedbackResponseDTOS;
    }
}