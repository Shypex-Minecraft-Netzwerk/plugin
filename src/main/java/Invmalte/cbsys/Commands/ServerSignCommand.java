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
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class ServerSignCommand implements CommandExecutor {
    private String message(String path) {return (String) CBSys.getInstance().getMessageConfig().getString(path, "err");}
    private String config(String path) {return (String) CBSys.getInstance().getConfig().getString(path, "err");}
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CBSys.PREFIX + message("all.no.player"));
            return false;
        }

        Player player = (Player) sender;
        if (player.hasPermission("cbsys.serversign.use")) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();

            if (itemStack.getType().isAir()) {
                player.sendMessage(CBSys.PREFIX + message("command.serversign.no_hand_item"));
                return false;
            }

            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta == null) {
                player.sendMessage(CBSys.PREFIX + message("command.serversign.cant_sign_item"));
                return false;
            }

            LocalDate localDate = LocalDate.now();
            String formatedDate = localDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

            String message = String.join(" ", args);
            if (args.length == 0) {
                Arrays array = null;
                itemMeta.setLore(array.asList(
                        config("command.serversign.without_text.line1"),
                        config("command.serversign.without_text.line2").replace("%date%", formatedDate).replace("%servername%", message("all.servername"))
                ));
            } else {
                String finalMessage = ChatColor.translateAlternateColorCodes('&', message);
                Arrays array = null;
                itemMeta.setLore(array.asList(
                        config("command.serversign.with_text.line1"),
                        config("command.serversign.with_text.line2").replace("%date%", formatedDate).replace("%servername%", message("all.servername")),
                        config("command.serversign.with_text.line3").replace("%message%", finalMessage)
                ));
            }


            itemStack.setItemMeta(itemMeta);
            player.sendMessage(CBSys.PREFIX + message("command.serversign.success"));
        } else {
            player.sendMessage(CBSys.PREFIX + message("all.no.perms"));
        }
        return true;
    }
}
