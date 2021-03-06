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
package com.stevpet.sonar.plugins.dotnet.mscover.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class MethodIdModelEqualityTest {



    @Test
    public void sameMethodState_Equal() {
        MethodId method1 = createMethodeOne();
        MethodId method2 = createMethodeOne();
        assertTrue(method1.equals(method2));
    }
    
    @Test
    public void differentMethodState_UnEqual() {
        MethodId method1 = createMethodeOne();
        MethodId method2 = new MethodId("module.dll","namespace","class2","method2");
        assertFalse(method1.equals(method2));
    }
    
    @Test
    public void differentClassState_UnEqual() {
        MethodId method1 = createMethodeOne();
        MethodId method2 = new MethodId("module.dll","namespace","class2","method");
        assertFalse(method1.equals(method2));
    }
    
    @Test
    public void differentAssemblyState_UnEqual() {
        MethodId method1 = createMethodeOne();
        MethodId method2 = new MethodId("module2.dll","namespace","class2","method");
        assertFalse(method1.equals(method2));
        
    }
    
    @Test
    public void differentNameSpace2State_UnEqual() {
        MethodId method1 = createMethodeOne();
        MethodId method2 = new MethodId("module.dll","namespace2","class","method");
        assertFalse(method1.equals(method2));
        
    }
    @Test
    public void sameMethodState_EqualSign() {
        MethodId method1 = createMethodeOne();
        MethodId method2 = createMethodeOne();
        assertFalse(method1==method2);
    }
    
    @Test
    public void sameMethodState_SameHashCode() {
        MethodId method1 = createMethodeOne();
        int hash1=method1.hashCode();
        MethodId method2 = createMethodeOne();
        int hash2=method2.hashCode();
        assertTrue(hash1==hash2);
    }
    
    @Test
    public void fallBackShouldNotBeEqual() {
    	MethodId method1 = createMethodeOne() ;
    	MethodId fallBack = method1.getFallBack();
    	assertNotEquals("fallBack should be different",method1,fallBack);
    }
    
    @Test
    public void differentType_ShouldBeUnequal() {
        String myString = " bogus";
        MethodId method1 = createMethodeOne();
        assertTrue("different type, should be unequal",!method1.equals(myString));
    }
    private MethodId createMethodeOne() {
        return new MethodId("module.dll","namespace","class","method");
    }
    

}
