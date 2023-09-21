package com.ouvidoria.bootcampcieloouvidoria;

import com.ouvidoria.bootcampcieloouvidoria.services.SNSService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BootcampCieloOuvidoriaApplication {

	public static void main(String[] args) {
//		String accessKey = "your_access_key";
//		String secretKey = "your_secret_key";
		String accessKey = "AKIA2JJPSFU5FZM3V6V3"; //System.getenv("AWS_ACCESS_KEY_ID");
		String secretKey = "H1elCczbmXR4QXyIzAq+Lav2+JZuA/9FHxhHhz68"; //System.getenv("AWS_SECRET_ACCESS_KEY");
		String region = "us-east-1";
		String topicArn = "arn:aws:sns:us-east-1:707158748474:firstTopic.fifo";
		String message = "Hello, AWS SNS!";

		SNSService snsService = new SNSService(accessKey, secretKey, region);

		snsService.publishMessage(topicArn, message);

		snsService.shutdown();
	}

}
