package com.intuit.cg.backendtechassessment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = "classpath:application.yml")
@SpringBootApplication
public class BackendTechAssessmentApplication {
	public static void main(String[] args) {
		SpringApplication.run(BackendTechAssessmentApplication.class, args);
	}

	@Bean
	public static YamlPropertySourceLoader yamlPropertySourceLoader() {
		return new YamlPropertySourceLoader();
	}
}
