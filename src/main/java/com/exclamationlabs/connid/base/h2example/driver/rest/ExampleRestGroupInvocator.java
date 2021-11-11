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
import com.exclamationlabs.connid.base.connector.results.ResultsFilter;
import com.exclamationlabs.connid.base.connector.results.ResultsPaginator;
import com.exclamationlabs.connid.base.h2example.model.H2ExampleGroup;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

import java.util.Map;
import java.util.Set;

public class ExampleRestGroupInvocator implements DriverInvocator<ExampleRestDriver, H2ExampleGroup> {

    @Override
    public String create(ExampleRestDriver driver, H2ExampleGroup model)
            throws ConnectorException {
        H2ExampleGroup response = driver.executePostRequest(
                "/groups", H2ExampleGroup.class, model).getResponseObject();
        return response.getId();
    }

    @Override
    public void update(ExampleRestDriver driver, String groupId, H2ExampleGroup model)
            throws ConnectorException {
        driver.executePatchRequest(
                "/groups/" + groupId, null, model);
    }

    @Override
    public void delete(ExampleRestDriver driver, String groupId) throws ConnectorException {
        driver.executeDeleteRequest("/groups/" + groupId, null);
    }

    @Override
    public Set<H2ExampleGroup> getAll(ExampleRestDriver driver, ResultsFilter filter, ResultsPaginator paginator,
                                      Integer resultCap) throws ConnectorException {
        TestGroupsResponse response = driver.executeGetRequest(
                "/groups", TestGroupsResponse.class).getResponseObject();
        return response.getGroups();
    }

    @Override
    public H2ExampleGroup getOne(ExampleRestDriver driver, String groupId, Map<String,Object> headerMap) throws ConnectorException {
        return driver.executeGetRequest("/groups/" + groupId, H2ExampleGroup.class).getResponseObject();
    }

    static class TestGroupsResponse {
        Set<H2ExampleGroup> groups;

        public Set<H2ExampleGroup> getGroups() {
            return groups;
        }

        @SuppressWarnings("unused")
        public void setGroups(Set<H2ExampleGroup> groups) {
            this.groups = groups;
        }
    }

}
