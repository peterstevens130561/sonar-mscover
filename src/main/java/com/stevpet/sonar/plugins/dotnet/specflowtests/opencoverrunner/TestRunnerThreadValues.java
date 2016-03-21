package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class TestRunnerThreadValues {

    private final Future<Boolean> future;
    private final Callable<Boolean> callable;
    private final String name;

    public TestRunnerThreadValues(Future<Boolean> future,Callable<Boolean> callable,String name) {
        this.future = future;
        this.callable=callable;
        this.name = name;
    }
    
    public Future<Boolean> getFuture() {
        return future;
    }
    
    public Callable<Boolean> getCallable() {
        return callable;
    }
    
    public String getName() {
        return name;
    }
}
