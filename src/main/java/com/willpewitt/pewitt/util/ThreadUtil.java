package com.willpewitt.pewitt.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public final class ThreadUtil {

	private static final Logger LOG = LoggerFactory.getLogger(ThreadUtil.class);

	private ThreadUtil() {}

	public static void waitUninterruptedly(final Duration durationToWait) {
		try {
			Thread.sleep(durationToWait.toMillis());
		} catch (final InterruptedException e) {
			LOG.error("Unable to wait", e);
			Thread.currentThread().interrupt();
		}
	}

}
