package com.example.web.api;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Greeting;
import com.example.service.GreetingService;

@RestController
public class GreetingController {

	@Autowired
	private GreetingService greetingService;

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

}
