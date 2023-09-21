package com.ouvidoria.bootcampcieloouvidoria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BootcampCieloOuvidoriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootcampCieloOuvidoriaApplication.class, args);

//		SnsClient snsClient = SnsClient.builder()
//				.region(Region.US_EAST_1) // Substitua pela região desejada
//				.credentialsProvider(EnvironmentVariableCredentialsProvider.create())
//				.build();
//
//		String message = "Esta é a minha mensagem de exemplo.";
//		String topicArn = "arn:aws:sns:YOUR_REGION:YOUR_ACCOUNT_ID:YOUR_TOPIC_NAME";
//
//		HelloSNS.listSNSTopics(snsClient);
//		HelloSNS.pubTopic(snsClient, message, topicArn);
//
//		snsClient.close();
	}

}
