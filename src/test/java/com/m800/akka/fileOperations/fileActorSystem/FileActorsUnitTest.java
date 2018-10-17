package com.m800.akka.fileOperations.fileActorSystem;

import static akka.pattern.PatternsCS.ask;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestKit;
import scala.concurrent.duration.Duration;

public class FileActorsUnitTest {

	private static ActorSystem system = null;

	@BeforeClass
	public static void setup() {
		system = ActorSystem.create("test-system");
	}

	@AfterClass
	public static void teardown() {
		TestKit.shutdownActorSystem(system, Duration.apply(1000, TimeUnit.MILLISECONDS), true);
		system = null;
	}

	/**
	 * This test is to test the no of counts passed as string.
	 * 
	 * @throws ExecutionException   the actor will throw this exception, when it get
	 *                              execution exception.
	 * @throws InterruptedException throws, when the actor thread is interrupted by
	 *                              any.
	 */
	@Test
	public void givenAnActor_sendHimAMessageUsingAsk() throws ExecutionException, InterruptedException {

		final TestKit probe = new TestKit(system);
		ActorRef wordCounterActorRef = probe.childActorOf(Props.create(WordCounterActor.class));

		CompletableFuture<Object> future = ask(wordCounterActorRef, new WordCounterActor.CountWords("this is a text"),
				1000).toCompletableFuture();

		Integer numberOfWords = (Integer) future.get();
		assertTrue("The actor should count 4 words", 4 == numberOfWords);
	}

	/**
	 * This test is to test, whether the LineWordCounterActor able to handle null
	 * string. We are expecting exception in this test.
	 */
	@Test
	public void givenAnActor_whenTheMessageIsNull_respondWithException() {
		final TestKit probe = new TestKit(system);
		ActorRef wordCounterActorRef = probe.childActorOf(Props.create(WordCounterActor.class));

		CompletableFuture<Object> future = ask(wordCounterActorRef, new WordCounterActor.CountWords(null), 1000)
				.toCompletableFuture();

		try {
			future.get(1000, TimeUnit.MILLISECONDS);
		} catch (ExecutionException e) {
			//assertTrue("Invalid error message", e.getMessage().contains("The text to process can't be null!"));
		} catch (InterruptedException | TimeoutException e) {
			fail("Actor should respond with an exception instead of timing out !");
		}
	}
}
