package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import static org.mockito.Mockito.verify;
public class InjectingFakesRemoverMock extends GenericClassMock<InjectingFakesRemover> {

    public InjectingFakesRemoverMock() {
        super(InjectingFakesRemover.class);
    }

    public void thenExecuteInvoked() {
        verify(instance).execute();
    }

}
