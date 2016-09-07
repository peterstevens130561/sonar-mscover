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
package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public class OpenCoverCoverageParserTest {

    @Mock private MsCoverConfiguration configuration;

    @Before 
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
    }
    

    @Ignore
    @Test
    public void ReadFile() {
        //Arrange
        OpenCoverCoverageParser parser = new OpenCoverCoverageParser(configuration);
        //File file = new File("E:\\Users\\stevpet\\My Documents\\BAM\\coverage-report.xml");
        File file = new File("E:\\Users\\stevpet\\My Documents\\GitHub\\sonar-mscover\\coverage-subsurface.xml");
        SonarCoverage registry = new SonarCoverage();
        //Act
        parser.parse(registry, file);
        //Assert
        assertEquals(356,registry.size());
        assertNull(registry.getCoveredFile("5").getAbsolutePath()); // is not included
        assertNotNull(registry.getCoveredFile("86").getAbsolutePath()); // is included
    }
}
