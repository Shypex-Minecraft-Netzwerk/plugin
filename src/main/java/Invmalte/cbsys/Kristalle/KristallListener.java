package Invmalte.cbsys.Kristalle;

import Invmalte.cbsys.CBSys;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class KristallListener implements Listener {
    private String message(String path) {return (String) CBSys.getInstance().getMessageConfig().getString(path, "err");}
    private String config(String path) {return (String) CBSys.getInstance().getConfig().getString(path, "err");}
    @EventHandler
    public void KristallKaufen(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(CBSys.PREFIX + message("kristall.shop.gui.title"))) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack item = event.getCurrentItem();
            if (item == null) return;
            String itemName = item.getItemMeta().getDisplayName();

            if (itemName.equalsIgnoreCase(message("kristall.shop.1.name"))) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), message("kristall.shop.1.cmd1").replace("%player%", player.getName()).replace("%price%", message("kristall.shop.1.preis")));
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), message("kristall.shop.1.cmd2").replace("%player%", player.getName()));
                player.sendMessage(CBSys.PREFIX + message("kristall.shop.1.answer"));
            } else if (itemName.equalsIgnoreCase(message("kristall.shop.2.name"))) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), message("kristall.shop.2.cmd1").replace("%player%", player.getName()).replace("%price%", message("kristall.shop.2.preis")));
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), message("kristall.shop.2.cmd2").replace("%player%", player.getName()));
                player.sendMessage(CBSys.PREFIX + message("kristall.shop.2.answer"));
            } else if (itemName.equalsIgnoreCase(message("kristall.shop.3.name"))) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), message("kristall.shop.3.cmd1").replace("%player%", player.getName()).replace("%price%", message("kristall.shop.3.preis")));
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), message("kristall.shop.3.cmd2").replace("%player%", player.getName()));
                player.sendMessage(CBSys.PREFIX + message("kristall.shop.3.answer"));
            }
        }
    }
}
