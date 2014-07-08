package com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.SonarException;
import org.sonar.api.utils.command.CommandExecutor;
import org.sonar.api.utils.command.StreamConsumer;

import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.ShellCommand;

/**
 * A simple commandline executer.
 *
 * Execute any command by invoking execute. Afterwards get stdout and stderr
 * 
 */
public class WindowsCommandLineExecutor implements CommandLineExecutor {
    private static Logger LOG = LoggerFactory
            .getLogger(WindowsCommandLineExecutor.class);
    private StringStreamConsumer stdOut = new StringStreamConsumer();
    private StringStreamConsumer stdErr = new StringStreamConsumer();

    protected CommandExecutor commandExecutor = CommandExecutor.create();
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutor#execute(com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.ShellCommand)
     */
    public int execute(ShellCommand command) {
        stdOut = new StringStreamConsumer();
        stdErr = new StringStreamConsumer();
        long timeOut = (long) (30 * 60000);
        int exitCode = commandExecutor.execute(command.toCommand(),
                stdOut, stdErr, timeOut);
        if (exitCode != 0 && exitCode != 1) {
            String msg = command.toCommandLine() + " failed with exitCode "
                    + exitCode;
            LOG.error(stdErr.toString());
            throw new SonarException(msg);
        }
        return exitCode;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutor#getStdOut()
     */
    public String getStdOut() {
        return stdOut.toString();
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutor#getStdErr()
     */
    public String getStdErr() {
        return stdErr.toString();
    }
    
    class StringStreamConsumer implements StreamConsumer {
        private StringBuilder log;

        StringStreamConsumer() {
            log = new StringBuilder();
        }

        @Override
        public String toString() {
            return log.toString();
        }

        public void consumeLine(String line) {
            LOG.info(line);
            log.append(line);
            log.append("\r\n");
        }
    }
}
