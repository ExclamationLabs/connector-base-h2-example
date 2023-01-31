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

import com.exclamationlabs.connid.base.connector.driver.rest.BaseRestDriver;
import com.exclamationlabs.connid.base.connector.driver.rest.RestFaultProcessor;
import com.exclamationlabs.connid.base.h2example.configuration.H2ExampleConfiguration;
import com.exclamationlabs.connid.base.h2example.model.H2ExampleGroup;
import com.exclamationlabs.connid.base.h2example.model.H2ExampleUser;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

/**
 * This is an example of what a simple RESTful driver might look like for the Base Connector
 * framework. This is just a fictional implementation, not real web services.
 */
public class ExampleRestDriver extends BaseRestDriver<H2ExampleConfiguration> {

  public ExampleRestDriver() {
    addInvocator(H2ExampleUser.class, new ExampleRestUserInvocator());
    addInvocator(H2ExampleGroup.class, new ExampleRestGroupInvocator());
  }

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
}
