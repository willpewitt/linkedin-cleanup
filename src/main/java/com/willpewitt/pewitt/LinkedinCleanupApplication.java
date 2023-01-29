package com.willpewitt.pewitt;

import com.willpewitt.pewitt.commands.ExportConnectionsCommand;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

@SpringBootApplication
public class LinkedinCleanupApplication implements CommandLineRunner, ExitCodeGenerator {

	private int exitCode;

	private final ExportConnectionsCommand exportConnectionsCommand;
	private final IFactory factory;

	public LinkedinCleanupApplication(final ExportConnectionsCommand exportConnectionsCommand, final IFactory factory) {
		this.exportConnectionsCommand = exportConnectionsCommand;
		this.factory = factory;
	}

	public static void main(String[] args) {
		SpringApplication.run(LinkedinCleanupApplication.class, args);
	}

	@Override
	public void run(String... args) {
		exitCode = new CommandLine(exportConnectionsCommand, factory).execute(args);
	}

	@Override
	public int getExitCode() {
		return exitCode;
	}
}
