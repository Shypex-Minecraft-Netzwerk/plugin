package Invmalte.cbsys.Commands;

import Invmalte.cbsys.CBSys;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DiscordCommand implements CommandExecutor {
    private String message(String path) {return (String) CBSys.getInstance().getMessageConfig().getString(path, "err");}
    private String config(String path) {return (String) CBSys.getInstance().getConfig().getString(path, "err");}
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CBSys.PREFIX + message("all.no.player"));
            return true;
        }

        TextComponent message = new TextComponent(message("command.discord.message"));
        TextComponent prefix = new TextComponent(CBSys.PREFIX);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, config("command.discord.link")));
        prefix.addExtra(message);
        sender.spigot().sendMessage(prefix);
        return true;
    }
}