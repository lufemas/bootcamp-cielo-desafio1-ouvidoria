package com.ouvidoria.bootcampcieloouvidoria.threads;

import com.amazonaws.services.sqs.AmazonSQSClient;
import org.springframework.beans.factory.annotation.Autowired;

public class GetPraiseMessageThread extends Thread {
    @Autowired
    private AmazonSQSClient amazonSQSClient;

    @Override
    public void run() {
        AmazonSQSClient amazonSQSClient = new AmazonSQSClient();
        ProcessMessage processMessage = new ProcessMessage();

        String queueUrl = amazonSQSClient.getQueueUrl("Praise.fifo").getQueueUrl();
        processMessage.processMessage(queueUrl);
    }
}
