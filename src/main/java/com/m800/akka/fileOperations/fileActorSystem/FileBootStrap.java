package com.m800.akka.fileOperations.fileActorSystem;

import java.util.Scanner;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * This is File BootStrap.
 * 
 * @author: venkata harikrishna nallamalli on Oct 16, 2018
 * 
 */
public class FileBootStrap {
	
	private static final ActorSystem ACTOR_SYSTEM = ActorSystem.create("file-operations-actor-system");
	private final static ActorRef SCANE_DIR_ACTOR_REF = ACTOR_SYSTEM.actorOf(Props.create(ScanDirectoryActor.class),
			"ScanDirectoryActor");

	public static void main(String[] args) {

		try {
			@SuppressWarnings("resource")
			final Scanner scanner = new Scanner(System.in);

			System.out.println("Enter Directory name: ");
			final String dirName = scanner.nextLine();

			SCANE_DIR_ACTOR_REF.tell(dirName, ActorRef.noSender());

			System.out.println("Make Sure all the files are processed...");
			System.out.println(">>>>> Press Enter to Exit actor system <<<<<");
			scanner.nextLine();
		} catch (final Exception exp) {
			exp.printStackTrace();
		} finally {
			if (ACTOR_SYSTEM != null)
				ACTOR_SYSTEM.terminate();
		}
	}
}
