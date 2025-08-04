package Invmalte.cbsys.Kristalle;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class KristallPlaceholder extends PlaceholderExpansion {

    private final KristallManager manager;
    private final JavaPlugin plugin;

    public KristallPlaceholder(JavaPlugin plugin, KristallManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "cbsys";
    }

    @Override
    public String getAuthor() {
        return "Invmalte";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (player == null) return "";

        if (params.equalsIgnoreCase("kristallanzahl")) {
            int kristalle = manager.getKristallGuthaben(player);
            return String.valueOf(kristalle);
        }

        return null;
    }
}