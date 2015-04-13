package com.stevpet.sonar.plugins.dotnet.mscover.vstest.command;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import static org.mockito.Mockito.when;
public class VSTestCommandMock extends GenericClassMock<VSTestCommand> {

	public VSTestCommandMock() {
		super(VSTestCommand.class);
	}
	
	public void giveExeDir(String path) {
		when(instance.getExecutable()).thenReturn(path);
	}

	public void givenArguments(String arguments) {
		when(instance.getArguments()).thenReturn(arguments);
	}
}
