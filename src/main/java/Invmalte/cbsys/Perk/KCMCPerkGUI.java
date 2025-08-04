package Invmalte.cbsys.Perk;

import Invmalte.cbsys.CBSys;
import Invmalte.cbsys.GUI.Gui;
import Invmalte.cbsys.Util.MySQLManager;
import com.plotsquared.core.database.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Arrays;

public class KCMCPerkGUI implements CommandExecutor {
    private MySQLManager sql;
    private DecimalFormat formatter = CBSys.getInstance().formatter;

    public KCMCPerkGUI(MySQLManager sql) {
        this.sql = sql;
    }

    private static String config(String path) { return CBSys.getInstance().getConfig().getString(path); }


    public void Gui(Player player) {
        ItemStack perk1;
        ItemStack perk2;
        ItemStack perk3;
        ItemStack perk4;
        Inventory inventory = Bukkit.createInventory(null, 27, config("gui.perks.title"));
        String[] strengthUserInfo = this.sql.loadSpecificPerks(player.getUniqueId(), "strength");
        if (Integer.parseInt(strengthUserInfo[0]) != 0) {
            perk1 = new ItemStack(Material.valueOf(config("gui.perks.perks.strength.item")));
            ItemMeta perk1Meta = perk1.getItemMeta();
            perk1Meta.setDisplayName(config("gui.perks.perks.strength.name"));
            if (Integer.parseInt(strengthUserInfo[1]) == 1) {
                perk1Meta.setLore(Arrays.asList("§7Perk Status: §aaktiv §8• §7Level: §e" + strengthUserInfo[0]));
                perk1Meta.addEnchant(Enchantment.MENDING, 1, true);
                perk1Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                perk1Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            } else {
                perk1Meta.setLore(Arrays.asList("§7Perk Status: §cinaktiv §8• §7Level: §e" + strengthUserInfo[0]));
            }
            perk1.setItemMeta(perk1Meta);
        } else {
            perk1 = new ItemStack(Material.BARRIER);
            ItemMeta perk1Meta = perk1.getItemMeta();
            perk1Meta.setDisplayName(config("gui.perks.perks.strength.name"));
            perk1Meta.setLore(Arrays.asList("§7Perk Status: §cnicht freigeschaltet §8• §7Level: §e0 \n§7Schalte dir das Perk zuerst im §b/perkshop §7frei!"));
            perk1.setItemMeta(perk1Meta);
        }

        String[] hasteUserInfo = this.sql.loadSpecificPerks(player.getUniqueId(), "haste");
        if (Integer.parseInt(hasteUserInfo[0]) != 0) {
            perk2 = new ItemStack(Material.valueOf(config("gui.perks.perks.haste.item")));
            ItemMeta perk2Meta = perk2.getItemMeta();
            perk2Meta.setDisplayName(config("gui.perks.perks.haste.name"));
            if (Integer.parseInt(hasteUserInfo[1]) == 1) {
                perk2Meta.setLore(Arrays.asList("§7Perk Status: §aaktiv §8• §7Level: §e" + hasteUserInfo[0]));
                perk2Meta.addEnchant(Enchantment.MENDING, 1, true);
                perk2Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                perk2Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            } else {
                perk2Meta.setLore(Arrays.asList("§7Perk Status: §cinaktiv §8• §7Level: §e" + hasteUserInfo[0]));
            }
            perk2.setItemMeta(perk2Meta);
        } else {
            perk2 = new ItemStack(Material.BARRIER);
            ItemMeta perk2Meta = perk2.getItemMeta();
            perk2Meta.setDisplayName(config("gui.perks.perks.haste.name"));
            perk2Meta.setLore(Arrays.asList("§7Perk Status: §cnicht freigeschaltet §8• §7Level: §e0 \n§7Schalte dir das Perk zuerst im §b/perkshop §7frei!"));
            perk2.setItemMeta(perk2Meta);
        }

        String[] fireUserInfo = this.sql.loadSpecificPerks(player.getUniqueId(), "fire");
        if (Integer.parseInt(fireUserInfo[0]) != 0) {
            perk3 = new ItemStack(Material.valueOf(config("gui.perks.perks.fire.item")));
            ItemMeta perk3Meta = perk3.getItemMeta();
            perk3Meta.setDisplayName(config("gui.perks.perks.fire.name"));
            if (Integer.parseInt(fireUserInfo[1]) == 1) {
                perk3Meta.setLore(Arrays.asList("§7Perk Status: §aaktiv §8• §7Level: §e" + fireUserInfo[0]));
                perk3Meta.addEnchant(Enchantment.MENDING, 1, true);
                perk3Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                perk3Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            } else {
                perk3Meta.setLore(Arrays.asList("§7Perk Status: §cinaktiv §8• §7Level: §e" + fireUserInfo[0]));
            }
            perk3.setItemMeta(perk3Meta);
        } else {
            perk3 = new ItemStack(Material.BARRIER);
            ItemMeta perk3Meta = perk3.getItemMeta();
            perk3Meta.setDisplayName(config("gui.perks.perks.fire.name"));
            perk3Meta.setLore(Arrays.asList("§7Perk Status: §cnicht freigeschaltet §8• §7Level: §e0 \n§7Schalte dir das Perk zuerst im §b/perkshop §7frei!"));
            perk3.setItemMeta(perk3Meta);
        }

        String[] waterUserInfo = this.sql.loadSpecificPerks(player.getUniqueId(), "water");
        if (Integer.parseInt(waterUserInfo[0]) != 0) {
            perk4 = new ItemStack(Material.valueOf(config("gui.perks.perks.water.item")));
            ItemMeta perk4Meta = perk4.getItemMeta();
            perk4Meta.setDisplayName(config("gui.perks.perks.water.name"));
            if (Integer.parseInt(waterUserInfo[1]) == 1) {
                perk4Meta.setLore(Arrays.asList("§7Perk Status: §aaktiv §8• §7Level: §e" + waterUserInfo[0]));
                perk4Meta.addEnchant(Enchantment.MENDING, 1, true);
                perk4Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                perk4Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            } else {
                perk4Meta.setLore(Arrays.asList("§7Perk Status: §cinaktiv §8• §7Level: §e" + waterUserInfo[0]));
            }
            perk4.setItemMeta(perk4Meta);
        } else {
            perk4 = new ItemStack(Material.BARRIER);
            ItemMeta perk4Meta = perk4.getItemMeta();
            perk4Meta.setDisplayName(config("gui.perks.perks.water.name"));
            perk4Meta.setLore(Arrays.asList("§7Perk Status: §cnicht freigeschaltet §8• §7Level: §e0 \n§7Schalte dir das Perk zuerst im §b/perkshop §7frei!"));
            perk4.setItemMeta(perk4Meta);
        }
        // Entweder es klappt so, oder der Halter wird nach oben vor die anderen Sachen gepackt und die setItems kommen jeweils einmal in die if und else abfragen.
        Halter(inventory);

        inventory.setItem(10, perk1);
        inventory.setItem(12, perk2);
        inventory.setItem(14, perk3);
        inventory.setItem(16, perk4);

        player.openInventory(inventory);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        Gui(player);
        return true;
    }

    public void Halter(Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack Halter = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta HalterMeta = Halter.getItemMeta();
            HalterMeta.setDisplayName("§7");
            Halter.setItemMeta(HalterMeta);
            inventory.setItem(i, Halter);
        }
    }
}