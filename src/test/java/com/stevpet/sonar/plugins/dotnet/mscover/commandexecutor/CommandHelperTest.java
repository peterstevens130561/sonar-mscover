package com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class CommandHelperTest {

    @Test
    public void nullList_ExpectEmptyList() {
        List<String> parenthesized=CommandHelper.parenthesizeArguments(null);
        assertNotNull(parenthesized);
        assertEquals(parenthesized.size(),0);
    }
    
    @Test
    public void normalItem_ExpectParenthesis() {
        List<String> input = new ArrayList<String>();
        input.add("john");
        List<String> parenthesized=CommandHelper.parenthesizeArguments(input);
        assertNotNull(parenthesized);
        assertEquals(parenthesized.size(),1);
        assertEquals("\"john\"",parenthesized.get(0));
    }
    
    @Test
    public void nullItem_ExpectSkipped() {
        List<String> input = new ArrayList<String>();
        input.add(null);
        List<String> parenthesized=CommandHelper.parenthesizeArguments(input);
        assertNotNull(parenthesized);
        assertEquals(parenthesized.size(),0);
    }

}
