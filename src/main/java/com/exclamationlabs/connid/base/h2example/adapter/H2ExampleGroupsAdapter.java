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
import com.exclamationlabs.connid.base.connector.adapter.BaseGroupsAdapter;
import com.exclamationlabs.connid.base.h2example.model.H2ExampleGroup;
import com.exclamationlabs.connid.base.h2example.model.H2ExampleUser;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObject;

import java.util.Set;

import static com.exclamationlabs.connid.base.h2example.attribute.H2ExampleGroupAttribute.*;

public class H2ExampleGroupsAdapter extends BaseGroupsAdapter<H2ExampleUser, H2ExampleGroup> {


    @Override
    protected H2ExampleGroup constructGroup(Set<Attribute> attributes, boolean creation) {
        H2ExampleGroup group = new H2ExampleGroup();
        group.setId(AdapterValueTypeConverter.getIdentityIdAttributeValue(attributes));
        group.setName(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, GROUP_NAME));
        group.setDescription(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, GROUP_DESCRIPTION));
        return group;
    }

    @Override
    protected ConnectorObject constructConnectorObject(H2ExampleGroup group) {
        return getConnectorObjectBuilder(group)
                .addAttribute(AttributeBuilder.build(GROUP_ID.name(), group.getId()))
                .addAttribute(AttributeBuilder.build(GROUP_NAME.name(), group.getName()))
                .addAttribute(AttributeBuilder.build(GROUP_DESCRIPTION.name(), group.getDescription()))
                .build();
    }
}
