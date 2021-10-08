package br.com.blackbeard.blackbeardapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlackbeardApiApplication {

	public static void main(String[] args) {
		System.setProperty("user.timezone", "America/Sao_Paulo");
		SpringApplication.run(BlackbeardApiApplication.class, args);
	}

}
