package com.mattoi.frambo_mock;

import org.springframework.boot.SpringApplication;

public class TestFramboMockApplication {

	public static void main(String[] args) {
		SpringApplication.from(FramboMockApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
