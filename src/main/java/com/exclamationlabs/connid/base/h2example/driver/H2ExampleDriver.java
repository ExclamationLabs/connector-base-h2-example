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

import com.exclamationlabs.connid.base.connector.authenticator.Authenticator;
import com.exclamationlabs.connid.base.connector.configuration.BaseConnectorConfiguration;
import com.exclamationlabs.connid.base.connector.configuration.ConnectorProperty;
import com.exclamationlabs.connid.base.connector.driver.Driver;
import com.exclamationlabs.connid.base.h2example.model.H2ExampleGroup;
import com.exclamationlabs.connid.base.h2example.model.H2ExampleUser;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

import java.sql.*;
import java.util.*;

/**
 * This is an example Base connector driver using a just-in-time H2 in-memory database
 * for Identity Access Management.
 */
public class H2ExampleDriver implements Driver<H2ExampleUser, H2ExampleGroup> {

    private static final Log LOG = Log.getLog(H2ExampleDriver.class);

    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:mem:h2ExampleDB;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASS = "";

    private Connection connection;

    @Override
    public Set<ConnectorProperty> getRequiredPropertyNames() {
        return null;
    }

    @Override
    public void initialize(BaseConnectorConfiguration baseConnectorConfiguration, Authenticator authenticator) throws ConnectorException {

        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

            Statement stmt = connection.createStatement();
            String sql = "DROP TABLE IF EXISTS DEMO_USERS";
            stmt.executeUpdate(sql);

            sql = "DROP TABLE IF EXISTS DEMO_GROUPS";
            stmt.executeUpdate(sql);

            sql = "DROP TABLE IF EXISTS DEMO_USERS_XREF";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE DEMO_USERS (id INTEGER NOT NULL, " +
                    "first_name VARCHAR(255), last_name VARCHAR(255), " +
                    "timezone VARCHAR(255), user_description VARCHAR(255), " +
                    "email VARCHAR(255), PRIMARY KEY (id))";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE DEMO_GROUPS (ID INTEGER NOT NULL, NAME VARCHAR(255), " +
                    "DESCRIPTION VARCHAR(255), PRIMARY KEY (id))";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE DEMO_USERS_XREF (user_id INTEGER NOT NULL, " +
                    "group_id INTEGER NOT NULL, PRIMARY KEY (user_id, group_id))";
            stmt.executeUpdate(sql);
            stmt.close();

            createInitialUsers();
            createInitialGroups();
        } catch (ClassNotFoundException | SQLException e) {
            LOG.error("Error creating connection", e);
            throw new ConnectorException(e);
        }
    }

    @Override
    public void test() throws ConnectorException {
        try {
            Statement stmt = connection.createStatement();
            String sql = "SELECT 1 AS test FROM dual";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                LOG.info("Health data {0}", rs.getInt("test"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String createUser(H2ExampleUser h2ExampleUser) throws ConnectorException {
        int newId = new Random().nextInt();
        try {
            String sql = "INSERT INTO DEMO_USERS (id, first_name, last_name, email, " +
                    "user_description, timezone) VALUES (?,?,?,?,?,?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
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
    public String createGroup(H2ExampleGroup h2ExampleGroup) throws ConnectorException {
        int newId = new Random().nextInt();
        try {
            String sql = "INSERT INTO DEMO_GROUPS (ID, NAME, DESCRIPTION) VALUES (?,?,?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, newId);
            stmt.setString(2, h2ExampleGroup.getName());
            stmt.setString(3, h2ExampleGroup.getDescription());
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }
        return "" + newId;
    }

    @Override
    public void updateUser(String userId, H2ExampleUser h2ExampleUser) throws ConnectorException {
        try {
            String sql = "UPDATE DEMO_USERS SET user_description = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
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
    public void updateGroup(String groupId, H2ExampleGroup h2ExampleGroup) throws ConnectorException {
        if (h2ExampleGroup.getDescription() == null) {
            return;
        }
        try {
            String sql = "UPDATE DEMO_GROUPS SET DESCRIPTION = ? WHERE ID = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, h2ExampleGroup.getDescription());
            stmt.setInt(2, Integer.parseInt(groupId));
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }
    }

    @Override
    public void deleteUser(String userId) throws ConnectorException {
        try {
            String sql = "DELETE FROM DEMO_USERS WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(userId));
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }
    }

    @Override
    public void deleteGroup(String groupId) throws ConnectorException {
        try {
            String sql = "DELETE FROM DEMO_GROUPS WHERE ID = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(groupId));
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }
    }

    @Override
    public List<H2ExampleUser> getUsers() throws ConnectorException {
        List<H2ExampleUser> users = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
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
    public List<H2ExampleGroup> getGroups() throws ConnectorException {
        List<H2ExampleGroup> groups = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM DEMO_GROUPS ORDER BY NAME ASC";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                H2ExampleGroup group = new H2ExampleGroup();
                group.setId(rs.getString("ID"));
                group.setName(rs.getString("NAME"));
                group.setDescription(rs.getString("DESCRIPTION"));
                groups.add(group);
            }
            rs.close();
            stmt.close();
        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }
        return groups;
    }

    @Override
    public H2ExampleUser getUser(String userId) throws ConnectorException {
        H2ExampleUser user = null;
        try {
            String sql = "SELECT * FROM DEMO_USERS WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
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
            PreparedStatement stmt = connection.prepareStatement(sql);
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

    @Override
    public H2ExampleGroup getGroup(String groupId) throws ConnectorException {
        H2ExampleGroup group = null;
        try {
            String sql = "SELECT * FROM DEMO_GROUPS WHERE ID = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(groupId));
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                group = new H2ExampleGroup();
                group.setId(rs.getString("ID"));
                group.setName(rs.getString("NAME"));
                group.setDescription(rs.getString("DESCRIPTION"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }
        return group;
    }

    @Override
    public void addGroupToUser(String groupId, String userId) throws ConnectorException {
        try {
            String sql = "INSERT INTO DEMO_USERS_XREF (group_id, user_id) VALUES (?,?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(groupId));
            stmt.setInt(2, Integer.parseInt(userId));
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }
    }

    @Override
    public void removeGroupFromUser(String groupId, String userId) throws ConnectorException {
        try {
            String sql = "DELETE FROM DEMO_USERS_XREF WHERE group_id = ? AND user_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(groupId));
            stmt.setInt(2, Integer.parseInt(userId));
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }
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

    private void createInitialUsers() {
        H2ExampleUser person = new H2ExampleUser();
        person.setFirstName("Peter");
        person.setLastName("Rasputin");
        person.setEmail("peter@xmen.com");
        person.setTimezone("Central");
        person.setDescription("X-Man");
        createUser(person);

        person = new H2ExampleUser();
        person.setFirstName("Scott");
        person.setLastName("Summers");
        person.setEmail("scott@xmen.com");
        person.setTimezone("Central");
        person.setDescription("X-Man");
        createUser(person);

        for (int xx=1; xx <= 80; xx++) {
            createMultipleMan(xx);
        }
    }

    private void createMultipleMan(int index) {
        H2ExampleUser person = new H2ExampleUser();
        person.setFirstName("James");
        person.setLastName("Madrox-" + index);
        person.setEmail("madrox-" + index + "@newmutants.com");
        person.setTimezone("Central");
        person.setDescription("New Mutant");
        createUser(person);
    }

    private void createInitialGroups() {
        H2ExampleGroup group1 = new H2ExampleGroup();
        group1.setName("New Mutants");
        group1.setDescription("Mutant Team 1");
        createGroup(group1);

        H2ExampleGroup group2 = new H2ExampleGroup();
        group2.setName("X-Factor");
        group2.setDescription("Mutant Team 2");
        createGroup(group2);
    }
}
