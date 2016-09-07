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
package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class TestRunnerThreadValuesTest {
    private static final String PROJECT_NAME = "ProjectName";
    private TestRunnerThreadValues threadValues;
    @Mock private Future<Boolean> future;
    @Mock private Callable<Boolean> callable;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        threadValues = new TestRunnerThreadValues(future, callable, PROJECT_NAME);
    }
    
    @Test
    public void isSameFuture() {
        assertEquals("future gotten is same as inserted",future,threadValues.getFuture());
    }
    
    @Test
    public void isSameCallable() {
        assertEquals("callable gotten is samee as inserted",callable,threadValues.getCallable());
    }
    
    @Test
    public void isSameName() {
        assertEquals("project name gotten is same as inserted",PROJECT_NAME,threadValues.getName());
    }
}
