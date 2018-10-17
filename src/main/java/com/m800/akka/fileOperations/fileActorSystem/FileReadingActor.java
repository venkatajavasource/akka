package com.m800.akka.fileOperations.fileActorSystem;

import static akka.pattern.PatternsCS.ask;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import akka.actor.AbstractActorWithStash;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * This Actor is to read each line from the file and feed the line string to the
 * WorkCounterActor.
 * 
 * @author venkata harikrishna nallamalli on Oct 16, 2018
 *
 */
public class FileReadingActor extends AbstractActorWithStash {

	private final ActorRef PRINT_WORD_COUNT_ACTOR_REF = getContext().actorOf(Props.create(PrintWordCountActor.class),
			"Printer-Actor");
	private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

	@Override
	public void preStart() {
		LOGGER.debug("Starting FileReadingActor {}", this);
	}

	@Override
	public void postStop() {
		LOGGER.debug("Stopping FileReadingActor {}", this);
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(Path.class, fileName -> {
			@SuppressWarnings("rawtypes")
			List<CompletableFuture> wordCountActorFutures = processFile(fileName);
			printFileWordCount(wordCountActorFutures, fileName);
		}).build();
	}

	/**
	 * This method is to process the file and read each line and call
	 * WordCounterActor by passing line string as message.
	 * 
	 * @param inFileName
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	private List<CompletableFuture> processFile(final Path inFileName) throws IOException {
		final List<String> lines = Files.readAllLines(inFileName);
		final List<CompletableFuture> wordCountActorFutures = new ArrayList<>();
		ActorRef wordCounterActorRef = getContext().actorOf(Props.create(WordCounterActor.class));
		lines.forEach(line -> {
			LOGGER.debug(line);
			// TODO: Word count is minute task, that is why using the ask method, this can
			// be replaced with tell to achieve not-blocking.
			CompletableFuture<Object> wordCountActorFuture = ask(wordCounterActorRef,
					new WordCounterActor.CountWords(line), 1000).toCompletableFuture();
			wordCountActorFutures.add(wordCountActorFuture);
		});
		return wordCountActorFutures;
	}

	/**
	 * This method is to print the word count by calling PrintWordCountActor.
	 * 
	 * @param inFutures
	 * @param inFileName
	 */
	private void printFileWordCount(@SuppressWarnings("rawtypes") final List<CompletableFuture> inFutures,
			final Path inFileName) {
		final Integer totalNumberOfWordsInFile = inFutures.stream().map(CompletableFuture::join)
				.mapToInt(eachLineWordCount -> (Integer) eachLineWordCount).sum();
		PRINT_WORD_COUNT_ACTOR_REF.forward(
				new PrintWordCountActor.PrintFinalResult(totalNumberOfWordsInFile, inFileName.getFileName().toString()),
				getContext());
	}
}
