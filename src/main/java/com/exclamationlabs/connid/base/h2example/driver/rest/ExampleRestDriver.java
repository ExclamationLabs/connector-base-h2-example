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

package com.exclamationlabs.connid.base.h2example.driver.rest;

import com.exclamationlabs.connid.base.connector.configuration.ConnectorProperty;
import com.exclamationlabs.connid.base.connector.driver.rest.BaseRestDriver;
import com.exclamationlabs.connid.base.connector.driver.rest.RestFaultProcessor;
import com.exclamationlabs.connid.base.h2example.model.H2ExampleGroup;
import com.exclamationlabs.connid.base.h2example.model.H2ExampleUser;
import com.exclamationlabs.connid.base.h2example.model.response.ListGroupsResponse;
import com.exclamationlabs.connid.base.h2example.model.response.ListUsersResponse;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

import java.util.List;
import java.util.Set;

/**
 * This is an example of what a simple RESTful driver might look like for the Base
 * Connector framework.  This is just a fictional implementation, not real web services.
 */
public class ExampleRestDriver extends BaseRestDriver<H2ExampleUser, H2ExampleGroup> {

    @Override
    protected RestFaultProcessor getFaultProcessor() {
        return ExampleFaultProcessor.getInstance();
    }

    @Override
    protected String getBaseServiceUrl() {
        return "https://fictional-demo-url.com";
    }

    @Override
    protected boolean usesBearerAuthorization() {
        return true;
    }

    @Override
    public Set<ConnectorProperty> getRequiredPropertyNames() {
        return null;
    }

    @Override
    public void test() throws ConnectorException {
        try {
            executeGetRequest("/fictional/health/check", null);
        } catch (Exception e) {
            throw new ConnectorException("Health check for service failed.", e);
        }
    }

    @Override
    public void close() {
        configuration = null;
        authenticator = null;
    }

    @Override
    public String createUser(H2ExampleUser userModel) throws ConnectorException {
        H2ExampleUser newUser = executePostRequest("/users", H2ExampleUser.class, userModel);

        if (newUser == null) {
            throw new ConnectorException("Response from user creation was invalid");
        }
        return newUser.getId();
    }

    @Override
    public String createGroup(H2ExampleGroup groupModel) throws ConnectorException {
        H2ExampleGroup newGroup = executePostRequest("/groups", H2ExampleGroup.class, groupModel);
        if (newGroup == null || newGroup.getId() == null) {
            throw new ConnectorException("Response from group creation was invalid");
        }

        return newGroup.getId();
    }

    @Override
    public void updateUser(String userId, H2ExampleUser userModel) throws ConnectorException {
        executePatchRequest("/users/" + userId, null, userModel);
    }

    @Override
    public void updateGroup(String groupId, H2ExampleGroup groupModel) throws ConnectorException {
        executePatchRequest("/groups/" + groupId, null, groupModel);
    }

    @Override
    public void deleteUser(String userId) throws ConnectorException {
        executeDeleteRequest("/users/" + userId, null);
    }

    @Override
    public void deleteGroup(String groupId) throws ConnectorException {
        executeDeleteRequest("/groups/" + groupId, null);
    }

    @Override
    public List<H2ExampleUser> getUsers() throws ConnectorException {
        ListUsersResponse response = executeGetRequest("/users", ListUsersResponse.class);
        return response.getUsers();
    }

    @Override
    public List<H2ExampleGroup> getGroups() throws ConnectorException {
        ListGroupsResponse response = executeGetRequest("/groups", ListGroupsResponse.class);
        return response.getGroups();
    }

    @Override
    public H2ExampleUser getUser(String userId) throws ConnectorException {
        return executeGetRequest("/users/" + userId, H2ExampleUser.class);
    }

    @Override
    public H2ExampleGroup getGroup(String groupId) throws ConnectorException {
        return executeGetRequest("/groups/" + groupId, H2ExampleGroup.class);
    }

    @Override
    public void addGroupToUser(String groupId, String userId) throws ConnectorException {
        executePutRequest("/fictional/groupmembership/" +
                groupId + "/" + userId, null, null);
    }

    @Override
    public void removeGroupFromUser(String groupId, String userId) throws ConnectorException {
        executeDeleteRequest("/fictional/groupmembership/" + groupId +
                "/" + userId, null);
    }
}