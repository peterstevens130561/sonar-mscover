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
package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.test.TestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverFileNamesParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.DefaultProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;

/**
 * Integration test!
 * @author stevpet
 *
 */
public class OpenCoverCoverageParserTest {
    private OpenCoverCoverageParser coverageParser ;
    private OpenCoverFileNamesParser fileNamesParser;
    @Mock private MsCoverConfiguration configuration;
    private ProjectCoverageRepository registry;
    private MethodToSourceFileIdMap map = new MethodToSourceFileIdMap();
    private SourceFileNameTable sourceFileNamesTable = new SourceFileNameTable();
    
    @Before
    public void before() {
        initMocks(this);
        coverageParser = new OpenCoverCoverageParser(configuration);
        fileNamesParser = new OpenCoverFileNamesParser(map, sourceFileNamesTable);
        registry = new DefaultProjectCoverageRepository();
        
    }
    
    @Test
    public void readCoverageMixedCSharpCppFile() {
        File mixedFile=TestUtils.getResource("OpenCoverCoverageParser/coverage-report.xml");
        assertNotNull("file is in resources",mixedFile);
        coverageParser.parse(registry, mixedFile);
        assertEquals(210,registry.size());
    }
    
    @Test
    public void readFileNAmesMixedCSharpCppFile() {
        File mixedFile=TestUtils.getResource("OpenCoverCoverageParser/coverage-report.xml");
        assertNotNull("file is in resources",mixedFile);
        fileNamesParser.parse(mixedFile);
        assertEquals(3128,map.size());
        assertEquals(210,sourceFileNamesTable.size());
        
    }
}
