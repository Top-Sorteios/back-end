package br.com.topsorteio;

import jakarta.persistence.ExcludeSuperclassListeners;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class TopsorteioApplication {

	public static void main(String[] args) {
		SpringApplication.run(TopsorteioApplication.class, args);
	}

}
