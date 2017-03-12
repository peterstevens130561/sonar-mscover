/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2017 Baker Hughes
 * peter.stevens@bakerhughes.com
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
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.sonar.api.utils.text.XmlWriter;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyInt;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.eq;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.DefaultProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;

public class DefaultCoverageSerializationServiceTests {

    @Mock XmlWriter xmlWriter ;
    CoverageSerializationService service ;
    private ProjectCoverageRepository repository; 
   
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        when(xmlWriter.begin(anyString())).thenReturn(xmlWriter);
        when(xmlWriter.prop(anyString(),anyString())).thenReturn(xmlWriter);
        when(xmlWriter.prop(anyString(),anyInt())).thenReturn(xmlWriter);
        service = new DefaultCoverageSerializationService();
        repository=new DefaultProjectCoverageRepository();
    }
    
    @Test
    public void noCoverage_ShouldHaveBody() {

        whenSerialize();
        
        InOrder order = Mockito.inOrder(xmlWriter);
        order.verify(xmlWriter,times(1)).begin("coverage");
        order.verify(xmlWriter,times(1)).prop("version","1");
        order.verify(xmlWriter,times(1)).end();
        verify(xmlWriter,times(1)).begin(anyString());
    }
    
    @Test
    public void oneFileNoResults_ShouldHaveOneFile() {

        repository.linkFileNameToFileId("myfile", "1");
        whenSerialize();
        verify(xmlWriter,times(1)).begin("coverage");
        verify(xmlWriter,times(1)).begin("file");
        verify(xmlWriter,times(1)).prop("path","myfile");
        verify(xmlWriter,times(2)).end();
        
        // no others should be invoked
        verify(xmlWriter,times(2)).begin(anyString());
        verify(xmlWriter,times(2)).prop(anyString(),anyString());
    }
    
    @Test
    public void oneFileOneLines_ShouldBeInRepor() {

        repository.linkFileNameToFileId("myfile", "1");
        repository.addLinePoint("1", 1, true);
        
        whenSerialize();
        
        InOrder order = Mockito.inOrder(xmlWriter);
        
        verify(xmlWriter,times(1)).prop("lineNumber",1);
        verify(xmlWriter,times(1)).prop("covered","true");
        
        // check that there is no attempt to create branch coverage
        verify(xmlWriter,times(0)).prop(eq("branchesToCover"),anyString());
        verify(xmlWriter,times(0)).prop(eq("coveredBranches"),anyString());
        order.verify(xmlWriter,times(1)).begin("lineToCover");
        order.verify(xmlWriter,times(3)).end();
       
        // no others should be invoked
        verify(xmlWriter,times(3)).begin(anyString());
    }
    
    @Test
    public void oneFileTwoLinesOneNotCovered_ShouldBeInRepor() {

        repository.linkFileNameToFileId("myfile", "1");
        repository.addLinePoint("1", 1, true);
        repository.addLinePoint("1", 2, false);
        
        whenSerialize();
        InOrder order = Mockito.inOrder(xmlWriter);
        
        // check that there is no attempt to create branch coverage
        verify(xmlWriter,times(0)).prop(eq("branchesToCover"),anyString());
        verify(xmlWriter,times(0)).prop(eq("coveredBranches"),anyString());
        
        // check two lines are thee
        verify(xmlWriter,times(1)).prop("lineNumber",1);
        verify(xmlWriter,times(1)).prop("covered","true");
        verify(xmlWriter,times(1)).prop("lineNumber",2);
        verify(xmlWriter,times(1)).prop("covered","false");
        
        order.verify(xmlWriter,times(1)).begin("lineToCover");
        order.verify(xmlWriter,times(4)).end();
       
        // no others should be invoked
        verify(xmlWriter,times(4)).begin(anyString());
    }
    @Test
    public void twoFileNoResults_ShouldHaveTwoFile() {

        repository.linkFileNameToFileId("myfile", "1");
        repository.linkFileNameToFileId("secondfile", "2");
        whenSerialize();
        verify(xmlWriter,times(1)).begin("coverage");
        verify(xmlWriter,times(2)).begin("file");
        verify(xmlWriter,times(1)).prop("path","myfile");
        verify(xmlWriter,times(1)).prop("path","secondfile");
        verify(xmlWriter,times(3)).end();
        
        // no others should be invoked
        verify(xmlWriter,times(3)).begin(anyString());
        verify(xmlWriter,times(3)).prop(anyString(),anyString());
    }
    
    @Test
    public void oneFileOneLineLinesWithBranchInfo_ShouldBeInRepor() {
       
        repository.linkFileNameToFileId("myfile", "1");
        repository.addLinePoint("1", 1, true);
        repository.addBranchPoint("1",1,1,true);
        repository.addBranchPoint("1",1,2,false);
        
        whenSerialize();
        
        InOrder order = Mockito.inOrder(xmlWriter);
        
        //this is the line
        verify(xmlWriter,times(1)).prop("lineNumber",1);
        verify(xmlWriter,times(1)).prop("covered","true");
        // check that there is  attempt to create branch coverage
        verify(xmlWriter,times(1)).prop("branchesToCover",2);
        verify(xmlWriter,times(1)).prop("coveredBranches",1);
        

        //this should be it
        order.verify(xmlWriter,times(1)).begin("coverage");
        order.verify(xmlWriter,times(1)).begin("file");
        order.verify(xmlWriter,times(1)).begin("lineToCover");
        order.verify(xmlWriter,times(3)).end();
       
        // no others should be invoked
        verify(xmlWriter,times(3)).begin(anyString());
    }
    
    @Test
    public void oneFileSeveralLinesWithSomeBranchPoints_ShouldBeInRepor() {
        File file = null;
        String fileId="1";
        repository.linkFileNameToFileId("myfile", "1");
        
        // 10 lines
        for(int i=1;i<10; i++) {
            repository.addLinePoint(fileId, i, true);
        }
        
        // a branchpoint on line 3
        repository.addBranchPoint(fileId,3,1,true);
        
        repository.addBranchPoint(fileId,5,1,true);
        repository.addBranchPoint(fileId,7,1,true);
        
        whenSerialize();
        InOrder order = Mockito.inOrder(xmlWriter);
        
        //this should be it
        order.verify(xmlWriter,times(1)).begin("coverage");
        order.verify(xmlWriter,times(1)).begin("file");
        order.verify(xmlWriter,times(1)).begin("lineToCover");
        //this is the line
        order.verify(xmlWriter,times(1)).prop("lineNumber",3);
        order.verify(xmlWriter,times(1)).prop("covered","true");
        // check that there is  attempt to create branch coverage
        order.verify(xmlWriter,times(1)).prop("branchesToCover",1);
        order.verify(xmlWriter,times(1)).prop("coveredBranches",1);
        
        // line 5
        order.verify(xmlWriter,times(1)).prop("lineNumber",5);
        order.verify(xmlWriter,times(1)).prop("covered","true");
        order.verify(xmlWriter,times(1)).prop("branchesToCover",1);
        order.verify(xmlWriter,times(1)).prop("coveredBranches",1);

        order.verify(xmlWriter,times(1)).prop("lineNumber",7);
        order.verify(xmlWriter,times(1)).prop("covered","true");
        order.verify(xmlWriter,times(1)).prop("branchesToCover",1);
        order.verify(xmlWriter,times(1)).prop("coveredBranches",1);
        
        order.verify(xmlWriter,times(5)).end();
        // no others should be invoked
        verify(xmlWriter,times(11)).begin(anyString());
    }
    
    @Test
    public void oneFileSeveralLinesWithInvalidBranchPoint_ShouldThrowException() {
        File file = null;
        String fileId="1";
        repository.linkFileNameToFileId("myfile", "1");
        
        // 10 lines
        for(int i=1;i<10; i++) {
            repository.addLinePoint(fileId, i, true);
        }
        
        // a branchpoint on line 3
        repository.addBranchPoint(fileId,10,1,true);
        try {
            whenSerialize();
        } catch (IllegalStateException e) {
            // not interested in the text
            return;
        }
        fail("expected IllegalStateException");

    }

    private void whenSerialize() {
        service.Serialize(xmlWriter, repository);
    }
}
