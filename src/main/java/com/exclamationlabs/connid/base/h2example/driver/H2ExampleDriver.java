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
import com.exclamationlabs.connid.base.connector.driver.BaseDriver;
import com.exclamationlabs.connid.base.h2example.model.H2ExampleGroup;
import com.exclamationlabs.connid.base.h2example.model.H2ExamplePower;
import com.exclamationlabs.connid.base.h2example.model.H2ExampleUser;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

import java.sql.*;
import java.util.*;

/**
 * This is an example Base connector driver using a just-in-time H2 in-memory database
 * for Identity Access Management.
 */
public class H2ExampleDriver extends BaseDriver {

    private static final Log LOG = Log.getLog(H2ExampleDriver.class);

    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:mem:h2ExampleDB;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASS = "";

    private Connection connection;

    public H2ExampleDriver() {
        addInvocator(H2ExampleUser.class, new H2ExampleUserInvocator());
        addInvocator(H2ExampleGroup.class, new H2ExampleGroupInvocator());
        addInvocator(H2ExamplePower.class, new H2ExamplePowerInvocator());
    }

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

            sql = "DROP TABLE IF EXISTS DEMO_POWERS";
            stmt.executeUpdate(sql);

            sql = "DROP TABLE IF EXISTS DEMO_USERS_XREF";
            stmt.executeUpdate(sql);

            sql = "DROP TABLE IF EXISTS DEMO_USERS_POWERS_XREF";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE DEMO_USERS (id INTEGER NOT NULL, " +
                    "first_name VARCHAR(255), last_name VARCHAR(255), " +
                    "timezone VARCHAR(255), user_description VARCHAR(255), " +
                    "email VARCHAR(255), PRIMARY KEY (id))";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE DEMO_GROUPS (ID INTEGER NOT NULL, NAME VARCHAR(255), " +
                    "DESCRIPTION VARCHAR(255), PRIMARY KEY (id))";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE DEMO_POWERS (ID INTEGER NOT NULL, NAME VARCHAR(255), " +
                    "DESCRIPTION VARCHAR(255), PRIMARY KEY (id))";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE DEMO_USERS_XREF (user_id INTEGER NOT NULL, " +
                    "group_id INTEGER NOT NULL, PRIMARY KEY (user_id, group_id))";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE DEMO_USERS_POWERS_XREF (user_id INTEGER NOT NULL, " +
                    "power_id INTEGER NOT NULL, PRIMARY KEY (user_id, power_id))";
            stmt.executeUpdate(sql);
            stmt.close();

            H2ExampleUserInvocator userInvocator = (H2ExampleUserInvocator)
                    getInvocator(H2ExampleUser.class);
            userInvocator.createInitialUsers(this);

            H2ExampleGroupInvocator groupInvocator = (H2ExampleGroupInvocator)
                    getInvocator(H2ExampleGroup.class);
            groupInvocator.createInitialGroups(this);

            H2ExamplePowerInvocator powerInvocator = (H2ExamplePowerInvocator)
                    getInvocator(H2ExamplePower.class);
            powerInvocator.createInitialPowers(this);
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

    Connection getConnection() {
        return connection;
    }
}
