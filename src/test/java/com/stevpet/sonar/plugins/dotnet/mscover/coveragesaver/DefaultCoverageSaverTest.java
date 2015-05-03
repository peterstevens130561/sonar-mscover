package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.picocontainer.DefaultPicoContainer;
import org.sonar.api.batch.SensorContext;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.ResourceResolver;

public class DefaultCoverageSaverTest {
	private static final String SECOND_FILE = "b/c";
	private static final String FIRST_FILE = "a/b";
	private DefaultPicoContainer container = new DefaultPicoContainer();
	private SonarCoverage coverage = new SonarCoverage();
	private CoverageSaver saver;
	private List<File> testFiles;
	private BranchFileCoverageSaverMock branchFileCoverageSaverMock = new BranchFileCoverageSaverMock();
	private LineFileCoverageSaverMock lineFileCoverageSaverMock = new LineFileCoverageSaverMock();
	@Mock private SensorContext sensorContext;
	@Mock private ResourceResolver resourceResolver;
	
	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		container.addComponent(DefaultCoverageSaver.class).addComponent(sensorContext).addComponent(resourceResolver);
		testFiles = new ArrayList<File>();
	}
	
	@Test
	public void createWithLineandBranchCoverageSaver() {
		container
		.addComponent(DefaultLineFileCoverageSaver.class)
		.addComponent(DefaultBranchFileCoverageSaver.class);
		CoverageSaver saver = container.getComponent(DefaultCoverageSaver.class);
		assertNotNull("could not create coveragesaver with both savers",saver);
	}
	
	@Test
	public void createWithOnlyLineCoverageSaver() {
		container
		.addComponent(DefaultLineFileCoverageSaver.class)
		.addComponent(NullBranchFileCoverageSaver.class);
		CoverageSaver saver = container.getComponent(DefaultCoverageSaver.class);
		assertNotNull("could not create coveragesaver with null branch saver saver",saver);
	}
	
	@Test
	public void coverageWithTwoFiles_CalledTwice() {
		injectSaverMocks();
		givenTwoCoveredFiles();
		whenSaverInvoked();
		thenSaveMeasureIsCalledTimes(2);
	}

	@Test
	public void coverageWithNoFiles_CalledNone() {
		injectSaverMocks();
		//No files
		whenSaverInvoked();
		//Then two saves are expected
		thenSaveMeasureIsCalledTimes(0);
	}
	
	@Test
	public void coverageWithTwoFiles_OneExcluded() {
		injectSaverMocks();
		givenTwoCoveredFiles();
		givenExclude(FIRST_FILE);
		//No files
		whenSaverInvoked();
		//Then two saves are expected
		lineFileCoverageSaverMock.thenSaveMeasureCalled(1,SECOND_FILE);
		branchFileCoverageSaverMock.thenSaveMeasureCalled(1,SECOND_FILE);
	}
	
	private void thenSaveMeasureIsCalledTimes(int times) {
		lineFileCoverageSaverMock.thenSaveMeasureCalled(times);
		branchFileCoverageSaverMock.thenSaveMeasureCalled(times);
	}
	
	private void givenExclude(String path) {
		testFiles.add(new File(path));
		saver.setExcludeSourceFiles(testFiles);
	}
	
	private void whenSaverInvoked() {
		saver.save(coverage);
	}
	
	private void givenTwoCoveredFiles() {
		SonarFileCoverage fileCoverage=coverage.getCoveredFile("0");
		fileCoverage.addBranchPoint(1, true);
		fileCoverage.addBranchPoint(2, true);
		fileCoverage.setAbsolutePath(FIRST_FILE);

		fileCoverage=coverage.getCoveredFile("1");
		fileCoverage.addLinePoint(3,true);
		fileCoverage.addLinePoint(4,true);
		fileCoverage.setAbsolutePath(SECOND_FILE);
	}
	

	private void injectSaverMocks() {
		container.addComponent(branchFileCoverageSaverMock.getMock());
		container.addComponent(lineFileCoverageSaverMock.getMock());
		saver=container.getComponent(DefaultCoverageSaver.class);
	}

}
