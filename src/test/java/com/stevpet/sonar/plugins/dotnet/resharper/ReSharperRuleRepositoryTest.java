package com.stevpet.sonar.plugins.dotnet.resharper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.api.config.Settings;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleRepository;

import com.stevpet.sonar.plugins.dotnet.resharper.ReSharperConfiguration;
import com.stevpet.sonar.plugins.dotnet.resharper.ReSharperRuleRepository;

public class ReSharperRuleRepositoryTest {

    @Mock private Settings settings;
    @Before
    public void before() {
        initMocks(this);
    }
    
    @Test
    public void shouldLoadTheDefaultRules() {
        RuleRepository ruleRepository = new ReSharperRuleRepository("resharper","cs",settings);
        
        List<Rule> rules = ruleRepository.createRules();
        
        assertNotNull("expect list of rules",rules);
        assertEquals("rules to be found in defaultrules",809,rules.size());
        
    }
    
    @Test
    public void shouldLoadTheCustomRules() {
        RuleRepository ruleRepository = new ReSharperRuleRepository("resharper","cs",settings);
        when(settings.getString(ReSharperConfiguration.CUSTOM_RULES_PROP_KEY)).thenReturn("bogus");
        List<Rule> rules = ruleRepository.createRules();
        
        assertNotNull("expect list of rules",rules);
        assertEquals("rules to be found in defaultrules",809,rules.size());
        
    }
    
    @Test
    public void rulesShouldBeUnique() {
        RuleRepository ruleRepository = new ReSharperRuleRepository("resharper","cs",settings);
        List<Rule> rules = ruleRepository.createRules();
        Collection<String> ruleKeys = new ArrayList<>();
        for(Rule rule : rules) {
            assertTrue("key should not be in list" + rule.toString(),!ruleKeys.contains(rule.getKey()));
            ruleKeys.add(rule.getKey());
        }
        assertNotNull("expect list of rules",rules);
        assertEquals("rules to be found in defaultrules",809,rules.size());
        
    }
}
