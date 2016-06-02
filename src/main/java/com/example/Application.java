package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
public class Application {

	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * In-memory ConcurrentMapCacheManager - for dev use only!
	 */
	@Bean
	public CacheManager cacheManager() {
		final GuavaCacheManager cacheManager = new GuavaCacheManager("greetings");
		return cacheManager;
	}
}
