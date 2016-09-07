/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
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
 *
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.common.commandexecutor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.command.StreamConsumer;

import com.stevpet.sonar.plugins.common.api.ShellCommand;

/**
 * A simple commandline executer.
 *
 * Execute any command by invoking execute. Afterwards get stdout and stderr
 * 
 */
public class WindowsCommandLineExecutor implements CommandLineExecutorWithEvents {
    private static final int DEFAULT_TIMEOUT = 120;
    private static Logger LOG = LoggerFactory.getLogger(WindowsCommandLineExecutor.class);
    private StringStreamConsumer stdOut = new StringStreamConsumer();
    private StringStreamConsumer stdErr = new StringStreamConsumer();

    private CommandExecutors commandExecutors;

    /**
     * Inject own factory
     * 
     * @param commandExecutors
     */
    public WindowsCommandLineExecutor(CommandExecutors commandExecutors) {
        this.commandExecutors = commandExecutors;
    }

    /**
     * default constructor
     */
    public WindowsCommandLineExecutor() {
        this(new CommandExecutors());
    }

    public int execute(ShellCommand command) {
        return execute(command, DEFAULT_TIMEOUT);
    }

    public int execute(ShellCommand command, int timeOutMinutes) {
        CommandExecutor commandExecutor = commandExecutors.create();
        stdOut = new StringStreamConsumer();
        stdErr = new StringStreamConsumer();
        long timeOut = (long) (timeOutMinutes * 60000);
        int exitCode = 1;
        exitCode = commandExecutor.execute(command.toCommand(), stdOut, stdErr, timeOut);
        if (exitCode != 0 && exitCode != 1) {
            String msg = command.toCommandLine() + " failed with exitCode " + exitCode;
            LOG.error(stdOut.toString());
            LOG.error(stdErr.toString());
            throw new IllegalStateException(msg);
        }
        return exitCode;
    }

    private List<LineReceivedListener> listeners = new ArrayList<>();

    @Override
    public void addLineReceivedListener(LineReceivedListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeLineReceivedListener(LineReceivedListener listener) {
        listeners.remove(listener);
    }

    private void lineReceived(String line) { 
        LineReceivedEvent event = new LineReceivedEvent() {
            @Override
            public String getLine() { return line ; }


            @Override
            public LocalDateTime getDateTime() {
                return LocalDateTime.now(); 
            }

        };
        listeners.forEach(listener -> listener.lineReceivedPerformed(event));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutor
     * #getStdOut()
     */
    public String getStdOut() {
        return stdOut.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutor
     * #getStdErr()
     */
    public String getStdErr() {
        return stdErr.toString();
    }

    private class StringStreamConsumer implements StreamConsumer {
        private StringBuilder log;

        StringStreamConsumer() {
            log = new StringBuilder();
        }

        @Override
        public String toString() {
            return log.toString();
        }

        public void consumeLine(String line) {
            lineReceived(line);
            LOG.debug(line);
            log.append(line);
            log.append("\r\n");
        }

    }

}
