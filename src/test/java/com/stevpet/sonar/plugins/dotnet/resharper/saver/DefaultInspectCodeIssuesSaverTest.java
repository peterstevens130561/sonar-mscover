package com.stevpet.sonar.plugins.dotnet.resharper.saver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.eq;

import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.issue.Issuable.IssueBuilder;
import org.sonar.api.issue.Issue;
import org.sonar.api.resources.Resource;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.MessageException;
import org.sonar.test.TestUtils;

import static org.mockito.MockitoAnnotations.initMocks;

import com.stevpet.sonar.plugins.dotnet.resharper.InspectCodeIssue;
import com.stevpet.sonar.plugins.dotnet.resharper.saver.DefaultInspectCodeIssuesSaver;
import com.stevpet.sonar.plugins.dotnet.resharper.saver.InspectCodeIssuesSaver;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolution;

public class DefaultInspectCodeIssuesSaverTest {

    private static final String TESTS_SOURCEFILE = "DefaultInspectCodeIssuesSaverTest/UnitTests/unittests.cs";
    @Mock private MicrosoftWindowsEnvironment microsoftWindowsEnvironment ;
    InspectCodeIssuesSaver saver ;
    @Mock private ResourcePerspectives resourcePerspectives;
    @Mock private Issuable issuable;
    @Mock private IssueBuilder issueBuilder;
    @Mock private Issue issue;
    private  File testFile=TestUtils.getResource(TESTS_SOURCEFILE);
    @Mock private VisualStudioSolution solution;
    @Before
    public void before() {
        initMocks(this);
        saver = new DefaultInspectCodeIssuesSaver(resourcePerspectives, microsoftWindowsEnvironment);
        when(resourcePerspectives.as(eq(Issuable.class),any(Resource.class))).thenReturn( issuable);
        when(issuable.newIssueBuilder()).thenReturn(issueBuilder);
        when(issueBuilder.message(anyString())).thenReturn(issueBuilder);
        when(issueBuilder.ruleKey(any(RuleKey.class))).thenReturn(issueBuilder);
        when(issueBuilder.line(any(Integer.class))).thenReturn(issueBuilder);
        when(issueBuilder.build()).thenReturn(issue);
        
        assertNotNull(testFile);
        File solutionDir=testFile.getParentFile().getParentFile();
        when(solution.getSolutionDir()).thenReturn(solutionDir);
        when(microsoftWindowsEnvironment.getCurrentSolution()).thenReturn(solution);
    }
    
    @Test
    public void issueInTestProject_shouldNotSave() {
        List<InspectCodeIssue> inspectCodeIssues = givenFileIsTestFile();
        saver.saveIssues(inspectCodeIssues);
        verify(issuable,times(0)).addIssue(issue);
    }

    private List<InspectCodeIssue> givenFileIsTestFile() {
        List<InspectCodeIssue> inspectCodeIssues = createIssueInTestFile();
        List<File> sourceFileList= new ArrayList<>();
        sourceFileList.add(testFile);
        assertNotNull("could not get testresource");
        sourceFileList.add(testFile);
        when(microsoftWindowsEnvironment.getUnitTestSourceFiles()).thenReturn(sourceFileList);
        return inspectCodeIssues;
    }
    
    @Test
    public void inTestProject_shoulSave() {
        List<InspectCodeIssue> inspectCodeIssues = createNormalIssue();

        saver.saveIssues(inspectCodeIssues);
        verify(issuable,times(1)).addIssue(issue);
        verify(issueBuilder,times(1)).message("defaultmessage");
        verify(issueBuilder,times(1)).line(10);
    }
    
    @Test
    public void inProject_MessageExceptionThrownAndCaught() {
        List<InspectCodeIssue> inspectCodeIssues = createNormalIssue();

        saver.saveIssues(inspectCodeIssues);
        MessageException.of("some error");
        when(issuable.addIssue(issue)).thenThrow(MessageException.of("some error"));
        verify(issuable,times(1)).addIssue(issue);
    }

    private List<InspectCodeIssue> createNormalIssue() {
        List<InspectCodeIssue> inspectCodeIssues = new ArrayList<>();
        String issuePath="MyProject/sourcefile.cs";
        buildIssue(inspectCodeIssues, issuePath);
        return inspectCodeIssues;
    }

    private void buildIssue(List<InspectCodeIssue> inspectCodeIssues, String issuePath) {
        InspectCodeIssue inspectCodeissue = new InspectCodeIssue();
        inspectCodeissue.setRelativePath(issuePath);
        inspectCodeissue.setLine("10");
        inspectCodeissue.setTypeId("CS3017");
        inspectCodeissue.setMessage("defaultmessage");
        inspectCodeIssues.add(inspectCodeissue);
    }
    
    private List<InspectCodeIssue> createIssueInTestFile() {
        List<InspectCodeIssue> inspectCodeIssues = new ArrayList<>();
        String issuePath="UnitTests/unittests.cs";
        buildIssue(inspectCodeIssues, issuePath);
        return inspectCodeIssues;
    }
    
 
}

