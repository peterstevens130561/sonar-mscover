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
}