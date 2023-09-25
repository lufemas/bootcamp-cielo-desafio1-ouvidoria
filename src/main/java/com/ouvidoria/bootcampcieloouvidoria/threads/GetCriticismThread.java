package com.ouvidoria.bootcampcieloouvidoria.threads;

import com.amazonaws.services.sqs.AmazonSQSClient;
import org.springframework.beans.factory.annotation.Autowired;

public class GetCriticismThread extends Thread{
    @Autowired
    private AmazonSQSClient amazonSQSClient;

    @Override
    public void run() {
        AmazonSQSClient amazonSQSClient = new AmazonSQSClient();
        ProcessMessage processMessage = new ProcessMessage();

        String queueUrl = amazonSQSClient.getQueueUrl("Criticism.fifo").getQueueUrl();
        processMessage.processMessage(queueUrl);
    }
}
