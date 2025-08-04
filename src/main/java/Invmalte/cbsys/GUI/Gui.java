package Invmalte.cbsys.GUI;

import java.text.DecimalFormat;
import java.util.Arrays;

import Invmalte.cbsys.CBSys;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Gui implements CommandExecutor {
    private String message(String path) {return (String) CBSys.getInstance().getMessageConfig().getString(path, "err");}
    private String config(String path) {return (String) CBSys.getInstance().getConfig().getString(path, "err");}
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player)sender;
        GUI(player);
        return true;
    }
    Inventory inventory = Bukkit.createInventory(null, 27, config("gui.all_gui.title").replace("%servername%", message("all.servername")));

    public void GUI(Player player) {
        ItemStack Perk = new ItemStack(Material.REDSTONE);
        ItemMeta PerkMeta = Perk.getItemMeta();
        PerkMeta.setDisplayName(config("gui.all_gui.perks.name"));
        PerkMeta.setLore(Arrays.asList(config("gui.all_gui.perks.lore")));
        Perk.setItemMeta(PerkMeta);
        Halter();
        this.inventory.setItem(13, Perk);
        player.openInventory(this.inventory);
    }

    public void Halter() {
        for (int i = 0; i < this.inventory.getSize(); i++) {
            ItemStack Halter = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta HalterMeta = Halter.getItemMeta();
            HalterMeta.setDisplayName("§7");
            Halter.setItemMeta(HalterMeta);
            this.inventory.setItem(i, Halter);
        }
    }
}
