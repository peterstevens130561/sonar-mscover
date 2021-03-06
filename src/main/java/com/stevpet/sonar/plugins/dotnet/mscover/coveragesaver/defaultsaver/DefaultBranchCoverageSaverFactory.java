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
package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.BranchFileCoverageSaver;

import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;

public class DefaultBranchCoverageSaverFactory implements BranchCoverageSaverFactory {
    private final ResourceResolver resourceResolver;
    private final CoverageSaverHelper coverageSaverHelper;

    public DefaultBranchCoverageSaverFactory(ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
        this.coverageSaverHelper = new DefaultCoverageSaverHelper();
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.BranchCoverageSaverFactory#createIntegrationTestBranchSaver()
     */
    @Override
    public BranchFileCoverageSaver createIntegrationTestSaver() {
        return new DefaultBranchCoverageSaver(resourceResolver,new IntegrationTestBranchCoverageMetrics(),coverageSaverHelper);
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.BranchCoverageSaverFactory#createUnitTestBranchSaver()
     */
    @Override
    public BranchFileCoverageSaver createUnitTestSaver() {
        return new DefaultBranchCoverageSaver(resourceResolver,new UnitTestBranchCoverageMetrics(), coverageSaverHelper);       
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.BranchCoverageSaverFactory#createOverallBranchSaver()
     */
    @Override
    public BranchFileCoverageSaver createOverallSaver() {        
        return new DefaultBranchCoverageSaver(resourceResolver,new OverallBranchCoverageMetrics(),coverageSaverHelper);       
    }
}
