package Invmalte.cbsys.Commands;

import Invmalte.cbsys.CBSys;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class XPPayCommand implements CommandExecutor {
    private String message(String path) {return (String) CBSys.getInstance().getMessageConfig().getString(path, "err");}
    private String config(String path) {return (String) CBSys.getInstance().getConfig().getString(path, "err");}
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            sender.sendMessage(CBSys.PREFIX + message("command.xppay.use"));
            return false;
        }

        Player player = (Player) sender;
        String playerName = args[0];
        int Level = 0;

        try {
            Level = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(CBSys.PREFIX + message("all.invalid.amount"));
            return false;
        }

        if (player.getLevel() < Level) {
            player.sendMessage(CBSys.PREFIX + message("command.xppay.too_many"));
            return false;
        }

        Player ZielPlayer = Bukkit.getPlayer(playerName);
        if (ZielPlayer == player) {
            player.sendMessage(CBSys.PREFIX + message("command.xppay.self"));
            return false;
        }

        if (ZielPlayer != null) {
            ZielPlayer.setLevel(ZielPlayer.getLevel() + Level);
            player.setLevel(player.getLevel() - Level);
            player.sendMessage(CBSys.PREFIX + message("command.xppay.sender").replace("%level%", String.valueOf(Level)).replace("%target%", ZielPlayer.getName()));
            ZielPlayer.sendMessage(CBSys.PREFIX + message("command.xppay.target").replace("%level%", String.valueOf(Level)).replace("%sender%", player.getName()));
        } else {
            player.sendMessage(CBSys.PREFIX + message("all.player_not_found"));
        }
return true;
    }
}
