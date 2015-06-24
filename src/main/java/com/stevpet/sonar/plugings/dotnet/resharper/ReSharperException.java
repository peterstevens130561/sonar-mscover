package com.stevpet.sonar.plugings.dotnet.resharper;

import org.sonar.api.utils.SonarException;

public class ReSharperException extends SonarException{

    public ReSharperException(String msg) {
        super(msg);
    }
}
