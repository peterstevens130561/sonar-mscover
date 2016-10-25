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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser;

import static org.junit.Assert.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.VsTestLinesObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.VsTestCoverageObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.DefaultProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;

public class VsTestLinesObserverTest extends VsTestObserverTest {
	private VsTestCoverageObserver observer;
	private ProjectCoverageRepository registry;
	private XmlParser parser;
	@Before
	public void before() {
		observer = new VsTestLinesObserver();
		registry = new DefaultProjectCoverageRepository();
		observer.setVsTestRegistry(registry);
		parser = new DefaultXmlParser();
		parser.registerObserver(observer);
	}
	
	@Test
	public void parseOneCoveredLine() throws ParserConfigurationException, TransformerException {
		createCoverage();
		addLine("8","37","0");
		String coverageDoc=docToString();
		parser.parseString(coverageDoc);
		SonarFileCoverage fileCoverage=registry.getCoverageOfFile("8");
		CoverageLinePoints linePoints=fileCoverage.getLinePoints();
		assertEquals(1,linePoints.size());
		assertEquals(37,linePoints.getPoints().get(0).getLine());
		assertEquals(1,linePoints.getPoints().get(0).getCovered());
	}
	
	@Test
	public void parseOneUnCoveredLine() throws ParserConfigurationException, TransformerException {
		createCoverage();
		addLine("8","37","1");
		String coverageDoc=docToString();
		parser.parseString(coverageDoc);
		SonarFileCoverage fileCoverage=registry.getCoverageOfFile("8");
		CoverageLinePoints linePoints=fileCoverage.getLinePoints();
		assertEquals(1,linePoints.size());
		assertEquals(37,linePoints.getPoints().get(0).getLine());
		assertEquals(0,linePoints.getPoints().get(0).getCovered());
	}
	
	@Test
	public void parseTwoLines() throws ParserConfigurationException, TransformerException {
		createCoverage();
		addLine("8","37","1");
		addLine("8","38","0");
		String coverageDoc=docToString();
		parser.parseString(coverageDoc);
		SonarFileCoverage fileCoverage=registry.getCoverageOfFile("8");
		CoverageLinePoints linePoints=fileCoverage.getLinePoints();
		assertEquals(2,linePoints.size());
	}
				
	private void createCoverage() throws ParserConfigurationException, TransformerException {
			createNewDoc();
			createModuleToMethod();
	}
			
}
