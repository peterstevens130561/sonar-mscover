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
}
