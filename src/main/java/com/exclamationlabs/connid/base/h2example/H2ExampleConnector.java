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

import com.exclamationlabs.connid.base.connector.BaseConnector;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttributeMapBuilder;
import com.exclamationlabs.connid.base.connector.authenticator.Authenticator;
import com.exclamationlabs.connid.base.connector.configuration.BaseConnectorConfiguration;
import com.exclamationlabs.connid.base.connector.configuration.ConnectorProperty;
import com.exclamationlabs.connid.base.h2example.adapter.H2ExampleGroupsAdapter;
import com.exclamationlabs.connid.base.h2example.adapter.H2ExampleUsersAdapter;
import com.exclamationlabs.connid.base.h2example.attribute.H2ExampleGroupAttribute;
import com.exclamationlabs.connid.base.h2example.attribute.H2ExampleUserAttribute;
import com.exclamationlabs.connid.base.h2example.configuration.H2ExampleConfiguration;
import com.exclamationlabs.connid.base.h2example.driver.H2ExampleDriver;
import com.exclamationlabs.connid.base.h2example.model.H2ExampleGroup;
import com.exclamationlabs.connid.base.h2example.model.H2ExampleUser;
import org.identityconnectors.framework.common.exceptions.ConnectorSecurityException;
import org.identityconnectors.framework.spi.ConnectorClass;

import java.util.Set;

import static com.exclamationlabs.connid.base.connector.attribute.ConnectorAttributeDataType.STRING;
import static com.exclamationlabs.connid.base.h2example.attribute.H2ExampleUserAttribute.*;
import static com.exclamationlabs.connid.base.h2example.attribute.H2ExampleGroupAttribute.*;
import static org.identityconnectors.framework.common.objects.AttributeInfo.Flags.*;

@ConnectorClass(displayNameKey = "h2example.connector.display", configurationClass = H2ExampleConfiguration.class)
public class H2ExampleConnector extends BaseConnector<H2ExampleUser, H2ExampleGroup> {

    public H2ExampleConnector() {

        setAuthenticator(new Authenticator() {
            @Override
            public Set<ConnectorProperty> getRequiredPropertyNames() {
                return null;
            }

            @Override
            public String authenticate(BaseConnectorConfiguration baseConnectorConfiguration) throws ConnectorSecurityException {
                return "good";
            }
        });
        setDriver(new H2ExampleDriver());
        setUsersAdapter(new H2ExampleUsersAdapter());
        setGroupsAdapter(new H2ExampleGroupsAdapter());
        setUserAttributes( new ConnectorAttributeMapBuilder<>(H2ExampleUserAttribute.class)
                .add(USER_ID, STRING, NOT_UPDATEABLE)
                .add(FIRST_NAME, STRING, NOT_UPDATEABLE)
                .add(LAST_NAME, STRING, NOT_UPDATEABLE)
                .add(EMAIL, STRING, NOT_UPDATEABLE)
                .add(TIME_ZONE, STRING, NOT_UPDATEABLE)
                .add(DESCRIPTION, STRING)
                .add(GROUP_IDS, STRING, MULTIVALUED)
                .build());
        setGroupAttributes(new ConnectorAttributeMapBuilder<>(H2ExampleGroupAttribute.class)
                .add(GROUP_ID, STRING, NOT_UPDATEABLE)
                .add(GROUP_NAME, STRING, REQUIRED, NOT_UPDATEABLE)
                .add(GROUP_DESCRIPTION, STRING)
                .build());
    }

}
