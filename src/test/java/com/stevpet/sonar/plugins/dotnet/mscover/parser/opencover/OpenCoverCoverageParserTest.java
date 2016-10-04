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
package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.test.TestUtils;

import static org.junit.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverFileNamesParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.DefaultProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.MethodRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.SourceFileRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.impl.DefaultSourceFileRepository;

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
    private MethodRepository methodRepository = new MethodToSourceFileIdRepository();
    private SourceFileRepository sourcefileRepository= new DefaultSourceFileRepository();
    
    @Before
    public void before() {
        initMocks(this);
        coverageParser = new OpenCoverCoverageParser(configuration);
        fileNamesParser = new OpenCoverFileNamesParser(methodRepository,sourcefileRepository);
        registry = new DefaultProjectCoverageRepository();
        
    }
    
    @Test
    public void readCoverageMixedCSharpCppFile() {
        File mixedFile=TestUtils.getResource("OpenCoverCoverageParser/coverage-report.xml");
        assertNotNull("file is in resources",mixedFile);
        coverageParser.parse(registry, mixedFile);
    }
    
    @Test
    public void readFileNAmesMixedCSharpCppFile() {
        File mixedFile=TestUtils.getResource("OpenCoverCoverageParser/coverage-report.xml");
        assertNotNull("file is in resources",mixedFile);
        fileNamesParser.parse(mixedFile);
        SourceFileRepository table = fileNamesParser.getSourceFileRepository();
        MethodRepository map = fileNamesParser.getMethodToSourceFileIdMap();
        assertNotNull(table);
        assertNotNull(map);
        
    }
}
