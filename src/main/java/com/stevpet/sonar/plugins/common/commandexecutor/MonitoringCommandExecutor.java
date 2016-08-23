package com.stevpet.sonar.plugins.common.commandexecutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Nullable;

import org.sonar.api.utils.command.StreamConsumer;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import com.google.common.base.Charsets;


import org.sonar.api.utils.command.Command;

/**
 * Will not wait indefinitely on the process to close
 * 
 * @author stevpet
 *
 */

public class MonitoringCommandExecutor implements CommandExecutor {

    private static final Logger LOG = Loggers.get(CommandExecutor.class);
    private long lastTrigger;

    /**
     * @throws org.sonar.api.utils.command.TimeoutException
     *             on timeout, since 4.4
     * @throws CommandException
     *             on any other error
     * @param timeoutMilliseconds
     *            any negative value means no timeout.
     * @since 3.0
     */
    @Override
    public int execute(Command sonarCommand, StreamConsumer stdOut, StreamConsumer stdErr, long timeoutMilliseconds) {
        DefaultCommand command = DefaultCommand.create(sonarCommand.getExecutable());
        command.addArguments(sonarCommand.getArguments());
        ExecutorService executorService = null;
        Process process = null;
        try {
            ProcessBuilder builder = new ProcessBuilder(command.toStrings(false));
            if (command.getDirectory() != null) {
                builder.directory(command.getDirectory());
            }
            builder.environment().putAll(command.getEnvironmentVariables());
            process = builder.start();

            try (StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), stdOut);
                    StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), stdErr);) {
                outputGobbler.start();
                errorGobbler.start();
                triggerWatchdog();

                final Process finalProcess = process;
                executorService = Executors.newSingleThreadExecutor();
                Future<Integer> ft = executorService.submit(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return finalProcess.waitFor();
                    }
                });
                LOG.debug("waiting for completion");
                int exitCode;
                if (timeoutMilliseconds < 0) {
                    exitCode = ft.get();
                } else {
                    long pollingTime = 1000;
                    long start = System.currentTimeMillis();
                    boolean reported = false;
                    long lapse=0;
                    while (!ft.isDone()) {
                        Thread.sleep(pollingTime);
                        lapse += pollingTime;
                        // tests are just taking too long
                        if (lapse > timeoutMilliseconds) {
                            LOG.error("Timeout after {}" + lapse);
                            throw new TimeoutException(command, "after " + lapse);
                        }
                        if(watchdogBarks(stdOut, stdErr)) {
                            throw new TimeoutException(command, "watchdog expired");
                        }

                    }
                    exitCode = ft.get();
                }
                waitUntilFinish(outputGobbler);
                waitUntilFinish(errorGobbler);
                verifyGobbler(command, outputGobbler, "stdOut");
                verifyGobbler(command, errorGobbler, "stdErr");
                return exitCode;
            }

        } catch (java.util.concurrent.TimeoutException te) {
            process.destroyForcibly();
            throw new TimeoutException(command, "Timeout exceeded: " + timeoutMilliseconds + " ms", te);

        } catch (CommandException e) {
            throw e;

        } catch (Exception e) {
            throw new CommandException(command, e);
        } finally {
            if (executorService != null) {
                executorService.shutdown();
            }
        }
    }

    private boolean watchdogBarks(StreamConsumer stdOut, StreamConsumer stdErr) {
        boolean bark= (lastTrigger + 10*60*1000) < System.currentTimeMillis();
        return bark;
    }

    private void verifyGobbler(DefaultCommand command, StreamGobbler gobbler, String type) {
        if (gobbler.getException() != null) {
            throw new CommandException(command, "Error inside " + type + " stream", gobbler.getException());
        }
    }


    /**
     * Execute command and display error and output streams in log. Method
     * {@link #execute(DefaultCommand, StreamConsumer, StreamConsumer, long)} is
     * preferable, when fine-grained control of output of command required.
     * 
     * @param timeoutMilliseconds
     *            any negative value means no timeout.
     * 
     * @throws CommandException
     */
    @Override
    public int execute(Command command, long timeoutMilliseconds) {

        LOG.info("Executing command: " + command);
        return execute(command, new DefaultConsumer(), new DefaultConsumer(), timeoutMilliseconds);
    }

    private void waitUntilFinish(@Nullable StreamGobbler thread) {
        if (thread != null) {
            try {
                thread.join(20000);
            } catch (InterruptedException e) {
                LOG.error("InterruptedException while waiting finish of " + thread.toString(), e);
            }
        }
    }

    private void triggerWatchdog() {
        lastTrigger = System.currentTimeMillis();
    }

    private class StreamGobbler extends Thread implements AutoCloseable {
        private final InputStream is;
        private final StreamConsumer consumer;
        private volatile Exception exception;

        StreamGobbler(InputStream is, StreamConsumer consumer) {
            super("ProcessStreamGobbler");
            this.is = is;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, Charsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    triggerWatchdog();
                    consumeLine(line);
                }
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        private void consumeLine(String line) {
            if (exception == null) {
                try {
                    consumer.consumeLine(line);
                } catch (Exception e) {
                    exception = e;
                }
            }
        }

        public Exception getException() {
            return exception;
        }

        @Override
        public void close() throws Exception {
            // Intentionally empty, to prevent being blocked forever
        }
    }

    private class DefaultConsumer implements StreamConsumer {
        @Override
        public void consumeLine(String line) {
            LOG.info(line);
        }
    }
}
