/*
    Copyright 2020 Exclamation Labs
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
        http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.exclamationlabs.connid.base.h2example.configuration;

import com.exclamationlabs.connid.base.connector.configuration.BaseConnectorConfiguration;
import com.exclamationlabs.connid.base.connector.configuration.ConnectorProperty;
import org.identityconnectors.framework.spi.ConfigurationClass;
import org.identityconnectors.framework.spi.ConfigurationProperty;

import java.util.Properties;

@ConfigurationClass(skipUnsupported = true)
public class H2ExampleConfiguration extends BaseConnectorConfiguration {

    public H2ExampleConfiguration() {
        super();
    }

    public H2ExampleConfiguration(String configurationName) {
        super(configurationName);
    }

    @Override
    @ConfigurationProperty(
            displayMessageKey = "H2Example Configuration File Path",
            helpMessageKey = "File path for the H2Example Configuration File",
            required = true)
    public String getConfigurationFilePath() {
        return getMidPointConfigurationFilePath();
    }

    /**
     * Don't need to load configuration file for H2Example.  Normal usages should not override
     * setup and should utilize a configuration file path.
     */
    @Override
    public void setup() {
        Properties properties = new Properties();
        properties.setProperty(ConnectorProperty.CONNECTOR_BASE_CONFIGURATION_ACTIVE.name(), "Y");
        setConnectorProperties(properties);
    }
}
