/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
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
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.ResourceSeam;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.ResourceSeamFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.SonarResourceSeamFactory;

public class DefaultResourceMediator implements ResourceMediator {
    
    private static final Logger LOG = LoggerFactory
            .getLogger(DefaultResourceMediator.class);

    protected DateFilter dateFilter = DateFilterFactory.createEmptyDateFilter();
    protected ResourceFilter resourceFilter = ResourceFilterFactory.createEmptyFilter();
    private ResourceSeamFactory resourceSeamFactory;

    public DefaultResourceMediator() {
        resourceSeamFactory = new SonarResourceSeamFactory();       
    }
 

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediatorInterface#setDateFilter(com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilter)
     */
        
    @Override
    public void setDateFilter(DateFilter dateFilter) {
        this.dateFilter = dateFilter;
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediatorInterface#setResourceFilter(com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilter)
     */
    @Override
    public void setResourceFilter(ResourceFilter resourceFilter) {
        this.resourceFilter = resourceFilter;
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediatorInterface#getSonarResource(org.sonar.api.batch.SensorContext, org.sonar.api.resources.Project, java.io.File)
     */
    @Override
    public ResourceSeam getSonarResource(SensorContext sensorContext,Project project,File file) {
        ResourceSeam resource;
        org.sonar.api.resources.File sonarFile =org.sonar.api.resources.File.fromIOFile(file, project);
        if (sonarFile == null) {
            LOG.debug("Could not create sonarFile for "
                    + file.getAbsolutePath());
            resource=resourceSeamFactory.createNullResource();
        } else {
            resource=resourceSeamFactory.createFileResource(sensorContext,sonarFile);
        }

        String longName = resource.getLongName();
        if(!resourceFilter.isPassed(longName)) {
            resource.setIsExcluded();
        }
        if (!dateFilter.isResourceIncludedInResults(sonarFile)) {
            resource.setIsExcluded();
            LOG.debug("Skipping file of which commit date is before cutoff date " +sonarFile.getLongName());
        }
        return resource;
    }

}
