package com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command;


/**
 * Use shim to disable the install attempt. When invoking execute make sure a mock is used, as the command should
 * not be executed in unit tests
 * @author stevpet
 *
 */
public class WindowsCodeCoverageCommandShim extends WindowsCodeCoverageCommand {

	@Override 
	public void install() {
		
	}
	
	@Override
	public String toString() {
		return toCommandLine();
	}
}
