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

public class PlotWallCommand implements CommandExecutor {

    private final PlotAPI plotAPI;
    private static String message(String path) {return (String) CBSys.getInstance().getMessageConfig().getString(path, "err");}
    private static String config(String path) {return (String) CBSys.getInstance().getConfig().getString(path, "err");}
    private static <Int> Material block(Int number) {return (Material) Material.valueOf(CBSys.getInstance().getConfig().getString("wall." + number + ".material", "BARRIER"));}
    public PlotWallCommand(PlotAPI plotAPI) {this.plotAPI = plotAPI;}

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("cbsys.wall.use")) {
            PlotPlayer pla = plotAPI.wrapPlayer(((Player) sender).getUniqueId());
            Plot plot = pla.getCurrentPlot();
            if (plot != null) {
                if (!plot.isOwner(player.getUniqueId())) {
                    if (player.hasPermission("cbsys.wall.admin")) {
                        Material wallMaterial;
                        if (args.length == 1) {
                            try {
                                wallMaterial = Material.valueOf(args[0].toUpperCase());
                            } catch (IllegalArgumentException e) {
                                player.sendMessage(CBSys.PREFIX + message("all.invalid.material"));
                                return true;
                            }
                            changePlotWall(plot, wallMaterial, player.getWorld());
                            player.sendMessage(CBSys.PREFIX + message("command.wall.success").replace("%block%", String.valueOf(wallMaterial)));
                        } else {
                            player.sendMessage(CBSys.PREFIX + message("command.wall.usage"));
                            return true;
                        }
                    } else {
                        player.sendMessage(CBSys.PREFIX + message("command.wall.owner_err"));
                        return true;
                    }
                }

                Material wallMaterial = STONE_BRICKS;
                if (args.length > 0) {
                    try {
                        wallMaterial = Material.valueOf(args[0].toUpperCase());
                    } catch (IllegalArgumentException e) {
                        player.sendMessage(CBSys.PREFIX + CBSys.getInstance().getMessageConfig().getString("all.invalid.material"));
                        return true;
                    }
                }

                if (player.hasPermission("cbsys.wall.admin") || player.hasPermission("*")) {
                    changePlotWall(plot, wallMaterial, ((Player) sender).getWorld());
                    player.sendMessage(CBSys.PREFIX + CBSys.getInstance().getMessageConfig().getString("command.wall.success").replace("%block%", String.valueOf(wallMaterial)));
                } else {
                    int wallCount = CBSys.getInstance().getConfig().getInt("wall.count");
                    boolean foundMatch = false;
                    for (int i = 1; i <= wallCount; i++) {
                        String number = String.format("%02d", i);
                        if (wallMaterial == block(number)) {
                            foundBlock(plot, number, player);
                            foundMatch = true;
                            break;
                        }
                    }
                    if(!foundMatch) {
                        player.sendMessage(CBSys.PREFIX + message("command.wall.not_available").replace("%block%", String.valueOf(wallMaterial)));
                    }
                }
                return true;
            } else {
                player.sendMessage(CBSys.PREFIX + message("command.wall.no_plot"));
            }
            return true;
        } else {
            player.sendMessage(CBSys.PREFIX + message("all.no.perms"));
        }
        return true;
    }

    public static <Int> void foundBlock(Plot plot, Int number, Player player) {
        if (player.hasPermission("cbsys.wall.use." + config("wall."+number+".permission")) || player.hasPermission("cbsys.wall.use.*")) {
            changePlotWall(plot, block(number), player.getWorld());
            player.sendMessage(CBSys.PREFIX + message("command.wall.success").replace("%block%", String.valueOf(block(number))));
        } else {
            player.sendMessage(CBSys.PREFIX + message("command.wall.no_perms").replace("%block%", String.valueOf(block(number))));
        }
    }

    private static void changePlotWall(Plot plot, Material material, World world) {
        Set<Plot> plots = plot.getConnectedPlots();

        int minY = CBSys.getInstance().getConfig().getInt("command.wall.minhigh");
        int maxY = CBSys.getInstance().getConfig().getInt("command.wall.maxhigh");

        int minX = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;

        for (Plot p : plots) {
            com.plotsquared.core.location.Location bottom = p.getBottomAbs();
            com.plotsquared.core.location.Location top = p.getTopAbs();

            minX = Math.min(minX, bottom.getX());
            minZ = Math.min(minZ, bottom.getZ());
            maxX = Math.max(maxX, top.getX());
            maxZ = Math.max(maxZ, top.getZ());
        }

        for (int y = minY; y <= maxY; y++) {
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
