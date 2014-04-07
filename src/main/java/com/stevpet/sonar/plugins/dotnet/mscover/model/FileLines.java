package com.stevpet.sonar.plugins.dotnet.mscover.model;

import java.util.ArrayList;
import java.util.List;

public class FileLines {
    List<LineModel> lines = new ArrayList<LineModel>() ;
    
    public void add(LineModel line) {
        lines.add(line);
    }

    public List<LineModel> get() {
        return lines;
    }
    
}
