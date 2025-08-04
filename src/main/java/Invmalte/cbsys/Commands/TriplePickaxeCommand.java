package Invmalte.cbsys.Commands;

import Invmalte.cbsys.CBSys;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class TriplePickaxeCommand implements CommandExecutor {
    private String message(String path) {return (String) CBSys.getInstance().getMessageConfig().getString(path, "err");}
    private String config(String path) {return (String) CBSys.getInstance().getConfig().getString(path, "err");}
    private final JavaPlugin plugin;
    public TriplePickaxeCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CBSys.PREFIX + message("all.no.player"));
            return true;
        }

        if (!player.hasPermission("cbsys.nbt.pickaxe.3x3")) {
            player.sendMessage(CBSys.PREFIX + message("all.no.perms"));
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || !item.getType().name().endsWith("_PICKAXE")) {
            player.sendMessage(CBSys.PREFIX + message("command.triplepickaxe.no_pick"));
            return true;
        }

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(plugin, "cbsystem-3x3pickaxe");
        container.set(key, PersistentDataType.BYTE, (byte) 1);

        item.setItemMeta(meta);
        player.sendMessage(CBSys.PREFIX + message("command.triplepickaxe.success"));
        return true;
    }
}
