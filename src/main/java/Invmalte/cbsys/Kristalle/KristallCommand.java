package Invmalte.cbsys.Kristalle;

import Invmalte.cbsys.CBSys;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class KristallCommand implements CommandExecutor {
    private String message(String path) {return (String) CBSys.getInstance().getMessageConfig().getString(path, "err");}
    private String config(String path) {return (String) CBSys.getInstance().getConfig().getString(path, "err");}
    private final KristallManager manager;
    private final JavaPlugin plugin;

    public KristallCommand(JavaPlugin plugin, KristallManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = plugin.getConfig();

        if (command.getName().equalsIgnoreCase("kristalle")) {
            // Admin-Befehle: add / remove
            if (args.length == 3 && (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove"))) {
                Player zielSpieler = Bukkit.getPlayer(args[1]);
                if (zielSpieler == null) {
                    String spielerNichtGefunden = message("all.player_not_found")
                            .replace("%player%", args[1]);
                    sender.sendMessage(CBSys.PREFIX + spielerNichtGefunden);
                    return true;
                }

                try {
                    int menge = Integer.parseInt(args[2]);

                    if (args[0].equalsIgnoreCase("add")) {
                        manager.fügeKristalleHinzu(zielSpieler, menge);
                        String msg = message("kristall.cmd.add")
                                .replace("%amount%", String.valueOf(menge))
                                .replace("%player%", zielSpieler.getName());
                        sender.sendMessage(CBSys.PREFIX + msg);
                    } else {
                        manager.entferneKristalle(zielSpieler, menge);
                        String msg = message("kristall.cmd.remove")
                                .replace("%amount%", String.valueOf(menge))
                                .replace("%player%", zielSpieler.getName());
                        sender.sendMessage(CBSys.PREFIX + msg);
                    }

                } catch (NumberFormatException e) {
                    sender.sendMessage(CBSys.PREFIX + CBSys.getInstance().getMessageConfig().getString("all.invalid.amount"));
                }

                return true;
            }

            // Spieler → Guthaben anzeigen oder Hinweis
            if (sender instanceof Player player) {
                if (args.length == 0) {
                    int guthaben = manager.getKristallGuthaben(player);
                    String message = message("kristall.cmd.amount")
                            .replace("%amount%", String.valueOf(guthaben));
                    player.sendMessage(CBSys.PREFIX + message);
                } else {
                    String usage = message("kristall.cmd.use");
                    player.sendMessage(CBSys.PREFIX + usage);
                }
                return true;
            }

            // Konsole → Nur diese zwei Befehle erlaubt
            sender.sendMessage(CBSys.PREFIX + message("kristall.cmd.use"));
            return true;
        }


        if (command.getName().equalsIgnoreCase("kristallpay")) {
            if (args.length != 2) {
                sender.sendMessage(CBSys.PREFIX + CBSys.getInstance().getMessageConfig().getString("kristall.pay.use"));
                return true;
            }

            Player zielSpieler = Bukkit.getPlayer(args[0]);
            if (zielSpieler == null) {
                sender.sendMessage(CBSys.PREFIX + CBSys.getInstance().getMessageConfig().getString("all.player_not_found"));
                return true;
            }

            try {
                int kosten = Integer.parseInt(args[1]);

                if (manager.bezahleMitKristallen(zielSpieler, kosten)) {
                    sender.sendMessage(CBSys.PREFIX + CBSys.getInstance().getMessageConfig().getString("kristall.pay.sender").replace("%amount%", String.valueOf(kosten)).replace("%target%", zielSpieler.getName()));
                    zielSpieler.sendMessage(CBSys.PREFIX + CBSys.getInstance().getMessageConfig().getString("kristall.pay.target").replace("%amount%", String.valueOf(kosten)).replace("%sender%", sender.getName()));
                } else {
                    sender.sendMessage(CBSys.PREFIX + CBSys.getInstance().getMessageConfig().getString("kristall.pay.sender_err"));
                }

            } catch (NumberFormatException e) {
                sender.sendMessage(CBSys.PREFIX + CBSys.getInstance().getMessageConfig().getString("all.invalid.amount"));
            }

            return true;
        }

        return false;
    }
}
