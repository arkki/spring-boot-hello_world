package com.example.web.api;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Greeting;

@RestController
public class GreetingController {

	private static BigInteger nextId;
	private static Map<BigInteger, Greeting> greetingMap;

	static {
		final Greeting g1 = new Greeting();
		g1.setText("g1");
		save(g1);

		final Greeting g2 = new Greeting();
		g2.setText("g2");
		save(g2);

		final Greeting g3 = new Greeting();
		g3.setText("g3");
		save(g3);
	}

	private static Greeting save(final Greeting greeting) {
		if (greeting == null) {
			throw new IllegalArgumentException("Greeting was null!");
		}

		if (greetingMap == null) {
			greetingMap = new HashMap<>();
			nextId = BigInteger.ONE;
		}

		final BigInteger oldId = greeting.getId();
		if (oldId != null) {
			return saveUpdate(oldId, greeting);
		}

		return saveCreate(greeting);
	}

	private static Greeting saveUpdate(final BigInteger oldId, final Greeting greeting) {
		final Greeting greetingOld = greetingMap.get(oldId);
		if (greetingOld == null) {
			return null;
		}
		greetingMap.put(oldId, greeting);
		return greeting;
	}

	private static Greeting saveCreate(final Greeting greeting) {
		greeting.setId(nextId);
		nextId = nextId.add(BigInteger.ONE);
		greetingMap.put(greeting.getId(), greeting);
		return greeting;
	}

	private static boolean delete(final BigInteger id) {
		final Greeting deletedGreeting = greetingMap.remove(id);
		if (deletedGreeting == null) {
			return false;
		} else {
			return true;
		}
	}

	@RequestMapping(
			value = "/api/greetings",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Greeting>> getGreetings() {
		final Collection<Greeting> greetings = greetingMap.values();

		return new ResponseEntity<>(greetings, HttpStatus.OK);
	}

	@RequestMapping(
			value = "/api/greetings/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Greeting> getGreeting(@PathVariable("id") final BigInteger id) {
		final Greeting greeting = greetingMap.get(id);
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
		final Greeting savedGreeting = save(greeting);
		return new ResponseEntity<>(savedGreeting, HttpStatus.CREATED);
	}

	@RequestMapping(
			value = "/api/greetings",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Greeting> updateGreeting(@RequestBody final Greeting greeting) {

		final Greeting updatedGreeting = save(greeting);
		if (updatedGreeting == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity<>(updatedGreeting, HttpStatus.CREATED);
	}

	@RequestMapping(
			value = "/api/greetings/{id}",
			method = RequestMethod.DELETE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Greeting> deleteGreeting(@PathVariable("id") final BigInteger id,
			@RequestBody final Greeting greeting) {
		final boolean deleted = delete(id);

		if (!deleted) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}
}
