package net.akari.homex.utils;

import net.akari.homex.HomeX;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CooldownManager {

    private final HomeX plugin;
    private final Map<Player, Long> cooldowns;

    public CooldownManager(HomeX plugin) {
        this.plugin = plugin;
        this.cooldowns = new HashMap<>();
    }

    public void startCooldown(Player player, int seconds, Runnable callback) {
        long cooldownMillis = seconds * 1000L;
        long expiration = System.currentTimeMillis() + cooldownMillis;
        cooldowns.put(player, expiration);

        String successMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("messages.success.coolDown")));
        player.sendMessage(successMessage);

        new BukkitRunnable() {
            @Override
            public void run() {
                cooldowns.remove(player);
                callback.run();
            }
        }.runTaskLater(plugin, seconds * 20L);
    }

    public long getRemainingTime(Player player) {
        if (cooldowns.containsKey(player)) {
            long expiration = cooldowns.get(player);
            long currentTime = System.currentTimeMillis();
            if (currentTime < expiration) {
                return (expiration - currentTime) / 1000L;
            }
        }
        return 0;
    }

    public void removeCooldown(Player player) {
        cooldowns.remove(player);
    }

    public boolean hasCooldown(Player player) {
        if (cooldowns.containsKey(player)) {
            long expiration = cooldowns.get(player);
            return System.currentTimeMillis() < expiration;
        }
        return false;
    }
}