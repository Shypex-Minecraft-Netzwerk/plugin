package Invmalte.cbsys.Perk;

import Invmalte.cbsys.CBSys;
import Invmalte.cbsys.GUI.Gui;
import Invmalte.cbsys.Util.MySQLManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Arrays;

public class KCMCPerkShopGUI implements CommandExecutor {
    private MySQLManager sql;
    private DecimalFormat formatter = CBSys.getInstance().formatter;
    public KCMCPerkShopGUI(MySQLManager sql) {
        this.sql = sql;
    }
    private static String config(String path) { return CBSys.getInstance().getConfig().getString(path); }

    public void Gui(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, config("gui.perks.shoptitle"));
        ItemStack perk1 = new ItemStack(Material.valueOf(config("gui.perks.perks.strength.item")));
        ItemMeta perk1Meta = perk1.getItemMeta();
        perk1Meta.setDisplayName(config("gui.perks.perks.strength.name"));
        String[] strengthUserInfo = this.sql.loadSpecificPerks(player.getUniqueId(), "strength");
        if (Integer.parseInt(strengthUserInfo[0]) == this.sql.getPerkMax("strength")) {
            perk1Meta.setLore(Arrays.asList("§aMaximales Level freigeschaltet! §eLevel: " + this.sql.getPerkMax("strength")));
            perk1Meta.addEnchant(Enchantment.MENDING, 1, true);
            perk1Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            perk1Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        } else {
            perk1Meta.setLore(Arrays.asList("§7Level §e" + strengthUserInfo[0] + " §7von §e" + this.sql.getPerkMax("strength") + " §8• §7Preis für das nächste Level: §e" + formatter.format(this.sql.getPerkPrice("strength", Integer.parseInt(strengthUserInfo[0]) + 1)) + "€"));
        }
        perk1.setItemMeta(perk1Meta);

        ItemStack perk2 = new ItemStack(Material.valueOf(config("gui.perks.perks.haste.item")));
        ItemMeta perk2Meta = perk2.getItemMeta();
        perk2Meta.setDisplayName(config("gui.perks.perks.haste.name"));
        String[] hasteUserInfo = this.sql.loadSpecificPerks(player.getUniqueId(), "haste");
        if (Integer.parseInt(hasteUserInfo[0]) == this.sql.getPerkMax("haste")) {
            perk2Meta.setLore(Arrays.asList("§aMaximales Level freigeschaltet! §eLevel: " + this.sql.getPerkMax("haste")));
            perk2Meta.addEnchant(Enchantment.MENDING, 1, true);
            perk2Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            perk2Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        } else {
            perk2Meta.setLore(Arrays.asList("§7Level §e" + hasteUserInfo[0] + " §7von §e" + this.sql.getPerkMax("haste") + " §8• §7Preis für das nächste Level: §e" + formatter.format(this.sql.getPerkPrice("haste", Integer.parseInt(hasteUserInfo[0]) + 1)) + "€"));
        }
        perk2.setItemMeta(perk2Meta);

        ItemStack perk3 = new ItemStack(Material.valueOf(config("gui.perks.perks.fire.item")));
        ItemMeta perk3Meta = perk3.getItemMeta();
        perk3Meta.setDisplayName(config("gui.perks.perks.fire.name"));
        String[] fireUserInfo = this.sql.loadSpecificPerks(player.getUniqueId(), "fire");
        if (Integer.parseInt(fireUserInfo[0]) == this.sql.getPerkMax("fire")) {
            perk3Meta.setLore(Arrays.asList("§aMaximales Level freigeschaltet! §eLevel: " + this.sql.getPerkMax("fire")));
            perk3Meta.addEnchant(Enchantment.MENDING, 1, true);
            perk3Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            perk3Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        } else {
            perk3Meta.setLore(Arrays.asList("§7Level §e" + fireUserInfo[0] + " §7von §e" + this.sql.getPerkMax("fire") + " §8• §7Preis für das nächste Level: §e" + formatter.format(this.sql.getPerkPrice("fire", Integer.parseInt(fireUserInfo[0]) + 1)) + "€"));
        }
        perk3.setItemMeta(perk3Meta);

        ItemStack perk4 = new ItemStack(Material.valueOf(config("gui.perks.perks.water.item")));
        ItemMeta perk4Meta = perk4.getItemMeta();
        perk4Meta.setDisplayName(config("gui.perks.perks.water.name"));
        String[] waterUserInfo = this.sql.loadSpecificPerks(player.getUniqueId(), "water");
        if (Integer.parseInt(waterUserInfo[0]) == this.sql.getPerkMax("water")) {
            perk4Meta.setLore(Arrays.asList("§aMaximales Level freigeschaltet! §eLevel: " + this.sql.getPerkMax("water")));
            perk4Meta.addEnchant(Enchantment.MENDING, 1, true);
            perk4Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            perk4Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        } else {
            perk4Meta.setLore(Arrays.asList("§7Level §e" + waterUserInfo[0] + " §7von §e" + this.sql.getPerkMax("water") + " §8• §7Preis für das nächste Level: §e" + formatter.format(this.sql.getPerkPrice("water", Integer.parseInt(waterUserInfo[0]) + 1)) + "€"));
        }
        perk4.setItemMeta(perk4Meta);

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
