package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

interface ModuleSplitter {

	/**
	 * Parse the OpenCover coverage file, and give each Module to the moduleLambda to process
	 * 
	 * @param file - OpenCover coverage file
	 * @return
	 * @throws FileNotFoundException
	 * @throws XMLStreamException
	 * @throws TransformerException
	 */
	int splitFile(File file) throws FileNotFoundException, XMLStreamException,
			TransformerException;

}