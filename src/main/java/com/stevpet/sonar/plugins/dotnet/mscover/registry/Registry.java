package com.stevpet.sonar.plugins.dotnet.mscover.registry;

public interface Registry<T> {
     void add(String fileId,T model);
    
    T get(String fileId);
    
     int size() ;
}
