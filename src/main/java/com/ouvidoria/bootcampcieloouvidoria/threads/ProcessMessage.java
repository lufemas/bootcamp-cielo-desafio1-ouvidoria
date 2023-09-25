package com.ouvidoria.bootcampcieloouvidoria.threads;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

public class ProcessMessage {

    public String processMessage(String queueUrl) {
        AmazonSQSClient amazonSQSClient = new AmazonSQSClient();
        while (true) {
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
                    .withMaxNumberOfMessages(10) // maximum number of messages per request
                    .withVisibilityTimeout(20) // make messages visible to other consumers immediately after receiving them
                    .withWaitTimeSeconds(20) // do not wait for messages if queue is empty
                    .withMessageAttributeNames("All"); // get all message attributes, including type and status

            var receiveMessageResult = amazonSQSClient.receiveMessage(receiveMessageRequest);

            for (Message message : receiveMessageResult.getMessages()) {
                // Processar a mensagem aqui
                System.out.println("Mensagem recebida: " + message.getBody());
                System.out.println("*****PROCESSANDO MENSAGEM*****");
                // Exemplo de processamento simulado
                try {
                    Thread.sleep(1000); // Simular algum processamento
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Excluir a mensagem da fila após processamento bem-sucedido
                amazonSQSClient.deleteMessage(queueUrl, message.getReceiptHandle());
            }
        }
    }

}
