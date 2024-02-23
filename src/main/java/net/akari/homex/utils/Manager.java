package net.akari.homex.utils;

import net.akari.homex.HomeX;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Manager {

    private static HomeX plugin = null;
    private static FileConfiguration homesConfig;
    private static File homesFile = null;

    public Manager(HomeX plugin, FileConfiguration homesConfig, File homesFile) {
        Manager.plugin = plugin;
        Manager.homesConfig = homesConfig;
        Manager.homesFile = homesFile;
    }

    public static void setHome(Player player, String homeName) {
        homesConfig.set("homes." + player.getUniqueId() + "." + homeName + ".world", Objects.requireNonNull(player.getLocation().getWorld()).getName());
        homesConfig.set("homes." + player.getUniqueId() + "." + homeName + ".x", player.getLocation().getX());
        homesConfig.set("homes." + player.getUniqueId() + "." + homeName + ".y", player.getLocation().getY());
        homesConfig.set("homes." + player.getUniqueId() + "." + homeName + ".z", player.getLocation().getZ());
        homesConfig.set("homes." + player.getUniqueId() + "." + homeName + ".pitch", player.getLocation().getPitch());
        homesConfig.set("homes." + player.getUniqueId() + "." + homeName + ".yaw", player.getLocation().getYaw());
        saveHomesConfig();
    }

    public static void deleteHome(Player player, String homeName) {
        homesConfig.set("homes." + player.getUniqueId() + "." + homeName, null);
        saveHomesConfig();
    }

    public static void teleportToHome(Player player, String homeName) {
        if (homeExists(player, homeName)) {
            String worldName = homesConfig.getString("homes." + player.getUniqueId() + "." + homeName + ".world");
            double x = homesConfig.getDouble("homes." + player.getUniqueId() + "." + homeName + ".x");
            double y = homesConfig.getDouble("homes." + player.getUniqueId() + "." + homeName + ".y");
            double z = homesConfig.getDouble("homes." + player.getUniqueId() + "." + homeName + ".z");
            float pitch = (float) homesConfig.getDouble("homes." + player.getUniqueId() + "." + homeName + ".pitch");
            float yaw = (float) homesConfig.getDouble("homes." + player.getUniqueId() + "." + homeName + ".yaw");

            Location homeLocation = new Location(player.getServer().getWorld(worldName), x, y, z, yaw, pitch);
            player.teleport(homeLocation);
            String successMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.success.teleportedToHome").replace("%home%", homeName));
            player.sendMessage(successMessage);
        } else {
            String errorMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.error.homeNotFound").replace("%home%", homeName));
            player.sendMessage(errorMessage);
        }
    }

    public int getHomeCount(Player player) {
        int count = 0;
        if (homesConfig.contains("homes." + player.getUniqueId())) {
            ConfigurationSection playerHomes = homesConfig.getConfigurationSection("homes." + player.getUniqueId());
            if (playerHomes != null) {
                count = playerHomes.getKeys(false).size();
            }
        }
        return count;
    }

    public List<String> getHomes(Player player) {
        List<String> homeList = new ArrayList<>();
        ConfigurationSection playerHomes = homesConfig.getConfigurationSection("homes." + player.getUniqueId());
        if (playerHomes != null) {
            homeList.addAll(playerHomes.getKeys(false));
        }
        return homeList;
    }

    public static boolean homeExists(Player player, String homeName) {
        return homesConfig.contains("homes." + player.getUniqueId() + "." + homeName);
    }

    public static void saveHomesConfig() {
        try {
            homesConfig.save(homesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}