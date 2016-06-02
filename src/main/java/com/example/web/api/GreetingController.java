package com.example.web.api;

import java.util.Collection;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Greeting;
import com.example.service.EmailService;
import com.example.service.GreetingService;

@RestController
public class GreetingController {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private GreetingService greetingService;

	@Autowired
	private EmailService emailService;

	@RequestMapping(
			value = "/api/greetings",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Greeting>> getGreetings() {
		return new ResponseEntity<>(greetingService.findAll(), HttpStatus.OK);
	}

	@RequestMapping(
			value = "/api/greetings/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Greeting> getGreeting(@PathVariable("id") final Long id) {
		final Greeting greeting = greetingService.findOne(id);
		if (greeting == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(greeting, HttpStatus.OK);
	}

	@RequestMapping(
			value = "/api/greetings",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Greeting> createGreeting(@RequestBody final Greeting greeting) {
		final Greeting savedGreeting = greetingService.create(greeting);
		return new ResponseEntity<>(savedGreeting, HttpStatus.CREATED);
	}

	@RequestMapping(
			value = "/api/greetings",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Greeting> updateGreeting(@RequestBody final Greeting greeting) {

		final Greeting updatedGreeting = greetingService.update(greeting);
		if (updatedGreeting == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity<>(updatedGreeting, HttpStatus.CREATED);
	}

	@RequestMapping(
			value = "/api/greetings/{id}",
			method = RequestMethod.DELETE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Greeting> deleteGreeting(@PathVariable("id") final Long id,
			@RequestBody final Greeting greeting) {
		greetingService.delete(id);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);

	}

	/**
	 * Web service endpoint to fetch a single Greeting entity by primary key
	 * identifier and send it as an email.
	 *
	 * If found, the Greeting is returned as JSON with HTTP status 200 and sent
	 * via Email.
	 *
	 * If not found, the service returns an empty response body with HTTP status
	 * 404.
	 *
	 * @param id
	 *            A Long URL path variable containing the Greeting primary key
	 *            identifier.
	 * @param waitForAsyncResult
	 *            A boolean indicating if the web service should wait for the
	 *            asynchronous email transmission.
	 * @return A ResponseEntity containing a single Greeting object, if found,
	 *         and a HTTP status code as described in the method comment.
	 */
	@RequestMapping(
			value = "/api/greetings/{id}/send",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Greeting> sendGreeting(@PathVariable("id") final Long id, @RequestParam(
			value = "wait",
			defaultValue = "false") final boolean waitForAsyncResult) {

		LOGGER.info("> sendGreeting id:{}", id);

		Greeting greeting = null;

		try {
			greeting = greetingService.findOne(id);
			if (greeting == null) {
				LOGGER.info("< sendGreeting id:{}", id);
				return new ResponseEntity<Greeting>(HttpStatus.NOT_FOUND);
			}

			if (waitForAsyncResult) {
				final Future<Boolean> asyncResponse = emailService.sendAsyncWithResult(greeting);
				final boolean emailSent = asyncResponse.get();
				LOGGER.info("- greeting email sent? {}", emailSent);
			} else {
				emailService.sendAsync(greeting);
			}
		} catch (final Exception e) {
			LOGGER.error("A problem occurred sending the Greeting.", e);
			return new ResponseEntity<Greeting>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		LOGGER.info("< sendGreeting id:{}", id);
		return new ResponseEntity<Greeting>(greeting, HttpStatus.OK);
	}

}
