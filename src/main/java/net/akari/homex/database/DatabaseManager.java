package net.akari.homex.database;

import net.akari.homex.HomeX;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static HomeX plugin;

    public DatabaseManager(HomeX plugin) {
        DatabaseManager.plugin = plugin;
    }

    public static void setHome(Player player, String homeName) {
        try (Connection connection = plugin.getMySQL().getHikari().getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO homes (player_uuid, home_name, world, x, y, z, pitch, yaw) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, homeName);
            statement.setString(3, player.getLocation().getWorld().getName());
            statement.setDouble(4, player.getLocation().getX());
            statement.setDouble(5, player.getLocation().getY());
            statement.setDouble(6, player.getLocation().getZ());
            statement.setFloat(7, player.getLocation().getPitch());
            statement.setFloat(8, player.getLocation().getYaw());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteHome(Player player, String homeName) {
        try (Connection connection = plugin.getMySQL().getHikari().getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM homes WHERE player_uuid = ? AND home_name = ?")) {
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, homeName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void teleportToHome(Player player, String homeName) {
        try (Connection connection = plugin.getMySQL().getHikari().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM homes WHERE player_uuid = ? AND home_name = ?")) {
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, homeName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String worldName = resultSet.getString("world");
                double x = resultSet.getDouble("x");
                double y = resultSet.getDouble("y");
                double z = resultSet.getDouble("z");
                float pitch = resultSet.getFloat("pitch");
                float yaw = resultSet.getFloat("yaw");

                Location homeLocation = new Location(player.getServer().getWorld(worldName), x, y, z, yaw, pitch);
                player.teleport(homeLocation);
                String successMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.success.teleportedToHome").replace("%home%", homeName));
                player.sendMessage(successMessage);
            } else {
                String errorMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.error.homeNotFound").replace("%home%", homeName));
                player.sendMessage(errorMessage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean homeExists(Player player, String homeName) {
        try (Connection connection = plugin.getMySQL().getHikari().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM homes WHERE player_uuid = ? AND home_name = ?")) {
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, homeName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getHomeCount(Player player) {
        try (Connection connection = plugin.getMySQL().getHikari().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM homes WHERE player_uuid = ?")) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static List<String> getHomes(Player player) {
        List<String> homeList = new ArrayList<>();
        try (Connection connection = plugin.getMySQL().getHikari().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT home_name FROM homes WHERE player_uuid = ?")) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                homeList.add(resultSet.getString("home_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return homeList;
    }
}