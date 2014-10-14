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
