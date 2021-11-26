package com.icommerce.shopping;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ShoppingApplicationTests {

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void contextLoads() {
		assertNotNull(objectMapper);
	}
}
