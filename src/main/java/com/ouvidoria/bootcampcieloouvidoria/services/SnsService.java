package com.ouvidoria.bootcampcieloouvidoria.services;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;
import software.amazon.awssdk.services.sns.paginators.ListTopicsIterable;

import java.util.Map;

public class SnsService {

    public static void main(String[] args) {
        // Configura e inicializa o cliente SNS
        SnsClient snsClient = SnsClient.builder()
                .region(Region.US_EAST_1) // Define a região da AWS
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create()) // Carrega as credenciais do ambiente
                .build();

        // Chama a função para listar tópicos
        listSNSTopics(snsClient);

        // Fecha o cliente SNS
        snsClient.close();
    }

    /**
     * Lista os tópicos SNS e imprime os ARNs no console.
     *
     * @param snsClient  Cliente SNS
     */
    public static void listSNSTopics(SnsClient snsClient) {
        try {
            ListTopicsIterable listTopics = snsClient.listTopicsPaginator();
            listTopics.stream()
                    .flatMap(r -> r.topics().stream())
                    .forEach(content -> System.out.println(" Topic ARN: " + content.topicArn()));

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    /**
     * Publica uma mensagem em um tópico SNS FIFO.
     *
     * @param snsClient       Cliente SNS
     * @param message         Mensagem a ser publicada
     * @param topicArn        ARN do tópico SNS
     * @param messageGroupId  ID de grupo da mensagem (necessário para tópicos FIFO)
     *
     * @example
     *
     *             SnsClient snsClient = SnsClient.builder()
     *                     .region(Region.US_EAST_1)
     *                     .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
     *                     .build();
     *             String message = "Esta é a minha mensagem de exemplo.";
     *             String topicArn = "arn:aws:sns:us-east-1:707158748474:secondTopic.fifo";
     *             String messageGroupId = "GrupoHello";
     *
     *             SnsService.pubTopic(snsClient, message, topicArn, messageGroupId);
     */
    public static void publishMessageOnTopic(SnsClient snsClient, String message, String topicArn, String messageGroupId) {

        try {
            PublishRequest request = PublishRequest.builder()
                    .message(message)
                    .topicArn(topicArn)
                    .messageGroupId(messageGroupId) // Define o ID de grupo da mensagem (necessário para tópicos FIFO)
                    .build();

            PublishResponse result = snsClient.publish(request);
            System.out.println(result.messageId() + " Message sent. Status is " + result.sdkHttpResponse().statusCode());

        } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}
