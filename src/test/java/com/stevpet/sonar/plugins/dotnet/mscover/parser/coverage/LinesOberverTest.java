package com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage;

import org.junit.Assert;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.parser.coverage.LinesObserver;



public class LinesOberverTest {
    private static final String PATTERN_LN_END = "Module/NamespaceTable/Class/Method/Lines/LnEnd";
    private static final String PATTERN_SOURCEFILEID = "Module/NamespaceTable/Class/Method/Lines/SourceFileID";
    private static final String PATTERN_COVERAGE = "Module/NamespaceTable/Class/Method/Lines/Coverage";
    private static final String PATTERN_LNSTART = "Module/NamespaceTable/Class/Method/Lines/LnStart";
    LinesObserver observer = new LinesObserver();
    @Test 
    public void MatchLnStart_ShouldMatch() {
        String path = PATTERN_LNSTART;
        //Act
        boolean isMatch = observer.isMatch(path);
        //Assert
        Assert.assertTrue(isMatch);
    }
    
    @Test 
    public void MatchCoverage_ShouldMatch() {
        String path = PATTERN_COVERAGE;
        //Act
        boolean isMatch = observer.isMatch(path);
        //Assert
        Assert.assertTrue(isMatch);
    }
    
    @Test 
    public void MatchSourceFileID_ShouldMatch() {
        String path = PATTERN_SOURCEFILEID;
        //Act
        boolean isMatch = observer.isMatch(path);
        //Assert
        Assert.assertTrue(isMatch);
    }
    
    @Test 
    public void MatchLnEnd_ShouldNotMatch() {
        String path = PATTERN_LN_END;
        //Act
        boolean isMatch = observer.isMatch(path);
        //Assert
        Assert.assertFalse(isMatch);
    }
 
}
