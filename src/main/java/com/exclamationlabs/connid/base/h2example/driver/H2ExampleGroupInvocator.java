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
import com.exclamationlabs.connid.base.h2example.model.H2ExampleGroup;
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

public class H2ExampleGroupInvocator implements DriverInvocator<H2ExampleDriver, H2ExampleGroup> {

    private static final Log LOG = Log.getLog(H2ExampleGroupInvocator.class);

    @Override
    public String create(H2ExampleDriver h2Driver, H2ExampleGroup h2ExampleGroup, Map<String, List<String>> map) throws ConnectorException {
        int newId = new Random().nextInt();
        try {
            String sql = "INSERT INTO DEMO_GROUPS (ID, NAME, DESCRIPTION) VALUES (?,?,?)";
            PreparedStatement stmt = h2Driver.getConnection().prepareStatement(sql);
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
    public void update(H2ExampleDriver h2Driver, String groupId, H2ExampleGroup h2ExampleGroup, Map<String, List<String>> map) throws ConnectorException {
        if (h2ExampleGroup.getDescription() == null) {
            return;
        }
        try {
            String sql = "UPDATE DEMO_GROUPS SET DESCRIPTION = ? WHERE ID = ?";
            PreparedStatement stmt = h2Driver.getConnection().prepareStatement(sql);
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
    public void delete(H2ExampleDriver h2Driver, String groupId) throws ConnectorException {
        try {
            String sql = "DELETE FROM DEMO_GROUPS WHERE ID = ?";
            PreparedStatement stmt = h2Driver.getConnection().prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(groupId));
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }
    }

    @Override
    public List<H2ExampleGroup> getAll(H2ExampleDriver h2Driver) throws ConnectorException {
        List<H2ExampleGroup> groups = new ArrayList<>();
        try {
            Statement stmt = h2Driver.getConnection().createStatement();
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
    public H2ExampleGroup getOne(H2ExampleDriver h2Driver, String groupId) throws ConnectorException {
        H2ExampleGroup group = null;
        try {
            String sql = "SELECT * FROM DEMO_GROUPS WHERE ID = ?";
            PreparedStatement stmt = h2Driver.getConnection().prepareStatement(sql);
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

    void createInitialGroups(H2ExampleDriver driver) {
        H2ExampleGroup group1 = new H2ExampleGroup();
        group1.setName("New Mutants");
        group1.setDescription("Mutant Team 1");
        create(driver, group1, null);

        H2ExampleGroup group2 = new H2ExampleGroup();
        group2.setName("X-Factor");
        group2.setDescription("Mutant Team 2");
        create(driver, group2, null);
    }
}
