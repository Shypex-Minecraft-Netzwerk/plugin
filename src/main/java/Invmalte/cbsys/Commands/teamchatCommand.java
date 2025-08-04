package Invmalte.cbsys.Commands;

import Invmalte.cbsys.CBSys;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class teamchatCommand implements CommandExecutor {
    private String message(String path) {return (String) CBSys.getInstance().getMessageConfig().getString(path, "err");}
    private String config(String path) {return (String) CBSys.getInstance().getConfig().getString(path, "err");}
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player)sender;
        if (!player.hasPermission("cbsys.teamchat.send")) {
            player.sendMessage(CBSys.PREFIX + message("all.no.perms"));
            return false;
        }

        if (args.length == 0) {
            player.sendMessage(CBSys.PREFIX + message("command.teamchat.use"));
            return false;
        }

        String message = String.join(" ", args);
        String Prefix = config("command.teamchat.prefix").replace("%name%", player.getName());
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("cbsys.teamchat.see"))
                onlinePlayer.sendMessage(Prefix + message);
        }
        return true;
    }
}
