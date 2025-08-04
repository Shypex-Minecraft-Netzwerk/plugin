package Invmalte.cbsys.Perk;

import Invmalte.cbsys.CBSys;
import Invmalte.cbsys.GUI.Gui;
import Invmalte.cbsys.Util.MySQLManager;
import me.clip.placeholderapi.libs.kyori.adventure.text.Component;
import me.clip.placeholderapi.libs.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.milkbowl.vault.economy.Economy;
import org.apache.maven.model.Developer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashSet;
import java.util.Locale;
import java.util.UUID;

public class KCMCPerkShopListener implements Listener {
    private final KCMCPerkShopGUI gui;
    private Economy economy;
    private MySQLManager sql;
    private static String config(String path) { return CBSys.getInstance().getConfig().getString(path); }
    private DecimalFormat formatter = CBSys.getInstance().formatter;

    public KCMCPerkShopListener(KCMCPerkShopGUI gui, Economy economy, MySQLManager sql) {
        this.gui = gui;
        this.economy = economy;
        this.sql = sql;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(config("gui.perks.shoptitle"))) {
            ItemStack item = event.getCurrentItem();
            if (item == null) return;
            String itemName = item.getItemMeta().getDisplayName();
            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true);


            if (itemName.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', config("gui.perks.perks.strength.name")))) {
                String type = "strength";
                if (Integer.parseInt(this.sql.loadSpecificPerks(player.getUniqueId(), type)[0]) == this.sql.getPerkMax(type)) {
                    player.closeInventory();
                    player.sendMessage(CBSys.PREFIX + "Du hast dieses Perk bereits auf höchster Stufe!");
                } else {
                    int price = this.sql.getPerkPrice(type, Integer.parseInt(this.sql.loadSpecificPerks(player.getUniqueId(), type)[0]) + 1);
                    if (price >= economy.getBalance(player)) {
                        player.closeInventory();
                        player.sendMessage(CBSys.PREFIX + "§cDu hast nicht genug Geld für diesen Kauf! Du benötigst §e" + formatter.format(price) + "€");
                    } else {
                        economy.withdrawPlayer(player, Double.valueOf(price));
                        buyPerk(player, type, Integer.parseInt(this.sql.loadSpecificPerks(player.getUniqueId(), type)[0]) + 1);
                        player.sendMessage(CBSys.PREFIX + "§aErfolreich Perk " + config("gui.perks.perks."+ type + ".name") + " §aLevel: §e" + (Integer.parseInt(this.sql.loadSpecificPerks(player.getUniqueId(), type)[0]) + 1) + " §agekauft!\n" + CBSys.PREFIX + "Nutzes es mit §b/perks");
                    }
                }
            } else if (itemName.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', config("gui.perks.perks.haste.name")))) {
                String type = "haste";
                if (Integer.parseInt(this.sql.loadSpecificPerks(player.getUniqueId(), type)[0]) == this.sql.getPerkMax(type)) {
                    player.closeInventory();
                    player.sendMessage(CBSys.PREFIX + "Du hast dieses Perk bereits auf höchster Stufe!");
                } else {
                    int price = this.sql.getPerkPrice(type, Integer.parseInt(this.sql.loadSpecificPerks(player.getUniqueId(), type)[0]) + 1);
                    if (price >= economy.getBalance(player)) {
                        player.closeInventory();
                        player.sendMessage(CBSys.PREFIX + "§cDu hast nicht genug Geld für diesen Kauf! Du benötigst §e" + formatter.format(price) + "€");
                    } else {
                        economy.withdrawPlayer(player, Double.valueOf(price));
                        buyPerk(player, type, Integer.parseInt(this.sql.loadSpecificPerks(player.getUniqueId(), type)[0]) + 1);
                        player.sendMessage(CBSys.PREFIX + "§aErfolreich Perk " + config("gui.perks.perks."+ type + ".name") + " §aLevel: §e" + (Integer.parseInt(this.sql.loadSpecificPerks(player.getUniqueId(), type)[0]) + 1) + " §agekauft!\n" + CBSys.PREFIX + "Nutzes es mit §b/perks");
                    }
                }
            } else if (itemName.equalsIgnoreCase(config("gui.perks.perks.fire.name"))) {
                String type = "fire";
                if (Integer.parseInt(this.sql.loadSpecificPerks(player.getUniqueId(), type)[0]) == this.sql.getPerkMax(type)) {
                    player.closeInventory();
                    player.sendMessage(CBSys.PREFIX + "Du hast dieses Perk bereits auf höchster Stufe!");
                } else {
                    int price = this.sql.getPerkPrice(type, Integer.parseInt(this.sql.loadSpecificPerks(player.getUniqueId(), type)[0]) + 1);
                    if (price >= economy.getBalance(player)) {
                        player.closeInventory();
                        player.sendMessage(CBSys.PREFIX + "§cDu hast nicht genug Geld für diesen Kauf! Du benötigst §e" + formatter.format(price) + "€");
                    } else {
                        economy.withdrawPlayer(player, Double.valueOf(price));
                        buyPerk(player, type, Integer.parseInt(this.sql.loadSpecificPerks(player.getUniqueId(), type)[0]) + 1);
                        player.sendMessage(CBSys.PREFIX + "§aErfolreich Perk " + config("gui.perks.perks."+ type + ".name") + " §aLevel: §e" + (Integer.parseInt(this.sql.loadSpecificPerks(player.getUniqueId(), type)[0]) + 1) + " §agekauft!\n" + CBSys.PREFIX + "Nutzes es mit §b/perks");
                    }
                }
            } else if (itemName.equalsIgnoreCase(config("gui.perks.perks.water.name"))) {
                String type = "water";
                if (Integer.parseInt(this.sql.loadSpecificPerks(player.getUniqueId(), type)[0]) == this.sql.getPerkMax(type)) {
                    player.closeInventory();
                    player.sendMessage(CBSys.PREFIX + "Du hast dieses Perk bereits auf höchster Stufe!");
                } else {
                    int price = this.sql.getPerkPrice(type, Integer.parseInt(this.sql.loadSpecificPerks(player.getUniqueId(), type)[0]) + 1);
                    if (price >= economy.getBalance(player)) {
                        player.closeInventory();
                        player.sendMessage(CBSys.PREFIX + "§cDu hast nicht genug Geld für diesen Kauf! Du benötigst §e" + formatter.format(price) + "€");
                    } else {
                        economy.withdrawPlayer(player, Double.valueOf(price));
                        buyPerk(player, type, Integer.parseInt(this.sql.loadSpecificPerks(player.getUniqueId(), type)[0]) + 1);
                        player.sendMessage(CBSys.PREFIX + "§aErfolreich Perk " + config("gui.perks.perks."+ type + ".name") + " §aLevel: §e" + (Integer.parseInt(this.sql.loadSpecificPerks(player.getUniqueId(), type)[0]) + 1) + " §agekauft!\n" + CBSys.PREFIX + "Nutzes es mit §b/perks");
                    }
                }
            } else {
                gui.Gui(player);
            }
        }
    }

    public void buyPerk(Player player, String type, int level) {
        this.sql.updatePerk(player, type, level, Integer.parseInt(this.sql.loadSpecificPerks(player.getUniqueId(), type)[1]));
        Bukkit.getConsoleSender().sendMessage(CBSys.PREFIX + "Spieler §e" + player.getName() + " §7hat Perk §e" + type + " §7Level §e" + level + " §7für Preis §e" + formatter.format(this.sql.getPerkPrice(type, level)) + "€ §7gekauft!");
    }
}
