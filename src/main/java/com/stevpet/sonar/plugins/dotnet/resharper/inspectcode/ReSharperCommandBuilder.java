/*
 * .NET tools :: ReSharper Runner
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
package com.stevpet.sonar.plugins.dotnet.resharper.inspectcode;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;
import org.sonar.api.utils.command.Command;

import com.stevpet.sonar.plugins.common.api.ShellCommand;
import com.stevpet.sonar.plugins.dotnet.resharper.exceptions.ReSharperException;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolution;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to build the command line to run ReSharper inspectcoe.
 */
public class ReSharperCommandBuilder implements BatchExtension,ShellCommand{


	private static final Logger LOG = LoggerFactory
			.getLogger(ReSharperCommandBuilder.class);

	private File resharperReportFile;
	protected File executable;

	private ArrayList<String> arguments = new ArrayList<String>();
	private VisualStudioSolution solution;
	private List<String> propertiesList;

    public ReSharperCommandBuilder() {
        
    }

	/**
	 * Constructs a {@link ReSharperCommandBuilder} object for the given Visual
	 * Studio project.
	 * 
	 * @param solution
	 *            the current VS solution
	 * @param project
	 *            the VS project to analyze
	 * @param settings
	 * 
	 * @return a ReSharper builder for this project
	 */
	public static ReSharperCommandBuilder createBuilder() {
		ReSharperCommandBuilder builder = new ReSharperCommandBuilder();
		return builder;
	}

	public void setSolution(VisualStudioSolution visualStudioSolution) {
	    this.solution=visualStudioSolution;
	}
	
	public void setProperties(List<String> properties) {
	    this.propertiesList=properties;
	}
	/**
	 * Sets the report file to generate
	 * 
	 * @param reportFile
	 *            the report file
	 * @return the current builder
	 */
	public ReSharperCommandBuilder setReportFile(File reportFile) {
		this.resharperReportFile = reportFile;
		return this;
	}

	/**
	 * Sets the executable
	 * 
	 * @param executable
	 *            the executable
	 * 
	 */
	public void setExecutable(File executable) {
		this.executable = executable;
	}

	/**
	 * Transforms this command object into a array of string that can be passed
	 * to the CommandExecutor.
	 * 
	 * @return the Command that represent the command to launch.
	 */
	public Command toCommand() throws ReSharperException {

		// $> c:\ThirdPartyTools\jb-commandline-8.0.0.39\inspectcode.exe /help
		// InspectCode for .NET
		// Running in 32-bit mode, .NET runtime 4.0.30319.18051 under Microsoft
		// Windows NT 6.2.9200.0
		// Usage: InspectCode [options] SolutionFile
		//
		// Options:
		// /output (/o) : Write inspections report to specified file.
		// /no-swea : Disable solution-wide analysis (default: False)
		// /project : Analyze only projects selected by provided wildcards
		// (default: analyze all solution)
		// /profile (/p) : Path to the file to use custom settings from
		// (default: Use R#'s solution shared settings if exists)
		// /no-buildin-settings : Supress solution shared settings profile usage
		// (default: False)
		// /caches-home : Path to the directory where produced cashes will be
		// stored.
		// /debug (/d) : Show debugging messages (default: False)
		// /help (/h) : Show help and exit
		// /version (/v) : Show tool version and exit
		// /dumpPlatforms (/dpl) : Dump platforms description to file and exit
		// /dumpProject (/dpm) : Dump project model description to file and exit

		LOG.debug("- ReSharper program         : " + executable);
		Command command = Command.create(executable.getAbsolutePath());


		LOG.debug("- Report file               : " + resharperReportFile);
		command.addArgument("/output=" + resharperReportFile.getAbsolutePath());

		LOG.debug("- Solution file               : " + solution.getSolutionFile().getAbsolutePath());

		for (String argument : arguments) {
			LOG.debug(" - Argument : " + argument);
			command.addArgument(argument);
		}

		addPropertiesIfSet(command);
		command.addArgument(solution.getSolutionFile().getAbsolutePath());

		return command;
	}

	private void addPropertiesIfSet(Command command) {
		if (propertiesList != null && propertiesList.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (String property : propertiesList) {
				sb.append(property).append(";");
			}
			//removeTrailing ;
			String dirtyArgument = sb.toString();
			String argument = dirtyArgument.substring(0,
					dirtyArgument.length() - 1);
			
			LOG.debug("- Properties                 : " + argument);
			command.addArgument("/properties:" + argument);
		}
	}

	/**
	 * if value is not empty, the concatenation of name & value are added to the
	 * arguments When building the arguments are added before the solution
	 * argument.
	 * 
	 * @param name
	 *            of the argument
	 * @param value
	 *            of the agument
	 */
	public void addArgument(String name, String value) {
		if (StringUtils.isEmpty(value))
			return;
		arguments.add(name + value);
	}

	public void setCachesHome(String cachesHome) {
		if(StringUtils.isEmpty(cachesHome)) return;
		arguments.add("/caches-home=" + cachesHome);
	}

	public void setProfile(String profile) {
		if(StringUtils.isEmpty(profile)) return;
		arguments.add("/profile=" + profile);
	}

	/**
	 * add additional msbuild properties
	 * @param properties
	 */
	public void setProperties(String properties) {
		if(!StringUtils.isEmpty(properties)) {
			this.propertiesList.add(properties);
		}
	}

    @Override
    public String toCommandLine() {
        return toCommand().toString();
    }

    @Override
    public String getExecutable() {
        return "InspectCode";
    }


}
