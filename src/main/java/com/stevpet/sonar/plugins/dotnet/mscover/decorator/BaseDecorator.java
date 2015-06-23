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
package com.stevpet.sonar.plugins.dotnet.mscover.decorator;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Decorator;
import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.resources.ResourceUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.AlwaysPassThroughDateFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilterFactory;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

/**
 * Decorates resources that do not have coverage metrics because they were not
 * touched by any test, and thus not present in the coverage report file.
 */
public abstract class BaseDecorator implements Decorator {

    private static final Logger LOG = LoggerFactory
            .getLogger(BaseDecorator.class);

    protected MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    protected String executionMode;
    protected Set<String> excludedAssemblies;
    protected Metric testMetric;


    private MsCoverProperties propertiesHelper;

    private ResourceFilter fileFilter;

    private DateFilter dateFilter = new AlwaysPassThroughDateFilter();

    protected BaseDecorator(MsCoverProperties propertiesHelper,TimeMachine timeMachine) {
        this.propertiesHelper=propertiesHelper;
        dateFilter = DateFilterFactory.createCutOffDateFilter(timeMachine, propertiesHelper);
        fileFilter = ResourceFilterFactory.createAntPatternResourceFilter(propertiesHelper);
    }
    

    

    /**
     * {@inheritDoc}
     */
    public boolean shouldExecuteOnProject(Project project) {
        return shouldExecuteDecorator(project, propertiesHelper);
    }

    /**
     * checks whether a specific subclass of the decorator should be executed.
     * Will only be invoked if the plugin is enabled.
     */
    public abstract boolean shouldExecuteDecorator(Project project,
            MsCoverProperties propertiesHelper);

    /**
     * {@inheritDoc}
     */

    public void decorate(Resource resource, DecoratorContext context) {
        if (!(ResourceUtils.isFile(resource)
                && context.getMeasure(testMetric) == null)) {
            return ;
        }
            
        Measure ncloc = context.getMeasure(CoreMetrics.NCLOC);
        if (ncloc == null) {
            return ;
        }

        Measure sts = context.getMeasure(CoreMetrics.STATEMENTS);
        if(sts == null) {
            return ;
        }
        double lines = Math.min(ncloc.getValue(), sts.getValue());
        if(lines==0) {
            return ; 
        }
        if (!dateFilter.isResourceIncludedInResults(resource)) {
            return ;
        }

        String longName=resource.getLongName();
        boolean isPassed=fileFilter.isPassed(longName);
        if(!isPassed) {
            return ;
        }
        LOG.debug(
                "Coverage metrics have not been set on '{}': default values will be inserted.",
                resource.getName());
        handleUncoveredResource(context, lines);
    }

    protected abstract void handleUncoveredResource(DecoratorContext context,
            double lines);
}
