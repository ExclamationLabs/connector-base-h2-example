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

import com.exclamationlabs.connid.base.connector.driver.DriverInvocator;
import com.exclamationlabs.connid.base.h2example.model.H2ExampleUser;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

import java.util.List;

public class ExampleRestUserInvocator implements DriverInvocator<ExampleRestDriver, H2ExampleUser> {

    @Override
    public String create(ExampleRestDriver driver, H2ExampleUser userModel)
            throws ConnectorException {
        H2ExampleUser response = driver.executePostRequest(
                "/users", H2ExampleUser.class, userModel);
        return response.getId();
    }

    @Override
    public void update(ExampleRestDriver driver, String userId, H2ExampleUser userModel)
            throws ConnectorException {
        driver.executePatchRequest(
                "/users/" + userId, null, userModel);
    }

    @Override
    public void delete(ExampleRestDriver driver, String userId) throws ConnectorException {
        driver.executeDeleteRequest("/users/" + userId, null);
    }

    @Override
    public List<H2ExampleUser> getAll(ExampleRestDriver driver) throws ConnectorException {
        TestUsersResponse usersResponse = driver.executeGetRequest(
                "/users", TestUsersResponse.class);
        return usersResponse.getUsers();
    }

    @Override
    public H2ExampleUser getOne(ExampleRestDriver driver, String userId) throws ConnectorException {
        return driver.executeGetRequest("/users/" + userId, H2ExampleUser.class);
    }

    static class TestUsersResponse {
        List<H2ExampleUser> users;

        public List<H2ExampleUser> getUsers() {
            return users;
        }

        @SuppressWarnings("unused")
        public void setPeople(List<H2ExampleUser> users) {
            this.users = users;
        }
    }

}
