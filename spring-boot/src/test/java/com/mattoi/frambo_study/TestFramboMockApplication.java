package com.mattoi.frambo_study;

import org.springframework.boot.SpringApplication;

public class TestFramboMockApplication {

	public static void main(String[] args) {
		SpringApplication.from(FramboStudyApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
