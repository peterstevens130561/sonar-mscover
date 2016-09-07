/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
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
 *
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.PropertyDefinitions;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.property.ConfigurationProperty;
import com.stevpet.sonar.plugins.dotnet.mscover.property.InvalidPropertyValueException;

public class ConfigurationPropertyTestsBase<T> {

    protected Settings settings;
    private PropertyDefinitions propDefinitions;
    protected ConfigurationProperty<T> property;

    /**
     * @usage setup(new SomeProperty) ; setProperty(new SomeProperty(settings));
     * @param configurationProperty
     */
    public void setup(ConfigurationProperty configurationProperty) {

        PropertyDefinition definition = configurationProperty.getPropertyBuilder().build();
        propDefinitions = new PropertyDefinitions();
        propDefinitions.addComponent(definition);
        settings = new Settings(propDefinitions);
    }

    /**
     * Add the property definitions to the settings.
     * @usage setup(SomeProperty.class)
     * @param clazz
     */
    public void setup(Class clazz) {
        try {
            T instance = (T) clazz.newInstance();

            setup((ConfigurationProperty) instance);
            setProperty((ConfigurationProperty<T>) clazz.getConstructor(Settings.class).newInstance(settings));
        } catch (Exception e) {
            throw new RuntimeException("could not instantiate", e);
        }
    }

    public void setProperty(ConfigurationProperty<T> property) {
        this.property = property;
    }

    protected void checkInRangeInt(T value) {
        setPropertyValue(value.toString());
        T actual = property.getValue();
        assertEquals(value, actual);
    }

    protected void checkOutsideRangeInt(T value) {
        setPropertyValue(value.toString());
        try {
            property.validate();
        } catch (InvalidPropertyValueException e) {
            return; // this is expected
        } catch (Exception e) {
            fail("just too much expected InvalidPropertyException");
        }
    }

    protected void checkExceptionOnNotInt() {
        setPropertyValue("groble");
        try {
            property.validate();
        } catch (InvalidPropertyValueException e) {
            return; // this is expected
        } catch (Exception e) {
            fail("expected InvalidPropertyException");
        }
    }

    protected void checkDefaultOnNotSet(T expected) {
        T value = property.getValue();
        assertEquals("expect default", expected, value);
    }

    public void setPropertyValue(String value) {
        settings.setProperty(property.getKey(), value);
    }

    public void expectInvalidPropetyValueExceptionOnGetValue() {
        try {
            property.validate();
        } catch (InvalidPropertyValueException e) {
            return; // this is expected
        } catch (Exception e) {
            fail("expected InvalidPropertyException");
        }  
    }

}