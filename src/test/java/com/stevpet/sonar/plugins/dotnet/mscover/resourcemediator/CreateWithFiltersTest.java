package com.stevpet.sonar.plugins.dotnet.mscover.resourcemediator;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.InjectedResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;

public class CreateWithFiltersTest {


    @Test
    public void createShouldWork() {
        FileSystemMock fileSystemMock=new FileSystemMock();
        TimeMachine timeMachine = mock(TimeMachine.class);
        Settings settings = mock(Settings.class);
        MsCoverProperties propertiesHelper = PropertiesHelper.create(settings);
        
        fileSystemMock.givenDefaultEncoding();
        ResourceMediator resourceMediator = new InjectedResourceMediator(timeMachine, propertiesHelper);
        assertNotNull(resourceMediator);
    }

}
