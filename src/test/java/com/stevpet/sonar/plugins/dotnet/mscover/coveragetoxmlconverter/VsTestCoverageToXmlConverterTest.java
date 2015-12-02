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
		converter = new VsTestCoverageToXmlConverter(fileSystem, codecoverageCommand,commandLineExecutor, processLock);
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
