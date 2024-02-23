package net.akari.homex;

import net.akari.homex.commands.HomeCommand;
import net.akari.homex.inventory.HomeInventory;
import net.akari.homex.utils.CooldownManager;
import net.akari.homex.utils.Manager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class HomeX extends JavaPlugin {

    private Manager manager;

    @Override
    public void onEnable() {
        this.getLogger().info("\n\n\n" +
                "This Plugin was made with ❤\uFE0F by Akari_my\n" +
                "GitHub: github.com/Akari-my\n" +
                "Discord for support: akari_my\n\n\n");


        File homesFile = new File(getDataFolder(), "homes.yml");
        if (!homesFile.exists()) {
            homesFile.getParentFile().mkdirs();
            saveResource("homes.yml", false);
        }

        FileConfiguration homesConfig = YamlConfiguration.loadConfiguration(homesFile);

        manager = new Manager(this, homesConfig, homesFile);

        Objects.requireNonNull(getCommand("home")).setExecutor(new HomeCommand(this, manager));
        getServer().getPluginManager().registerEvents(new HomeInventory(this, manager), this);

        saveConfig();
    }

    @Override
    public void onDisable() {
        Manager.saveHomesConfig();
    }
}