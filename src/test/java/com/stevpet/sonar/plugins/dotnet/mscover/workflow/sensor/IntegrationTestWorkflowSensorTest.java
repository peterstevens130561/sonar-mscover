package com.stevpet.sonar.plugins.dotnet.mscover.workflow.sensor;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.picocontainer.DefaultPicoContainer;
import org.sonar.api.BatchExtension;
import org.sonar.api.Extension;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.common.commandexecutor.DefaultProcessLock;
import com.stevpet.sonar.plugins.common.commandexecutor.ProcessLock;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.plugin.MsCoverPlugin;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.IntegrationTestResourceResolver;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class IntegrationTestWorkflowSensorTest {
    private Sensor sensor ;
    @Mock private MsCoverConfiguration msCoverProperties;
    @Mock private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    @Mock private FileSystem fileSystem;
    @Mock private IntegrationTestResourceResolver resourceResolver;
    @Mock private PathResolver pathResolver;
    @Mock private IntegrationTestCache integrationTestCache;
    @Mock private ProcessLock processLock;
    @Mock private List extensions;
    
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
    }

    public void injectionCreate() {
        DefaultPicoContainer container = new DefaultPicoContainer();
        extensions= new MsCoverPlugin().getExtensions();
        extensions.add(DefaultProcessLock.class);
        

        for(Object extension:extensions) {
            if(extension instanceof PropertyDefinition) {
                continue;
            }
            if(superImplementExtension((Class)extension)){              
                container.addComponent(extension);
            }
        }
        // the sonar provided stuff
        container.addComponent(fileSystem).addComponent(pathResolver);
        
        sensor=container.getComponent(IntegrationTestWorkflowSensor.class);
    }
    
    @Test
    public void directCreate() {
        sensor = new IntegrationTestWorkflowSensor(msCoverProperties, 
                microsoftWindowsEnvironment, 
                fileSystem, 
                resourceResolver, 
                pathResolver, 
                integrationTestCache);
    }
    
    private boolean superImplementExtension(Class extensionClass) {
        boolean interfaceImplements=false;
        if(implementsExtension(extensionClass)) {
            return true;
        }
        Class superClass = extensionClass.getSuperclass() ;
        if(superClass==null) {
            return false;
        }
        return superImplementExtension(superClass);
    }
    private boolean implementsExtension(Class extensionClass) {
        String extensionClassName = Extension.class.getCanonicalName();
        boolean doesImplement=false;
        for(Class interfaze : extensionClass.getInterfaces()) {
            
            String interfazeName=interfaze.getCanonicalName();
            if (interfazeName.equals(extensionClassName)){
                return true;
            }
            Class[] extendedInterfaces = interfaze.getInterfaces();
            for(Class extendedInterface : extendedInterfaces) {
                if(superImplementExtension(extendedInterface))
                    return true;
            }
        }

        return false;
    }
    
}
