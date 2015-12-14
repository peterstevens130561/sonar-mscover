package com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter;

import java.io.File;

public interface BinaryCoverageToXmlConverter {


	/**
	 * does the conversion of either file or directory

	 * @param source
	 * If the source is a single file, then it returns the converted file
	 * IF the source is a directory, then it returns that directory.
	 * The conversion will remove the original files
	 * If there are no binary files, but there are xml files, then it will behave is if they are there
	 * If there are no binary files, and no xml files, then null will be returned.
	 */

	File convertFiles(File source);

	void convert(File file, File file2);

}