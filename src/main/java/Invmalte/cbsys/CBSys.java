package Invmalte.cbsys;


import Invmalte.cbsys.Booster.*;
import Invmalte.cbsys.Commands.*;
import Invmalte.cbsys.GUI.*;
import Invmalte.cbsys.Kristalle.*;
import Invmalte.cbsys.Listener.*;
import Invmalte.cbsys.Perk.*;
import Invmalte.cbsys.Util.*;
import Invmalte.cbsys.Commands.PlotBorderCommand;
import Invmalte.cbsys.Commands.PlotWallCommand;
import Invmalte.cbsys.Commands.freerankCommand;
import com.plotsquared.core.PlotAPI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;

public final class CBSys extends JavaPlugin {
    public static String PREFIX;
    public static String DEBUG_PREFIX = "§aCB-System §1DEBUG §8» §7";
    private PlotAPI plotAPI;
    private LuckPerms luckPerms;
    private File configFile;
    private File messageFile;
    private File sqlFile;
    private FileConfiguration configFileConfig;
    private FileConfiguration messageFileConfig;
    private FileConfiguration sqlFileConfig;
    private static CBSys instance;
    private Economy economy;
    private Gui gui;
    private KCMCPerkShopGUI perkShopGui;
    private KCMCPerkShopListener perkShopGuiListener;
    private KCMCPerkGUI perkGUI;
    private KCMCPerkListener perkGuiListener;
    private KristallManager kristallManager;
    private MySQLManager mySQLManager;
    public static boolean cmi = false;
    public static boolean started = false;
    public DecimalFormat formatter = new DecimalFormat("#,###", DecimalFormatSymbols.getInstance(Locale.GERMANY));


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        this.mySQLManager = new MySQLManager();
        try {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            economy = rsp.getProvider();
            saveDefaultConfig();
            reloadConfig();
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Plugin start begin...");

            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Check Depends and Softdepends");
            Plugin plotPlugin = Bukkit.getPluginManager().getPlugin("PlotSquared");
            if (plotPlugin == null) {
                getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "PlotSquared not found! Install it and restart the Server!\n" + DEBUG_PREFIX + "§cPlugin disabled!");
                throw new RuntimeException("PlotSquared not found – plugin not loaded.");
            }
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "PlotSquared found.");
            Plugin luckpermsPlugin = Bukkit.getPluginManager().getPlugin("LuckPerms");
            if (luckpermsPlugin == null) {
                getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "LuckPerms not found! Install it and restart the Server!\n" + DEBUG_PREFIX + "§cPlugin disabled!");
                throw new RuntimeException("LuckPerms not found – plugin not loaded.");
            }
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "LuckPerms found.");
            Plugin papiPlugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
            if (papiPlugin == null) {
                getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "PlaceholderAPI not found! Install it and restart the Server!\n" + DEBUG_PREFIX + "§cPlugin disabled!");
                throw new RuntimeException("PlaceholderAPI not found – plugin not loaded.");
            }
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "PlaceholderAPI found.");
            Plugin vaultPlugin = Bukkit.getPluginManager().getPlugin("Vault");
            if (vaultPlugin == null) {
                getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Vault not found! Install it and restart the Server!\n" + DEBUG_PREFIX + "§cPlugin disabled!");
                throw new RuntimeException("Vault not found – plugin not loaded.");
            }
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Vault found.");
            Plugin cmiPlugin = Bukkit.getPluginManager().getPlugin("CMI");
            if (cmiPlugin != null) {
                cmi = true;
                getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "CMI found! Enable CMI Modules");
            } else {
                getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "CMI not found! Disable CMI Modules");
            }
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "All Depends found!");

            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Starting asset loading...");
            loadMessageConfig();
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "message.yml loaded");
            loadConfig();
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "config.yml loaded");
            loadSqlConfig();
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "sql.yml loaded");
            this.plotAPI = new PlotAPI();
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Plot API asset");
            this.gui = new Gui();
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "GUI asset");
            this.perkGUI = new KCMCPerkGUI(mySQLManager);
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "PerkGUI asset");
            this.perkShopGui = new KCMCPerkShopGUI(mySQLManager);
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "PerkShopGUI asset");
            this.luckPerms = LuckPermsProvider.get();
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Luckperms Provider asset");
            kristallManager = new KristallManager(this);
            kristallManager.enable();
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Kristall Manager");
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Successfully end asset loading.");

            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Starting command loading...");
            getCommand("border").setExecutor(new PlotBorderCommand(plotAPI));
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Border Command");
            getCommand("Fullbright").setExecutor(new FullBrightCommand());
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Fullbright Command");
            getCommand("wall").setExecutor(new PlotWallCommand(plotAPI));
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Wall Command");
            getCommand("freerank").setExecutor(new freerankCommand(this));
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Freerank Command");
            getCommand("AllGui").setExecutor(new Gui());
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Allgui Command");
            getCommand("teamchat").setExecutor(new teamchatCommand());
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Teamchat Command");
            getCommand("discord").setExecutor(new DiscordCommand());
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Discord Command");
            getCommand("perks").setExecutor(new KCMCPerkGUI(mySQLManager));
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Perks Command");
            getCommand("perkshop").setExecutor(new KCMCPerkShopGUI(mySQLManager));
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Perkshop Command");
            getCommand("levelpay").setExecutor(new XPPayCommand());
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Levelpay Command");
            getCommand("Sign").setExecutor(new SignCommand());
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Sign Command");
            getCommand("Serversign").setExecutor(new ServerSignCommand());
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Serversign Command");
            getCommand("kristallshop").setExecutor(new KristallShop());
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Kristallshop Command");
            getCommand("kristalle").setExecutor(new KristallCommand(this, kristallManager));
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Kristalle Command");
            getCommand("kristallpay").setExecutor(new KristallCommand(this, kristallManager));
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Kristallpay Command");
            getCommand("triplepickaxe").setExecutor(new TriplePickaxeCommand(this));
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Tripepickaxe Command");
            getCommand("rand").setExecutor(new RandGUI());
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "RandGUI command");
            getCommand("wand").setExecutor(new WandGUI());
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "WandGUI command");
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Succesfully end Command load.");

            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Starting loading Listeners...");
            Bukkit.getPluginManager().registerEvents(new KCMCPerkListener(perkGUI, mySQLManager), this);
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Perk Listener");
            Bukkit.getPluginManager().registerEvents(new KCMCPerkShopListener(perkShopGui, economy, mySQLManager),this);
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Perkshop Listener");
            Bukkit.getPluginManager().registerEvents(new GuiListener(perkShopGui), this);
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "GUI Listener");
            Bukkit.getPluginManager().registerEvents(new ChatEvent(), this);
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Chat Listener");
            Bukkit.getPluginManager().registerEvents(new KristallListener(), this);
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Kristall Listener");
            Bukkit.getPluginManager().registerEvents(new TriplePickaxeListener(this), this);
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Spitzhacken Listener");
            Bukkit.getPluginManager().registerEvents(new RandGUIListener(this.plotAPI), this);
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "RandGUI Listener");
            Bukkit.getPluginManager().registerEvents(new WandGUIListener(this.plotAPI), this);
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "WandGUI Listener");
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Succesfully end Listener load.");

            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Starting loading other files...");
            RezepteCustom rezepteCustom = new RezepteCustom(this);
            rezepteCustom.registerRecipes();
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Custom Rezepte Manager");
            PREFIX = getMessageConfig().getString("all.prefix");
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Prefix loaded");

            this.mySQLManager.connect(sqlFileConfig.getString("mysql.host"), sqlFileConfig.getInt("mysql.port"), sqlFileConfig.getString("mysql.database"), sqlFileConfig.getString("mysql.username"), sqlFileConfig.getString("mysql.password"));
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Connected to Database!");
            Map<String, Long> loadedBoosters = this.mySQLManager.loadActiveBoosters();
            getCommand("booster").setExecutor(new BoosterCommand(this.mySQLManager, loadedBoosters));
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Booster command");
            Bukkit.getPluginManager().registerEvents(new PlayerListener(this, mySQLManager, perkGuiListener), this);
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Player Listener");
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Succesfully end Second load.");
            started = true;
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "§aSuccesfully loaded Plugin");
        } catch (SQLException e) {
            getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Database Connection failed!! Check it and restart the Server!\n" + DEBUG_PREFIX + "§cPlugin disabled!");
            e.printStackTrace();
            throw new RuntimeException("Database Connection failed – plugin not loaded.");
        }
    }

    @Override
    public void onDisable() {
        if (!started) return;
        getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Deactivating Custom Recipes...");
        RezepteCustom rezepteCustom = new RezepteCustom(this);
        rezepteCustom.unregisterRecipes();
        getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Custom Recipes deactivated!");
        getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Saving Kristalle...");
        kristallManager.disable();
        getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Kristalle saved!");
        this.mySQLManager.disconnect();
        getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "Disconnected from Database");
        getServer().getConsoleSender().sendMessage(DEBUG_PREFIX + "§4Plugin deactivated.");
    }

    public static CBSys getInstance() {return instance;}
    public void saveConfigData(){saveConfig();}
    public FileConfiguration getMessageConfig() {return messageFileConfig;}
    public FileConfiguration getConfig() {return configFileConfig;}
    public MySQLManager getMySQLManager() {return this.mySQLManager;}
    public void loadConfig() {configFile = new File(getDataFolder(), "config.yml"); if (!configFile.exists()) saveResource("config.yml", false); configFileConfig = YamlConfiguration.loadConfiguration(configFile);}
    public void loadMessageConfig() {messageFile = new File(getDataFolder(), "message.yml"); if (!messageFile.exists()) saveResource("message.yml", false); messageFileConfig = YamlConfiguration.loadConfiguration(messageFile);}
    public void loadSqlConfig() {sqlFile = new File(getDataFolder(), "sql.yml"); if (!sqlFile.exists()) saveResource("sql.yml", false); sqlFileConfig = YamlConfiguration.loadConfiguration(sqlFile);}
}
