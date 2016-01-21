package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.InstantiationStrategy;

/**
 * Should be used by all unit test sensors
 * @author stevpet
 *
 */
@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
public class UnitTestCache extends TestCacheBase implements BatchExtension{
    
}
