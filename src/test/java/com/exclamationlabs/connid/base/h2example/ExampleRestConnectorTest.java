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

import com.exclamationlabs.connid.base.connector.test.util.ConnectorMockRestTest;
import com.exclamationlabs.connid.base.connector.test.util.ConnectorTestUtils;
import com.exclamationlabs.connid.base.h2example.attribute.H2ExampleGroupAttribute;
import com.exclamationlabs.connid.base.h2example.attribute.H2ExampleUserAttribute;
import com.exclamationlabs.connid.base.h2example.configuration.H2ExampleConfiguration;
import com.exclamationlabs.connid.base.h2example.driver.H2ExampleDriver;
import com.exclamationlabs.connid.base.h2example.driver.rest.ExampleRestDriver;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.spi.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ExampleRestConnectorTest extends ConnectorMockRestTest {

    private H2ExampleConnector connector;

    @Before
    public void setup() {
        connector = new H2ExampleConnector() {
            @Override
            public void init(Configuration configuration) {
                setAuthenticator(null);
                setDriver(new H2ExampleDriver());
                setDriver(new ExampleRestDriver() {
                    @Override
                    protected HttpClient createClient() {
                        return stubClient;
                    }
                });
                super.init(configuration);
            }
        };
        H2ExampleConfiguration configuration = new H2ExampleConfiguration();
        configuration.setTestConfiguration();
        connector.init(configuration);
    }

    @Test
    public void test110UserCreate() {
        final String responseData = "{\"id\":\"keGi76UxSBePr_kFhIaM2Q\",\"firstName\":\"Captain\",\"lastName\":\"America\",\"email\":\"captain@america.com\"}";
        prepareMockResponse(responseData);

        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(H2ExampleUserAttribute.FIRST_NAME.name()).addValue("Captain").build());
        attributes.add(new AttributeBuilder().setName(H2ExampleUserAttribute.LAST_NAME.name()).addValue("America").build());
        attributes.add(new AttributeBuilder().setName(H2ExampleUserAttribute.EMAIL.name()).addValue("captain@america.com").build());

        Uid newId = connector.create(ObjectClass.ACCOUNT, attributes, new OperationOptionsBuilder().build());
        assertNotNull(newId);
        assertNotNull(newId.getUidValue());
    }

    @Test
    public void test120UserModify() {
        prepareMockResponse();
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(H2ExampleUserAttribute.DESCRIPTION.name()).addValue("super hero").build());

        Uid newId = connector.update(ObjectClass.ACCOUNT, new Uid("1234"), attributes, new OperationOptionsBuilder().build());
        assertNotNull(newId);
        assertNotNull(newId.getUidValue());
    }

    @Test
    public void test130UsersGet() {
        String responseData = "{\"totalRecords\":1,\"users\":[{\"id\":\"ZpRAY4X9SEipRS9kS--Img\",\"firstName\":\"Alfred\",\"last_name\":\"Neuman\",\"email\":\"alfred@mad.com\"}]}";
        prepareMockResponse(responseData);

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
        String responseData = "{\"id\":\"ZpRAY4X9SEipRS9kS--Img\",\"firstName\":\"Alfred\",\"last_name\":\"Neuman\",\"email\":\"alfred@mad.com\"}";
        prepareMockResponse(responseData);

        List<String> idValues = new ArrayList<>();
        List<String> nameValues = new ArrayList<>();
        ResultsHandler resultsHandler = ConnectorTestUtils.buildResultsHandler(idValues, nameValues);

        connector.executeQuery(ObjectClass.ACCOUNT, "1234", resultsHandler, new OperationOptionsBuilder().build());
        assertEquals(1, idValues.size());
        assertTrue(StringUtils.isNotBlank(idValues.get(0)));
    }


    @Test
    public void test210GroupCreate() {
        final String responseData = "{\"id\":\"yRU7LBa6RmenCOjsoEJkxw\",\"name\":\"Alpha Flight\"}";
        prepareMockResponse(responseData);
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(H2ExampleGroupAttribute.GROUP_NAME.name()).addValue("Alpha Flight").build());

        Uid newId = connector.create(ObjectClass.GROUP, attributes, new OperationOptionsBuilder().build());
        assertNotNull(newId);
        assertNotNull(newId.getUidValue());
    }

    @Test
    public void test220GroupModify() {
        prepareMockResponse();
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(H2ExampleGroupAttribute.GROUP_NAME.name()).addValue("Alpha Flight2").build());

        Uid newId = connector.update(ObjectClass.GROUP, new Uid("1234"), attributes, new OperationOptionsBuilder().build());
        assertNotNull(newId);
        assertNotNull(newId.getUidValue());
    }

    @Test
    public void test230GroupsGet() {
        String responseData = "{\"totalRecords\":3,\"groups\":[{\"id\":\"tAKM1nXqSSS4kgtNu91_uQ\",\"name\":\"Alpha Flight\"},{\"id\":\"loiFdqtuR4WoCq2Rn3G8uw\",\"name\":\"Avengers\"},{\"id\":\"nu7kJQ4PRwWrlyXoGHHopg\",\"name\":\"West Coast Avengers\"}]}";
        prepareMockResponse(responseData);
        List<String> idValues = new ArrayList<>();
        List<String> nameValues = new ArrayList<>();
        ResultsHandler resultsHandler = ConnectorTestUtils.buildResultsHandler(idValues, nameValues);

        connector.executeQuery(ObjectClass.GROUP, "", resultsHandler, new OperationOptionsBuilder().build());
        assertTrue(idValues.size() >= 1);
        assertTrue(StringUtils.isNotBlank(idValues.get(0)));
        assertTrue(StringUtils.isNotBlank(nameValues.get(0)));
    }

    @Test
    public void test240GroupGet() {
        String responseData = "{\"id\":\"tAKM1nXqSSS4kgtNu91_uQ\",\"name\":\"Alpha Flight\"}";
        prepareMockResponse(responseData);
        List<String> idValues = new ArrayList<>();
        List<String> nameValues = new ArrayList<>();
        ResultsHandler resultsHandler = ConnectorTestUtils.buildResultsHandler(idValues, nameValues);

        connector.executeQuery(ObjectClass.GROUP, "1234", resultsHandler, new OperationOptionsBuilder().build());
        assertEquals(1, idValues.size());
        assertTrue(StringUtils.isNotBlank(idValues.get(0)));
        assertTrue(StringUtils.isNotBlank(nameValues.get(0)));
    }

    @Test
    public void test290GroupDelete() {
        prepareMockResponse();
        connector.delete(ObjectClass.GROUP, new Uid("1234"), new OperationOptionsBuilder().build());
    }

    @Test
    public void test390UserDelete() {
        prepareMockResponse();
        connector.delete(ObjectClass.ACCOUNT, new Uid("1234"), new OperationOptionsBuilder().build());
    }

}