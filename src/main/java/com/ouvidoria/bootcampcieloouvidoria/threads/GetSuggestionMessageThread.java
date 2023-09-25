package com.ouvidoria.bootcampcieloouvidoria.threads;


import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

public class GetSuggestionMessageThread extends Thread {
    @Autowired
    private AmazonSQSClient amazonSQSClient;

    @Override
    public void run() {
        AmazonSQSClient amazonSQSClient = new AmazonSQSClient();
        ProcessMessage processMessage = new ProcessMessage();

            String queueUrl = amazonSQSClient.getQueueUrl("Suggestion.fifo").getQueueUrl();
            processMessage.processMessage(queueUrl);

    }
}
