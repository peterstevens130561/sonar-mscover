package com.stevpet.sonar.plugins.dotnet.mscover.model;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import static org.mockito.Mockito.when;
public class UnitTestFileResultModelMock extends GenericClassMock<UnitTestFileResultModel> {

    public UnitTestFileResultModelMock() {
        super(UnitTestFileResultModel.class);
    }

    public  UnitTestFileResultModelMock givenDensity(double density) {
        when(instance.getDensity()).thenReturn(density);
        return this;
    }
    public  UnitTestFileResultModelMock givenFailed(int failed) {
        when(instance.getFail()).thenReturn((double) failed);
        return this;
    }

    public  UnitTestFileResultModelMock givenTests(int tests) {
        when(instance.getTests()).thenReturn((double) tests);
        return this;
    }
    

    

}
