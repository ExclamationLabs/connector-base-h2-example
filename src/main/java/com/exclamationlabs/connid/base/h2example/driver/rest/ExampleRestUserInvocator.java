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
import com.exclamationlabs.connid.base.h2example.model.H2ExampleUser;
import java.util.Map;
import java.util.Set;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

public class ExampleRestUserInvocator implements DriverInvocator<ExampleRestDriver, H2ExampleUser> {

  @Override
  public String create(ExampleRestDriver driver, H2ExampleUser userModel)
      throws ConnectorException {
    H2ExampleUser response =
        driver.executePostRequest("/users", H2ExampleUser.class, userModel).getResponseObject();
    return response.getId();
  }

  @Override
  public void update(ExampleRestDriver driver, String userId, H2ExampleUser userModel)
      throws ConnectorException {
    driver.executePatchRequest("/users/" + userId, null, userModel);
  }

  @Override
  public void delete(ExampleRestDriver driver, String userId) throws ConnectorException {
    driver.executeDeleteRequest("/users/" + userId, null);
  }

  @Override
  public Set<H2ExampleUser> getAll(
      ExampleRestDriver driver, ResultsFilter filter, ResultsPaginator paginator, Integer resultCap)
      throws ConnectorException {
    TestUsersResponse usersResponse =
        driver.executeGetRequest("/users", TestUsersResponse.class).getResponseObject();
    return usersResponse.getUsers();
  }

  @Override
  public H2ExampleUser getOne(
      ExampleRestDriver driver, String userId, Map<String, Object> headerMap)
      throws ConnectorException {
    return driver.executeGetRequest("/users/" + userId, H2ExampleUser.class).getResponseObject();
  }

  static class TestUsersResponse {
    Set<H2ExampleUser> users;

    public Set<H2ExampleUser> getUsers() {
      return users;
    }

    @SuppressWarnings("unused")
    public void setPeople(Set<H2ExampleUser> users) {
      this.users = users;
    }
  }
}
