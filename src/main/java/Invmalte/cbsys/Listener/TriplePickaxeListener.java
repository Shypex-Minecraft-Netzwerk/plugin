package Invmalte.cbsys.Listener;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class TriplePickaxeListener implements Listener {
    private final JavaPlugin plugin;
    private final NamespacedKey tagKey;
    public TriplePickaxeListener(JavaPlugin plugin) {this.plugin = plugin;this.tagKey = new NamespacedKey(plugin, "cbsystem-3x3pickaxe");}


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block center = event.getBlock();
        ItemStack tool = player.getInventory().getItemInMainHand();
        event.setCancelled(false);

        if (tool == null || !tool.getType().name().endsWith("_PICKAXE")) return;
        if (!tool.hasItemMeta()) return;
        ItemMeta meta = tool.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (meta == null || !container.has(tagKey, PersistentDataType.BYTE)) return;

        List<Block> toBreak;

        int playerHeadY = (int) Math.floor(player.getLocation().getY()+1);
        int blockY = (int) Math.floor(center.getY());

        if (blockY < playerHeadY) {
            toBreak = getHorizontalLayer(center);
        } else if (blockY > playerHeadY) {
            toBreak = getHorizontalLayer(center);
        } else {
            BlockFace face = player.getFacing();
            toBreak = getVerticalWall(center, face);
        }

        for (Block block : toBreak) {
            if (block.getType() != Material.AIR && block.getType().isBlock()) {
                block.breakNaturally(tool);
            }
        }
    }




    private List<Block> getHorizontalLayer(Block center) {
        List<Block> blocks = new ArrayList<>();
        int cx = center.getX();
        int cy = center.getY();
        int cz = center.getZ();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                blocks.add(center.getWorld().getBlockAt(cx + dx, cy, cz + dz));
            }
        }
        return blocks;
    }

    private List<Block> getVerticalWall(Block center, BlockFace facing) {
        List<Block> blocks = new ArrayList<>();
        int cx = center.getX();
        int cy = center.getY();
        int cz = center.getZ();

        boolean wallAlongZ = (facing == BlockFace.EAST || facing == BlockFace.WEST);

        for (int dy = -1; dy <= 1; dy++) {
            for (int d = -1; d <= 1; d++) {
                int x = cx;
                int y = cy + dy;
                int z = cz;

                if (wallAlongZ) {
                    z += d;
                } else {
                    x += d;
                }

                blocks.add(center.getWorld().getBlockAt(x, y, z));
            }
        }
        return blocks;
    }
}
