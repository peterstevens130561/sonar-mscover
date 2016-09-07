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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.AssemblyResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.BaseAssemblyResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.BinConfigAssemblyResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.FailedAssemblyResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.HintPathAssemblyResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.IgnoreMissingAssemblyResolver;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;

public class DefaultAssembliesFinder extends AbstractAssembliesFinder implements
		AssemblyResolver {
	private AssemblyResolver assemblyResolver = new BaseAssemblyResolver();

	public DefaultAssembliesFinder(MsCoverConfiguration propertiesHelper) {
		super(propertiesHelper);
		AssemblyResolver[] assembliesFinders = { new FailedAssemblyResolver(),
				new IgnoreMissingAssemblyResolver(),
				new HintPathAssemblyResolver(), new BinConfigAssemblyResolver() };

		AssemblyResolver nextResolver = null;
		for (AssemblyResolver resolver : assembliesFinders) {
			resolver.setMsCoverProperties(propertiesHelper);
			resolver.setResolver(nextResolver);
			nextResolver = resolver;
		}

		setMsCoverProperties(propertiesHelper);
		setResolver(nextResolver);
	}

	public File resolveAssembly(File assemblyFile, VisualStudioProject project,
			String buildConfiguration) {

		return null;
	}

	/**
	 * Invoked from AbstractAssembliesFinder
	 */
	public File searchNonExistingFile(File assemblyFile,
			VisualStudioProject project, String buildConfiguration) {

		return resolveChain(assemblyFile, project, buildConfiguration);
	}

	public File resolveChain(File assemblyFile, VisualStudioProject project,
			String buildConfiguration) {

		return assemblyResolver.resolveChain(assemblyFile, project,
				buildConfiguration);
	}

	public void setResolver(AssemblyResolver assemblyResolver) {
		this.assemblyResolver.setResolver(assemblyResolver);
	}

	public void setMsCoverProperties(MsCoverConfiguration msCoverProperties) {
		assemblyResolver.setMsCoverProperties(msCoverProperties);
	}

	public MsCoverConfiguration getMsCoverProperties() {
		return assemblyResolver.getMsCoverProperties();
	}
}
