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

import static org.junit.Assert.*;

import org.junit.Test;

public class MethodIdModelEqualityTest {



    @Test
    public void sameMethodState_Equal() {
        MethodIdModel method1 = createMethodeOne();
        MethodIdModel method2 = createMethodeOne();
        assertTrue(method1.equals(method2));
    }
    
    @Test
    public void differentMethodState_UnEqual() {
        MethodIdModel method1 = createMethodeOne();
        MethodIdModel method2 = createMethodTwo();
        assertFalse(method1.equals(method2));
    }
    
    @Test
    public void differentClassState_UnEqual() {
        MethodIdModel method1 = createMethodeOne();
        MethodIdModel method2 = createMethodTwo();
        method2.setClassName("class2");
        assertFalse(method1.equals(method2));
    }
    
    @Test
    public void differentAssemblyState_UnEqual() {
        MethodIdModel method1 = createMethodeOne();
        MethodIdModel method2 = createMethodTwo();
        method2.setModuleName("module2.dll");
        assertFalse(method1.equals(method2));
        
    }
    
    @Test
    public void differentNameSpace2State_UnEqual() {
        MethodIdModel method1 = createMethodeOne();
        MethodIdModel method2 = createMethodTwo();
        method2.setNamespaceName("namespace2");
        assertFalse(method1.equals(method2));
        
    }
    @Test
    public void sameMethodState_EqualSign() {
        MethodIdModel method1 = createMethodeOne();
        MethodIdModel method2 = createMethodeOne();
        assertFalse(method1==method2);
    }
    
    @Test
    public void sameMethodState_SameHashCode() {
        MethodIdModel method1 = createMethodeOne();
        int hash1=method1.hashCode();
        MethodIdModel method2 = createMethodeOne();
        int hash2=method2.hashCode();
        assertTrue(hash1==hash2);
    }
    private MethodIdModel createMethodeOne() {
        return new MethodIdModel("module.dll","namespace","class","method");
    }
    
    private MethodIdModel createMethodTwo() {
        return new MethodIdModel("module.dll","namespace","class","method2");
    }

}
