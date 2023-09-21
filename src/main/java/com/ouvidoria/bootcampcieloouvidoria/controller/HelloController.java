package com.ouvidoria.bootcampcieloouvidoria.controller;


import com.ouvidoria.bootcampcieloouvidoria.services.HelloSNS;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {

        try {
            SnsClient snsClient = SnsClient.builder()
                    .region(Region.US_EAST_1)
                    .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                    .build();

            String message = "Esta é a minha mensagem de exemplo.";
            String topicArn = "arn:aws:sns:us-east-1:707158748474:firstTopic.fifo\n";

            HelloSNS.listSNSTopics(snsClient);
            HelloSNS.pubTopic(snsClient, message, topicArn);

            snsClient.close();
        } catch (SdkClientException e) {
            System.err.println("Erro ao criar o cliente SNS: " + e.getMessage());
            return String.format("<h1 style=\"color: red;\">ERROR:%s <h1>", e.getMessage());
        }

        return String.format("Hello %s!", name);
    }
}
