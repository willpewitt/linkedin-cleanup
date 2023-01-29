package com.willpewitt.pewitt.service.impl;

import com.willpewitt.pewitt.service.LinkedInService;
import com.willpewitt.pewitt.util.ThreadUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static com.willpewitt.pewitt.constants.LinkedInConstants.LOGIN_PAGE;
import static com.willpewitt.pewitt.constants.LinkedInConstants.MANAGE_CONNECTIONS_PAGE;

@Service
public class LinkedInServiceImpl implements LinkedInService {

	private static final Logger LOG = LoggerFactory.getLogger(LinkedInServiceImpl.class);
	private static final Duration WAIT_DURATION = Duration.ofMillis(1500);

	private final WebDriver webDriver;

	public LinkedInServiceImpl(final WebDriver webDriver) {
		this.webDriver = webDriver;
	}

	@Override
	public void openManageConnectionPage() {
		webDriver.get(MANAGE_CONNECTIONS_PAGE);
		webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
	}

	@Override
	public void login(final String username, final String password) {
		webDriver.get(LOGIN_PAGE);
		WebElement loginField = webDriver.findElement(new By.ById("username"));
		WebElement passwordField = webDriver.findElement(new By.ById("password"));

		loginField.sendKeys(username);
		passwordField.sendKeys(password);

		passwordField.submit();
	}

	@Override
	public void scrollToBottomOfManageConnectionPage() {
		scrollPage(0);
	}

	private void scrollPage(int totalConnections) {
		ThreadUtil.waitUninterruptedly(WAIT_DURATION);

		try {
			clickShowMoreResultsButton();
			ThreadUtil.waitUninterruptedly(WAIT_DURATION);
			scrollToBottomOfPage();
		} catch (final StaleElementReferenceException e) {
			LOG.error("Caught a {}", StaleElementReferenceException.class.getSimpleName(), e);
		}

		int renderedConnections = getConnectionElements().size();

		int newlyAddedConnections = renderedConnections - totalConnections;
		if (newlyAddedConnections == 0) {
			LOG.info("Reached bottom of page, {} are displayed", renderedConnections);
			return;
		}

		scrollPage(renderedConnections);
	}

	private void clickShowMoreResultsButton() {
		Optional<WebElement> showMoreButton = getShowMoreResultsButton();
		if (showMoreButton.isEmpty()) {
			LOG.info("Show more button is not visible");
			return;
		}

		LOG.info("Show more button is visible");
		try {
			WebElement showMoreResultsButton = showMoreButton.get();
			showMoreResultsButton.click();
		} catch (final StaleElementReferenceException e) {
			LOG.error("Show more results button had a stale reference, attempting again", e);
			getShowMoreResultsButton().ifPresent(WebElement::click);
		}

		LOG.info("Clicked show more results button");
	}

	private Optional<WebElement> getShowMoreResultsButton() {
		try {
			return Optional.of(webDriver.findElement(new ByClassName("scaffold-finite-scroll__load-button")));
		} catch (final NoSuchElementException e) {
			LOG.info("Unable to get show more button, it must not be displayed");
			return Optional.empty();
		}
	}

	private List<WebElement> getConnectionElements() {
		List<WebElement> connectionElements = webDriver.findElements(new ByClassName("mn-connection-card"));
		LOG.debug("Found {} connection elements", connectionElements.size());
		return connectionElements;
	}

	private void scrollToBottomOfPage() {
		LOG.info("Scrolling to bottom of page");
		JavascriptExecutor javascriptExecutor = (JavascriptExecutor) webDriver;
		javascriptExecutor.executeScript("window.scrollBy(0, document.body.scrollHeight)");
	}
}