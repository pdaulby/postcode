package com.paul.postcode;

import com.paul.postcode.crimeLocation.CrimeLocationController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = { CrimeLocationController.class, AppConfig.class })
public class PostcodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostcodeApplication.class, args);
	}

}
