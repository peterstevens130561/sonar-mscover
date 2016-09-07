/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Resource;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.DefaultResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.TestResultsFormatter;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.VsTestTestResultsSaver;

public class VsTestTestResultsSaverTest {

	@Mock private PathResolver pathResolver;
	@Mock private FileSystem fileSystem;
	@Mock private DefaultResourceResolver resourceResolver;
	@Mock private TestResultsFormatter testResultsFormatter;
	private VsTestTestResultsSaver saver;
	@Mock private SensorContext sensorContext;
	@Mock private ProjectUnitTestResults projectUnitTestResults;
    @Mock private ResourcePerspectives resourcePerspectives;

	@Before
	public void before() {
		org.mockito.MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void instantion() {
		try {
			VsTestTestResultsSaver.create(pathResolver,fileSystem,resourcePerspectives);
		} catch (Exception e) {
			fail("could not instantiate");
		}
	}
	
	@Test 
	public void noResults() {
		saver= new VsTestTestResultsSaver(resourceResolver,resourcePerspectives);
		when(projectUnitTestResults.values()).thenReturn(new ArrayList<ClassUnitTestResult>());
		saver.save(sensorContext, projectUnitTestResults);

		verify(sensorContext,times(0)).saveMeasure(any(Resource.class),any(Metric.class),any(Double.class));
		verify(sensorContext,times(0)).saveMeasure(any(Resource.class),any(Measure.class));
		verify(sensorContext,times(0)).saveMeasure(any(Metric.class), any(Double.class));
	}
	
	@Test 
	public void one() {
		ProjectUnitTestResults projectUnitTestResults = new ProjectUnitTestResults();
		File sourceFile = new File("source.cs");
		org.sonar.api.resources.File resourceFile=org.sonar.api.resources.File.create("source.cs");
		projectUnitTestResults.addFile(sourceFile);
		
		when(resourceResolver.getFile(sourceFile)).thenReturn(resourceFile);
		
		saver= new VsTestTestResultsSaver(resourceResolver,resourcePerspectives);

		saver.save(sensorContext, projectUnitTestResults);

		verify(sensorContext,times(6)).saveMeasure(any(Resource.class),any(Metric.class),any(Double.class));
		//TODO: improve this test
		//verify(sensorContext,times(1)).saveMeasure(any(Resource.class),any(Measure.class));
		//verify(resourcePerspectives,times(1)).
		verify(sensorContext,times(0)).saveMeasure(any(Metric.class), any(Double.class));
	}
}
