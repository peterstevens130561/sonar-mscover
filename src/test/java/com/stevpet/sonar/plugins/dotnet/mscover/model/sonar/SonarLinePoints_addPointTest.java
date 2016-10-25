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
package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SonarLinePoints_addPointTest {
    private SonarLinePoints linePoints = new SonarLinePoints() ;
    
    @Test 
    public void emptyList() {
        SonarLinePoint point=(SonarLinePoint) linePoints.getLast();
        assertNull("empty list last is null",point);
    }
    @Test
    public void addFirstPoint() {
        linePoints.addPoint(10, false);
        SonarLinePoint point=(SonarLinePoint) linePoints.getPoints().get(0);
        assertNotNull("should have point");
        assertEquals(10,point.getLine());
        assertEquals(0,point.getCovered());
        assertEquals(1,point.getToCover());
    }
    
    @Test
    public void addSecondPointSameLine() {
        linePoints.addPoint(10, true);
        linePoints.addPoint(10, false);
        SonarLinePoint point=(SonarLinePoint) linePoints.getPoints().get(0);
        assertEquals(10,point.getLine());
        assertEquals("second point indicates line is partially covered",0,point.getCovered());
        assertEquals("though two points, cover once",1,point.getToCover());
    }
    
    @Test
    public void addSecondPointSameLineNotCovered() {
        linePoints.addPoint(10, false);
        linePoints.addPoint(10, true);
        SonarLinePoint point=(SonarLinePoint) linePoints.getPoints().get(0);
        assertEquals(10,point.getLine());
        assertEquals("first point indicates line is partially covered",0,point.getCovered());
        assertEquals("though two points, cover once",1,point.getToCover());
    }
    
    @Test
    public void addSecondPointSameLineCovered() {
        linePoints.addPoint(10, true);
        linePoints.addPoint(10, true);
        SonarLinePoint point=(SonarLinePoint) linePoints.getPoints().get(0);
        assertEquals(10,point.getLine());
        assertEquals("both points indicates line is fully covered",1,point.getCovered());
        assertEquals("though two points, cover once",1,point.getToCover());
    }
    
    @Test
    public void addThirdPointSameLineNotCovered() {
        linePoints.addPoint(10, true);
        linePoints.addPoint(10, false);
        linePoints.addPoint(10, true);
        SonarLinePoint point=(SonarLinePoint) linePoints.getPoints().get(0);
        assertEquals(10,point.getLine());
        assertEquals("one points indicates line is partially covered",0,point.getCovered());
        assertEquals("though three points, cover once",1,point.getToCover());
    }
}
