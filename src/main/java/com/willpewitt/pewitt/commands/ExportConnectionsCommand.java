package com.willpewitt.pewitt.commands;

import com.willpewitt.pewitt.constants.LinkedInConstants;
import com.willpewitt.pewitt.service.LinkedInService;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Command(name = "exportCommand")
@Component
public class ExportConnectionsCommand implements Callable<Integer> {

	private static final Logger LOG = LoggerFactory.getLogger(ExportConnectionsCommand.class);

	private final LinkedInService linkedInService;
	private final WebDriver webDriver;

	@Option(names = "-username", required = true, arity = "1", description = "Your LinkedIn username")
	private String username;

	@Option(names = "-password", required = true, arity = "1", description = "Your LinkedIn password")
	private String password;

	public ExportConnectionsCommand(final LinkedInService linkedInService, final WebDriver webDriver) {
		this.linkedInService = linkedInService;
		this.webDriver = webDriver;
	}

	@Override
	public Integer call() {
		linkedInService.openManageConnectionPage();

		// need to check if redirected to login page...
		String currentUrl = webDriver.getCurrentUrl();
		if (!currentUrl.equals(LinkedInConstants.MANAGE_CONNECTIONS_PAGE)) {
			LOG.info("Tried to navigate to {}, was directed to {}", LinkedInConstants.MANAGE_CONNECTIONS_PAGE, LinkedInConstants.LOGIN_PAGE);
			linkedInService.login(username, password);
			linkedInService.openManageConnectionPage();
		}

		linkedInService.scrollToBottomOfManageConnectionPage();

		return 0;
	}
}
