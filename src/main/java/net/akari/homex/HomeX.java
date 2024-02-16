package net.akari.homex;

import net.akari.homex.commands.HomeCommand;
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
        this.getLogger().info("#      " +
                "This Plugin HomeX was made with ❤ by Akari_my\n" +
                "#      GitHub: github.com/Akari-my\n" +
                "#      Discord for support: akari_my\n" +
                "#" +
                "#      version: 1.0-BETA");
        File homesFile = new File(getDataFolder(), "homes.yml");
        if (!homesFile.exists()) {
            homesFile.getParentFile().mkdirs();
            saveResource("homes.yml", false);
        }

        FileConfiguration homesConfig = YamlConfiguration.loadConfiguration(homesFile);
        manager = new Manager(this, homesConfig, homesFile);

        saveConfig();

        Objects.requireNonNull(getCommand("home")).setExecutor(new HomeCommand(this, manager));
    }

    @Override
    public void onDisable() {
        Manager.saveHomesConfig();
    }
}