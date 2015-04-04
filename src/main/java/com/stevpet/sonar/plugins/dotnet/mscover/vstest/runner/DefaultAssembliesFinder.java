package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.AssemblyResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.BaseAssemblyResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.BinConfigAssemblyResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.FailedAssemblyResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.HintPathAssemblyResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.IgnoreMissingAssemblyResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;

public class DefaultAssembliesFinder extends AbstractAssembliesFinder implements
		AssemblyResolver {
	private AssemblyResolver assemblyResolver = new BaseAssemblyResolver();

	public DefaultAssembliesFinder(MsCoverProperties propertiesHelper) {
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

	public void setMsCoverProperties(MsCoverProperties msCoverProperties) {
		assemblyResolver.setMsCoverProperties(msCoverProperties);
	}

	public MsCoverProperties getMsCoverProperties() {
		return assemblyResolver.getMsCoverProperties();
	}
}
