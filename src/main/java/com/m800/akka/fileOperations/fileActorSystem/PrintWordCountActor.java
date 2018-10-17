package com.m800.akka.fileOperations.fileActorSystem;

import akka.actor.AbstractActorWithStash;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * This actor is to print the file name and no.of words.
 * @author: venkata harikrishna nallamalli on Oct 16, 2018
 *
 */
public class PrintWordCountActor extends AbstractActorWithStash {

	private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

	@Override
	public void preStart() {
		LOGGER.debug("Starting PrintWordCountActor {}", this);
	}

	@Override
	public void postStop() {
		LOGGER.debug("Stopping PrintWordCountActor {}", this);
	}	
	
	@Override
	public Receive createReceive() {
		return receiveBuilder().match(PrintWordCountActor.PrintFinalResult.class, printResult -> {
			LOGGER.debug("Received PrintFinalResult message from " + getSender());
			LOGGER.info("The File >>{}<< has a total number of >>{}<< words", printResult.fileName, printResult.totalNumberOfWords);		
		}).build();
	}
	
	/**
	 * static class with two properties fileName and no.of words.
	 * @author venkata harikrishna nallamalli on Oct 16, 2018
	 *
	 */
	public static final class PrintFinalResult {
		final Integer totalNumberOfWords;
		final String fileName;

		public PrintFinalResult(final Integer totalNumberOfWords, final String fileName) {
			this.totalNumberOfWords = totalNumberOfWords;
			this.fileName = fileName;
		}
	}
}
