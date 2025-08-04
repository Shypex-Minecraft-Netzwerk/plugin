package Invmalte.cbsys.Commands;

import Invmalte.cbsys.CBSys;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.ScopedNode;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.*;
import java.time.Duration;
import java.util.UUID;

public class freerankCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final LuckPerms luckPerms;
    public freerankCommand(CBSys plugin) {this.plugin = plugin;this.luckPerms = LuckPermsProvider.get();}
    private String message(String path) {return (String) CBSys.getInstance().getMessageConfig().getString(path, "err");}
    private String config(String path) {return (String) CBSys.getInstance().getConfig().getString(path, "err");}

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
         if (!(sender instanceof Player)) {
             sender.sendMessage(CBSys.PREFIX + message("all.no.player"));
             return true;
         }

         Player player = (Player) sender;
         UUID uuid = player.getUniqueId();

         if (player.hasPermission("cbsys.freerank.claimed")) {
             player.sendMessage(CBSys.PREFIX + message("command.freerank.already_used"));
         } else {
             User user = luckPerms.getUserManager().getUser(uuid);
             if (user == null) {
                 player.sendMessage(CBSys.PREFIX + message("all.player_not_found"));
                 return true;
             }
             ScopedNode scopedNode1 = ((InheritanceNode.builder(config("command.freerank.rang_name")).value(true)).expiry(Duration.ofDays(Long.parseLong(config("command.freerank.duration"))))).build();
             user.data().add(scopedNode1);
             ScopedNode scopedNode2 = PermissionNode.builder("cbsys.freerank.claimed").build();
             user.data().add(scopedNode2);
             this.luckPerms.getUserManager().saveUser(user);
             player.sendMessage(CBSys.PREFIX + message("command.freerank.claim_success"));
         }
         return true;
    }
}
