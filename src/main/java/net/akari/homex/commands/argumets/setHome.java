package net.akari.homex.commands.argumets;

import net.akari.homex.HomeX;
import net.akari.homex.commands.SubCommand;
import net.akari.homex.database.DatabaseManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class setHome implements SubCommand {

    private final HomeX plugin;
    private final DatabaseManager manager;

    public setHome(HomeX plugin, DatabaseManager manager){
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /home setHome <homeName>");
            return;
        }

        String homeName = args[0];
        int maxHomes = plugin.getConfig().getInt("settings.maxHomes");

        if (DatabaseManager.getHomeCount(player) >= maxHomes) {
            String errorMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("messages.error.maxHomesReached")));
            player.sendMessage(errorMessage);
            return;
        }

        if (!DatabaseManager.homeExists(player, homeName)) {
            DatabaseManager.setHome(player, homeName);
            String successMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("messages.success.homeSet")).replace("%home%", homeName));
            player.sendMessage(successMessage);
        } else {
            String errorMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("messages.error.homeAlreadyExists")).replace("%home%", homeName));
            player.sendMessage(errorMessage);
        }
    }
}