package com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter;

public interface CoverageToXmlConverter {

	/**
	 * Convert a .coverage file to a .xml file
	 * @param destination full path to .xml file
	 * @param source full path to .coverage file
	 */
	void convert(String destination, String source);

}