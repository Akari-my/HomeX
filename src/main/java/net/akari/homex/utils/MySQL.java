package net.akari.homex.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.akari.homex.HomeX;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQL {

    private final HomeX plugin;

    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    private HikariDataSource hikari;

    public MySQL(HomeX plugin) {
        this.plugin = plugin;
        this.host = plugin.getConfig().getString("Database.hostname");
        this.port = plugin.getConfig().getInt("Database.port");
        this.database = plugin.getConfig().getString("Database.database");
        this.username = plugin.getConfig().getString("Database.user");
        this.password = plugin.getConfig().getString("Database.password");
    }

    public void connect() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        config.setUsername(username);
        config.setPassword(password);

        hikari = new HikariDataSource(config);

        createTables();
    }

    public void disconnect() {
        if (isConnected()) {
            hikari.close();
        }
    }

    private void createTables() {
        try (Connection connection = hikari.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS homes (player_uuid VARCHAR(40), home_name VARCHAR(50), world VARCHAR(50), x DOUBLE, y DOUBLE, z DOUBLE, pitch FLOAT, yaw FLOAT)");
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return hikari != null && !hikari.isClosed();
    }

    public HikariDataSource getHikari() {
        return hikari;
    }
}
