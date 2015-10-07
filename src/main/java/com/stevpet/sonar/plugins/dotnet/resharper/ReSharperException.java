package com.stevpet.sonar.plugins.dotnet.resharper;

import org.sonar.api.utils.SonarException;

public class ReSharperException extends SonarException{

    public ReSharperException(String msg) {
        super(msg);
    }
}
