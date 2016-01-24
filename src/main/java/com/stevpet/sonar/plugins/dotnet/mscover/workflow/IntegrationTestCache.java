package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import javax.annotation.Nonnull;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.InstantiationStrategy;

import com.google.common.base.Preconditions;

/**
 * Should be used by all integration test sensors
 * @author stevpet
 *
 */
@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
public class IntegrationTestCache extends TestCacheBase implements BatchExtension{

	private String firstModule;

	public void setFirstModule(@Nonnull String firstModule) {
		this.firstModule=firstModule;
	}
	
	public String getFirstModule() {
		Preconditions.checkNotNull(firstModule);
		return firstModule;
	}
    
}