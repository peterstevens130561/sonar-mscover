package com.stevpet.sonar.plugins.common.commandexecutor;

import com.stevpet.sonar.plugins.common.api.CommandLineExecutor;

public interface CommandLineExecutorWithEvents extends CommandLineExecutor {

    void addLineReceivedListener(LineReceivedListener listener);

    void removeLineReceivedListener(LineReceivedListener listener);

}
