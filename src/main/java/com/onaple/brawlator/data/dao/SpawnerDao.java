package com.onaple.brawlator.data.dao;

import com.flowpowered.math.vector.Vector3i;
import com.onaple.brawlator.Brawlator;
import com.onaple.brawlator.data.beans.SpawnerBean;
import com.onaple.brawlator.data.handlers.ConfigurationHandler;
import com.onaple.brawlator.data.handlers.DatabaseHandler;
import com.onaple.brawlator.utils.SpawnerBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.naming.ServiceUnavailableException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class SpawnerDao {
    private static final String ERROR_DATABASE_PREFIX = "Error while connecting to database : ";

    public SpawnerDao() {
    }

    @Inject
    private SpawnerBuilder spawnerBuilder;

    /**
     * Generate database tables if they do not exist
     */
    public void createTableIfNotExist() {
        String query = "CREATE TABLE IF NOT EXISTS spawner (id INTEGER PRIMARY KEY, x INT, y INT, z INT, worldName VARCHAR(50), spawnerTypeName VARCHAR(50), monsterName VARCHAR(50))";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DatabaseHandler.getDatasource().getConnection();
            statement = connection.prepareStatement(query);
            statement.execute();
            statement.close();
        } catch (ServiceUnavailableException e) {
            Brawlator.getLogger().error(ERROR_DATABASE_PREFIX.concat(e.getMessage()));
        } catch (SQLException e) {
            Brawlator.getLogger().error("Error while creating spawners table : " + e.getMessage());
        } finally {
            closeConnection(connection, statement, null);
        }
    }

    /**
     * Fetch database to query every spawners
     * @return List of spawners
     */
    public List<SpawnerBean> getSpawners() {
        String query = "SELECT id, x, y, z, worldName, spawnerTypeName, monsterName FROM spawner";
        List<SpawnerBean> spawners = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet results = null;
        try {
            connection = DatabaseHandler.getDatasource().getConnection();
            statement = connection.prepareStatement(query);
            results = statement.executeQuery();
            while (results.next()) {
                spawners.add(spawnerBuilder.buildSpawner(results.getInt("id"),
                        new Vector3i(results.getInt("x"), results.getInt("y"),
                                results.getInt("z")), results.getString("worldName"),
                        results.getString("spawnerTypeName"), results.getString("monsterName")));
            }
            statement.close();
        } catch (ServiceUnavailableException e) {
            Brawlator.getLogger().error(ERROR_DATABASE_PREFIX.concat(e.getMessage()));
        } catch (SQLException e) {
            Brawlator.getLogger().error("Error while fetching spawners : " + e.getMessage());
        } finally {
            closeConnection(connection, statement, results);
        }
        return spawners;
    }

    public List<SpawnerBean> getSpawnersAround(Vector3i position) {
        String query = "SELECT id, x, y, z, worldName, spawnerTypeName, monsterName FROM spawner WHERE x > ? AND x < ? AND y > ? AND y < ? AND z > ? AND z < ?";
        List<SpawnerBean> spawners = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet results = null;
        try {
            connection = DatabaseHandler.getDatasource().getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, position.getX() - 2);
            statement.setInt(2, position.getX() + 2);
            statement.setInt(3, position.getY() - 2);
            statement.setInt(4, position.getY() + 2);
            statement.setInt(5, position.getZ() - 2);
            statement.setInt(6, position.getZ() + 2);
            results = statement.executeQuery();
            while (results.next()) {
                spawners.add(spawnerBuilder.buildSpawner(results.getInt("id"),
                        new Vector3i(results.getInt("x"), results.getInt("y"),
                                results.getInt("z")), results.getString("worldName"),
                        results.getString("spawnerTypeName"), results.getString("monsterName")));
            }
            statement.close();
        } catch (ServiceUnavailableException e) {
            Brawlator.getLogger().error(ERROR_DATABASE_PREFIX.concat(e.getMessage()));
        } catch (SQLException e) {
            Brawlator.getLogger().error("Error while fetching spawners around position : " + e.getMessage());
        } finally {
            closeConnection(connection, statement, results);
        }
        return spawners;
    }

    /**
     * Add a spawner into database
     * @param spawner Spawner to respawn later
     */
    public void addSpawner(SpawnerBean spawner) {
        String query = "INSERT INTO spawner (x, y, z, worldName, spawnerTypeName, monsterName) VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DatabaseHandler.getDatasource().getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, spawner.getPosition().getX());
            statement.setInt(2, spawner.getPosition().getY());
            statement.setInt(3, spawner.getPosition().getZ());
            statement.setString(4, spawner.getWorldName());
            statement.setString(5, spawner.getSpawnerTypeName());
            statement.setString(6, spawner.getMonsterName());
            statement.execute();
            statement.close();
        } catch (ServiceUnavailableException e) {
            Brawlator.getLogger().error(ERROR_DATABASE_PREFIX.concat(e.getMessage()));
        } catch (SQLException e) {
            Brawlator.getLogger().error("Error while inserting spawner : " + e.getMessage());
        } finally {
            closeConnection(connection, statement, null);
        }
    }

    /**
     * Delete a list of spawners from database
     * @param spawners List of spawners to remove
     */
    public void deleteSpawners(List<SpawnerBean> spawners) {
        String query = "DELETE FROM spawner WHERE id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DatabaseHandler.getDatasource().getConnection();
            for (SpawnerBean spawner : spawners) {
                statement = connection.prepareStatement(query);
                statement.setInt(1, spawner.getId());
                statement.execute();
                statement.close();
            }
        } catch (ServiceUnavailableException e) {
            Brawlator.getLogger().error(ERROR_DATABASE_PREFIX.concat(e.getMessage()));
        } catch (SQLException e) {
            Brawlator.getLogger().error("Error while deleting spawners : " + e.getMessage());
        } finally {
            closeConnection(connection, statement, null);
        }
    }

    /**
     * Close a database connection
     * @param connection Connection to close
     */
    private void closeConnection(Connection connection, PreparedStatement statement, ResultSet resultSet) {
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
