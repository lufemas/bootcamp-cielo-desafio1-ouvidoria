package com.ouvidoria.bootcampcieloouvidoria.services;

import org.springframework.stereotype.Component;

@Component
public class SnsService {
    String topicArn = "<Enter your topic ARN>";

    private SnsClient getSnsClient() {
        return SnsClient.builder()
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .region(Region.US_WEST_2)
                .build();
    }

    public String pubTopic(String message, String lang) {
        try {
            String body;

            SnsClient snsClient = getSnsClient();
            PublishRequest request = PublishRequest.builder()
                    .message(body)
                    .topicArn(topicArn)
                    .build();

            PublishResponse result = snsClient.publish(request);
            return " Message sent in " + lang + ". Status was " + result.sdkHttpResponse().statusCode();

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "Error - msg not sent";
    }
}
