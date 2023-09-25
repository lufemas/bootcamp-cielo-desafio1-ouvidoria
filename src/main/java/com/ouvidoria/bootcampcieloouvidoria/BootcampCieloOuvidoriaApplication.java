package com.ouvidoria.bootcampcieloouvidoria;

import com.ouvidoria.bootcampcieloouvidoria.threads.GetCriticismThread;
import com.ouvidoria.bootcampcieloouvidoria.threads.GetPraiseMessageThread;
import com.ouvidoria.bootcampcieloouvidoria.threads.GetSuggestionMessageThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
public class BootcampCieloOuvidoriaApplication {
	public static void main(String[] args) {
		SpringApplication.run(BootcampCieloOuvidoriaApplication.class, args);

		// Suggestion
		GetSuggestionMessageThread suggestion = new GetSuggestionMessageThread();
		suggestion.run();

		// Praise
		GetPraiseMessageThread praise = new GetPraiseMessageThread();
		praise.run();

		// Criticism
		GetCriticismThread criticism = new GetCriticismThread();
		criticism.run();

	}
}
