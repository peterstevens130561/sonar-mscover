package com.stevpet.sonar.plugins.dotnet.mscover;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration.Mode;
import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration.Tool;

import static org.mockito.Mockito.when;
public class DefaultIntegrationTestsConfigurationTests {
	private static final String SONAR_MSCOVER_INTEGRATIONTESTS_MODE = "sonar.mscover.integrationtests.mode";
	private static final String SONAR_MSCOVER_INTEGRATIONTESTS_TOOL = "sonar.mscover.integrationtests.tool";

	@Mock private Settings settings;


	private IntegrationTestsConfiguration configuration;
	@Before
	public void before()
	{
		org.mockito.MockitoAnnotations.initMocks(this);
		configuration=new DefaultIntegrationTestsConfiguration(settings);
	}
	
	@Test
	public void modeDisabled() {
		when(settings.getString(SONAR_MSCOVER_INTEGRATIONTESTS_MODE)).thenReturn("Disabled");
		assertTrue(configuration.getMode()==Mode.DISABLED);
	}
	
	@Test
	public void modeEmptyDefaultsToDisabled() {
		when(settings.getString(SONAR_MSCOVER_INTEGRATIONTESTS_MODE)).thenReturn(null);
		assertEquals(configuration.getMode(),Mode.DISABLED);
	}
	
	@Test
	public void modeCrap() {
		when(settings.getString(SONAR_MSCOVER_INTEGRATIONTESTS_MODE)).thenReturn("crap");
		try { 
			configuration.getMode();
		} catch ( IllegalArgumentException e ) {
			return;
		}
		fail("exepcted IllegalArgumentException");
	}
	
	@Test
	public void modeRun() {
		when(settings.getString(SONAR_MSCOVER_INTEGRATIONTESTS_MODE)).thenReturn("rUn");
		assertEquals(configuration.getMode(),Mode.RUN);
	}
	
	@Test
	public void modeRead() {
		when(settings.getString(SONAR_MSCOVER_INTEGRATIONTESTS_MODE)).thenReturn("read");
		assertEquals(configuration.getMode(),Mode.READ);
	}
	
	
	@Test
	public void toolIsOpenCover() {
		when(settings.getString(SONAR_MSCOVER_INTEGRATIONTESTS_TOOL)).thenReturn("oPenCover");
		assertEquals(configuration.getTool(),IntegrationTestsConfiguration.Tool.OPENCOVER);
	}
	
	@Test
	public void toolCrap() {
		when(settings.getString(SONAR_MSCOVER_INTEGRATIONTESTS_TOOL)).thenReturn("crap");
		try { 
			configuration.getTool();
		} catch ( IllegalArgumentException e ) {
			return;
		}
		fail("exepcted IllegalArgumentException");
	}
	
	@Test
	public void toolDefaultIsOpenCover() {
		when(settings.getString(SONAR_MSCOVER_INTEGRATIONTESTS_TOOL)).thenReturn("");
		assertEquals(configuration.getTool(),Tool.OPENCOVER);
	}
	
	@Test
	public void toolVsTest() {
		when(settings.getString(SONAR_MSCOVER_INTEGRATIONTESTS_TOOL)).thenReturn("vstest");
		assertEquals(Tool.VSTEST,configuration.getTool());
	}
	
	@Test
	public void isDisabled() {
		when(settings.getString(SONAR_MSCOVER_INTEGRATIONTESTS_MODE)).thenReturn("Disabled");
		when(settings.getString(SONAR_MSCOVER_INTEGRATIONTESTS_TOOL)).thenReturn("vstest");
		assertFalse("even when checking on mode disabled we should not match",configuration.matches(Tool.OPENCOVER,Mode.DISABLED));
	}
	
	@Test
	public void isOpenCoverRun() {
		when(settings.getString(SONAR_MSCOVER_INTEGRATIONTESTS_MODE)).thenReturn("run");
		when(settings.getString(SONAR_MSCOVER_INTEGRATIONTESTS_TOOL)).thenReturn("opencover");
		assertTrue(configuration.matches(Tool.OPENCOVER,Mode.RUN));
	}
	
	@Test
	public void isDisabledRequestsOpenCover() {
		when(settings.getString(SONAR_MSCOVER_INTEGRATIONTESTS_MODE)).thenReturn("disabled");
		when(settings.getString(SONAR_MSCOVER_INTEGRATIONTESTS_TOOL)).thenReturn("opencover");
		assertFalse(configuration.matches(Tool.OPENCOVER,Mode.RUN));
	}
}
