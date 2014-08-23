package com.stevpet.sonar.plugins.dotnet.mscover.model;

import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.opencover.model.OpenCoverSequencePoint;

public class Method {
    private int fileRef ;
    private List<OpenCoverSequencePoint> sequencePoints ;
    private List<BranchPoint> branchPoints ;
    
 }
