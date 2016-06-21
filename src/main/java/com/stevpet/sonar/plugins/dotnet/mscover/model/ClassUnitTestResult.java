/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
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
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.model;

import java.io.File;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult.TestResult;

public class ClassUnitTestResult {
    private Logger LOG = LoggerFactory.getLogger(ClassUnitTestResult.class);
    private int error;
    private int ignored;
    private int passed;
    private LocalTime localTime = LocalTime.MIN;
    private List<UnitTestMethodResult> tests = new ArrayList<UnitTestMethodResult>();
    private File file;

    public ClassUnitTestResult(File file) {
        this.file = file;
    }

    public void add(UnitTestMethodResult unitTest) {
        tests.add(unitTest);
        TestResult  testResult= unitTest.getTestResult();
        switch(testResult) {
            case Passed : 
                ++passed; 
                break;
            case Ignored : 
                ++ignored ; 
                break;
            case Failed : 
                ++error;
                break;
            default:
                throw new InvalidTestResultException(testResult.toString());
        }
        localTime=localTime.plus(unitTest.getLocalTime().toNanoOfDay(),ChronoUnit.NANOS);
                   
    }

    public File getFile() {
        return file;
    }

    public double getPassed() {
        return (double) passed;
    }

    public double getFail() {
        return (double) error;
    }

    public double getIgnored() {
        return (double) ignored;
    }
    public double getTests() {
        return (double) tests.size();
    }

    public double getDensity() {
        if (tests.isEmpty()) {
            return 1.0;
        }
        double executedTests =  getTests() - getIgnored() ;
        if(executedTests == 0) {
            return 1.0;
        }
        
        return getPassed() / executedTests;
    }

    public List<UnitTestMethodResult> getUnitTests() {
        return tests;
    }

   
    public LocalTime getLocalTime() {
        return localTime;
    }

    public Double getLocalTimeMillis() {
       return (double) (localTime.getNano()/1000000);
    }
}
