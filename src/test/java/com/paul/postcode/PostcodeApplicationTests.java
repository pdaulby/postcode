package com.paul.postcode;

import com.paul.postcode.crimeLocation.CrimeLocationController;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
class PostcodeApplicationTests {

	@Autowired
	private CrimeLocationController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
