package com.ibm.big.cachingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CachingappApplication {

	public static void main(String[] args) {
		SpringApplication.run(CachingappApplication.class, args);
	}
}
