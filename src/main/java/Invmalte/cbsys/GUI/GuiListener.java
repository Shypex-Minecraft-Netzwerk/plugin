package Invmalte.cbsys.GUI;

import Invmalte.cbsys.CBSys;
import Invmalte.cbsys.Perk.KCMCPerkShopGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GuiListener implements Listener {
    private final KCMCPerkShopGUI perkGui;
    private String message(String path) {return (String) CBSys.getInstance().getMessageConfig().getString(path, "err");}
    private String config(String path) {return (String) CBSys.getInstance().getConfig().getString(path, "err");}

    public GuiListener(KCMCPerkShopGUI perkGui) {
        this.perkGui = perkGui;
    }

    @EventHandler
    public void GUI(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(config("gui.all_gui.title").replace("%servername%", message("all.servername")))) {
            event.setCancelled(true);
            Player player = (Player)event.getWhoClicked();
            ItemStack item = event.getCurrentItem();
            String ItemName = item.getItemMeta().getDisplayName();
            if (ItemName.equalsIgnoreCase(config("gui.all_gui.perks.name")))
                this.perkGui.Gui(player);
        }
    }
}
