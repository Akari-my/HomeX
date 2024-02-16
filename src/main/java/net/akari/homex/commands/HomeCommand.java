package net.akari.homex.commands;

import net.akari.homex.HomeX;
import net.akari.homex.utils.Manager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {

    private final HomeX plugin;
    private final Manager manager;

    public HomeCommand(HomeX plugin, Manager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("home")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (args.length < 1) {
                    player.sendMessage(ChatColor.RED + "==== " + ChatColor.GRAY + "HomeX " + ChatColor.RED + "====");
                    player.sendMessage("");
                    player.sendMessage(ChatColor.RED + "/home setHome <name Home>");
                    player.sendMessage(ChatColor.RED + "/home delHome <name Home>");
                    player.sendMessage(ChatColor.RED + "/home home <name Home>");
                    player.sendMessage("");
                    player.sendMessage(ChatColor.RED + "This Plugin was made with ❤ by Akari_my");
                    player.sendMessage("");
                    player.sendMessage(ChatColor.RED + "==== " + ChatColor.GRAY + "HomeX " + ChatColor.RED + "====");
                    return true;
                }

                String subCommand = args[0];

                if (subCommand.equalsIgnoreCase("setHome")) {
                    if (args.length < 2) {
                        player.sendMessage(ChatColor.RED + "Usage: /home setHome <homeName>");
                        return true;
                    }

                    String homeName = args[1];
                    if (!Manager.homeExists(player, homeName)) {
                        Manager.setHome(player, homeName);
                        String successMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.success.homeSet").replace("%home%", homeName));
                        player.sendMessage(successMessage);
                    } else {
                        String errorMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.error.homeAlreadyExists").replace("%home%", homeName));
                        player.sendMessage(errorMessage);
                    }
                } else if (subCommand.equalsIgnoreCase("delHome")) {
                    if (args.length < 2) {
                        player.sendMessage(ChatColor.RED + "Usage: /home delHome <homeName>");
                        return true;
                    }

                    String homeName = args[1];
                    if (Manager.homeExists(player, homeName)) {
                        Manager.deleteHome(player, homeName);
                        String successMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.success.homeDeleted").replace("%home%", homeName));
                        player.sendMessage(successMessage);
                    } else {
                        String errorMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.error.homeNotFound").replace("%home%", homeName));
                        player.sendMessage(errorMessage);
                    }
                } else if (subCommand.equalsIgnoreCase("home")) {
                    if (args.length < 2) {
                        player.sendMessage(ChatColor.RED + "Usage: /home home <homeName>");
                        return true;
                    }

                    String homeName = args[1];
                    Manager.teleportToHome(player, homeName);
                } else {
                    player.sendMessage(ChatColor.RED + "Unknown sub-command. Available sub-commands: setHome, delHome, home");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            }
            return true;
        }
        return false;
    }
}
