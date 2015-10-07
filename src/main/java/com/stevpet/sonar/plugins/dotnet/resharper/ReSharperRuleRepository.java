/*
 * Sonar .NET Plugin :: ReSharper
 * Copyright (C) 2013 John M. Wright
 * dev@sonar.codehaus.org
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
 */
package com.stevpet.sonar.plugins.dotnet.resharper;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.config.Settings;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleRepository;

import com.stevpet.sonar.plugins.dotnet.resharper.profiles.ReSharperFileParser;
import com.stevpet.sonar.plugins.dotnet.resharper.profiles.ReSharperRule;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads the ReSharper rules configuration file.
 * To update the configuration file use InspectCode /dumpIssuesTypes /output=results.xml, 
 * then copy/paste the contents into DefaultRules.ReSharper
 * 
 */
public class ReSharperRuleRepository extends RuleRepository {


    private static final Logger LOG = LoggerFactory.getLogger(ReSharperRuleRepository.class);

    private Settings settings;

    public ReSharperRuleRepository(String repoKey, String languageKey, Settings settings) {
        super(repoKey, languageKey);
        setName(ReSharperConfiguration.REPOSITORY_NAME);
        this.settings = settings;
    }

    @Override
    public List<Rule> createRules() {
        LOG.debug("--- Creating rules");
        List<Rule> rules = new ArrayList<Rule>();

        // ReSharper rules
        InputStream rulesFileStream = ReSharperRuleRepository.class.getResourceAsStream(ReSharperConfiguration.DEFAULT_RULES);
        Reader reader = new InputStreamReader(rulesFileStream);
        ReSharperFileParser parser = new ReSharperFileParser();
        List<ReSharperRule> reSharperRules = parser.parseRules(reader);
        for(ReSharperRule rRule: reSharperRules) {
            LOG.debug("--- adding rule");
            rules.add(rRule.toSonarRule());
        }

        // Custom rules through the Web interface
        String customRules = settings.getString(ReSharperConfiguration.CUSTOM_RULES_PROP_KEY);
        if (StringUtils.isNotBlank(customRules)) {
            try {
                String customRulesXml = "<Report><IssueTypes>" + customRules + "</IssueTypes></Report>";

                Reader customRulesReader = new StringReader(customRulesXml);
                List<ReSharperRule> customReSharperRules = parser.parseRules(customRulesReader);
                for(ReSharperRule rRule: customReSharperRules) {
                    if(!rules.add(rRule.toSonarRule())) {
                        LOG.warn("--- could not add " + rRule.toSonarRule().toString());
                    }
                }
            } catch (Exception ex)
            {
                LOG.warn("Error parsing ReSharper Custom Rules: " + ex.getMessage());
            }
        }

        return rules;
    }

}
