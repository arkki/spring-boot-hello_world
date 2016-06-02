package com.example.service;

import java.util.concurrent.Future;

import com.example.model.Greeting;

public interface EmailService {

	Boolean send(Greeting greeting);

	void sendAsync(Greeting greeting);

	Future<Boolean> sendAsyncWithResult(Greeting greeting);
}
