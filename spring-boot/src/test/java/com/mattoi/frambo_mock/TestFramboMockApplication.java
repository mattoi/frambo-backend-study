package com.mattoi.frambo_mock;

import org.springframework.boot.SpringApplication;

import com.mattoi.frambo_study.FramboStudyApplication;

public class TestFramboMockApplication {

	public static void main(String[] args) {
		SpringApplication.from(FramboStudyApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
