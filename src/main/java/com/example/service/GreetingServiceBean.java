package com.example.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Greeting;
import com.example.repository.GreetingRepository;

@Service
public class GreetingServiceBean implements GreetingService {

	@Autowired
	private GreetingRepository greetingRepository;

	@Override
	public Collection<Greeting> findAll() {
		return greetingRepository.findAll();
	}

	@Override
	public Greeting findOne(final Long id) {
		return greetingRepository.findOne(id);
	}

	@Override
	public Greeting create(final Greeting greeting) {
		if (greeting.getId() != null) {
			return null;
		}

		final Greeting savedGreeting = greetingRepository.save(greeting);
		return savedGreeting;
	}

	@Override
	public Greeting update(final Greeting greeting) {
		final Greeting greetingPersisted = findOne(greeting.getId());
		if (greetingPersisted == null) {
			return null;
		}

		final Greeting savedGreeting = greetingRepository.save(greeting);
		return savedGreeting;
	}

	@Override
	public void delete(final Long id) {
		final Greeting deletedGreeting = greetingRepository.findOne(id);
		if (deletedGreeting != null) {
			greetingRepository.delete(id);
		}
	}
}
