package net.akari.homex;

import net.akari.homex.commands.HomeCommand;
import net.akari.homex.inventory.HomeInventory;
import net.akari.homex.utils.Manager;
import net.akari.homex.utils.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class HomeX extends JavaPlugin {

    @Override
    public void onEnable() {
        Metrics metrics = new Metrics(this, 21470);

        this.getLogger().info("\n\n\n" +
                "This Plugin was made with ❤\uFE0F by Akari_my\n" +
                "GitHub: github.com/Akari-my\n" +
                "Discord for support: akari_my\n\n\n");

        new UpdateChecker(this, 115103).getLatestVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                return;
            } else {
                Bukkit.getLogger().warning("" +
                        ">> There is a new version of HomeX!!" +
                        "" +
                        ">> Downloads: https://www.spigotmc.org/resources/homex.115103/");
            }
        });


        File homesFile = new File(getDataFolder(), "homes.yml");
        if (!homesFile.exists()) {
            homesFile.getParentFile().mkdirs();
            saveResource("homes.yml", false);
        }

        FileConfiguration homesConfig = YamlConfiguration.loadConfiguration(homesFile);

        Manager manager = new Manager(this, homesConfig, homesFile);

        Objects.requireNonNull(getCommand("home")).setExecutor(new HomeCommand(this, manager));
        getServer().getPluginManager().registerEvents(new HomeInventory(this, manager), this);

        saveConfig();
    }

    @Override
    public void onDisable() {
        Manager.saveHomesConfig();
    }
}