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

package com.exclamationlabs.connid.base.h2example.model;

import com.exclamationlabs.connid.base.connector.model.IdentityModel;
import com.exclamationlabs.connid.base.h2example.attribute.H2ExampleUserAttribute;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Uid;

public class H2ExampleUser implements IdentityModel {

  private String id;

  private String firstName;

  private String lastName;

  private String email;

  private String timezone;

  private String description;

  private String gender;

  private Set<String> groupIds;

  private Set<String> powerIds;

  public H2ExampleUser() {
    setId(UUID.randomUUID().toString());
  }

  @Override
  public String getIdentityIdValue() {
    return getId();
  }

  @Override
  public String getIdentityNameValue() {
    return getEmail();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

  public Set<String> getGroupIds() {
    return groupIds;
  }

  public void setGroupIds(Set<String> groupIds) {
    this.groupIds = groupIds;
  }

  public Set<String> getPowerIds() {
    return powerIds;
  }

  public void setPowerIds(Set<String> powerIds) {
    this.powerIds = powerIds;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  @Override
  public String getValueBySearchableAttributeName(String attributeName) {
    String value = null;
    if (StringUtils.equalsIgnoreCase(attributeName, Uid.NAME)) {
      return getIdentityIdValue();
    }
    if (StringUtils.equalsIgnoreCase(attributeName, Name.NAME)) {
      return getIdentityNameValue();
    }
    switch (H2ExampleUserAttribute.valueOf(attributeName)) {
      case USER_ID:
        value = getIdentityIdValue();
        break;
      case EMAIL:
        value = getIdentityNameValue();
        break;
      case FIRST_NAME:
        value = getFirstName();
        break;
      case LAST_NAME:
        value = getLastName();
        break;
      case DESCRIPTION:
        value = getDescription();
        break;
      case TIME_ZONE:
        value = getTimezone();
        break;
      case GENDER:
        value = getGender();
        break;
      default:
        break;
    }
    return value;
  }

  @Override
  public boolean equals(Object input) {
    return identityEquals(H2ExampleUser.class, this, input);
  }

  @Override
  public int hashCode() {
    return identityHashCode();
  }

  @Override
  public String toString() {
    return identityToString();
  }
}
