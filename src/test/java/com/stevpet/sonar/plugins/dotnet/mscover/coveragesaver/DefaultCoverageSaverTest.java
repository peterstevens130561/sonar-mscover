package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.picocontainer.DefaultPicoContainer;

import com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver.DefaultBranchSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver.DefaultLineFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver.NullBranchFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.DefaultResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.SonarMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.CoverageSaver;

public class DefaultCoverageSaverTest {
	DefaultPicoContainer container = new DefaultPicoContainer();
	@Before
	public void before() {
		container.addComponent(SonarMeasureSaver.class)
		.addComponent(DefaultResourceMediator.class)
		.addComponent(DefaultLineFileCoverageSaver.class)
		.addComponent(DefaultCoverageSaver.class);
	}
	@Test
	public void createWithLineandBranchCoverageSaver() {
		container
		.addComponent(DefaultBranchSaver.class);
		CoverageSaver saver = container.getComponent(DefaultCoverageSaver.class);
		assertNotNull("could not create coveragesaver with both savers",saver);
	}
	
	@Test
	public void createWithOnlyLineCoverageSaver() {
		container
		.addComponent(NullBranchFileCoverageSaver.class);
		CoverageSaver saver = container.getComponent(DefaultCoverageSaver.class);
		assertNotNull("could not create coveragesaver with null branch saver saver",saver);
	}
	

}
