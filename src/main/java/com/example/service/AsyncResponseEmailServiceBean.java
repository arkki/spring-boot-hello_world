package com.example.service;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import com.example.model.Greeting;
import com.example.util.AsyncResponse;

/**
 * The EmailServiceBean implements all business behaviors defined by the
 * EmailService interface.
 *
 * @author Matt Warman
 *
 * @deprecated Use JDK 1.8 CompletableFuture instead! Check class
 *             CompletableFutureEmailServiceBean.java
 */
// @Service
@Deprecated
public class AsyncResponseEmailServiceBean implements EmailService {

	/**
	 * The Logger for this class.
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Boolean send(final Greeting greeting) {
		logger.info("> send");

		Boolean success = Boolean.FALSE;

		// Simulate method execution time
		final long pause = 5000;
		try {
			Thread.sleep(pause);
		} catch (final Exception e) {
			// do nothing
		}
		logger.info("Processing time was {} seconds.", pause / 1000);

		success = Boolean.TRUE;

		logger.info("< send");
		return success;
	}

	@Async
	@Override
	public void sendAsync(final Greeting greeting) {
		logger.info("> sendAsync");

		try {
			send(greeting);
		} catch (final Exception e) {
			logger.warn("Exception caught sending asynchronous mail.", e);
		}

		logger.info("< sendAsync");
	}

	@Async
	@Override
	public Future<Boolean> sendAsyncWithResult(final Greeting greeting) {
		logger.info("> sendAsyncWithResult");

		final AsyncResponse<Boolean> response = new AsyncResponse<Boolean>();

		try {
			final Boolean success = send(greeting);
			response.complete(success);
		} catch (final Exception e) {
			logger.warn("Exception caught sending asynchronous mail.", e);
			response.completeExceptionally(e);
		}

		logger.info("< sendAsyncWithResult");
		return response;
	}

}