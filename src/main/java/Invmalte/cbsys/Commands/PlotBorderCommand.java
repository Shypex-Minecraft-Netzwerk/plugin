package Invmalte.cbsys.Commands;

import Invmalte.cbsys.CBSys;
import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

import static org.bukkit.Material.*;

public class PlotBorderCommand implements CommandExecutor {

    private final PlotAPI plotAPI;
    private static String message(String path) {return (String) CBSys.getInstance().getMessageConfig().getString(path, "err");}
    private static String config(String path) {return (String) CBSys.getInstance().getConfig().getString(path, "err");}
    private static <Int> Material block(Int number) {return (Material) Material.valueOf(CBSys.getInstance().getConfig().getString("border." + number + ".material", "BARRIER"));}
    public PlotBorderCommand(PlotAPI plotAPI) {this.plotAPI = plotAPI;}

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("cbsys.border.use")) {
            PlotPlayer pla = plotAPI.wrapPlayer(((Player) sender).getUniqueId());
            Plot plot = pla.getCurrentPlot();
            if (plot != null) {
                if (!plot.isOwner(player.getUniqueId())) {
                    if (player.hasPermission("cbsys.border.admin")) {
                        Material wallMaterial;
                        if (args.length == 1) {
                            try {
                                wallMaterial = Material.valueOf(args[0].toUpperCase());
                            } catch (IllegalArgumentException e) {
                                player.sendMessage(CBSys.PREFIX + message("all.invalid.material"));
                                return true;
                            }
                            changePlotWall(plot, wallMaterial, player.getWorld());
                            player.sendMessage(CBSys.PREFIX + message("command.border.success").replace("%block%", String.valueOf(wallMaterial)));
                        } else {
                            player.sendMessage(CBSys.PREFIX + message("command.border.usage"));
                            return true;
                        }
                    } else {
                        player.sendMessage(CBSys.PREFIX + message("command.border.owner_err"));
                        return true;
                    }
                }

                Material wallMaterial = TUFF_BRICK_SLAB;
                if (args.length == 1) {
                    try {
                        wallMaterial = Material.valueOf(args[0].toUpperCase());
                    } catch (IllegalArgumentException e) {
                        player.sendMessage(CBSys.PREFIX + message("all.invalid.material"));
                        return true;
                    }
                } else {
                    player.sendMessage(CBSys.PREFIX + message("command.border.usage"));
                    return true;
                }

                if (player.hasPermission("cbsys.border.admin") || player.hasPermission("*")) {
                    changePlotWall(plot, wallMaterial, player.getWorld());
                    player.sendMessage(CBSys.PREFIX + message("command.border.success").replace("%block%", String.valueOf(wallMaterial)));
                } else {
                    int borderCount = CBSys.getInstance().getConfig().getInt("border.count");
                    boolean foundMatch = false;
                    for (int i = 1; i <= borderCount; i++) {
                        String number = String.format("%02d", i);
                        if (wallMaterial == block(number)) {
                            foundBlock(plot, number, player);
                            foundMatch = true;
                            break;
                        }
                    }
                    if(!foundMatch) {
                        player.sendMessage(CBSys.PREFIX + message("command.border.not_available").replace("%block%", String.valueOf(wallMaterial)));
                        return true;
                    }
                }
                return true;
            } else {
                player.sendMessage(CBSys.PREFIX + message("command.border.no_plot"));
            }
            return true;
        } else {
            player.sendMessage(CBSys.PREFIX + message("all.no.perms"));
        }
        return true;
    }

    public static <Int> void foundBlock(Plot plot, Int number, Player player) {
        if (player.hasPermission("cbsys.border.use." + config("border."+number+".permission")) || player.hasPermission("cbsys.border.use.*")) {
            changePlotWall(plot, block(number), player.getWorld());
            player.sendMessage(CBSys.PREFIX + message("command.border.success").replace("%block%", String.valueOf(block(number))));
        } else {
            player.sendMessage(CBSys.PREFIX + message("command.border.no_perms").replace("%block%", String.valueOf(block(number))));
        }
    }

    private static void changePlotWall(Plot plot, Material material, World world) {
        Set<Plot> plots = plot.getConnectedPlots();

        int minX = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;
        int Y = CBSys.getInstance().getConfig().getInt("border.high");

        for (Plot p : plots) {
            com.plotsquared.core.location.Location bottom = p.getBottomAbs();
            com.plotsquared.core.location.Location top = p.getTopAbs();

            minX = Math.min(minX, bottom.getX());
            minZ = Math.min(minZ, bottom.getZ());
            maxX = Math.max(maxX, top.getX());
            maxZ = Math.max(maxZ, top.getZ());
        }

        for (int y = Y; y <= Y; y++) {
            for (int x = minX - 1; x <= maxX + 1; x++) {
                new Location(world, x, y, minZ - 1).getBlock().setType(material);
                new Location(world, x, y, maxZ + 1).getBlock().setType(material);
            }
            for (int z = minZ - 1; z <= maxZ + 1; z++) {
                new Location(world, minX - 1, y, z).getBlock().setType(material);
                new Location(world, maxX + 1, y, z).getBlock().setType(material);
            }
        }
    }
}