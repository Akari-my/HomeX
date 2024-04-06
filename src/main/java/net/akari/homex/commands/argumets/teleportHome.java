package net.akari.homex.commands.argumets;

import net.akari.homex.HomeX;
import net.akari.homex.commands.SubCommand;
import net.akari.homex.utils.CooldownManager;
import net.akari.homex.database.DatabaseManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class teleportHome implements SubCommand {

    private final HomeX plugin;
    private final CooldownManager cooldownManager;

    public teleportHome(HomeX plugin){
        this.plugin = plugin;
        this.cooldownManager = new CooldownManager(plugin);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /home home <homeName>");
            return;
        }

        String homeName = args[0];
        if (cooldownManager.hasCooldown(player)) {
            String errorMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("messages.error.coolDownWait")));
            player.sendMessage(errorMessage);
            return;
        }

        if (DatabaseManager.homeExists(player, homeName)) {
            int cooldownSeconds = plugin.getConfig().getInt("settings.cooldownSeconds");
            cooldownManager.startCooldown(player, cooldownSeconds, () -> {
                cooldownManager.removeCooldown(player);
                DatabaseManager.teleportToHome(player, homeName);
            });
        } else {
            String errorMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("messages.error.homeNotFound")).replace("%home%", homeName));
            player.sendMessage(errorMessage);
        }
    }
}
