package Invmalte.cbsys.Kristalle;

import static Invmalte.cbsys.CBSys.DEBUG_PREFIX;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class KristallManager {
    private final JavaPlugin plugin;
    private HashMap<UUID, Integer> kristallGuthaben;
    private File guthabenFile;
    private FileConfiguration guthabenConfig;

    public KristallManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void enable() {
        this.kristallGuthaben = new HashMap<>();
        this.guthabenFile = new File(plugin.getDataFolder(), "kristallguthaben.yml");
        plugin.saveDefaultConfig();
        ladeGuthaben();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new KristallPlaceholder(plugin, this).register();
            plugin.getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Kristall-Placeholder erfolgreich registriert.");

        } else {
            plugin.getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "PlaceholderAPI nicht gefunden. Kristalle können nicht im Scoreboard angezeigt werden.");
        }
    }

    public void disable() {
        speichereGuthaben();
    }

    public void ladeGuthaben() {
        if (!this.guthabenFile.exists()) {
            try {
                this.guthabenFile.getParentFile().mkdirs();
                this.guthabenFile.createNewFile();
            } catch (IOException e) {
                plugin.getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Konnte kristallguthaben.yml nicht erstellen!");
            }
        }
        this.guthabenConfig = YamlConfiguration.loadConfiguration(this.guthabenFile);
        for (String key : this.guthabenConfig.getKeys(false)) {
            UUID playerId = UUID.fromString(key);
            int guthaben = this.guthabenConfig.getInt(key);
            this.kristallGuthaben.put(playerId, guthaben);
        }
    }

    public void speichereGuthaben() {
        if (this.guthabenConfig == null) return;
        for (UUID playerId : this.kristallGuthaben.keySet()) {
            this.guthabenConfig.set(playerId.toString(), this.kristallGuthaben.get(playerId));
        }
        try {
            this.guthabenConfig.save(this.guthabenFile);
        } catch (IOException e) {
            plugin.getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Fehler beim Speichern der Kristallguthaben-Daten!");
        }
    }

    public void fügeKristalleHinzu(Player player, int menge) {
        UUID playerId = player.getUniqueId();
        int neuesGuthaben = this.kristallGuthaben.getOrDefault(playerId, 0) + menge;
        this.kristallGuthaben.put(playerId, neuesGuthaben);
    }

    public void entferneKristalle(Player player, int menge) {
        UUID playerId = player.getUniqueId();
        int aktuellesGuthaben = this.kristallGuthaben.getOrDefault(playerId, 0);
        int neuesGuthaben = Math.max(aktuellesGuthaben - menge, 0);
        this.kristallGuthaben.put(playerId, neuesGuthaben);
    }

    public boolean bezahleMitKristallen(Player player, int kosten) {
        UUID playerId = player.getUniqueId();
        int aktuellesGuthaben = this.kristallGuthaben.getOrDefault(playerId, 0);
        if (aktuellesGuthaben >= kosten) {
            this.kristallGuthaben.put(playerId, aktuellesGuthaben - kosten);
            speichereGuthaben();
            return true;
        }
        return false;
    }

    public int getKristallGuthaben(Player player) {
        return this.kristallGuthaben.getOrDefault(player.getUniqueId(), 0);
    }
}
