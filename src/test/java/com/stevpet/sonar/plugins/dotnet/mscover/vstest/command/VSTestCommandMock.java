/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
 * dev@sonar.codehaus.org
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
 */
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
