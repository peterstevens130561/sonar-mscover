package com.stevpet.sonar.plugins.common.commandexecutor;

import org.sonar.api.utils.command.Command;

public class CommandException extends RuntimeException {

    private final Command command;

    public CommandException(Command command, String message, Throwable throwable) {
      super(message + " [command: " + command + "]", throwable);
      this.command = command;
    }

    public CommandException(Command command, Throwable throwable) {
      super(throwable);
      this.command = command;
    }

    public Command getCommand() {
      return command;
    }
  }