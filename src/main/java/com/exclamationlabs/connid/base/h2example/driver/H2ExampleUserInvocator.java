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

import com.exclamationlabs.connid.base.connector.driver.Driver;
import com.exclamationlabs.connid.base.connector.driver.DriverInvocator;
import com.exclamationlabs.connid.base.h2example.model.H2ExampleUser;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class H2ExampleUserInvocator implements DriverInvocator<H2ExampleDriver, H2ExampleUser> {

    private static final Log LOG = Log.getLog(H2ExampleUserInvocator.class);

    @Override
    public String create(H2ExampleDriver driver, H2ExampleUser h2ExampleUser, Map<String, List<String>> map) throws ConnectorException {
        H2ExampleDriver h2Driver = (H2ExampleDriver) driver;

        int newId = new Random().nextInt();
        try {
            String sql = "INSERT INTO DEMO_USERS (id, first_name, last_name, email, " +
                    "user_description, timezone) VALUES (?,?,?,?,?,?)";
            PreparedStatement stmt = h2Driver.getConnection().prepareStatement(sql);
            stmt.setInt(1, newId);
            stmt.setString(2, h2ExampleUser.getFirstName());
            stmt.setString(3, h2ExampleUser.getLastName());
            stmt.setString(4, h2ExampleUser.getEmail());
            stmt.setString(5, h2ExampleUser.getDescription());
            stmt.setString(6, h2ExampleUser.getTimezone());
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }
        return "" + newId;
    }

    @Override
    public void update(H2ExampleDriver driver, String userId, H2ExampleUser h2ExampleUser, Map<String, List<String>> map) throws ConnectorException {
        H2ExampleDriver h2Driver = (H2ExampleDriver) driver;
        try {
            String sql = "UPDATE DEMO_USERS SET user_description = ? WHERE id = ?";
            PreparedStatement stmt = h2Driver.getConnection().prepareStatement(sql);
            stmt.setString(1, h2ExampleUser.getDescription());
            stmt.setInt(2, Integer.parseInt(userId));
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }
    }

    @Override
    public void delete(H2ExampleDriver driver, String userId) throws ConnectorException {
        H2ExampleDriver h2Driver = (H2ExampleDriver) driver;
        try {
            String sql = "DELETE FROM DEMO_USERS WHERE id = ?";
            PreparedStatement stmt = h2Driver.getConnection().prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(userId));
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }
    }

    @Override
    public List<H2ExampleUser> getAll(H2ExampleDriver driver) throws ConnectorException {
        H2ExampleDriver h2Driver = (H2ExampleDriver) driver;
        List<H2ExampleUser> users = new ArrayList<>();
        try {
            Statement stmt = h2Driver.getConnection().createStatement();
            String sql = "SELECT * FROM DEMO_USERS ORDER BY email ASC";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                H2ExampleUser user = loadUserFromResultSet(rs);
                users.add(user);
            }
            rs.close();
            stmt.close();
        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }
        return users;
    }

    @Override
    public H2ExampleUser getOne(H2ExampleDriver driver, String userId) throws ConnectorException {
        H2ExampleDriver h2Driver = (H2ExampleDriver) driver;
        H2ExampleUser user = null;
        try {
            String sql = "SELECT * FROM DEMO_USERS WHERE id = ?";
            PreparedStatement stmt = h2Driver.getConnection().prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(userId));

            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                user = loadUserFromResultSet(rs);
            }
            rs.close();
            stmt.close();
        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }

        // Load groups for user
        try {
            List<String> groupIds = new ArrayList<>();
            String sql = "SELECT group_id FROM DEMO_USERS_XREF WHERE user_id = ?";
            PreparedStatement stmt = h2Driver.getConnection().prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(userId));

            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                groupIds.add(rs.getString("group_id"));
            }
            if (!groupIds.isEmpty()) {
                user.setGroupIds(groupIds);
            }
            rs.close();
            stmt.close();
        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }

        return user;
    }

    void createInitialUsers(H2ExampleDriver driver) {
        H2ExampleUser person = new H2ExampleUser();
        person.setFirstName("Peter");
        person.setLastName("Rasputin");
        person.setEmail("peter@xmen.com");
        person.setTimezone("Central");
        person.setDescription("X-Man");
        create(driver, person, null);

        person = new H2ExampleUser();
        person.setFirstName("Scott");
        person.setLastName("Summers");
        person.setEmail("scott@xmen.com");
        person.setTimezone("Central");
        person.setDescription("X-Man");
        create(driver, person, null);
    }

    private H2ExampleUser loadUserFromResultSet(ResultSet rs) throws SQLException {
        H2ExampleUser user = new H2ExampleUser();
        user.setId(rs.getString("id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setDescription(rs.getString("user_description"));
        user.setTimezone(rs.getString("timezone"));
        user.setEmail(rs.getString("email"));
        return user;
    }

}
