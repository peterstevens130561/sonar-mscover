package com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks;

import static org.mockito.Mockito.when;

import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

public class SettingsMock extends GenericClassMock<Settings> {

    public SettingsMock() {
        super(Settings.class);
    }

    public void givenString(String key,
            String value) {
        when(instance.getString(key)).thenReturn(value);
    }


}
