/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sonar.api.batch.fs.FileSystem;

import com.stevpet.sonar.plugins.common.api.CommandLineExecutor;
import com.stevpet.sonar.plugins.common.commandexecutor.ProcessLock;

public class VsTestCoverageToXmlConverterTest {

	@Mock private CommandLineExecutor commandLineExecutor;
	@Mock private FileSystem fileSystem ;
	@Mock private CodeCoverageCommand codecoverageCommand;
	private File workDirFile=new File("C:\\etc\\workdir");
	private File destination;
	private File source = new File("source.coverage");
	
	private BinaryCoverageToXmlConverter converter ;
	@Mock private ProcessLock processLock;
	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		converter = new VsTestCoverageToXmlConverterBase(fileSystem, codecoverageCommand,commandLineExecutor, processLock);
		when(fileSystem.workDir()).thenReturn(workDirFile);
		destination=converter.convertFiles(source);
	}
	
	@Test
	public void convert_SetOutputPath() {
		verify(codecoverageCommand,times(1)).setOutputPath(eq(destination.getAbsolutePath()));
	}

	@Test
	public void convert_SetSonarPath() {
		verify(codecoverageCommand,times(1)).setSonarPath(eq("C:\\etc\\workdir"));
	}
	
	@Test
	public void convert_Install() {
		verify(codecoverageCommand,times(1)).install();
		
	}
	
	@Test
	public void convert_Execute() {
		verify(commandLineExecutor,times(1)).execute(eq(codecoverageCommand));
		
	}
}
