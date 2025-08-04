package Invmalte.cbsys.Listener;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class ChatEvent implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        if (player.hasPermission("cbsys.chat.team")) {
            event.setCancelled(true);

            String PlayerName = replacePlaceholder(PlaceholderAPI.setPlaceholders(player, "%cmi_user_nickname%"));
            String Prefix = replacePlaceholder(PlaceholderAPI.setPlaceholders(player, "%cmi_user_prefix%"));
            String Chatcolor = replacePlaceholder(PlaceholderAPI.setPlaceholders(player, "%cmi_user_chatcolor%"));
            String Chatmessage = ChatColor.translateAlternateColorCodes('&', message);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                String name = onlinePlayer.getName();
                if (message.contains(name)) {
                    Chatmessage = Chatmessage.replace(name, "§b@" + name + "§7" + Chatcolor);
                    onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 6);
                }
            }
            Bukkit.getServer().broadcastMessage("§7 ");
            Bukkit.getServer().broadcastMessage(Prefix + PlayerName + " §8» §7" + Chatcolor + Chatmessage);
            Bukkit.getServer().broadcastMessage("§7 ");
            return;
        }

        if (event.getPlayer().hasPermission("cbsys.chat.color")) {
            event.setCancelled(true);
            String PlayerName = replacePlaceholder(PlaceholderAPI.setPlaceholders(player, "%cmi_user_nickname%"));
            String Prefix = replacePlaceholder(PlaceholderAPI.setPlaceholders(player, "%cmi_user_prefix%"));
            String Chatcolor = replacePlaceholder(PlaceholderAPI.setPlaceholders(player, "%cmi_user_chatcolor%"));
            String Chatmessage = ChatColor.translateAlternateColorCodes('&', message);
            Bukkit.getServer().broadcastMessage(Prefix + PlayerName + " §8» §7" + Chatcolor + Chatmessage);
        }
    }
    public String replacePlaceholder(String input) {
        if (input == null) {
            return null;
        } else {
            return input.replace("&", "§");
        }
    }
}
