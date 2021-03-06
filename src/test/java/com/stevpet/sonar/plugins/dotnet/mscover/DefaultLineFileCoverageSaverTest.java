/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
 * dev@sonar.codehaus.org
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
 */
package com.stevpet.sonar.plugins.dotnet.mscover;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.File;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.LineFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.UnitTestLineFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.DefaultProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

public class DefaultLineFileCoverageSaverTest {
	private static final String FIRST_FILE = "a/b";
	private ProjectCoverageRepository coverage = new DefaultProjectCoverageRepository();
	
	@Mock ResourceResolver resourceResolver ;
	@Mock SensorContext sensorContext;
	private LineFileCoverageSaver coverageSaver ;
	File coveredResource;
	java.io.File coveredFile;
	
	@Captor
	private ArgumentCaptor<Metric> metricsArgument;
	@Captor
	private ArgumentCaptor<Double> valueArgument;
	
	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		coverageSaver = new UnitTestLineFileCoverageSaver(resourceResolver, sensorContext);
		coveredFile = new java.io.File(FIRST_FILE);
		coveredResource = File.create(FIRST_FILE);
		when(resourceResolver.getFile(eq(coveredFile))).thenReturn(coveredResource);
		givenCoveredFile();
		CoverageLinePoints coveragePoints=coverage.getCoverageOfFile("1").getLinePoints();
		coverageSaver.saveMeasures(sensorContext,coveredFile, coveragePoints);
	}
	
	@Test
	public void resourceResolver_invoked() {
		verify(resourceResolver,times(1)).getFile(coveredFile);
	}
	
	@Test
	public void saveMeasureCoverage_invoked()
	{
		verify(sensorContext,times(1)).saveMeasure(eq(coveredResource),any(Measure.class));
	}
	
	@Test
	public void saveMeasure_FourTimes_invokedWithCorrectValues() {
		verify(sensorContext,times(4)).saveMeasure(eq(coveredResource),metricsArgument.capture(),valueArgument.capture());
		assertEquals("lines_to_cover",metricsArgument.getAllValues().get(0).getKey());
		assertEquals("number of lines", 3.0,valueArgument.getAllValues().get(0).doubleValue(),0.1);
		
		assertEquals("uncovered_lines",metricsArgument.getAllValues().get(1).getKey());
		assertEquals("uncovered lines", 2.0,valueArgument.getAllValues().get(1).doubleValue(),0.1);
		
		assertEquals("coverage",metricsArgument.getAllValues().get(2).getKey());
		assertEquals("coverage", 33.0,valueArgument.getAllValues().get(2).doubleValue(),0.1);
		
		assertEquals("line_coverage",metricsArgument.getAllValues().get(3).getKey());
		assertEquals("line coverage", 33.0,valueArgument.getAllValues().get(3).doubleValue(),0.1);
	}
	
	private void givenCoveredFile() {	
		SonarFileCoverage fileCoverage=coverage.getCoverageOfFile("1");
		fileCoverage.addLinePoint(3,true);
		fileCoverage.addLinePoint(4,false);
		fileCoverage.addLinePoint(5,false);
		fileCoverage.setAbsolutePath(FIRST_FILE);
	}
	
}
