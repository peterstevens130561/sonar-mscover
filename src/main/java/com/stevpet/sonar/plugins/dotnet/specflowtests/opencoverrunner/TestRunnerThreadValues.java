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

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class TestRunnerThreadValues {

    private final Future<Boolean> future;
    private final Callable<Boolean> callable;
    private final String name;

    public TestRunnerThreadValues(Future<Boolean> future,Callable<Boolean> callable,String name) {
        this.future = future;
        this.callable=callable;
        this.name = name;
    }
    
    public Future<Boolean> getFuture() {
        return future;
    }
    
    public Callable<Boolean> getCallable() {
        return callable;
    }
    
    public String getName() {
        return name;
    }
}
