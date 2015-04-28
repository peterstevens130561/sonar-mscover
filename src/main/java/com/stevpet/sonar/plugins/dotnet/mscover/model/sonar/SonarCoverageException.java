package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;

import org.sonar.api.utils.SonarException;

public class SonarCoverageException extends SonarException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4798530412097996920L;

	public SonarCoverageException(String msg) {
		super(msg);
	}
}
