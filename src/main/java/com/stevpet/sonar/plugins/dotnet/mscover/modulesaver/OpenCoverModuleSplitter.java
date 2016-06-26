package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;


/**
 * Parse the OpenCover file, and give each module to the moduleLambda
 * 
 */
public class OpenCoverModuleSplitter implements ModuleSplitter {

    private CoverageModuleSaver coverageModuleSaver;
    private CoverageHashes coverageHashes;
    public OpenCoverModuleSplitter(CoverageHashes coverageHashes) {
        this(new OpenCoverCoverageModuleSaver(new OpenCoverModuleParser()), coverageHashes);

    }

    public OpenCoverModuleSplitter(CoverageModuleSaver moduleHelper,CoverageHashes coverageHashes) {
        this.coverageModuleSaver = moduleHelper;
        this.coverageHashes = coverageHashes;
    }

    @Override
    public int splitCoverageFileInFilePerModule(File coverageRootDir, String testProjectName, File testCoverageFile) {
        setRoot(coverageRootDir);
        setProject(testProjectName);
        return splitFile(testCoverageFile);
    }
    public ModuleSplitter setRoot(File root) {
        coverageModuleSaver.setDirectory(root);
        return this;
    }

        InputStream inputStream = getInputStream(testCoverageFile);
        try {
            return split(coverageRootDir, testProjectName, inputStream);
        } catch (XMLStreamException | TransformerException e) {
            throw new IllegalStateException("XML exception", e);
    }
    }

    private InputStream getInputStream(File testCoverageFile) {
        try {
            return new FileInputStream(testCoverageFile);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Could not find" + testCoverageFile.getAbsolutePath());
        }

        }

    private int split(File coverageRootDir,String testProjectName,InputStream inputStream) throws XMLStreamException,
            TransformerException {

        BufferedReader in = new BufferedReader(new InputStreamReader(
                inputStream));

        XMLInputFactory factory = XMLInputFactory.newInstance();
        TransformerFactory tf = TransformerFactory.newInstance();

        Transformer t = tf.newTransformer();
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        XMLStreamReader streamReader = factory.createXMLStreamReader(in);
        int modules = 0;
        while (streamReader.hasNext()) {
            streamReader.next();

            if (streamReader.getEventType() == XMLStreamReader.START_ELEMENT
                    && "Module".equals(streamReader.getLocalName())) {
                String xml = getModuleIntoNewXmlDoc(t, streamReader);
                if(!coverageHashes.add(xml)) {
                    coverageModuleSaver.setProject(testProjectName).setDirectory(coverageRootDir).save(xml);
                }

            }

        }
        return modules;

    }

    private String getModuleIntoNewXmlDoc(Transformer t, XMLStreamReader streamReader) throws TransformerException {
        StringWriter writer = new StringWriter();
        t.transform(new StAXSource(streamReader), new StreamResult(
                writer));
        StringBuilder sb = writeXml(writer);
        String xml = sb.toString();
        return xml;
    }

    private StringBuilder writeXml(StringWriter writer) {
        StringBuilder sb = new StringBuilder(10240);
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        sb.append("<CoverageSession xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
        sb.append("<Modules>");
        sb.append(writer.toString());
        sb.append("</Modules>");
        sb.append("</CoverageSession>");
        return sb;
    }



}
