package com.stevpet.sonar.plugins.dotnet.mscover;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.OpenCoverCommandBuilder;

public class OpenCoverCommandBuilderMock extends GenericClassMock<OpenCoverCommandBuilder> {
    public OpenCoverCommandBuilderMock() {
        super(OpenCoverCommandBuilder.class);
    }
}
