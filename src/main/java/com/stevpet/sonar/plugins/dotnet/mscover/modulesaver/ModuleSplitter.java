package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

public interface ModuleSplitter {

	/**
	 * Parse the OpenCover coverage file, and save each module
	 * 
	 * @param file - OpenCover coverage file
	 * @return
	 * @throws FileNotFoundException
	 * @throws XMLStreamException
	 * @throws TransformerException
	 */  
    int splitCoverageFileInFilePerModule(File coverageRootDir, String testProjectName,File testCoverageFile);
}