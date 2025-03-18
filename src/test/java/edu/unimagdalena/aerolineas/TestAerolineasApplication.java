package edu.unimagdalena.aerolineas;

import org.springframework.boot.SpringApplication;

public class TestAerolineasApplication {

	public static void main(String[] args) {
		SpringApplication.from(AerolineasApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
