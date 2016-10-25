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
package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import org.picocontainer.DefaultPicoContainer;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.CoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.TestResultsSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;

/**
 * workflow deals with a specific usecase / scenario of the plugin. For each
 * workflow a Null object exists
 * 
 */
public interface WorkflowSteps {

    Class<? extends TestRunner> getTestRunner();

    Class<? extends CoverageReader> getCoverageReader();

    Class<? extends TestResultsBuilder> getTestResultsBuilder();

    Class<? extends CoverageSaver> getCoverageSaver();

    Class<? extends TestResultsSaver> getTestResultsSaver();

    /**
     * The steps may need specific components, add these here
     * 
     * @param picoContainer
     *            - to add the components to
     */
    void getComponents(DefaultPicoContainer picoContainer);

}
