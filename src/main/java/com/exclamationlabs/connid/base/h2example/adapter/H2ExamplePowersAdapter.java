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
import com.exclamationlabs.connid.base.connector.adapter.BaseAdapter;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttribute;
import com.exclamationlabs.connid.base.h2example.model.H2ExamplePower;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ObjectClass;

import java.util.HashSet;
import java.util.Set;

import static com.exclamationlabs.connid.base.connector.attribute.ConnectorAttributeDataType.STRING;
import static com.exclamationlabs.connid.base.h2example.attribute.H2ExamplePowerAttribute.*;
import static org.identityconnectors.framework.common.objects.AttributeInfo.Flags.NOT_UPDATEABLE;

public class H2ExamplePowersAdapter extends BaseAdapter<H2ExamplePower> {

    @Override
    public ObjectClass getType() {
        return new ObjectClass("Power");
    }

    @Override
    public Class<H2ExamplePower> getIdentityModelClass() {
        return H2ExamplePower.class;
    }

    @Override
    public Set<ConnectorAttribute> getConnectorAttributes() {
        Set<ConnectorAttribute> result = new HashSet<>();
        result.add(new ConnectorAttribute(POWER_ID.name(), STRING, NOT_UPDATEABLE));
        result.add(new ConnectorAttribute(POWER_NAME.name(), STRING, NOT_UPDATEABLE));
        result.add(new ConnectorAttribute(POWER_DESCRIPTION.name(), STRING));
        return result;
    }

    @Override
    protected H2ExamplePower constructModel(Set<Attribute> attributes, boolean creation) {
        H2ExamplePower power = new H2ExamplePower();
        power.setId(AdapterValueTypeConverter.getIdentityIdAttributeValue(attributes));
        power.setName(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, POWER_NAME));
        power.setDescription(AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, POWER_DESCRIPTION));
        return power;
    }

    @Override
    protected Set<Attribute> constructAttributes(H2ExamplePower power) {
        Set<Attribute> attributes = new HashSet<>();

        attributes.add(AttributeBuilder.build(POWER_ID.name(), power.getId()));
        attributes.add(AttributeBuilder.build(POWER_NAME.name(), power.getName()));
        attributes.add(AttributeBuilder.build(POWER_DESCRIPTION.name(), power.getDescription()));

        return attributes;
    }
}
