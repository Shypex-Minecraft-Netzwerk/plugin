package Invmalte.cbsys.Perk;

import Invmalte.cbsys.CBSys;
import Invmalte.cbsys.Util.MySQLManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.HashSet;
import java.util.UUID;

public class KCMCPerkListener implements Listener {
    private final KCMCPerkGUI gui;
    private static final HashSet<UUID> jumpPerks = new HashSet<>();
    private static final HashSet<UUID> staerkePerks = new HashSet<>();
    private static final HashSet<UUID> abbauPerks = new HashSet<>();
    private static File perksFile;
    private static FileConfiguration perksConfig;
    private Economy economy;
    private MySQLManager sql;
    private static String config(String path) { return CBSys.getInstance().getConfig().getString(path); }

    public KCMCPerkListener(KCMCPerkGUI gui, MySQLManager sql) {
        this.gui = gui;
        this.sql = sql;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(config("gui.perks.title"))) {
            event.setCancelled(true);

            ItemStack item = event.getCurrentItem();
            if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;

            Player player = (Player) event.getWhoClicked();
            String itemName = item.getItemMeta().getDisplayName();

            if (itemName.equals(config("gui.perks.perks.strength.name"))) {
                String type1 = "strength";
                int level1 = Integer.parseInt(sql.loadSpecificPerks(player.getUniqueId(), type1)[0]);
                if (level1 != 0) {
                    checkPerkState(player, type1);
                    return;
                } else {
                    player.sendMessage(CBSys.PREFIX + "§cDu besitzt das Perk §e" + config("gui.perks.perks." + type1 + ".name") + " §cnicht. Kaufe es zu erst im §b/perkshop");
                    return;
                }
            } else if (itemName.equals(config("gui.perks.perks.haste.name"))) {
                String type2 = "haste";
                int level2 = Integer.parseInt(sql.loadSpecificPerks(player.getUniqueId(), type2)[0]);
                if (level2 != 0) {
                    checkPerkState(player, type2);
                    return;
                } else {
                    player.sendMessage(CBSys.PREFIX + "§cDu besitzt das Perk §e" + config("gui.perks.perks." + type2 + ".name") + " §cnicht. Kaufe es zu erst im §b/perkshop");
                    return;
                }
            } else if (itemName.equals(config("gui.perks.perks.fire.name"))) {
                String type3 = "fire";
                int level3 = Integer.parseInt(sql.loadSpecificPerks(player.getUniqueId(), type3)[0]);
                if (level3 != 0) {
                    checkPerkState(player, type3);
                    return;
                } else {
                    player.sendMessage(CBSys.PREFIX + "§cDu besitzt das Perk §e" + config("gui.perks.perks." + type3 + ".name") + " §cnicht. Kaufe es zu erst im §b/perkshop");
                    return;
                }
            } else if (itemName.equals(config("gui.perks.perks.water.name"))) {
                String type4 = "water";
                int level4 = Integer.parseInt(sql.loadSpecificPerks(player.getUniqueId(), type4)[0]);
                if (level4 != 0) {
                    checkPerkState(player, type4);
                    return;
                } else {
                    player.sendMessage(CBSys.PREFIX + "§cDu besitzt das Perk §e" + config("gui.perks.perks." + type4 + ".name") + " §cnicht. Kaufe es zu erst im §b/perkshop");
                    return;
                }
            }
        }
    }

    public void checkPerkState(Player player, String type) {
        int level = Integer.parseInt(sql.loadSpecificPerks(player.getUniqueId(), type)[0]);
        String active = sql.loadSpecificPerks(player.getUniqueId(), type)[1];

        switch (active) {
            case "0":
                activatePerk(player, type, level);
                break;
            case "1":
                deactivePerk(player, type, level);
                break;
        }
    }


    public void activatePerk(Player player, String type, int level) {
        switch (type) {
            case "strength":
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, -1, level-1));
                sql.updatePerk(player, type, level, 1);
                player.sendMessage(CBSys.PREFIX + "Perk §eStärke §7Stufe: §e" + level + " §7erfolgreich §aaktiviert§7!");
                break;
            case "haste":
                player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, -1, level-1));
                sql.updatePerk(player, type, level, 1);
                player.sendMessage(CBSys.PREFIX + "Perk §eEile §7Stufe: §e" + level + " §7erfolgreich §aaktiviert§7!");
                break;
            case "fire":
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, -1, level-1));
                sql.updatePerk(player, type, level, 1);
                player.sendMessage(CBSys.PREFIX + "Perk §eFeuerresistenz §7erfolgreich §aaktiviert§7!");
                break;
            case "water":
                player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, -1, level-1));
                sql.updatePerk(player, type, level, 1);
                player.sendMessage(CBSys.PREFIX + "Perk §eWasseratmung §7erfolgreich §aaktiviert§7!");
                break;
            default:
                player.sendMessage(CBSys.PREFIX + "§cEin fehler ist aufgetreten! Kontaktiere bitte unseren Support!");
                break;
        }
    }

    public void deactivePerk(Player player, String type, int level) {
        switch (type) {
            case "strength":
                player.removePotionEffect(PotionEffectType.STRENGTH);
                sql.updatePerk(player, type, level, 0);
                player.sendMessage(CBSys.PREFIX + "Perk §eStärke §7Stufe: §e" + level + " §7erfolgreich §cdeaktiviert§7!");
                break;
            case "haste":
                player.removePotionEffect(PotionEffectType.HASTE);
                sql.updatePerk(player, type, level, 0);
                player.sendMessage(CBSys.PREFIX + "Perk §eStärke §7Stufe: §e" + level + " §7erfolgreich §cdeaktiviert§7!");
                break;
            case "fire":
                player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                sql.updatePerk(player, type, level, 0);
                player.sendMessage(CBSys.PREFIX + "Perk §eStärke §7Stufe: §e" + level + " §7erfolgreich §cdeaktiviert§7!");
                break;
            case "water":
                player.removePotionEffect(PotionEffectType.WATER_BREATHING);
                sql.updatePerk(player, type, level, 0);
                player.sendMessage(CBSys.PREFIX + "Perk §eStärke §7Stufe: §e" + level + " §7erfolgreich §cdeaktiviert§7!");
                break;
            default:
                player.sendMessage(CBSys.PREFIX + "§cEin fehler ist aufgetreten! Kontaktiere bitte unseren Support!");
                break;
        }
    }
}
