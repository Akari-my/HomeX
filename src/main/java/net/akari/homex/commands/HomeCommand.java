package net.akari.homex.commands;

import net.akari.homex.HomeX;
import net.akari.homex.inventory.HomeInventory;
import net.akari.homex.utils.CooldownManager;
import net.akari.homex.utils.Manager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class HomeCommand implements CommandExecutor {

    private final HomeX plugin;
    private final Manager manager;
    private final CooldownManager cooldownManager;

    public HomeCommand(HomeX plugin, Manager manager) {
        this.plugin = plugin;
        this.manager = manager;
        this.cooldownManager = new CooldownManager(plugin);
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

        if (args.length < 1) {
            sendHelpMessage(player);
            return true;
        }

        String subCommand = args[0];

        if (subCommand.equalsIgnoreCase("setHome")) {
            SetHome(player, args);
        } else if (subCommand.equalsIgnoreCase("delHome")) {
            DeleteHome(player, args);
        } else if (subCommand.equalsIgnoreCase("home")) {
            TeleportHome(player, args);
        } else {
            player.sendMessage(ChatColor.RED + "Unknown sub-command. Available sub-commands: setHome, delHome, home");
        }

        return true;
    }

    private void sendHelpMessage(Player player) {
        HomeInventory inventory = new HomeInventory(this.plugin, manager);
        inventory.openHomeInventory(player);
    }

    private void SetHome(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /home setHome <homeName>");
            return;
        }

        String homeName = args[1];
        int maxHomes = plugin.getConfig().getInt("settings.maxHomes");

        if (manager.getHomeCount(player) >= maxHomes) {
            String errorMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("messages.error.maxHomesReached")));
            player.sendMessage(errorMessage);
            return;
        }

        if (!manager.homeExists(player, homeName)) {
            manager.setHome(player, homeName);
            String successMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("messages.success.homeSet")).replace("%home%", homeName));
            player.sendMessage(successMessage);
        } else {
            String errorMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("messages.error.homeAlreadyExists")).replace("%home%", homeName));
            player.sendMessage(errorMessage);
        }
    }

    private void DeleteHome(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /home delHome <homeName>");
            return;
        }

        String homeName = args[1];
        if (Manager.homeExists(player, homeName)) {
            Manager.deleteHome(player, homeName);
            String successMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("messages.success.homeDeleted")).replace("%home%", homeName));
            player.sendMessage(successMessage);
        } else {
            String errorMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("messages.error.homeNotFound")).replace("%home%", homeName));
            player.sendMessage(errorMessage);
        }
    }

    private void TeleportHome(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /home home <homeName>");
            return;
        }

        String homeName = args[1];
        if (cooldownManager.hasCooldown(player)) {
            String errorMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("messages.error.coolDownWait")));
            player.sendMessage(errorMessage);
            return;
        }

        if (Manager.homeExists(player, homeName)) {
            int cooldownSeconds = plugin.getConfig().getInt("settings.cooldownSeconds");
            cooldownManager.startCooldown(player, cooldownSeconds, () -> {cooldownManager.removeCooldown(player);Manager.teleportToHome(player, homeName);
            });
        } else {
            String errorMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("messages.error.homeNotFound")).replace("%home%", homeName));
            player.sendMessage(errorMessage);
        }
    }
}