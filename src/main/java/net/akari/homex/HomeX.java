package net.akari.homex;

import net.akari.homex.commands.HomeCommand;
import net.akari.homex.inventory.HomeInventory;
import net.akari.homex.database.DatabaseManager;
import net.akari.homex.utils.MySQL;
import net.akari.homex.utils.UpdateChecker;
import org.bukkit.Bukkit;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class HomeX extends JavaPlugin {

    private MySQL mySQL;

    @Override
    public void onEnable() {
        this.mySQL = new MySQL(this);
        mySQL.connect();

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

        DatabaseManager manager = new DatabaseManager(this);

        Objects.requireNonNull(getCommand("home")).setExecutor(new HomeCommand(this, manager));
        getServer().getPluginManager().registerEvents(new HomeInventory(this, manager), this);

        saveConfig();
    }

    public MySQL getMySQL() {
        return mySQL;
    }
}