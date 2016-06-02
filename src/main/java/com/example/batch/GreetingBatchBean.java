package com.example.batch;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.model.Greeting;
import com.example.service.GreetingService;

@Component
public class GreetingBatchBean {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private GreetingService greetingService;

	@Scheduled(
			cron = "0,30 * * * * *")
	public void cronJob() {
		LOGGER.info(">>>>>>>>>> cron job");

		final Collection<Greeting> greetings = greetingService.findAll();
		LOGGER.info("Found {} greetings in the repo!", greetings.size());

		LOGGER.info("<<<<<<<<<< cron job");
	}

	@Scheduled(
			initialDelay = 5000,
			fixedRate = 15000)
	public void fixedRateJobWithInitialDelay() {
		LOGGER.info("> fixedRateJobWithInitialDelay");

		final long pause = 5000;
		final long start = System.currentTimeMillis();

		do {
			if (start + pause < System.currentTimeMillis()) {
				break;
			}
		} while (true);
		LOGGER.info("Processing time was {} seconds.", pause / 1000);

		LOGGER.info("< fixedRateJobWithInitialDelay");
	}

	@Scheduled(
			initialDelay = 5000,
			fixedDelay = 15000)
	public void fixedDelayJobWithInitialDelay() {
		LOGGER.info("> fixedDelayJobWithInitialDelay");

		final long pause = 5000;
		final long start = System.currentTimeMillis();

		do {
			if (start + pause < System.currentTimeMillis()) {
				break;
			}
		} while (true);
		LOGGER.info("Processing time was {} seconds.", pause / 1000);

		LOGGER.info("< fixedDelayJobWithInitialDelay");
	}

}
