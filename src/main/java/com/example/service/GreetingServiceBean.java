package com.example.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.Greeting;
import com.example.repository.GreetingRepository;

@Service
@Transactional(
		propagation = Propagation.SUPPORTS,
		readOnly = true)
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
	@Transactional(
			propagation = Propagation.REQUIRED,
			readOnly = false)
	public Greeting create(final Greeting greeting) {
		if (greeting.getId() != null) {
			return null;
		}

		final Greeting savedGreeting = greetingRepository.save(greeting);
		return savedGreeting;
	}

	@Override
	@Transactional(
			propagation = Propagation.REQUIRED,
			readOnly = false)
	public Greeting update(final Greeting greeting) {
		final Greeting greetingPersisted = findOne(greeting.getId());
		if (greetingPersisted == null) {
			return null;
		}

		final Greeting savedGreeting = greetingRepository.save(greeting);
		return savedGreeting;
	}

	@Override
	@Transactional(
			propagation = Propagation.REQUIRED,
			readOnly = false)
	public void delete(final Long id) {
		final Greeting deletedGreeting = greetingRepository.findOne(id);
		if (deletedGreeting != null) {
			greetingRepository.delete(id);
		}
	}
}
