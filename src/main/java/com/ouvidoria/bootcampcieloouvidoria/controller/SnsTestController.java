package com.ouvidoria.bootcampcieloouvidoria.controller;


import com.ouvidoria.bootcampcieloouvidoria.services.SnsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@RestController
public class SnsTestController {
    @GetMapping("/snstest")
    public String snstest() {
        String message = "Esta é a minha mensagem de exemplo.";
        String topicArn = "arn:aws:sns:us-east-1:707158748474:secondTopic.fifo";
        String messageGroupId = "GrupoHello";
        try {
            SnsClient snsClient = SnsClient.builder()
                    .region(Region.US_EAST_1)
                    .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                    .build();
            SnsService.listSNSTopics(snsClient);
            SnsService.publishMessageOnTopic(snsClient, message, topicArn, messageGroupId);

            snsClient.close();
        } catch (SdkClientException e) {
            System.err.println("Erro ao criar o cliente SNS: " + e.getMessage());
            return String.format("<h1 style=\"color: red;\">ERROR:%s </h1>", e.getMessage());
        }
        return String
                .format("<h1 style=\"color: blue;\">Mensagem Enviada:</h1>" +
                        "<ul>" +
                        "<li><b>message: </b>%s</li>" +
                        "<li><b>topicArn: </b>%s</li>" +
                        "<li><b>messageGroupId: </b>%s</li>" +
                        "</ul><h1>", message, topicArn, messageGroupId);
    }
}
