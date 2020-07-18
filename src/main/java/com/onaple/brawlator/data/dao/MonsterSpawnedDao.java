package com.onaple.brawlator.data.dao;

import com.onaple.brawlator.Brawlator;
import com.onaple.brawlator.data.beans.MonsterSpawnedBean;
import com.onaple.brawlator.data.handlers.DatabaseHandler;

import javax.naming.ServiceUnavailableException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MonsterSpawnedDao {
    private static String errorDatabasePrefix = "Error while connecting to database : ";

    /**
     * Generate database tables if they do not exist
     */
    public static void createTableIfNotExist() {
        String query = "CREATE TABLE IF NOT EXISTS monsterSpawned (spawnerId INTEGER, uuid VARCHAR(50), worldName VARCHAR(50))";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DatabaseHandler.getDatasource().getConnection();
            statement = connection.prepareStatement(query);
            statement.execute();
            statement.close();
        } catch (ServiceUnavailableException e) {
            Brawlator.getLogger().error(errorDatabasePrefix.concat(e.getMessage()));
        } catch (SQLException e) {
            Brawlator.getLogger().error("Error while creating spawned monster table : " + e.getMessage());
        } finally {
            closeConnection(connection, statement, null);
        }
    }

    /**
     * Clear the table
     */
    public static void clearTable() {
        String query = "DELETE FROM monsterSpawned;";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DatabaseHandler.getDatasource().getConnection();
            statement = connection.prepareStatement(query);
            statement.execute();
            statement.close();
        } catch (ServiceUnavailableException e) {
            Brawlator.getLogger().error(errorDatabasePrefix.concat(e.getMessage()));
        } catch (SQLException e) {
            Brawlator.getLogger().error("Error while cleaning spawned monster table : " + e.getMessage());
        } finally {
            closeConnection(connection, statement, null);
        }
    }

    /**
     * Add a monster spawned into database
     * @param monsterSpawned Monster spawned
     */
    public static void addMonsterSpawned(MonsterSpawnedBean monsterSpawned) {
        String query = "INSERT INTO monsterSpawned (spawnerId, uuid, worldName) VALUES (?, ?, ?)";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DatabaseHandler.getDatasource().getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, monsterSpawned.getSpawnerId());
            statement.setString(2, monsterSpawned.getUuid().toString());
            statement.setString(3, monsterSpawned.getWorldName().toString());
            statement.execute();
            statement.close();
        } catch (ServiceUnavailableException e) {
            Brawlator.getLogger().error(errorDatabasePrefix.concat(e.getMessage()));
        } catch (SQLException e) {
            Brawlator.getLogger().error("Error while inserting spawned monster : " + e.getMessage());
        } finally {
            closeConnection(connection, statement, null);
        }
    }

    public static List<MonsterSpawnedBean> getMonstersSpawned() {
        String query = "SELECT spawnerId, uuid, worldName FROM monsterSpawned";
        List<MonsterSpawnedBean> monsters = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet results = null;
        try {
            connection = DatabaseHandler.getDatasource().getConnection();
            statement = connection.prepareStatement(query);
            results = statement.executeQuery();
            while (results.next()) {
                monsters.add(new MonsterSpawnedBean(results.getInt("spawnerId"), 
                UUID.fromString(results.getString("uuid")), results.getString("worldName")));
            }
            statement.close();
        } catch (ServiceUnavailableException e) {
            Brawlator.getLogger().error(errorDatabasePrefix.concat(e.getMessage()));
        } catch (SQLException e) {
            Brawlator.getLogger().error("Error while fetching monster spawned for spawner : " + e.getMessage());
        } finally {
            closeConnection(connection, statement, results);
        }
        return monsters;
    }

    public static List<MonsterSpawnedBean> getMonstersBySpawner(int spawnerId) {
        String query = "SELECT spawnerId, uuid, worldName FROM monsterSpawned WHERE spawnerId = ?";
        List<MonsterSpawnedBean> monsters = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet results = null;
        try {
            connection = DatabaseHandler.getDatasource().getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, spawnerId);
            results = statement.executeQuery();
            while (results.next()) {
                monsters.add(new MonsterSpawnedBean(results.getInt("spawnerId"), 
                    UUID.fromString(results.getString("uuid")), results.getString("worldName")));
            }
            statement.close();
        } catch (ServiceUnavailableException e) {
            Brawlator.getLogger().error(errorDatabasePrefix.concat(e.getMessage()));
        } catch (SQLException e) {
            Brawlator.getLogger().error("Error while fetching monster spawned for spawner : " + e.getMessage());
        } finally {
            closeConnection(connection, statement, results);
        }
        return monsters;
    }

    public static void deleteMonsterByUuid(String uuid) {
        String query = "DELETE FROM monsterSpawned WHERE uuid = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DatabaseHandler.getDatasource().getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            statement.execute();
            statement.close();
        } catch (ServiceUnavailableException e) {
            Brawlator.getLogger().error(errorDatabasePrefix.concat(e.getMessage()));
        } catch (SQLException e) {
            Brawlator.getLogger().error("Error while deleting monsters spawned : " + e.getMessage());
        } finally {
            closeConnection(connection, statement, null);
        }
    }

    /**
     * Close a database connection
     * @param connection Connection to close
     */
    private static void closeConnection(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                Brawlator.getLogger().error("Error while closing result set : " + e.getMessage());
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                Brawlator.getLogger().error("Error while closing statement : " + e.getMessage());
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                Brawlator.getLogger().error("Error while closing connection : " + e.getMessage());
            }
        }
    }
}
