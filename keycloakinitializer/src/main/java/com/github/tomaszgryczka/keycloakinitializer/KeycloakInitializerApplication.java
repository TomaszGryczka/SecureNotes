package com.github.tomaszgryczka.keycloakinitializer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.github.tomaszgryczka.keycloakinitializer.config")
public class KeycloakInitializerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeycloakInitializerApplication.class, args);
	}

}
