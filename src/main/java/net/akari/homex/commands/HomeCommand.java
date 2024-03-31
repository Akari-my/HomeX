package net.akari.homex.commands;

import net.akari.homex.HomeX;
import net.akari.homex.commands.argumets.delHome;
import net.akari.homex.commands.argumets.setHome;
import net.akari.homex.commands.argumets.teleportHome;
import net.akari.homex.inventory.HomeInventory;
import net.akari.homex.utils.Manager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HomeCommand implements CommandExecutor {

    private final HomeX plugin;
    private final Manager manager;
    private final setHome setHome;
    private final delHome delHome;
    private final teleportHome teleportHome;

    public HomeCommand(HomeX plugin, Manager manager) {
        this.plugin = plugin;
        this.manager = manager;

        this.setHome = new setHome(plugin, manager);
        this.delHome = new delHome(plugin);
        this.teleportHome = new teleportHome(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("home"))
            return false;

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        List<String> newArgs = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            if (i == 0) {
                continue;
            }
            newArgs.add(args[i]);
        }

        if (args.length < 1) {
            sendHelpMessage(player);
            return true;
        }

        String subCommand = args[0];

        if (subCommand.equalsIgnoreCase("setHome")) {
            setHome.execute(sender, newArgs.toArray(new String[0]));
        } else if (subCommand.equalsIgnoreCase("delHome")) {
            delHome.execute(sender, newArgs.toArray(new String[0]));
        } else if (subCommand.equalsIgnoreCase("home")) {
            teleportHome.execute(sender, newArgs.toArray(new String[0]));
        } else {
            player.sendMessage(ChatColor.RED + "Unknown sub-command. Available sub-commands: setHome, delHome, home");
        }

        return true;
    }

    private void sendHelpMessage(Player player) {
        HomeInventory inventory = new HomeInventory(this.plugin, manager);
        inventory.openHomeInventory(player);
    }
}