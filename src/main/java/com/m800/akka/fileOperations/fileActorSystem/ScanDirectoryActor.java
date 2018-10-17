package com.m800.akka.fileOperations.fileActorSystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import akka.actor.AbstractActorWithStash;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * This Actor is to Scan user provided directory path and read each file name
 * and call the FileReadingActor for each file name.
 * 
 * @author venkata harikrishna nallamalli, Oct 16, 2018
 *
 */
public class ScanDirectoryActor extends AbstractActorWithStash {

	private final LoggingAdapter LOGGER = Logging.getLogger(getContext().getSystem(), this);

	@Override
	public void preStart() {
		LOGGER.debug("Starting ScanDirectoryActor {}", this);
	}

	@Override
	public void postStop() {
		LOGGER.debug("Stopping ScanDirectoryActor {}", this);
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(String.class, dirPath -> {
			processDirectory(dirPath);
		}).build();
	}

	/**
	 * This method is to process directory, read all the files and call the
	 * FileReadingActor for each file name.
	 * 
	 * @param inDirPath user provided directory path
	 */
	private void processDirectory(final String inDirPath) {
		LOGGER.debug("Received diretory path is :" + inDirPath);
		final Path targetDir = Paths.get(inDirPath);
		try (Stream<Path> filePaths = Files.walk(targetDir)) {
			final ActorRef fileReadActorRef = getContext().actorOf(Props.create(FileReadingActor.class));
			filePaths.forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					try {
						LOGGER.debug("Read file is: " + filePath);
						fileReadActorRef.tell(filePath, ActorRef.noSender());
					} catch (final Exception exception) {
						LOGGER.error("Error " + exception.getMessage() + ",while processing the file: " + filePath);
					}
				} else {
					LOGGER.warning("The file >>" + filePath + "<< is not a regular file, ignore it...");
				}
			});
		} catch (final IOException ioException) {
			LOGGER.error("Error while reading the directory: " + ioException.getStackTrace());
		}
	}
}
