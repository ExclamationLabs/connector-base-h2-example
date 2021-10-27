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

import com.exclamationlabs.connid.base.connector.configuration.ConfigurationNameBuilder;
import com.exclamationlabs.connid.base.connector.test.IntegrationTest;
import com.exclamationlabs.connid.base.connector.test.util.ConnectorTestUtils;
import com.exclamationlabs.connid.base.h2example.attribute.H2ExampleGroupAttribute;
import com.exclamationlabs.connid.base.h2example.attribute.H2ExampleUserAttribute;
import com.exclamationlabs.connid.base.h2example.configuration.H2ExampleConfiguration;
import com.exclamationlabs.connid.base.h2example.driver.H2ExampleDriver;
import org.apache.commons.lang3.StringUtils;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.spi.Configuration;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.*;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class H2ExampleConnectorIntegrationTest extends IntegrationTest {

    private static H2ExampleConnector connector;

    private static String generatedUserId;
    private static String generatedGroupId;

    @Override
    public String getConfigurationName() {
        return new ConfigurationNameBuilder().withConnector(() -> "H2EXAMPLE").build();
    }

    @BeforeClass
    public static void setup() {
        String configName = new ConfigurationNameBuilder().withConnector(() -> "H2EXAMPLE").build();
        connector = new H2ExampleConnector() {
            @Override
            public void init(Configuration configuration) {
                setAuthenticator(null);
                setDriver(new H2ExampleDriver());
                super.init(configuration);
            }
        };
        H2ExampleConfiguration configuration = new H2ExampleConfiguration(configName);
        connector.init(configuration);
    }

    @AfterClass
    public static void teardown() {
        connector.dispose();
    }

    @Test
    public void testConnection() {
        connector.test();
    }

    @Test
    public void test010GroupCreate() {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(H2ExampleGroupAttribute.GROUP_NAME.name()).addValue("Defenders").build());
        attributes.add(new AttributeBuilder().setName(H2ExampleGroupAttribute.GROUP_DESCRIPTION.name()).addValue("Defenders Superhero Team").build());

        Uid newId = connector.create(ObjectClass.GROUP, attributes, new OperationOptionsBuilder().build());
        assertNotNull(newId);
        assertNotNull(newId.getUidValue());
        generatedGroupId = newId.getUidValue();
    }

    @Test
    public void test020GroupModify() {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(H2ExampleGroupAttribute.GROUP_DESCRIPTION.name()).addValue("Defenders Superhero Team 2").build());

        Uid newId = connector.update(ObjectClass.GROUP, new Uid(generatedGroupId), attributes, new OperationOptionsBuilder().build());
        assertNotNull(newId);
        assertNotNull(newId.getUidValue());
    }

    @Test
    public void test030GroupsGet() {
        List<String> idValues = new ArrayList<>();
        List<String> nameValues = new ArrayList<>();
        ResultsHandler resultsHandler = ConnectorTestUtils.buildResultsHandler(idValues, nameValues);

        connector.executeQuery(ObjectClass.GROUP, "", resultsHandler, new OperationOptionsBuilder().build());
        assertTrue(idValues.size() >= 1);
        assertTrue(StringUtils.isNotBlank(idValues.get(0)));
        assertTrue(StringUtils.isNotBlank(nameValues.get(0)));
    }

    @Test
    public void test040GroupGet() {
        List<String> idValues = new ArrayList<>();
        List<String> nameValues = new ArrayList<>();
        ResultsHandler resultsHandler = ConnectorTestUtils.buildResultsHandler(idValues, nameValues);

        connector.executeQuery(ObjectClass.GROUP, generatedGroupId, resultsHandler, new OperationOptionsBuilder().build());
        assertEquals(1, idValues.size());
        assertTrue(StringUtils.isNotBlank(idValues.get(0)));
        assertTrue(StringUtils.isNotBlank(nameValues.get(0)));
    }

    @Test
    public void test110UserCreate() {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(H2ExampleUserAttribute.FIRST_NAME.name()).addValue("Clint").build());
        attributes.add(new AttributeBuilder().setName(H2ExampleUserAttribute.LAST_NAME.name()).addValue("Barton").build());
        attributes.add(new AttributeBuilder().setName(H2ExampleUserAttribute.DESCRIPTION.name()).addValue("Hawkeye").build());
        attributes.add(new AttributeBuilder().setName(H2ExampleUserAttribute.TIME_ZONE.name()).addValue("Central").build());
        attributes.add(new AttributeBuilder().setName(H2ExampleUserAttribute.EMAIL.name()).addValue("hawkeye@avengers.com").build());

        Uid newId = connector.create(ObjectClass.ACCOUNT, attributes, new OperationOptionsBuilder().build());
        assertNotNull(newId);
        assertNotNull(newId.getUidValue());
        generatedUserId = newId.getUidValue();
    }


    @Test
    public void test120UserModify() {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(H2ExampleUserAttribute.DESCRIPTION.name()).addValue("Hawkeye 2").build());

        Uid newId = connector.update(ObjectClass.ACCOUNT, new Uid(generatedUserId), attributes, new OperationOptionsBuilder().build());
        assertNotNull(newId);
        assertNotNull(newId.getUidValue());
    }

    @Test
    public void test130UsersGet() {
        List<String> idValues = new ArrayList<>();
        List<String> nameValues = new ArrayList<>();
        ResultsHandler resultsHandler = ConnectorTestUtils.buildResultsHandler(idValues, nameValues);

        connector.executeQuery(ObjectClass.ACCOUNT, "", resultsHandler, new OperationOptionsBuilder().build());
        assertTrue(idValues.size() >= 1);
        assertTrue(StringUtils.isNotBlank(idValues.get(0)));
        assertTrue(StringUtils.isNotBlank(nameValues.get(0)));
    }

    @Test
    public void test140UserGet() {
        List<String> idValues = new ArrayList<>();
        List<String> nameValues = new ArrayList<>();
        ResultsHandler resultsHandler = ConnectorTestUtils.buildResultsHandler(idValues, nameValues);

        connector.executeQuery(ObjectClass.ACCOUNT, generatedUserId, resultsHandler, new OperationOptionsBuilder().build());
        assertEquals(1, idValues.size());
        assertTrue(StringUtils.isNotBlank(idValues.get(0)));
    }

    @Test
    public void test250AddUserToGroup() {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(H2ExampleUserAttribute.GROUP_IDS.name()).addValue(
                Collections.singletonList(generatedGroupId)).build());

        Uid newId = connector.update(ObjectClass.ACCOUNT, new Uid(generatedUserId), attributes, new OperationOptionsBuilder().build());
        assertNotNull(newId);
        assertNotNull(newId.getUidValue());
    }

    @Test
    public void test260RemoveUserFromGroup() {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(H2ExampleUserAttribute.GROUP_IDS.name()).addValue(
                Collections.singletonList(null)).build());

        Uid newId = connector.update(ObjectClass.ACCOUNT, new Uid(generatedUserId), attributes, new OperationOptionsBuilder().build());
        assertNotNull(newId);
        assertNotNull(newId.getUidValue());
    }

    @Test
    public void test290GroupDelete() {
        connector.delete(ObjectClass.GROUP, new Uid(generatedGroupId), new OperationOptionsBuilder().build());
    }

    @Test
    public void test390UserDelete() {
        connector.delete(ObjectClass.ACCOUNT, new Uid(generatedUserId), new OperationOptionsBuilder().build());
    }

}