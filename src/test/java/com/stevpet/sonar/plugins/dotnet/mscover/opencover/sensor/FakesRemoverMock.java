package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.io.File;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

public class FakesRemoverMock extends GenericClassMock<FakesRemover> {

    public FakesRemoverMock() {
        super(FakesRemover.class);
    }

    public void verifyRemoveFakes(File file) {
        verify(instance,times(1)).removeFakes(file);
    }

}
