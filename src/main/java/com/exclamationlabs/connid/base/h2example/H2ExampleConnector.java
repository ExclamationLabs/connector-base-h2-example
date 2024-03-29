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

package com.exclamationlabs.connid.base.h2example;

import com.exclamationlabs.connid.base.connector.BaseFullAccessConnector;
import com.exclamationlabs.connid.base.h2example.adapter.H2ExampleGroupsAdapter;
import com.exclamationlabs.connid.base.h2example.adapter.H2ExamplePowersAdapter;
import com.exclamationlabs.connid.base.h2example.adapter.H2ExampleUsersAdapter;
import com.exclamationlabs.connid.base.h2example.attribute.H2ExampleUserAttribute;
import com.exclamationlabs.connid.base.h2example.configuration.H2ExampleConfiguration;
import com.exclamationlabs.connid.base.h2example.driver.H2ExampleDriver;
import java.util.Collections;
import java.util.HashSet;
import org.identityconnectors.framework.spi.ConnectorClass;

@ConnectorClass(
    displayNameKey = "h2example.connector.display",
    configurationClass = H2ExampleConfiguration.class)
public class H2ExampleConnector extends BaseFullAccessConnector<H2ExampleConfiguration> {

  public H2ExampleConnector() {
    super(H2ExampleConfiguration.class);
    setAuthenticator(connectorConfiguration -> "good");
    setDriver(new H2ExampleDriver());
    setAdapters(
        new H2ExampleUsersAdapter(), new H2ExampleGroupsAdapter(), new H2ExamplePowersAdapter());
    setEnhancedFiltering(true);
    setFilterAttributes(
        new HashSet<>(Collections.singleton(H2ExampleUserAttribute.DESCRIPTION.name())));
  }
}
