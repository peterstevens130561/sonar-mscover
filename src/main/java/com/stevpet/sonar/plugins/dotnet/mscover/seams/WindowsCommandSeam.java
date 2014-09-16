package com.stevpet.sonar.plugins.dotnet.mscover.seams;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.sonar.api.utils.command.Command;

public class WindowsCommandSeam {
    private final Command command;
    private WindowsCommandSeam(String executable) {
        command = Command.create(executable);
        
    }

    public static WindowsCommandSeam create(String executable) {
        return new WindowsCommandSeam(executable);
    }
    
    public String getExecutable() {
        return command.getExecutable();
      }

      public List<String> getArguments() {
        return command.getArguments();
      }

      public WindowsCommandSeam addArgument(String arg) {
        command.addArgument(arg);
        return this;
      }

      public WindowsCommandSeam addArguments(List<String> args) {
        command.addArguments(args);
        return this;
      }

      public WindowsCommandSeam addArguments(String[] args) {
        command.addArguments(args);
        return this;
      }

      public File getDirectory() {
        return command.getDirectory();
      }

      /**
       * Sets working directory.
       */
      public WindowsCommandSeam setDirectory(File d) {
        command.setDirectory(d);
        return this;
      }

      /**
       * @see org.sonar.api.utils.command.Command#getEnvironmentVariables()
       * @since 3.2
       */
      public WindowsCommandSeam setEnvironmentVariable(String name, String value) {
        command.setEnvironmentVariable(name, value);
        return this;
      }

      /**
       * Environment variables that are propagated during command execution.
       * The initial value is a copy of the environment of the current process.
       *
       * @return a non-null and immutable map of variables
       * @since 3.2
       */
      public Map<String, String> getEnvironmentVariables() {
        return command.getEnvironmentVariables();
      }

      boolean isNewShell() {
        return command.isNewShell();
      }

      public WindowsCommandSeam setNewShell(boolean b) {
        command.setNewShell(b);
        return this;
      }

      public String toCommandLine() {
        return command.toCommandLine();
      }

      @Override
      public String toString() {
        return toCommandLine();
      }

}
