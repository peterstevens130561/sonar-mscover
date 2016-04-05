package com.stevpet.sonar.plugins.common.commandexecutor;


public class CommandException extends RuntimeException {

    private final DefaultCommand command;

    public CommandException(DefaultCommand command, String message, Throwable throwable) {
      super(message + " [command: " + command + "]", throwable);
      this.command = command;
    }

    public CommandException(DefaultCommand command, Throwable throwable) {
      super(throwable);
      this.command = command;
    }

    public DefaultCommand getCommand() {
      return command;
    }
  }