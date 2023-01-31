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

import com.exclamationlabs.connid.base.connector.driver.rest.RestFaultProcessor;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

/** This is an example of a FaultProcessor used in conjunction with the ExampleRestDriver. */
public class ExampleFaultProcessor implements RestFaultProcessor {

  private static final Log LOG = Log.getLog(ExampleFaultProcessor.class);

  private static final ExampleFaultProcessor instance = new ExampleFaultProcessor();

  public static ExampleFaultProcessor getInstance() {
    return instance;
  }

  public void process(HttpResponse httpResponse, GsonBuilder gsonBuilder) {
    String rawResponse;
    try {
      rawResponse = EntityUtils.toString(httpResponse.getEntity(), Charsets.UTF_8);
      LOG.info("Raw Fault response {0}", rawResponse);

      Header responseType = httpResponse.getFirstHeader("Content-Type");
      String responseTypeValue = responseType.getValue();
      if (!StringUtils.contains(responseTypeValue, ContentType.APPLICATION_JSON.getMimeType())) {
        // received non-JSON error response, unable to process
        String errorMessage = "Unable to parse response, not valid JSON: ";
        LOG.info("{0} {1}", errorMessage, rawResponse);
        throw new ConnectorException(errorMessage + rawResponse);
      }

      handleFaultResponse(rawResponse, gsonBuilder);

    } catch (IOException e) {
      throw new ConnectorException(
          "Unable to read fault response from response. "
              + "Status: "
              + httpResponse.getStatusLine().getStatusCode()
              + ", "
              + httpResponse.getStatusLine().getReasonPhrase(),
          e);
    }
  }

  private void handleFaultResponse(String rawResponse, GsonBuilder gsonBuilder) {
    // Reading and interpretation of Fault JSON would go here
    // SomeErrorType faultData = gsonBuilder.create().fromJson(rawResponse, SomeErrorType.class);

    // Perhaps could throw
    // AlreadyExistsException if error response indicates user or group already exists.
    // InvalidAttributeValueException if error response indicates invalid input.
    // ConnectorException if unable to intrepret the fault data received from response
  }
}
