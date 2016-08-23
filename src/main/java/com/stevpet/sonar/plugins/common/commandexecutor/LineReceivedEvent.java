package com.stevpet.sonar.plugins.common.commandexecutor;

import java.time.LocalDateTime;


public interface LineReceivedEvent {
    /**
     * 
     * @return the received line
     */
    String getLine();
    
    /**
     * the local datetime at which the line was received by the system
     * @return
     */
    LocalDateTime getDateTime();
}

