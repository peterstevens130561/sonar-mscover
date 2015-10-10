package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;

public class SpecFlowCSharpFileParser extends StringSpecFlowParser {

    final Logger LOG = LoggerFactory.getLogger(SpecFlowCSharpFileParser.class);
    
    SpecFlowScenarioMap loadFile(File file) {


        try {
            InputStream inputStream = new FileInputStream(file.getAbsolutePath());
            return parse(file, inputStream);
        } catch (IOException e) {
            String msg = "IOException during accessing file " + file.getAbsolutePath();
            LOG.error(msg);
            throw new MsCoverException(msg,e);
        }
    }

}
