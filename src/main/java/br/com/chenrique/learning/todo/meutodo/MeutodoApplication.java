package br.com.chenrique.learning.todo.meutodo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MeutodoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeutodoApplication.class, args);
	}

}
