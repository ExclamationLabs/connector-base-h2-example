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

import static com.exclamationlabs.connid.base.h2example.attribute.H2ExampleGroupAttribute.*;
import static com.exclamationlabs.connid.base.h2example.attribute.H2ExampleUserAttribute.*;
import static org.junit.jupiter.api.Assertions.*;

import com.exclamationlabs.connid.base.connector.configuration.ConfigurationNameBuilder;
import com.exclamationlabs.connid.base.connector.configuration.ConfigurationReader;
import com.exclamationlabs.connid.base.connector.test.ApiIntegrationTest;
import com.exclamationlabs.connid.base.h2example.attribute.H2ExampleGroupAttribute;
import com.exclamationlabs.connid.base.h2example.attribute.H2ExampleUserAttribute;
import com.exclamationlabs.connid.base.h2example.configuration.H2ExampleConfiguration;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.objects.filter.EqualsFilter;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class H2ExampleConnectorIntegrationTest
    extends ApiIntegrationTest<H2ExampleConfiguration, H2ExampleConnector> {

  private static String generatedUserId;
  private static String generatedGroupId;

  @Override
  protected H2ExampleConfiguration getConfiguration() {
    return new H2ExampleConfiguration(
        new ConfigurationNameBuilder().withConnector(() -> "H2EXAMPLE").build());
  }

  @Override
  protected Class<H2ExampleConnector> getConnectorClass() {
    return H2ExampleConnector.class;
  }

  @Override
  protected void readConfiguration(H2ExampleConfiguration h2ExampleConfiguration) {
    ConfigurationReader.setupTestConfiguration(h2ExampleConfiguration);
  }

  @BeforeEach
  public void setup() {
    super.setup();
  }

  @Test
  @Order(5)
  public void testConnection() {
    getConnectorFacade().test();
  }

  @Test
  @Order(10)
  public void test010GroupCreate() {
    Set<Attribute> attributes = new HashSet<>();
    attributes.add(
        new AttributeBuilder()
            .setName(H2ExampleGroupAttribute.GROUP_NAME.name())
            .addValue("Defenders")
            .build());
    attributes.add(
        new AttributeBuilder()
            .setName(H2ExampleGroupAttribute.GROUP_DESCRIPTION.name())
            .addValue("Defenders Superhero Team")
            .build());

    Uid newId =
        getConnectorFacade()
            .create(ObjectClass.GROUP, attributes, new OperationOptionsBuilder().build());
    assertNotNull(newId);
    assertNotNull(newId.getUidValue());
    generatedGroupId = newId.getUidValue();
  }

  @Test
  @Order(20)
  public void test020GroupModify() {
    Set<AttributeDelta> attributes = new HashSet<>();
    attributes.add(
        new AttributeDeltaBuilder()
            .setName(H2ExampleGroupAttribute.GROUP_DESCRIPTION.name())
            .addValueToReplace("Defenders Superhero Team 2")
            .build());

    Set<AttributeDelta> result =
        getConnectorFacade()
            .updateDelta(
                ObjectClass.GROUP,
                new Uid(generatedGroupId),
                attributes,
                new OperationOptionsBuilder().build());
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  @Order(30)
  public void test030GroupsGet() {
    results = new ArrayList<>();
    getConnectorFacade()
        .search(ObjectClass.GROUP, null, handler, new OperationOptionsBuilder().build());
    assertTrue(results.size() >= 1);
    assertTrue(
        StringUtils.isNotBlank(
            results.get(0).getAttributeByName(GROUP_ID.name()).getValue().get(0).toString()));
    assertTrue(
        StringUtils.isNotBlank(
            results.get(0).getAttributeByName(GROUP_NAME.name()).getValue().get(0).toString()));
  }

  @Test
  @Disabled // Test doesn't work since H2 persistence is reset by ConnectorFacade
  @Order(40)
  public void test040GroupGet() {
    results = new ArrayList<>();
    Attribute idAttribute =
        new AttributeBuilder().setName(Uid.NAME).addValue(generatedGroupId).build();

    getConnectorFacade()
        .search(
            ObjectClass.GROUP,
            new EqualsFilter(idAttribute),
            handler,
            new OperationOptionsBuilder().build());
    assertEquals(1, results.size());
    assertTrue(
        StringUtils.isNotBlank(
            results.get(0).getAttributeByName(GROUP_ID.name()).getValue().get(0).toString()));
    assertTrue(
        StringUtils.isNotBlank(
            results.get(0).getAttributeByName(GROUP_NAME.name()).getValue().get(0).toString()));
  }

  @Test
  @Order(110)
  public void test110UserCreate() {
    Set<Attribute> attributes = new HashSet<>();
    attributes.add(
        new AttributeBuilder()
            .setName(H2ExampleUserAttribute.FIRST_NAME.name())
            .addValue("Clint")
            .build());
    attributes.add(
        new AttributeBuilder()
            .setName(H2ExampleUserAttribute.LAST_NAME.name())
            .addValue("Barton")
            .build());
    attributes.add(
        new AttributeBuilder()
            .setName(H2ExampleUserAttribute.DESCRIPTION.name())
            .addValue("Hawkeye")
            .build());
    attributes.add(
        new AttributeBuilder()
            .setName(H2ExampleUserAttribute.TIME_ZONE.name())
            .addValue("Central")
            .build());
    attributes.add(
        new AttributeBuilder()
            .setName(H2ExampleUserAttribute.EMAIL.name())
            .addValue("hawkeye@avengers.com")
            .build());

    Uid newId =
        getConnectorFacade()
            .create(ObjectClass.ACCOUNT, attributes, new OperationOptionsBuilder().build());
    assertNotNull(newId);
    assertNotNull(newId.getUidValue());
    generatedUserId = newId.getUidValue();
  }

  @Test
  @Order(120)
  public void test120UserModify() {
    Set<AttributeDelta> attributes = new HashSet<>();
    attributes.add(
        new AttributeDeltaBuilder()
            .setName(H2ExampleUserAttribute.DESCRIPTION.name())
            .addValueToReplace("Hawkeye 2")
            .build());

    Set<AttributeDelta> response =
        getConnectorFacade()
            .updateDelta(
                ObjectClass.ACCOUNT,
                new Uid(generatedUserId),
                attributes,
                new OperationOptionsBuilder().build());
    assertNotNull(response);
    assertTrue(response.isEmpty());
  }

  @Test
  @Order(130)
  public void test130UsersGet() {
    results = new ArrayList<>();
    getConnectorFacade()
        .search(ObjectClass.ACCOUNT, null, handler, new OperationOptionsBuilder().build());
    assertTrue(results.size() >= 1);
    assertTrue(
        StringUtils.isNotBlank(
            results.get(0).getAttributeByName(USER_ID.name()).getValue().get(0).toString()));
    assertTrue(
        StringUtils.isNotBlank(
            results.get(0).getAttributeByName(EMAIL.name()).getValue().get(0).toString()));
  }

  @Test
  @Disabled // Test doesn't work since H2 persistence is reset by ConnectorFacade
  @Order(140)
  public void test140UserGet() {
    results = new ArrayList<>();
    Attribute idAttribute =
        new AttributeBuilder().setName(Uid.NAME).addValue(generatedUserId).build();

    getConnectorFacade()
        .search(
            ObjectClass.ACCOUNT,
            new EqualsFilter(idAttribute),
            handler,
            new OperationOptionsBuilder().build());
    assertEquals(1, results.size());
    assertTrue(
        StringUtils.isNotBlank(
            results.get(0).getAttributeByName(USER_ID.name()).getValue().get(0).toString()));
  }

  @Test
  @Order(250)
  public void test250AddUserToGroup() {
    Set<AttributeDelta> attributes = new HashSet<>();
    attributes.add(
        new AttributeDeltaBuilder()
            .setName(H2ExampleUserAttribute.GROUP_IDS.name())
            .addValueToReplace(Collections.singletonList(generatedGroupId))
            .build());

    Set<AttributeDelta> response =
        getConnectorFacade()
            .updateDelta(
                ObjectClass.ACCOUNT,
                new Uid(generatedUserId),
                attributes,
                new OperationOptionsBuilder().build());
    assertNotNull(response);
    assertTrue(response.isEmpty());
  }

  @Test
  @Order(260)
  public void test260RemoveUserFromGroup() {
    Set<AttributeDelta> attributes = new HashSet<>();
    attributes.add(
        new AttributeDeltaBuilder()
            .setName(H2ExampleUserAttribute.GROUP_IDS.name())
            .addValueToReplace(Collections.singletonList(null))
            .build());

    Set<AttributeDelta> response =
        getConnectorFacade()
            .updateDelta(
                ObjectClass.ACCOUNT,
                new Uid(generatedUserId),
                attributes,
                new OperationOptionsBuilder().build());
    assertNotNull(response);
    assertTrue(response.isEmpty());
  }

  @Test
  @Order(290)
  public void test290GroupDelete() {
    getConnectorFacade()
        .delete(
            ObjectClass.GROUP, new Uid(generatedGroupId), new OperationOptionsBuilder().build());
  }

  @Test
  @Order(390)
  public void test390UserDelete() {
    getConnectorFacade()
        .delete(
            ObjectClass.ACCOUNT, new Uid(generatedUserId), new OperationOptionsBuilder().build());
  }
}
