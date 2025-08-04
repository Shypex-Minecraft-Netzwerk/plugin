package Invmalte.cbsys.Commands;

import Invmalte.cbsys.CBSys;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class SignCommand implements CommandExecutor {
    private String message(String path) {return (String) CBSys.getInstance().getMessageConfig().getString(path, "err");}
    private String config(String path) {return (String) CBSys.getInstance().getConfig().getString(path, "err");}
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CBSys.PREFIX + message("all.no.player"));
            return false;
        }

        Player player = (Player) sender;
        if (player.hasPermission("cbsys.sign.use")) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();

            if (itemStack.getType().isAir()) {
                player.sendMessage(CBSys.PREFIX + message("command.sign.no_hand_item"));
                return false;
            }

            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta == null) {
                player.sendMessage(CBSys.PREFIX + message("command.sign.cant_sign_item"));
                return false;
            }

            LocalDate localDate = LocalDate.now();
            LocalTime localTime = LocalTime.now();
            String formatedTime = localTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            String formatedDate = localDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            String name = player.getName();

            String message = String.join(" ", args);
            if (args.length == 0) {
                Arrays array = null;
                itemMeta.setLore(array.asList(
                        config("command.sign.without_text.line1"),
                        config("command.sign.without_text.line2").replace("%name%", name).replace("%date%", formatedDate).replace("%time%", formatedTime)
                ));
            } else {
                String finalMessage = ChatColor.translateAlternateColorCodes('&', message);
                Arrays array = null;
                itemMeta.setLore(array.asList(
                        config("command.sign.with_text.line1"),
                        config("command.sign.with_text.line2").replace("%name%", name).replace("%date%", formatedDate).replace("%time%", formatedTime),
                        config("command.sign.with_text.line3").replace("%message%", finalMessage)
                ));
            }


            itemStack.setItemMeta(itemMeta);
            player.sendMessage(CBSys.PREFIX + message("command.sign.success"));
        } else {
            player.sendMessage(CBSys.PREFIX + message("all.no.perms"));
        }
        return true;
    }
}
