package com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.File;
import org.sonar.api.scan.filesystem.PathResolver;

/**
 * Wrapper for the fromIOFile statuc
 * @author stevpet
 *
 */
public class DefaultResourceResolver implements ResourceResolver {
	
	private final PathResolver pathResolver ;
	private final FileSystem fileSystem ;
	
	@SuppressWarnings("ucd")
	public DefaultResourceResolver(PathResolver pathResolver,FileSystem fileSystem) {
		this.pathResolver = pathResolver;
		this.fileSystem=fileSystem;
	}
	
	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.workflow.ResourceResolver#getFile(java.io.File)
	 */
	@Override
	public File getFile(java.io.File file) {
		File sonarFile=null;
		String relativePathFromBasedir = pathResolver.relativePath(fileSystem.baseDir(), file);
	    if (relativePathFromBasedir != null) {
	        sonarFile= File.create(relativePathFromBasedir);
	    }
	    return sonarFile;
	}
}
