package com.stevpet.sonar.plugins.common.commandexecutor;


public class CommandException extends RuntimeException {

    private final DefaultCommand command;

    public CommandException(DefaultCommand command, String message, Throwable throwable) {
      super(message + " [command: " + command + "]", throwable);
      this.command = command;
    }

    public CommandException(DefaultCommand command, String message) {
      super(message + " [command: " + command + "]");
      this.command = command;
    }

    public CommandException(DefaultCommand command) {
        super();
        this.command = command;
      }
    
    public CommandException(DefaultCommand command, Exception e) {
       super(e);
       this.command=command;
    }

    public DefaultCommand getCommand() {
      return command;
    }
  }