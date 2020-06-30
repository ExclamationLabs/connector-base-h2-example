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

package com.exclamationlabs.connid.base.h2example.adapter;

import com.exclamationlabs.connid.base.connector.adapter.AdapterValueTypeConverter;
import com.exclamationlabs.connid.base.connector.adapter.BaseUsersAdapter;
import com.exclamationlabs.connid.base.h2example.model.H2ExampleGroup;
import com.exclamationlabs.connid.base.h2example.model.H2ExampleUser;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObject;

import java.util.Set;

import static com.exclamationlabs.connid.base.h2example.attribute.H2ExampleUserAttribute.*;

public class H2ExampleUsersAdapter extends BaseUsersAdapter<H2ExampleUser, H2ExampleGroup> {
    @Override
    protected H2ExampleUser constructUser(Set<Attribute> attributes, boolean creation) {
        H2ExampleUser user = new H2ExampleUser();
        user.setId(AdapterValueTypeConverter.getIdentityIdAttributeValue(attributes));

        user.setFirstName(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, FIRST_NAME));
        user.setLastName(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, LAST_NAME));
        user.setEmail(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, EMAIL));
        user.setTimezone(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, TIME_ZONE));
        user.setDescription(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, DESCRIPTION));
        return user;
    }

    @Override
    protected ConnectorObject constructConnectorObject(H2ExampleUser user) {
        return getConnectorObjectBuilder(user)
                .addAttribute(AttributeBuilder.build(USER_ID.name(), user.getId()))
                .addAttribute(AttributeBuilder.build(EMAIL.name(), user.getEmail()))
                .addAttribute(AttributeBuilder.build(FIRST_NAME.name(), user.getFirstName()))
                .addAttribute(AttributeBuilder.build(LAST_NAME.name(), user.getLastName()))
                .addAttribute(AttributeBuilder.build(DESCRIPTION.name(), user.getDescription()))
                .addAttribute(AttributeBuilder.build(TIME_ZONE.name(), user.getTimezone()))
                .addAttribute(AttributeBuilder.build(GROUP_IDS.name(), user.getGroupIds()))
                .build();
    }
}
