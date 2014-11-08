package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class OpenCoverCommandMock extends GenericClassMock<OpenCoverCommand> {

    public OpenCoverCommandMock() {
        super(OpenCoverCommand.class);
    }
    
    public void verifyOpenSkipAutoProps(boolean expected) {
        int number=expected?1:0;
        verify(instance,times(number)).setSkipAutoProps();
    }

    public void verifySetExcludeFromCodeCoverageAttributeFilter() {
        verify(instance,times(1)).setExcludeFromCodeCoverageAttributeFilter();
    }

    public void verifyFilter(String filter) {
        verify(instance,times(1)).setFilter(filter);     
    }

}
