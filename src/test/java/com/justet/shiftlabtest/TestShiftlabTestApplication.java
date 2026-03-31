package com.justet.shiftlabtest;

import org.springframework.boot.SpringApplication;

public class TestShiftlabTestApplication {

	public static void main(String[] args) {
		SpringApplication.from(ShiftlabTestApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
