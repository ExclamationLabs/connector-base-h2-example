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
import java.util.Random;

public class H2ExampleUserInvocator implements DriverInvocator<H2ExampleDriver, H2ExampleUser> {

    private static final Log LOG = Log.getLog(H2ExampleUserInvocator.class);

    @Override
    public String create(H2ExampleDriver h2Driver, H2ExampleUser h2ExampleUser)
            throws ConnectorException {
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

        String newIdString = "" + newId;
        updateGroups(h2Driver, h2ExampleUser.getGroupIds(), newIdString, false);
        updatePowers(h2Driver, h2ExampleUser.getPowerIds(), newIdString, false);

        return newIdString;
    }

    @Override
    public void update(H2ExampleDriver h2Driver, String userId, H2ExampleUser h2ExampleUser)
            throws ConnectorException {
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

        updateGroups(h2Driver, h2ExampleUser.getGroupIds(), userId, true);
        updatePowers(h2Driver, h2ExampleUser.getPowerIds(), userId, true);
    }

    @Override
    public void delete(H2ExampleDriver h2Driver, String userId) throws ConnectorException {
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
    public List<H2ExampleUser> getAll(H2ExampleDriver h2Driver) throws ConnectorException {
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
    public H2ExampleUser getOne(H2ExampleDriver h2Driver, String userId) throws ConnectorException {
        H2ExampleUser user;
        try {
            String sql = "SELECT * FROM DEMO_USERS WHERE id = ?";
            PreparedStatement stmt = h2Driver.getConnection().prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(userId));

            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                user = loadUserFromResultSet(rs);
            } else {
                return null;
            }
            rs.close();
            stmt.close();

        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }

        // Load groups for user
        List<String> groupIds = loadGroups(h2Driver, userId);
        if (!groupIds.isEmpty()) {
            user.setGroupIds(groupIds);
        }

        // Load powers for user
        List<String> powerIds = loadPowers(h2Driver, userId);
        if (!groupIds.isEmpty()) {
            user.setPowerIds(powerIds);
        }

        return user;
    }

    void createInitialUsers(H2ExampleDriver h2Driver) {
        H2ExampleUser person = new H2ExampleUser();
        person.setFirstName("Peter");
        person.setLastName("Rasputin");
        person.setEmail("peter@xmen.com");
        person.setTimezone("Central");
        person.setDescription("X-Man");
        create(h2Driver, person);

        person = new H2ExampleUser();
        person.setFirstName("Scott");
        person.setLastName("Summers");
        person.setEmail("scott@xmen.com");
        person.setTimezone("Central");
        person.setDescription("X-Man");
        create(h2Driver, person);
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


    private List<String> loadGroups(H2ExampleDriver h2Driver, String userId) {
        List<String> groupIds = new ArrayList<>();
        try {
            String sql = "SELECT group_id FROM DEMO_USERS_XREF WHERE user_id = ?";
            PreparedStatement stmt = h2Driver.getConnection().prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(userId));

            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                groupIds.add(rs.getString("group_id"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }

        return groupIds;
    }

    private List<String> loadPowers(H2ExampleDriver h2Driver, String userId) {
        List<String> powerIds = new ArrayList<>();
        try {
            String sql = "SELECT power_id FROM DEMO_USERS_POWERS_XREF WHERE user_id = ?";
            PreparedStatement stmt = h2Driver.getConnection().prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(userId));

            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                powerIds.add(rs.getString("power_id"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }

        return powerIds;
    }


    private void updateGroups(H2ExampleDriver h2Driver, List<String> newGroupIds, String userId, boolean isUpdate) {
        List<String> currentGroups = new ArrayList<>();
        if (isUpdate) {
            currentGroups = loadGroups(h2Driver, userId);

            for (String groupId : currentGroups) {
                if (newGroupIds == null || !newGroupIds.contains(groupId)) {
                    deleteUserGroupAssociation(h2Driver, groupId, userId);
                }
            }
        }

        if (newGroupIds != null) {
            for (String groupId : newGroupIds) {
                if (!currentGroups.contains(groupId)) {
                    addUserGroupAssociation(h2Driver, groupId, userId);
                }
            }
        }
    }

    private void updatePowers(H2ExampleDriver h2Driver, List<String> newPowerIds, String userId, boolean isUpdate) {
        List<String> currentPowers = new ArrayList<>();
        if (isUpdate) {
            currentPowers = loadPowers(h2Driver, userId);

            for (String powerId : currentPowers) {
                if (newPowerIds == null || !newPowerIds.contains(powerId)) {
                    deleteUserPowerAssociation(h2Driver, powerId, userId);
                }
            }
        }

        if (newPowerIds != null) {
            for (String powerId : newPowerIds) {
                if (!currentPowers.contains(powerId)) {
                    addUserPowerAssociation(h2Driver, powerId, userId);
                }
            }
        }
    }

    private void addUserGroupAssociation(H2ExampleDriver h2Driver, String groupId, String userId) throws ConnectorException {
        try {
            String sql = "INSERT INTO DEMO_USERS_XREF (group_id, user_id) VALUES (?,?)";
            PreparedStatement stmt = h2Driver.getConnection().prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(groupId));
            stmt.setInt(2, Integer.parseInt(userId));
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }
    }

    private void addUserPowerAssociation(H2ExampleDriver h2Driver, String powerId, String userId) throws ConnectorException {
        try {
            String sql = "INSERT INTO DEMO_USERS_POWERS_XREF (power_id, user_id) VALUES (?,?)";
            PreparedStatement stmt = h2Driver.getConnection().prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(powerId));
            stmt.setInt(2, Integer.parseInt(userId));
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }
    }

    private void deleteUserGroupAssociation(H2ExampleDriver h2Driver, String groupId, String userId) throws ConnectorException {
        try {
            String sql = "DELETE FROM DEMO_USERS_XREF WHERE group_id = ? AND user_id = ?";
            PreparedStatement stmt = h2Driver.getConnection().prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(groupId));
            stmt.setInt(2, Integer.parseInt(userId));
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }
    }

    private void deleteUserPowerAssociation(H2ExampleDriver h2Driver, String powerId, String userId) throws ConnectorException {
        try {
            String sql = "DELETE FROM DEMO_USERS_POWERS_XREF WHERE power_id = ? AND user_id = ?";
            PreparedStatement stmt = h2Driver.getConnection().prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(powerId));
            stmt.setInt(2, Integer.parseInt(userId));
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }
    }

}
