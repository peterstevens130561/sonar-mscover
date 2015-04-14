package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LineFileCoverageSaverMock extends GenericClassMock<LineFileCoverageSaver> {

	public LineFileCoverageSaverMock() {
		super(LineFileCoverageSaver.class);
	}

	public void thenSaveMeasureCalled(int timesCalled) {
		verify(instance,times(timesCalled)).saveMeasures(any(CoverageLinePoints.class), any(File.class));
	}

	public void thenSaveMeasureCalled(int timesCalled, String string) {
		verify(instance,times(timesCalled)).saveMeasures(any(CoverageLinePoints.class), eq(new File(string)));		
	}
}
