package com.alfred.backoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

// TODO: Excludes only for initial purposes. Remove them as soon as possible.
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BackofficeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackofficeApplication.class, args);
		System.out.println("¡Hola mundo!");
	}

}
