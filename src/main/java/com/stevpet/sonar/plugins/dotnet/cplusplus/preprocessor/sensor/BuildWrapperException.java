package com.stevpet.sonar.plugins.dotnet.cplusplus.preprocessor.sensor;

import org.sonar.api.utils.SonarException;

class BuildWrapperException extends SonarException {

    private static final long serialVersionUID = 1L;

    BuildWrapperException(String msg) {
        super(msg);
    }
}
