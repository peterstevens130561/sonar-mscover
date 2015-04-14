package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;

public class BranchFileCoverageSaverMock extends GenericClassMock<BranchFileCoverageSaver> {

	public BranchFileCoverageSaverMock() {
		super(BranchFileCoverageSaver.class);
	}

	public void thenSaveMeasureCalled(int timesCalled) {
		verify(instance,times(timesCalled)).saveMeasures(any(CoverageLinePoints.class), any(File.class));
	}

	public void thenSaveMeasureCalled(int timesCalled, String string) {
		verify(instance,times(timesCalled)).saveMeasures(any(CoverageLinePoints.class), eq(new File(string)));		
	}

}
