package com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter;

import java.io.File;

public interface CoverageToXmlConverter {

	/**
	 * Convert a .coverage file to a .xml file
	 * @param destination full path to .xml file
	 * @param source full path to .coverage file
	 */
	void convert(File destination, File source);

	/**
	 * does the conversion if destinationPath does not exist, or is older than source

	 * @param source
	 */
	File convertIfNeeded(File source);

}