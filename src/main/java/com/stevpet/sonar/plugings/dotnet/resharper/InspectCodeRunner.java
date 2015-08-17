package com.stevpet.sonar.plugings.dotnet.resharper;

import java.io.File;
import org.sonar.api.BatchExtension;

public interface InspectCodeRunner extends BatchExtension {
    /**
     * Execute inspectCode
     * @param project
     * @return resharper report
     */
    File inspectCode();

    /**
     * use to verify whether inspectcode has run
     * @return true= reportfile exists, so it has run
     */
    boolean hasRun();

    /**
     * runner should forget anything it knows
     */
    void dropCache();
}