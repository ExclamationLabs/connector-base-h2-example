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

package com.exclamationlabs.connid.base.h2example.driver;

import com.exclamationlabs.connid.base.h2example.model.H2ExampleUser;
import java.util.LinkedHashSet;
import java.util.Set;

public class InitialUsersGeneration {

  private InitialUsersGeneration() {}

  public static Set<H2ExampleUser> execute() {
    Set<H2ExampleUser> result = new LinkedHashSet<>();

    H2ExampleUser person = new H2ExampleUser();
    person.setFirstName("Peter");
    person.setLastName("Rasputin");
    person.setEmail("peter@xmen.com");
    person.setTimezone("Central");
    person.setDescription("X-Man");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Scott");
    person.setLastName("Summers");
    person.setEmail("scott@xmen.com");
    person.setTimezone("Central");
    person.setDescription("X-Man");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Kitty");
    person.setLastName("Pride");
    person.setEmail("kitty@xmen.com");
    person.setTimezone("Central");
    person.setDescription("X-Man");
    person.setGender("Female");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Charles");
    person.setLastName("Xavier");
    person.setEmail("xavier@xmen.com");
    person.setTimezone("Central");
    person.setDescription("X-Man");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Ororo");
    person.setLastName("Monroe");
    person.setEmail("ororo@xmen.com");
    person.setTimezone("Central");
    person.setDescription("X-Man");
    person.setGender("Female");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Jean");
    person.setLastName("Grey");
    person.setEmail("jean@xmen.com");
    person.setTimezone("Central");
    person.setDescription("X-Man");
    person.setGender("Female");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Betsy");
    person.setLastName("Braddock");
    person.setEmail("betsy@xmen.com");
    person.setTimezone("Central");
    person.setDescription("X-Man");
    person.setGender("Female");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Kurt");
    person.setLastName("Wagner");
    person.setEmail("kurt@xmen.com");
    person.setTimezone("Central");
    person.setDescription("X-Man");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Bobby");
    person.setLastName("Drake");
    person.setEmail("bobby@xmen.com");
    person.setTimezone("Central");
    person.setDescription("X-Man");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Hank");
    person.setLastName("McCoy");
    person.setEmail("hank@xmen.com");
    person.setTimezone("Central");
    person.setDescription("X-Man");
    person.setGender("Male");
    result.add(person);

    person.setFirstName("Ben");
    person.setLastName("Grimm");
    person.setEmail("ben@ff.com");
    person.setTimezone("Central");
    person.setDescription("Fantastic Four");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Johnny");
    person.setLastName("Storm");
    person.setEmail("johnny@ff.com");
    person.setTimezone("Central");
    person.setDescription("Fantastic Four");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Sue");
    person.setLastName("Storm");
    person.setEmail("sue@ff.com");
    person.setTimezone("Central");
    person.setDescription("Fantastic Four");
    person.setGender("Female");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Reed");
    person.setLastName("Richards");
    person.setEmail("reed@ff.com");
    person.setTimezone("Central");
    person.setDescription("Fantastic Four");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Steve");
    person.setLastName("Rodgers");
    person.setEmail("steve@avengers.com");
    person.setTimezone("Eastern");
    person.setDescription("Avengers");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Tony");
    person.setLastName("Stark");
    person.setEmail("tony@avengers.com");
    person.setTimezone("Eastern");
    person.setDescription("Avengers");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Janet");
    person.setLastName("VanDyne");
    person.setEmail("janet@avengers.com");
    person.setTimezone("Eastern");
    person.setDescription("Avengers");
    person.setGender("Female");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Bruce");
    person.setLastName("Banner");
    person.setEmail("bruce@avengers.com");
    person.setTimezone("Eastern");
    person.setDescription("Avengers");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Clint");
    person.setLastName("Barton");
    person.setEmail("clint@avengers.com");
    person.setTimezone("Eastern");
    person.setDescription("Avengers");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Henry");
    person.setLastName("Pym");
    person.setEmail("henry@avengers.com");
    person.setTimezone("Eastern");
    person.setDescription("Avengers");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Dane");
    person.setLastName("Whitman");
    person.setEmail("dane@avengers.com");
    person.setTimezone("Eastern");
    person.setDescription("Avengers");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Simon");
    person.setLastName("Williams");
    person.setEmail("simon@avengers.com");
    person.setTimezone("Eastern");
    person.setDescription("Avengers");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Namor");
    person.setLastName("McKenzie");
    person.setEmail("namor@avengers.com");
    person.setTimezone("Eastern");
    person.setDescription("Avengers");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Wendell");
    person.setLastName("Vaughn");
    person.setEmail("wendell@avengers.com");
    person.setTimezone("Eastern");
    person.setDescription("Avengers");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Scott");
    person.setLastName("Lang");
    person.setEmail("scott@avengers.com");
    person.setTimezone("Eastern");
    person.setDescription("Avengers");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Bucky");
    person.setLastName("Barnes");
    person.setEmail("bucky@avengers.com");
    person.setTimezone("Eastern");
    person.setDescription("Avengers");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Pietro");
    person.setLastName("Maximoff");
    person.setEmail("pietro@avengers.com");
    person.setTimezone("Eastern");
    person.setDescription("Avengers");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Wanda");
    person.setLastName("Maximoff");
    person.setEmail("wanda@avengers.com");
    person.setTimezone("Eastern");
    person.setDescription("Avengers");
    person.setGender("Female");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Natasha");
    person.setLastName("Romanov");
    person.setEmail("natasha@avengers.com");
    person.setTimezone("Eastern");
    person.setDescription("Avengers");
    person.setGender("Female");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Monica");
    person.setLastName("Rambeau");
    person.setEmail("monica@avengers.com");
    person.setTimezone("Eastern");
    person.setDescription("Avengers");
    person.setGender("Female");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Frank");
    person.setLastName("Castle");
    person.setEmail("frank@defenders.com");
    person.setTimezone("Pacific");
    person.setDescription("Defenders");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Luke");
    person.setLastName("Cage");
    person.setEmail("luke@defenders.com");
    person.setTimezone("Pacific");
    person.setDescription("Defenders");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Matt");
    person.setLastName("Murdock");
    person.setEmail("matt@defenders.com");
    person.setTimezone("Pacific");
    person.setDescription("Defenders");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Daniel");
    person.setLastName("Rand");
    person.setEmail("daniel@defenders.com");
    person.setTimezone("Pacific");
    person.setDescription("Defenders");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Stephen");
    person.setLastName("Strange");
    person.setEmail("stephen@defenders.com");
    person.setTimezone("Pacific");
    person.setDescription("Defenders");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Marc");
    person.setLastName("Spector");
    person.setEmail("marc@defenders.com");
    person.setTimezone("Pacific");
    person.setDescription("Defenders");
    person.setGender("Male");
    result.add(person);

    person = new H2ExampleUser();
    person.setFirstName("Jessica");
    person.setLastName("Jones");
    person.setEmail("jessica@defenders.com");
    person.setTimezone("Pacific");
    person.setDescription("Defenders");
    person.setGender("Female");
    result.add(person);

    return result;
  }
}
