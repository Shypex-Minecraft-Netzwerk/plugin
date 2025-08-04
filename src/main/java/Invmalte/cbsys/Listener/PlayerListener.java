package Invmalte.cbsys.Listener;

import Invmalte.cbsys.CBSys;
import Invmalte.cbsys.Perk.KCMCPerkListener;
import Invmalte.cbsys.Util.MySQLManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class PlayerListener implements Listener {
    private final Plugin plugin;
    private final MySQLManager sql;
    private KCMCPerkListener perks;

    public PlayerListener(Plugin plugin, MySQLManager sql, KCMCPerkListener perks) {
        this.plugin = plugin;
        this.sql = sql;
        this.perks = perks;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player clearPlayer = event.getPlayer();
        String playerName = event.getPlayer().getName();
        clearPlayer.sendMessage(CBSys.PREFIX +"§aHerzlich Willkommen auf unserem Server. \n" + CBSys.PREFIX + "§aWir wünschen dir Viel Spaß!");
        Bukkit.getConsoleSender().sendMessage(CBSys.PREFIX + playerName + " hat das Netzwerk betreten.");
        String username = this.sql.loadPerksUser(clearPlayer.getUniqueId());
        if (username == null) {
            this.sql.createPerksUser(clearPlayer);
        } else {
            if (!username.equals(clearPlayer.getName())) {
                clearPlayer.sendMessage(CBSys.PREFIX + "Namensänderung wird in die Datenbank eingetragen... Alter Name: §c" + this.sql.loadPerksUser(clearPlayer.getUniqueId()));
                this.sql.updatePerksUser(clearPlayer);
                clearPlayer.sendMessage(CBSys.PREFIX + "Namensänderung erfolgreich übernommen!");
            }
        }
        event.setJoinMessage(null);
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player clearPlayer = event.getPlayer();
        String playerName = event.getPlayer().getName();
        Bukkit.getConsoleSender().sendMessage(CBSys.PREFIX + playerName + " hat das Netzwerk verlassen.");
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Bukkit.getConsoleSender().sendMessage(CBSys.PREFIX + event.getPlayer().getName() + " ist gestorben. " + event.getDeathMessage());
        event.setDeathMessage(null);
    }
}
