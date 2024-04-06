package net.akari.homex.inventory;

import net.akari.homex.HomeX;
import net.akari.homex.utils.CooldownManager;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import net.akari.homex.database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeInventory implements Listener{

    private final HomeX plugin;
    private final DatabaseManager manager;
    private final CooldownManager cooldownManager;

    public HomeInventory(HomeX plugin, DatabaseManager manager) {
        this.plugin = plugin;
        this.manager = manager;
        this.cooldownManager = new CooldownManager(plugin);
    }

    public void openHomeInventory(Player player) {
        int size = 9;
        Inventory inventory = Bukkit.createInventory(null, size, "Home Menu");

        ItemStack headItem = createHeadItem(player);
        inventory.setItem(3, headItem);

        ItemStack homesItem = createHomesItems(player);
        inventory.setItem(5, homesItem);

        player.openInventory(inventory);
    }

    public void openTeleportHomesInventory(Player player) {
        List<String> homes = manager.getHomes(player);
        int size = 27;
        Inventory inventory = Bukkit.createInventory(null, size, "Teleport Homes");

        for (int i = 0; i < homes.size(); i++) {
            String home = homes.get(i);
            if (i >= size) {
                break;
            }
            ItemStack homeItem = createHomeItem(home);
            inventory.setItem(i, homeItem);
        }

        player.openInventory(inventory);
    }
    private ItemStack createHomeItem(String home) {
        ItemStack homeItem = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = homeItem.getItemMeta();
        itemMeta.setDisplayName("§c" + home);
        List<String> lore = new ArrayList<>();
        itemMeta.setLore(lore);
        homeItem.setItemMeta(itemMeta);
        return homeItem;
    }

    private ItemStack createHeadItem(Player player) {
        ItemStack headItem = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) headItem.getItemMeta();
        skullMeta.setOwner("Akari_my");
        skullMeta.setDisplayName("§cThis Plugin was Developed by §eAkari_my");
        headItem.setItemMeta(skullMeta);

        return headItem;
    }

    private ItemStack createHomesItems(Player player) {
        ItemStack homesItem = new ItemStack(Material.CHEST);
        ItemMeta itemMeta = homesItem.getItemMeta();
        itemMeta.setDisplayName("§aYour Home Setup");
        List<String> lore = new ArrayList<>();

        lore.add("§eClick me to see your Home Sets");

        itemMeta.setLore(lore);

        homesItem.setItemMeta(itemMeta);
        return homesItem;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Home Menu")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

            if (clickedItem.getItemMeta() != null && clickedItem.getItemMeta().hasDisplayName()) {
                String displayName = clickedItem.getItemMeta().getDisplayName();
                if (displayName.equals("§aYour Home Setup")) {
                    openTeleportHomesInventory(player);
                }
            }
        } else if (event.getView().getTitle().equals("Teleport Homes")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

            if (clickedItem.getItemMeta() != null && clickedItem.getItemMeta().hasDisplayName()) {
                String homeName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

                if (!DatabaseManager.homeExists(player, homeName)) {
                    String errorMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("messages.error.homeNotFound")).replace("%home%", homeName));
                    player.sendMessage(errorMessage);
                    return;
                }

                if (cooldownManager.hasCooldown(player)) {
                    long remainingTime = cooldownManager.getRemainingTime(player);
                    player.sendMessage("§cYou will be teleported to your Home in " + remainingTime + "!!");
                    return;
                }

                int cooldownSeconds = plugin.getConfig().getInt("settings.cooldownSeconds");
                cooldownManager.startCooldown(player, cooldownSeconds, () -> {
                    DatabaseManager.teleportToHome(player, homeName);
                });
            }
        }
    }
}

