package com.stevpet.sonar.plugins.dotnet.mscover;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunnerCommandBuilder;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.when;

public class VsTestRunnerCommandBuilderMock extends GenericClassMock<VsTestRunnerCommandBuilder> {

    public VsTestRunnerCommandBuilderMock() {
        super(VsTestRunnerCommandBuilder.class);
    }

	public void givenBuild(VSTestCommand openCoverCommand) {
		when(instance.build(anyBoolean())).thenReturn(openCoverCommand);
	}

}
