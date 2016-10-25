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
