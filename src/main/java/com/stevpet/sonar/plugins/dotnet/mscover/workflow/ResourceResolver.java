package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import org.sonar.api.resources.File;

public interface ResourceResolver {

	/**
	 * 
	 * @param file somewhere in the project
	 * @return the matching resource. If not in the project null is returned!
	 */
	File getFile(java.io.File file);

}