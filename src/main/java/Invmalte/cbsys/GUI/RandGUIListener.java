package Invmalte.cbsys.GUI;

import Invmalte.cbsys.Commands.PlotBorderCommand;
import Invmalte.cbsys.CBSys;
import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class RandGUIListener implements Listener {
    private final PlotAPI plotAPI;
    private String message(String path) {return (String) CBSys.getInstance().getMessageConfig().getString(path, "err");}
    private String config(String path) {return (String) CBSys.getInstance().getConfig().getString(path, "err");}
    private <Int> Material block(Int number) {return (Material) Material.valueOf(CBSys.getInstance().getConfig().getString("border." + number + ".gui-block", "BARRIER"));}
    public RandGUIListener(PlotAPI plotAPI) {this.plotAPI = plotAPI;}

    @EventHandler
    public boolean GUI(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(config("border.gui.title").replace("%servername%", message("all.servername")))) {
            PlotPlayer pla = plotAPI.wrapPlayer(event.getWhoClicked().getUniqueId());
            Plot plot = pla.getCurrentPlot();
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack item = event.getCurrentItem();

            if (item == null) return true;

            int borderCount = CBSys.getInstance().getConfig().getInt("border.count");
            for (int i = 1; i <= borderCount; i++) {
                String number = String.format("%02d", i);
                if (item.getType() == block(number)) {
                    PlotBorderCommand.foundBlock(plot, number, player);
                    break;
                }
            }
        }
        return false;
    }

}
