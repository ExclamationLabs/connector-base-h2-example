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

import static com.exclamationlabs.connid.base.connector.attribute.ConnectorAttributeDataType.STRING;
import static com.exclamationlabs.connid.base.h2example.attribute.H2ExampleGroupAttribute.*;
import static org.identityconnectors.framework.common.objects.AttributeInfo.Flags.NOT_UPDATEABLE;

import com.exclamationlabs.connid.base.connector.adapter.AdapterValueTypeConverter;
import com.exclamationlabs.connid.base.connector.adapter.BaseAdapter;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttribute;
import com.exclamationlabs.connid.base.h2example.configuration.H2ExampleConfiguration;
import com.exclamationlabs.connid.base.h2example.model.H2ExampleGroup;
import java.util.HashSet;
import java.util.Set;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ObjectClass;

public class H2ExampleGroupsAdapter extends BaseAdapter<H2ExampleGroup, H2ExampleConfiguration> {

  @Override
  public ObjectClass getType() {
    return ObjectClass.GROUP;
  }

  @Override
  public Class<H2ExampleGroup> getIdentityModelClass() {
    return H2ExampleGroup.class;
  }

  @Override
  public Set<ConnectorAttribute> getConnectorAttributes() {
    Set<ConnectorAttribute> result = new HashSet<>();
    result.add(new ConnectorAttribute(GROUP_ID.name(), STRING, NOT_UPDATEABLE));
    result.add(new ConnectorAttribute(GROUP_NAME.name(), STRING, NOT_UPDATEABLE));
    result.add(new ConnectorAttribute(GROUP_DESCRIPTION.name(), STRING));
    return result;
  }

  @Override
  protected H2ExampleGroup constructModel(
      Set<Attribute> attributes,
      Set<Attribute> multiValuesAdd,
      Set<Attribute> multiValuesRemove,
      boolean creation) {
    H2ExampleGroup group = new H2ExampleGroup();
    group.setId(AdapterValueTypeConverter.getIdentityIdAttributeValue(attributes));
    group.setName(
        AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, GROUP_NAME));
    group.setDescription(
        AdapterValueTypeConverter.getSingleAttributeValue(
            String.class, attributes, GROUP_DESCRIPTION));
    return group;
  }

  @Override
  protected Set<Attribute> constructAttributes(H2ExampleGroup group) {
    Set<Attribute> attributes = new HashSet<>();

    attributes.add(AttributeBuilder.build(GROUP_ID.name(), group.getId()));
    attributes.add(AttributeBuilder.build(GROUP_NAME.name(), group.getName()));
    attributes.add(AttributeBuilder.build(GROUP_DESCRIPTION.name(), group.getDescription()));

    return attributes;
  }
}
