/*
 * Sonar .NET Plugin :: ReSharper
 * Copyright (C) 2013 John M. Wright
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
package com.stevpet.sonar.plugins.dotnet.cplusplus.preprocessor.sensor;

import java.io.File;
import java.util.Collection;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Initializer;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutor;

/**
 * Collects the ReSharper reporting into sonar.
 */
public class BuildWrapperInitializer extends Initializer {


    private final static Logger LOG = LoggerFactory.getLogger(BuildWrapperInitializer.class);


    private Settings settings;

    private CommandLineExecutor commandLineExecutor;
    private BuildWrapperBuilder buildWrapperBuilder;

    /**
     * Constructs a {@link org.sonar.plugins.csharp.resharper.ReSharperSensor}.
     */
    public BuildWrapperInitializer(
            Settings settings,
            CommandLineExecutor commandLineExecutor,
            BuildWrapperBuilder buildWrapperBuilder) {
        this.settings = settings;
        this.commandLineExecutor = commandLineExecutor;
        this.buildWrapperBuilder = buildWrapperBuilder;

    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        boolean hasCpp = hasCppFiles();
        boolean isRoot = project.isRoot();
        boolean isEnabled = settings.getBoolean(BuildWrapperConstants.BUILDWRAPPER_ENABLED_KEY);
        return isEnabled &&hasCpp && isRoot;
    }

    protected boolean hasCppFiles() {
        String extensions[] = {"cpp","h"};
        File currentDir = new File(".");
        LOG.debug("Looking for cpp files in " + currentDir.getAbsolutePath());
        Collection<File> files= FileUtils.listFiles(new File("."),extensions,true);
        LOG.debug("found " + files.size() + " files");
        return files.size()>0;
    }
    @Override
    public void execute(Project project) {
        LOG.info("----- C++ Initializer is running -----");
        String relativePath = getRequiredProperty(BuildWrapperConstants.BUILDWRAPPER_OUTDIR_KEY);
        String buildWrapperInstallDir=getRequiredProperty(BuildWrapperConstants.BUILDWRAPPER_INSTALLDIR_KEY);
        
        File outputDir = new File("." + relativePath);
        String absolutePathInUnixFormat=outputDir.getAbsolutePath().replaceAll("\\\\", "/");
        LOG.info("set " + BuildWrapperConstants.BUILD_WRAPPER_CFAMILY_OUTPUT_KEY + "=" + absolutePathInUnixFormat);        
        settings.appendProperty(BuildWrapperConstants.BUILD_WRAPPER_CFAMILY_OUTPUT_KEY, absolutePathInUnixFormat);

        String msbuildOptions = settings.getString(BuildWrapperConstants.BUILDWRAPPER_MSBUILD_OPTIONS_KEY);
        buildWrapperBuilder
                .setInstallDir(buildWrapperInstallDir)
                .setMsBuildOptions(msbuildOptions)
                .setOutputPath(absolutePathInUnixFormat);
        
        commandLineExecutor.execute(buildWrapperBuilder);
    }
    
    private String getRequiredProperty(String key) {
        String value=settings.getString(key);
        if(StringUtils.isEmpty(value)) {
            throw new SonarException("Property not set, but required " + key);
            
        }
        return value;
    }

}
