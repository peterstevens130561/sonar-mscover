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
