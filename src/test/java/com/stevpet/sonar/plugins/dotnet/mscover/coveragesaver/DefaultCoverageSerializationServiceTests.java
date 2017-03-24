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
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.assertj.core.util.Files;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.sonar.test.TestUtils;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyInt;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.eq;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.DefaultProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;

public class DefaultCoverageSerializationServiceTests {

    @Mock XMLStreamWriter xmlStreamWriter ;
    CoverageSerializationService service ;
    private ProjectCoverageRepository repository; 
   
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        
  
        service = new DefaultCoverageSerializationService();
        repository=new DefaultProjectCoverageRepository();
    }
    
    @Test
    public void noCoverage_ShouldHaveBody() throws XMLStreamException {

        whenSerialize();
        
        InOrder order = Mockito.inOrder(xmlStreamWriter);
        order.verify(xmlStreamWriter,times(1)).writeStartElement("coverage");
        order.verify(xmlStreamWriter,times(1)).writeAttribute("version","1");
        order.verify(xmlStreamWriter,times(1)).writeEndElement();
        verify(xmlStreamWriter,times(1)).writeStartElement(anyString());
    }
    
    @Test
    public void oneFileNoResults_ShouldHaveOneFile() throws XMLStreamException {

        repository.linkFileNameToFileId("myfile", "1");
        whenSerialize();
        verify(xmlStreamWriter,times(1)).writeStartElement("coverage");
        verify(xmlStreamWriter,times(1)).writeStartElement("file");
        verify(xmlStreamWriter,times(1)).writeAttribute("path","myfile");
        verify(xmlStreamWriter,times(2)).writeEndElement();
        
        // no others should be invoked
        verify(xmlStreamWriter,times(2)).writeStartElement(anyString());
        verify(xmlStreamWriter,times(2)).writeAttribute(anyString(),anyString());
    }
    
    @Test
    public void oneFileOneLines_ShouldBeInRepor() throws XMLStreamException {

        repository.linkFileNameToFileId("myfile", "1");
        repository.addLinePoint("1", 1, true);
        
        whenSerialize();
        
        InOrder order = Mockito.inOrder(xmlStreamWriter);
        
        verify(xmlStreamWriter,times(1)).writeAttribute("lineNumber","1");
        verify(xmlStreamWriter,times(1)).writeAttribute("covered","true");
        
        // check that there is no attempt to create branch coverage
        verify(xmlStreamWriter,times(0)).writeAttribute(eq("branchesToCover"),anyString());
        verify(xmlStreamWriter,times(0)).writeAttribute(eq("coveredBranches"),anyString());
        order.verify(xmlStreamWriter,times(1)).writeStartElement("lineToCover");
        order.verify(xmlStreamWriter,times(3)).writeEndElement();
       
        // no others should be invoked
        verify(xmlStreamWriter,times(3)).writeStartElement(anyString());
    }
    
    @Test
    public void oneFileTwoLinesOneNotCovered_ShouldBeInRepor() throws XMLStreamException {

        repository.linkFileNameToFileId("myfile", "1");
        repository.addLinePoint("1", 1, true);
        repository.addLinePoint("1", 2, false);
        
        whenSerialize();
        InOrder order = Mockito.inOrder(xmlStreamWriter);
        
        // check that there is no attempt to create branch coverage
        verify(xmlStreamWriter,times(0)).writeAttribute(eq("branchesToCover"),anyString());
        verify(xmlStreamWriter,times(0)).writeAttribute(eq("coveredBranches"),anyString());
        
        // check two lines are thee
        verify(xmlStreamWriter,times(1)).writeAttribute("lineNumber","1");
        verify(xmlStreamWriter,times(1)).writeAttribute("covered","true");
        verify(xmlStreamWriter,times(1)).writeAttribute("lineNumber","2");
        verify(xmlStreamWriter,times(1)).writeAttribute("covered","false");
        
        order.verify(xmlStreamWriter,times(1)).writeStartElement("lineToCover");
        order.verify(xmlStreamWriter,times(4)).writeEndElement();
       
        // no others should be invoked
        verify(xmlStreamWriter,times(4)).writeStartElement(anyString());
    }
    @Test
    public void twoFileNoResults_ShouldHaveTwoFile() throws XMLStreamException {

        repository.linkFileNameToFileId("myfile", "1");
        repository.linkFileNameToFileId("secondfile", "2");
        whenSerialize();
        verify(xmlStreamWriter,times(1)).writeStartElement("coverage");
        verify(xmlStreamWriter,times(2)).writeStartElement("file");
        verify(xmlStreamWriter,times(1)).writeAttribute("path","myfile");
        verify(xmlStreamWriter,times(1)).writeAttribute("path","secondfile");
        verify(xmlStreamWriter,times(3)).writeEndElement();
        
        // no others should be invoked
        verify(xmlStreamWriter,times(3)).writeStartElement(anyString());
        verify(xmlStreamWriter,times(3)).writeAttribute(anyString(),anyString());
    }
    
    @Test
    public void oneFileOneLineLinesWithBranchInfo_ShouldBeInRepor() throws XMLStreamException {
       
        repository.linkFileNameToFileId("myfile", "1");
        repository.addLinePoint("1", 1, true);
        repository.addBranchPoint("1",1,1,true);
        repository.addBranchPoint("1",1,2,false);
        
        whenSerialize();
        
        InOrder order = Mockito.inOrder(xmlStreamWriter);
        
        //this is the line
        verify(xmlStreamWriter,times(1)).writeAttribute("lineNumber","1");
        verify(xmlStreamWriter,times(1)).writeAttribute("covered","true");
        // check that there is  attempt to create branch coverage
        verify(xmlStreamWriter,times(1)).writeAttribute("branchesToCover","2");
        verify(xmlStreamWriter,times(1)).writeAttribute("coveredBranches","1");
        

        //this should be it
        order.verify(xmlStreamWriter,times(1)).writeStartElement("coverage");
        order.verify(xmlStreamWriter,times(1)).writeStartElement("file");
        order.verify(xmlStreamWriter,times(1)).writeStartElement("lineToCover");
        order.verify(xmlStreamWriter,times(3)).writeEndElement();
       
        // no others should be invoked
        verify(xmlStreamWriter,times(3)).writeStartElement(anyString());
    }
    
    @Test
    public void oneFileSeveralLinesWithSomeBranchPoints_ShouldBeInRepor() throws XMLStreamException {
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
        InOrder order = Mockito.inOrder(xmlStreamWriter);
        
        //this should be it
        order.verify(xmlStreamWriter,times(1)).writeStartElement("coverage");
        order.verify(xmlStreamWriter,times(1)).writeStartElement("file");
        order.verify(xmlStreamWriter,times(1)).writeStartElement("lineToCover");
        //this is the line
        order.verify(xmlStreamWriter,times(1)).writeAttribute("lineNumber","3");
        order.verify(xmlStreamWriter,times(1)).writeAttribute("covered","true");
        // check that there is  attempt to create branch coverage
        order.verify(xmlStreamWriter,times(1)).writeAttribute("branchesToCover","1");
        order.verify(xmlStreamWriter,times(1)).writeAttribute("coveredBranches","1");
        
        // line 5
        order.verify(xmlStreamWriter,times(1)).writeAttribute("lineNumber","5");
        order.verify(xmlStreamWriter,times(1)).writeAttribute("covered","true");
        order.verify(xmlStreamWriter,times(1)).writeAttribute("branchesToCover","1");
        order.verify(xmlStreamWriter,times(1)).writeAttribute("coveredBranches","1");

        order.verify(xmlStreamWriter,times(1)).writeAttribute("lineNumber","7");
        order.verify(xmlStreamWriter,times(1)).writeAttribute("covered","true");
        order.verify(xmlStreamWriter,times(1)).writeAttribute("branchesToCover","1");
        order.verify(xmlStreamWriter,times(1)).writeAttribute("coveredBranches","1");
        
        order.verify(xmlStreamWriter,times(5)).writeEndElement();
        // no others should be invoked
        verify(xmlStreamWriter,times(11)).writeStartElement(anyString());
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

    @Test
    public void usageExampleTest() throws IOException, XMLStreamException {
        File file=TestUtils.getResource("DefaultCoverageSerializationServiceTests/dummy.txt");
        File tmpFile=Files.newTemporaryFile();
        assertNotNull("should be there",file);
        
        //setup of the writer
        Writer writer = new FileWriter(tmpFile.getAbsolutePath());
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlStreamWriter = outputFactory.createXMLStreamWriter(writer);
        
        // just some simple data
        repository.linkFileNameToFileId("myfile", "1");
        repository.addLinePoint("1", 1, true);
        repository.addBranchPoint("1",1,1,true);
        repository.addBranchPoint("1",1,2,false);
        
        service.Serialize(xmlStreamWriter,repository);
        //now the file should be there
        
        
    }
    private void whenSerialize() {
        service.Serialize(xmlStreamWriter, repository);
    }
}
