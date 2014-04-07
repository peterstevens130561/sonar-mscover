package com.stevpet.sonar.plugins.dotnet.mscover.registry;

public interface Registry<T> {
    public void add(String fileId,T model);
    
    public T get(String fileId);
    
    public int size() ;
}
