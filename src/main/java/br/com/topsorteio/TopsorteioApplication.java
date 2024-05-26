package br.com.topsorteio;


import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;


@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class TopsorteioApplication {

	public static void main(String[] args) {
		SpringApplication.run(TopsorteioApplication.class, args);
	}

}
