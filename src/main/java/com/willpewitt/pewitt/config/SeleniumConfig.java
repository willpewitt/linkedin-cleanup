package com.willpewitt.pewitt.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class SeleniumConfig {

	@Bean
	public WebDriver getWebDriver() {
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");

		WebDriver driver = new ChromeDriver(options);
		driver.manage()
				.timeouts()
				.implicitlyWait(Duration.ofSeconds(5));

		return driver;
	}

}
