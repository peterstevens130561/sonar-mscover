/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package com.stevpet.sonar.plugins.dotnet.mscover.workflow.sensor;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.spi.AppenderAttachable;

public class LogChanger {
	private static final Logger LOG = LoggerFactory.getLogger(LogChanger.class);
	
	@SuppressWarnings("unchecked")
	public static void setPattern() {
		PatternLayoutEncoder encoder = new PatternLayoutEncoder(); 
		ch.qos.logback.classic.Logger loggerImpl =(ch.qos.logback.classic.Logger) LOG;
		encoder.setContext(loggerImpl.getLoggerContext());
		encoder.setPattern("%level %logger{5} %msg%n");
		encoder.start();

		Logger rootLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		AppenderAttachable<ILoggingEvent> appenderAttachable = (AppenderAttachable<ILoggingEvent>) rootLogger;
		Iterator<Appender<ILoggingEvent>> iterator = appenderAttachable.iteratorForAppenders();
		while (iterator.hasNext()) {
			Appender<ILoggingEvent> appender = iterator.next();
			if (appender instanceof OutputStreamAppender) {
				((OutputStreamAppender)appender).setEncoder(encoder);
			}
			appender.stop();
			appender.start();
		}
	}
	
	   public static void setCustomPattern(String pattern) {
	        PatternLayoutEncoder encoder = new PatternLayoutEncoder(); 
	        ch.qos.logback.classic.Logger loggerImpl =(ch.qos.logback.classic.Logger) LOG;
	        encoder.setContext(loggerImpl.getLoggerContext());
	        encoder.setPattern(pattern);
	        encoder.start();

	        Logger rootLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
	        AppenderAttachable<ILoggingEvent> appenderAttachable = (AppenderAttachable<ILoggingEvent>) rootLogger;
	        Iterator<Appender<ILoggingEvent>> iterator = appenderAttachable.iteratorForAppenders();
	        while (iterator.hasNext()) {
	            Appender<ILoggingEvent> appender = iterator.next();
	            if (appender instanceof OutputStreamAppender) {
	                ((OutputStreamAppender)appender).setEncoder(encoder);
	            }
	            appender.stop();
	            appender.start();
	        }
	    }
}
