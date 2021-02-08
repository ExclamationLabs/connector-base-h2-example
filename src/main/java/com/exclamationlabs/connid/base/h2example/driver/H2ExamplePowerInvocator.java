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
import com.exclamationlabs.connid.base.h2example.model.H2ExamplePower;
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

public class H2ExamplePowerInvocator implements DriverInvocator<H2ExampleDriver, H2ExamplePower> {

    private static final Log LOG = Log.getLog(H2ExamplePowerInvocator.class);

    @Override
    public String create(H2ExampleDriver h2Driver, H2ExamplePower h2ExamplePower, Map<String, List<String>> map) throws ConnectorException {
        int newId = new Random().nextInt();
        try {
            String sql = "INSERT INTO DEMO_POWERS (ID, NAME, DESCRIPTION) VALUES (?,?,?)";
            PreparedStatement stmt = h2Driver.getConnection().prepareStatement(sql);
            stmt.setInt(1, newId);
            stmt.setString(2, h2ExamplePower.getName());
            stmt.setString(3, h2ExamplePower.getDescription());
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }
        return "" + newId;
    }

    @Override
    public void update(H2ExampleDriver h2Driver, String powerId, H2ExamplePower power, Map<String, List<String>> map) throws ConnectorException {
        if (power.getDescription() == null) {
            return;
        }
        try {
            String sql = "UPDATE DEMO_POWERS SET DESCRIPTION = ? WHERE ID = ?";
            PreparedStatement stmt = h2Driver.getConnection().prepareStatement(sql);
            stmt.setString(1, power.getDescription());
            stmt.setInt(2, Integer.parseInt(powerId));
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }
    }

    @Override
    public void delete(H2ExampleDriver h2Driver, String powerId) throws ConnectorException {
        try {
            String sql = "DELETE FROM DEMO_POWERS WHERE ID = ?";
            PreparedStatement stmt = h2Driver.getConnection().prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(powerId));
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }
    }

    @Override
    public List<H2ExamplePower> getAll(H2ExampleDriver h2Driver) throws ConnectorException {
        List<H2ExamplePower> powers = new ArrayList<>();
        try {
            Statement stmt = h2Driver.getConnection().createStatement();
            String sql = "SELECT * FROM DEMO_POWERS ORDER BY NAME ASC";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                H2ExamplePower power = new H2ExamplePower();
                power.setId(rs.getString("ID"));
                power.setName(rs.getString("NAME"));
                power.setDescription(rs.getString("DESCRIPTION"));
                powers.add(power);
            }
            rs.close();
            stmt.close();
        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }
        return powers;
    }

    @Override
    public H2ExamplePower getOne(H2ExampleDriver h2Driver, String powerId) throws ConnectorException {
        H2ExamplePower power = null;
        try {
            String sql = "SELECT * FROM DEMO_POWERS WHERE ID = ?";
            PreparedStatement stmt = h2Driver.getConnection().prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(powerId));
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                power = new H2ExamplePower();
                power.setId(rs.getString("ID"));
                power.setName(rs.getString("NAME"));
                power.setDescription(rs.getString("DESCRIPTION"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException sqlE) {
            LOG.error("Error running statement", sqlE);
            throw new ConnectorException(sqlE);
        }
        return power;
    }

    void createInitialPowers(H2ExampleDriver driver) {
        H2ExamplePower power1 = new H2ExamplePower();
        power1.setName("Flight");
        power1.setDescription("Ability to fly");
        create(driver, power1, null);

        H2ExamplePower power2 = new H2ExamplePower();
        power2.setName("Invisibility");
        power2.setDescription("Ability to turn invisible");
        create(driver, power2, null);
    }
}
