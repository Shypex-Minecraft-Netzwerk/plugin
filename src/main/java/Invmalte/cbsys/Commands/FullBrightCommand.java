package Invmalte.cbsys.Commands;

import Invmalte.cbsys.CBSys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class FullBrightCommand implements CommandExecutor {
    private String message(String path) {return (String) CBSys.getInstance().getMessageConfig().getString(path, "err");}
    private String config(String path) {return (String) CBSys.getInstance().getConfig().getString(path, "err");}
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CBSys.PREFIX + message("all.no.player"));
            return true;
        }
        if (!(sender.hasPermission("cbsys.fullbright.use"))) {
            sender.sendMessage(CBSys.PREFIX + message("all.no.perms"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(CBSys.PREFIX + message("command.fullbright.use"));
            return true;
        }

        if (args[0].equalsIgnoreCase("on")) {
            if (!player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
                player.sendMessage(CBSys.PREFIX + message("command.fullbright.on"));
            } else {
                player.sendMessage(CBSys.PREFIX + message("command.fullbright.already_on"));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("off")) {
            if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                player.sendMessage(CBSys.PREFIX + message("command.fullbright.off"));
            } else {
                player.sendMessage(CBSys.PREFIX + message("command.fullbright.already_off"));
            }
            return true;
        }

        player.sendMessage(CBSys.PREFIX + message("command.fullbright.use"));
        return true;
    }
}