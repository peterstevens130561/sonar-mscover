package com.stevpet.sonar.plugins.dotnet.mscover.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VsTestResultsParserHelperTests {

    private ParserHelper parserHelper = new ParserHelper();
    @Test
    public void justMs() {
        long result = parserHelper.parseDuration("00:00:00.5349695");
        assertEquals(534969,result);
    }
    
    @Test
    public void justSeconds() {
        long result = parserHelper.parseDuration("00:00:20.5349695");
        assertEquals(20534969,result);
    }
    
    @Test
    public void minutes() {
        long result = parserHelper.parseDuration("00:15:20.5349695");
        assertEquals(20534969+900*1E6,result,1E-6);
    }
    
    @Test
    public void oneHours() {
        long result = parserHelper.parseDuration("01:00:00.00");
        assertEquals(3600 * 1E6,result,1E-8);
    }
    
    @Test
    public void tenHours() {
        long result = parserHelper.parseDuration("10:00:00.00");
        assertEquals(36000 * 1E6,result,1E-8);
    }
    
    @Test
    public void hours() {
        long result = parserHelper.parseDuration("20:00:00.00");
        assertEquals(3600 * 20 * 1E6,result,1E-8);
    }
    
    @Test
    public void veryLong() {
        long result = parserHelper.parseDuration("11:15:20.5349695");
        assertEquals(20534969+900*1E6 + 11*3600*1E6,result,1E-6);       
    }
}
