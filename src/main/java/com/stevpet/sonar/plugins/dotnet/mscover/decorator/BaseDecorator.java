/*
 * Sonar .NET Plugin :: Gallio
 * Copyright (C) 2010 Jose Chillan, Alexandre Victoor and SonarSource
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
package com.stevpet.sonar.plugins.dotnet.mscover.decorator;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Decorator;
import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.resources.ResourceUtils;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.AlwaysPassThroughDateFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilterFactory;

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

    private final Settings settings;

    private PropertiesHelper propertiesHelper;

    private ResourceFilter fileFilter;

    private DateFilter dateFilter = new AlwaysPassThroughDateFilter();

    protected BaseDecorator(Settings settings,TimeMachine timeMachine) {
        this.settings = settings; 
        propertiesHelper=new PropertiesHelper(settings);
        dateFilter = DateFilterFactory.createCutOffDateFilter(timeMachine, propertiesHelper);
        fileFilter = ResourceFilterFactory.createAntPatternResourceFilter(propertiesHelper);
    }
    
    public void setPropertiesHelper(PropertiesHelper helper) {
        this.propertiesHelper = helper;
    }
    

    /**
     * {@inheritDoc}
     */
    public boolean shouldExecuteOnProject(Project project) {
        return shouldExecuteDecorator(project, settings);
    }

    /**
     * checks whether a specific subclass of the decorator should be executed.
     * Will only be invoked if the plugin is enabled.
     */
    public abstract boolean shouldExecuteDecorator(Project project,
            Settings settings);

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    public void decorate(Resource resource, DecoratorContext context) {
        if (!(ResourceUtils.isFile(resource)
                && context.getMeasure(testMetric) == null)) {
            return ;
        }
            
        Measure ncloc = context.getMeasure(CoreMetrics.NCLOC);
        if (ncloc == null) {
            return ;
        }
        //TODO: we need to split this into lines and blocks. Add a class between the Base and the Lines to
        // take care of the lines, and the same for the blocks
        // for the blocks we need to find a way to go from the name to the file
        Measure sts = context.getMeasure(CoreMetrics.STATEMENTS);
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
