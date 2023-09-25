package com.ouvidoria.bootcampcieloouvidoria.service.impl;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.Topic;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.*;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.ouvidoria.bootcampcieloouvidoria.dto.CustomerFeedbackResponseDTO;
import com.ouvidoria.bootcampcieloouvidoria.dto.FeedbackQueueSizeResponseDTO;
import com.ouvidoria.bootcampcieloouvidoria.dto.enums.FeedbackStatus;
import com.ouvidoria.bootcampcieloouvidoria.dto.enums.FeedbackType;
import com.ouvidoria.bootcampcieloouvidoria.models.CustomerFeedbackModel;
import com.ouvidoria.bootcampcieloouvidoria.repositories.CustomerFeedbackRepository;
import com.ouvidoria.bootcampcieloouvidoria.service.exceptions.InvalidFeedbackTypeException;
import com.ouvidoria.bootcampcieloouvidoria.service.exceptions.TopicNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.ouvidoria.bootcampcieloouvidoria.dto.CustomerFeedbackRequestDTO;
import com.ouvidoria.bootcampcieloouvidoria.service.CustomerFeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.sound.midi.SysexMessage;
import java.util.*;

@Service
public class CustomerFeedbackServiceImpl implements CustomerFeedbackService {

    @Autowired
    private AmazonSNSClient amazonSNSClient;

    @Autowired
    private AmazonSQSClient amazonSQSClient;

    @Autowired
    private CustomerFeedbackRepository customerFeedbackRepository;

    @Override
    public String sendFeedback(CustomerFeedbackRequestDTO feedback) {
        if (!feedback.getType().equals(FeedbackType.SUGGESTION) && !feedback.getType().equals(FeedbackType.PRAISE) && !feedback.getType().equals(FeedbackType.CRITICISM)) {
            throw new InvalidFeedbackTypeException(feedback.getType().toString());
        }

        PublishResult messagePublished = publishSns(feedback);
        feedback.setIdMessage(UUID.fromString(messagePublished.getMessageId()));

        createFeedbackDatabase(feedback);

        return "Feedback sent successfully";
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
        String queueUrl = amazonSQSClient.getQueueUrl(feedbackType.label + ".fifo").getQueueUrl();

        ReceiveMessageRequest receivedMessage = receiveMessageRequest(queueUrl);

        ReceiveMessageResult receiveMessageResult = amazonSQSClient.receiveMessage(receivedMessage);

        List<CustomerFeedbackResponseDTO> customerFeedbackResponseDTOS = getCustomerFeedbackResponseDTOS(receiveMessageResult);
        // return JSON array as response body
        return customerFeedbackResponseDTOS;
    }

    private static List<CustomerFeedbackResponseDTO> getCustomerFeedbackResponseDTOS(ReceiveMessageResult receiveMessageResult) {
        List<CustomerFeedbackResponseDTO> customerFeedbackResponseDTOS = new ArrayList<CustomerFeedbackResponseDTO>();
        for (Message message : receiveMessageResult.getMessages()) {

            CustomerFeedbackResponseDTO customerFeedbackResponseDTO = new CustomerFeedbackResponseDTO();
            customerFeedbackResponseDTO.setId(message.getMessageId());
            customerFeedbackResponseDTO.setType(FeedbackType.SUGGESTION.label);
            customerFeedbackResponseDTO.setMessage(message.getBody());
            customerFeedbackResponseDTO.setStatus(FeedbackStatus.IN_PROCESS.label);
            customerFeedbackResponseDTO.setIdMessage(UUID.fromString(message.getMessageId()));

            customerFeedbackResponseDTOS.add(customerFeedbackResponseDTO);
        }
        return customerFeedbackResponseDTOS;
    }

    @Override
    public CustomerFeedbackResponseDTO createFeedbackDatabase(CustomerFeedbackRequestDTO feedback) {
        CustomerFeedbackModel customerFeedbackModel = customerFeedbackRepository.save(new CustomerFeedbackModel(feedback));

        CustomerFeedbackResponseDTO customer = new CustomerFeedbackResponseDTO(customerFeedbackModel);

        return customer;
    }


    @Override
    public String getMessage(String type) {
        String queueUrl = amazonSQSClient.getQueueUrl(type +".fifo").getQueueUrl();
        ReceiveMessageRequest receivedMessage = receiveMessageRequest(queueUrl);

        List<Message> messages = amazonSQSClient.receiveMessage(receivedMessage).getMessages();
        Message messageToProcess = messages.get(0);
        CustomerFeedbackResponseDTO customerResponse = new CustomerFeedbackResponseDTO();

        messageToProcess.getAttributes();
        messageToProcess.getBody();
        messageToProcess.getMessageAttributes();
        var receiptHandle =  messageToProcess.getReceiptHandle();

        CustomerFeedbackResponseDTO customerFeedbackResponseDTO = new CustomerFeedbackResponseDTO();
        customerResponse.setId(messageToProcess.getMessageId());
        customerResponse.setType(FeedbackType.SUGGESTION.label);
        customerResponse.setMessage(messageToProcess.getBody());
        customerResponse.setStatus(FeedbackStatus.FINALIZED.label);

        Optional<CustomerFeedbackModel> customerRepo = customerFeedbackRepository.findByMessageId(UUID.fromString(messageToProcess.getMessageId()));
        if(customerRepo.isEmpty()) {
            return null;
        }

        amazonSQSClient.deleteMessage(queueUrl,receiptHandle);

        return "Message process successfully";
    }

    public ReceiveMessageRequest receiveMessageRequest(String queueUrl) {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
                .withMaxNumberOfMessages(10) // maximum number of messages per request
                .withVisibilityTimeout(20) // make messages visible to other consumers immediately after receiving them
                .withWaitTimeSeconds(20) // do not wait for messages if queue is empty
                .withMessageAttributeNames("All"); // get all message attributes, including type and status
        return receiveMessageRequest;
    }

    public PublishResult publishSns(CustomerFeedbackRequestDTO feedback) {
        // get topic ARN by feedback type
        String topicArn = amazonSNSClient.listTopics().getTopics().stream()
                .filter(t -> t.getTopicArn().endsWith(feedback.getType().label + ".fifo"))
                .findFirst()
                .map(Topic::getTopicArn)
                .orElse(null);
        // publish feedback message to SNS topic
        if (topicArn == null) {
            throw new TopicNotFoundException(feedback.getType().toString());
        }

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

        PublishResult publishedMessage = amazonSNSClient.publish(publishRequest);
        return publishedMessage;
    }
}
