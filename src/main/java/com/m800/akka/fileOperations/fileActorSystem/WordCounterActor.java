package com.m800.akka.fileOperations.fileActorSystem;

import akka.actor.AbstractActorWithStash;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * This actor is to count words for given line.
 * @author: venkata harikrishna nallamalli on Oct 16, 2018
 *
 */
public class WordCounterActor extends AbstractActorWithStash {

	private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

	public static final class CountWords {
		String line;

		public CountWords(String line) {
			this.line = line;
		}
	}

	@Override	
	public void postStop() {		
		LOGGER.debug("Starting WordCounterActor {}", this);
	}
	
	@Override
	public void preStart() {
		LOGGER.debug("Stopping WordCounterActor {}", this);
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(CountWords.class, rcountWords -> {
			countWords(rcountWords);
		}).build();
	}
	
	/**
	 * This method is to count words for given line of file.
	 * @param rcountWords 
	 * @throws Exception
	 */
	private void countWords(final CountWords inRcountWords) throws Exception{
		try {
			LOGGER.debug("Received CountWords message from " + getSender());
			final int numberOfWords = countWordsFromLine(inRcountWords.line);
			getSender().tell(numberOfWords, getSelf());
		} catch (final Exception exception) {
			getSender().tell(new akka.actor.Status.Failure(exception), getSelf());
			throw exception;
		}	
	}

	private int countWordsFromLine(String line) throws Exception {

		int numberOfWords = 0;
		
		if (line == null) {
			return numberOfWords;
			//throw new IllegalArgumentException("The text to process can't be null!");
		}
		
		final String[] words = line.split(" ");
		for (final String possibleWord : words) {
			if (possibleWord.trim().length() > 0) {
				numberOfWords++;
			}
		}
		return numberOfWords;
	}
}
