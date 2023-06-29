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

import static com.exclamationlabs.connid.base.connector.attribute.ConnectorAttributeDataType.ASSIGNMENT_IDENTIFIER;
import static com.exclamationlabs.connid.base.connector.attribute.ConnectorAttributeDataType.STRING;
import static com.exclamationlabs.connid.base.h2example.attribute.H2ExampleUserAttribute.*;
import static org.identityconnectors.framework.common.objects.AttributeInfo.Flags.MULTIVALUED;
import static org.identityconnectors.framework.common.objects.AttributeInfo.Flags.NOT_UPDATEABLE;

import com.exclamationlabs.connid.base.connector.adapter.*;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttribute;
import com.exclamationlabs.connid.base.h2example.configuration.H2ExampleConfiguration;
import com.exclamationlabs.connid.base.h2example.model.H2ExampleUser;
import java.util.HashSet;
import java.util.Set;
import org.identityconnectors.framework.common.objects.*;

public class H2ExampleUsersAdapter extends BaseAdapter<H2ExampleUser, H2ExampleConfiguration>
    implements EnhancedPaginationAndFiltering, PaginationCapableSource, FilterCapableSource {

  @Override
  public ObjectClass getType() {
    return new ObjectClass("user");
  }

  @Override
  public Class<H2ExampleUser> getIdentityModelClass() {
    return H2ExampleUser.class;
  }

  @Override
  public Set<ConnectorAttribute> getConnectorAttributes() {
    Set<ConnectorAttribute> result = new HashSet<>();
    result.add(new ConnectorAttribute(Uid.NAME, USER_ID.name(), STRING, NOT_UPDATEABLE));
    result.add(new ConnectorAttribute(Name.NAME, EMAIL.name(), STRING, NOT_UPDATEABLE));
    result.add(new ConnectorAttribute(FIRST_NAME.name(), STRING, NOT_UPDATEABLE));
    result.add(new ConnectorAttribute(LAST_NAME.name(), STRING, NOT_UPDATEABLE));
    result.add(new ConnectorAttribute(GENDER.name(), STRING, NOT_UPDATEABLE));
    result.add(new ConnectorAttribute(TIME_ZONE.name(), STRING, NOT_UPDATEABLE));
    result.add(new ConnectorAttribute(DESCRIPTION.name(), STRING));
    result.add(new ConnectorAttribute(GROUP_IDS.name(), ASSIGNMENT_IDENTIFIER, MULTIVALUED));
    result.add(new ConnectorAttribute(POWER_IDS.name(), ASSIGNMENT_IDENTIFIER, MULTIVALUED));
    return result;
  }

  @Override
  protected H2ExampleUser constructModel(
      Set<Attribute> attributes,
      Set<Attribute> multiValuesAdd,
      Set<Attribute> multiValuesRemove,
      boolean creation) {
    H2ExampleUser user = new H2ExampleUser();
    user.setId(AdapterValueTypeConverter.getIdentityIdAttributeValue(attributes));

    user.setFirstName(
        AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, FIRST_NAME));
    user.setLastName(
        AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, LAST_NAME));
    user.setEmail(
        AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, EMAIL));
    user.setTimezone(
        AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, TIME_ZONE));
    user.setDescription(
        AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, DESCRIPTION));
    user.setGender(
        AdapterValueTypeConverter.getSingleAttributeValue(String.class, attributes, GENDER));

    user.setGroupIds(readAssignments(attributes, GROUP_IDS));
    user.setPowerIds(readAssignments(attributes, POWER_IDS));

    return user;
  }

  @Override
  protected Set<Attribute> constructAttributes(H2ExampleUser user) {
    Set<Attribute> attributes = new HashSet<>();

    attributes.add(AttributeBuilder.build(FIRST_NAME.name(), user.getFirstName()));
    attributes.add(AttributeBuilder.build(LAST_NAME.name(), user.getLastName()));
    attributes.add(AttributeBuilder.build(DESCRIPTION.name(), user.getDescription()));
    attributes.add(AttributeBuilder.build(GENDER.name(), user.getGender()));
    attributes.add(AttributeBuilder.build(TIME_ZONE.name(), user.getTimezone()));
    attributes.add(AttributeBuilder.build(GROUP_IDS.name(), user.getGroupIds()));
    attributes.add(AttributeBuilder.build(POWER_IDS.name(), user.getPowerIds()));

    return attributes;
  }

  @Override
  public boolean getSearchResultsContainsAllAttributes() {
    return false;
  }

  @Override
  public boolean getSearchResultsContainsNameAttribute() {
    return true;
  }

  @Override
  public boolean getOneByName() {
    return true;
  }

  @Override
  public Set<String> getSearchResultsAttributesPresent() {
    return Set.of(
        USER_ID.name(),
        FIRST_NAME.name(),
        LAST_NAME.name(),
        EMAIL.name(),
        TIME_ZONE.name(),
        DESCRIPTION.name(),
        GENDER.name());
  }

  @Override
  public Set<String> getEqualsFilterAttributes() {
    return Set.of(
        USER_ID.name(),
        FIRST_NAME.name(),
        LAST_NAME.name(),
        EMAIL.name(),
        TIME_ZONE.name(),
        DESCRIPTION.name(),
        GENDER.name());
  }

  @Override
  public Set<String> getContainsFilterAttributes() {
    return Set.of(
        USER_ID.name(),
        FIRST_NAME.name(),
        LAST_NAME.name(),
        EMAIL.name(),
        TIME_ZONE.name(),
        DESCRIPTION.name(),
        GENDER.name());
  }

  @Override
  public boolean hasSearchResultsMaximum() {
    return true;
  }

  @Override
  public Integer getSearchResultsMaximum() {
    return 1000;
  }
}
