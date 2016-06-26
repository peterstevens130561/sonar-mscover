package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

public interface ModuleSplitter {

	/**
	 * Parse the OpenCover coverage file, and give each Module to the moduleLambda to process
	 * 
	 * @param file - OpenCover coverage file
	 * @return
	 * @throws FileNotFoundException
	 * @throws XMLStreamException
	 * @throws TransformerException
	 */
    @Deprecated
	int splitFile(File file) throws FileNotFoundException, XMLStreamException,
			TransformerException;

    @Deprecated
    ModuleSplitter setRoot(File coverageDir);

    @Deprecated
    ModuleSplitter setProject(String string);
    
    int splitCoverageFileInFilePerModule(File coverageRootDir, String testProjectName,File testCoverageFile);
}