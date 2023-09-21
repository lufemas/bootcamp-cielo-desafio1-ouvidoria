package com.ouvidoria.bootcampcieloouvidoria.services;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

public class SNSService {

    private AmazonSNS snsClient;

    public SNSService(String accessKey, String secretKey, String region) {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

        snsClient = AmazonSNSClient.builder()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(region)
                .build();
    }

    public void publishMessage(String topicArn, String message) {
        PublishRequest request = new PublishRequest()
                .withTopicArn(topicArn)
                .withMessage(message);

        PublishResult result = snsClient.publish(request);

        System.out.println("Message sent. Message ID: " + result.getMessageId());
    }

    public void shutdown() {
        snsClient.shutdown();
    }
}