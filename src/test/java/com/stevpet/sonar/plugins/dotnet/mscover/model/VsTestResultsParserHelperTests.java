package com.stevpet.sonar.plugins.dotnet.mscover.model;

import static org.junit.Assert.assertEquals;

import java.time.LocalTime;

import org.junit.Test;

public class VsTestResultsParserHelperTests {

    private ParserHelper parserHelper = new ParserHelper();

    
    @Test
    public void justMsTim() {
        LocalTime result = parserHelper.parseDurationToTime("00:00:00.5349695");
        assertEquals(534969508L,result.toNanoOfDay());
    }
    
    
    @Test
    public void veryLongTime() {
        LocalTime result = parserHelper.parseDurationToTime("11:15:20.5349695");
        assertEquals(11,result.getHour());
        assertEquals(15,result.getMinute());
        assertEquals(20,result.getSecond());
        assertEquals(534969508L,result.getNano());   
    }
}
