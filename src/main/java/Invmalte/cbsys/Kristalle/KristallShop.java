package Invmalte.cbsys.Kristalle;

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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class KristallShop implements CommandExecutor {
    private String message(String path) {return (String) CBSys.getInstance().getMessageConfig().getString(path, "err");}
    private String config(String path) {return (String) CBSys.getInstance().getConfig().getString(path, "err");}
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        Inventory inventory = Bukkit.createInventory(null, 45, message("kristall.shop.gui.title"));
        String Prefix = message("kristall.shop.gui.priceprefix");


        ItemStack Crate1 = new ItemStack(Material.ENDER_CHEST);
        ItemMeta Crate1Meta = Crate1.getItemMeta();
        Crate1Meta.setDisplayName(message("kristall.shop.1.name"));
        String Crate1Price = "§e" + message("kristall.shop.1.preis") + " Kristalle";
        ArrayList<String> lore1 = new ArrayList<>();
        lore1.add(Prefix + Crate1Price);
        Crate1Meta.setLore(lore1);
        Crate1.setItemMeta(Crate1Meta);

        ItemStack Crate2 = new ItemStack(Material.SCULK_SHRIEKER);
        ItemMeta Crate2Meta = Crate2.getItemMeta();
        Crate2Meta.setDisplayName(message("kristall.shop.2.name"));
        String Crate2Price = "§e" + message("kristall.shop.2.preis") + " Kristalle";
        ArrayList<String> lore2 = new ArrayList<>();
        lore2.add(Prefix + Crate2Price);
        Crate2Meta.setLore(lore2);
        Crate2.setItemMeta(Crate2Meta);

        ItemStack Crate3 = new ItemStack(Material.RESPAWN_ANCHOR);
        ItemMeta Crate3Meta = Crate3.getItemMeta();
        Crate3Meta.setDisplayName(message("kristall.shop.3.name"));
        String Crate3Price = "§e" + message("kristall.shop.3.preis") + " Kristalle";
        ArrayList<String> lore3 = new ArrayList<>();
        lore3.add(Prefix + Crate3Price);
        Crate3Meta.setLore(lore3);
        Crate3.setItemMeta(Crate3Meta);

        ItemStack Halter = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta HalterMeta = Halter.getItemMeta();
        HalterMeta.setDisplayName("§7");
        Halter.setItemMeta(HalterMeta);

        ItemStack Gelb = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
        ItemMeta GelbMeta = Gelb.getItemMeta();
        GelbMeta.setDisplayName("§7");
        Gelb.setItemMeta(GelbMeta);

        ItemStack Blau = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta BlauMeta = Blau.getItemMeta();
        BlauMeta.setDisplayName("§7");
        Blau.setItemMeta(BlauMeta);

        ItemStack Rot = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta RotMeta = Rot.getItemMeta();
        RotMeta.setDisplayName("§7");
        Rot.setItemMeta(RotMeta);

        inventory.setItem(9, Halter);
        inventory.setItem(11, Gelb);
        inventory.setItem(13, Blau);
        inventory.setItem(15, Rot);
        inventory.setItem(17, Halter);
        inventory.setItem(18, Halter);
        inventory.setItem(20, Crate1);
        inventory.setItem(22, Crate2);
        inventory.setItem(24, Crate3);
        inventory.setItem(26, Halter);
        inventory.setItem(27, Halter);
        inventory.setItem(29, Gelb);
        inventory.setItem(31, Blau);
        inventory.setItem(33, Rot);
        inventory.setItem(35, Halter);

        for (int i = 0; i <9; i++) {
            inventory.setItem(i, Halter);
        }
        for (int i = 36; i < 45; i++) {
            inventory.setItem(i, Halter);
        }
        player.openInventory(inventory);
        return true;
    }
}
