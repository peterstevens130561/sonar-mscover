package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import org.sonar.api.resources.File;

public interface ResourceResolver {

	File getFile(java.io.File file);

}