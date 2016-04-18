package com.stevpet.sonar.plugins.common.commandexecutor;


public class TimeoutException extends CommandException {

    public TimeoutException(DefaultCommand command, String message, Throwable throwable) {
      super(command, message, throwable);
    }
    public TimeoutException(DefaultCommand command, String message) {
        super(command, message);
      }   
  }