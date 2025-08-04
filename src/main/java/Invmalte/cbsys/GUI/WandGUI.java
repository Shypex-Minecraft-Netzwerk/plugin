package Invmalte.cbsys.GUI;

import Invmalte.cbsys.CBSys;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class WandGUI implements CommandExecutor {
    Inventory wandgui = Bukkit.createInventory(null, 45, config("wall.gui.title").replace("%servername%", message("all.servername")));

    private String message(String path) {return (String) CBSys.getInstance().getMessageConfig().getString(path, "err");}
    private String config(String path) {return (String) CBSys.getInstance().getConfig().getString(path, "err");}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (!(sender instanceof Player)) {
            sender.sendMessage(CBSys.PREFIX + message("all.no.player"));
            return true;
        }
        if (!player.hasPermission("cbsys.wall.use")) {
            sender.sendMessage(CBSys.PREFIX + message("all.no.perms"));
            return true;
        }

        randGUI(player);
        return false;
    }

    public void randGUI(Player player) {
        FileConfiguration config = CBSys.getInstance().getConfig();
        Halter();

        int playerCount = config.getInt("wall.gui.rang.01.amount");
        for (int i = 1; i <= playerCount && (1 + i) < 45; i++) {
            String number = String.format("%02d", i);
            ItemStack item = new ItemStack(Material.valueOf(config.getString("wall." + number + ".gui-block")));
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(config.getString("wall." + number + ".name", "err"));
            item.setItemMeta(itemMeta);

            this.wandgui.setItem(1 + i, item);
        }

        int premiumCount = config.getInt("wall.gui.rang.02.amount");
        for (int i = 1; i <= premiumCount && (1 + i) < 45; i++) {
            String number = String.format("%02d", i  + config.getInt("wall.gui.rang.01.amount"));
            ItemStack item = new ItemStack(Material.valueOf(config.getString("wall." + number + ".gui-block")));
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(config.getString("wall." + number + ".name", "err"));
            item.setItemMeta(itemMeta);

            this.wandgui.setItem(10 + i, item);
        }

        int titanCount = config.getInt("wall.gui.rang.03.amount");
        for (int i = 1; i <= titanCount && (1 + i) < 45; i++) {
            String number = String.format("%02d", i  + config.getInt("wall.gui.rang.01.amount") + config.getInt("wall.gui.rang.02.amount"));
            ItemStack item = new ItemStack(Material.valueOf(config.getString("wall." + number + ".gui-block")));
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(config.getString("wall." + number + ".name", "err"));
            item.setItemMeta(itemMeta);

            this.wandgui.setItem(19 + i, item);
        }

        int platinCount = config.getInt("wall.gui.rang.04.amount");
        for (int i = 1; i <= platinCount && (1 + i) < 45; i++) {
            String number = String.format("%02d", i + config.getInt("wall.gui.rang.01.amount") + config.getInt("wall.gui.rang.02.amount") + config.getInt("wall.gui.rang.03.amount"));
            ItemStack item = new ItemStack(Material.valueOf(config.getString("wall." + number + ".gui-block")));
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(config.getString("wall." + number + ".name", "err"));
            item.setItemMeta(itemMeta);

            this.wandgui.setItem(28 + i, item);
        }

        int mythicCount = config.getInt("wall.gui.rang.05.amount");
        for (int i = 1; i <= mythicCount && (1 + i) < 45; i++) {
            String number = String.format("%02d", i  + config.getInt("wall.gui.rang.01.amount") + config.getInt("wall.gui.rang.02.amount") + config.getInt("wall.gui.rang.03.amount") + config.getInt("wall.gui.rang.04.amount"));
            ItemStack item = new ItemStack(Material.valueOf(config.getString("wall." + number + ".gui-block")));
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(config.getString("wall." + number + ".name", "err"));
            item.setItemMeta(itemMeta);

            this.wandgui.setItem(37 + i, item);
        }

        ItemStack one = new ItemStack(Material.valueOf(config.getString("wall.gui.rang.01.gui-block")));
        ItemMeta oneMeta = one.getItemMeta();
        oneMeta.setDisplayName(config.getString("wall.gui.rang.01.name", "err"));
        one.setItemMeta(oneMeta);
        this.wandgui.setItem(0, one);

        ItemStack two = new ItemStack(Material.valueOf(config.getString("wall.gui.rang.02.gui-block")));
        ItemMeta twoMeta = two.getItemMeta();
        twoMeta.setDisplayName(config.getString("wall.gui.rang.02.name", "err"));
        two.setItemMeta(twoMeta);
        this.wandgui.setItem(9, two);

        ItemStack three = new ItemStack(Material.valueOf(config.getString("wall.gui.rang.03.gui-block")));
        ItemMeta threeMeta = three.getItemMeta();
        threeMeta.setDisplayName(config.getString("wall.gui.rang.03.name", "err"));
        three.setItemMeta(threeMeta);
        this.wandgui.setItem(18, three);

        ItemStack four = new ItemStack(Material.valueOf(config.getString("wall.gui.rang.04.gui-block")));
        ItemMeta fourMeta = four.getItemMeta();
        fourMeta.setDisplayName(config.getString("wall.gui.rang.04.name", "err"));
        four.setItemMeta(fourMeta);
        this.wandgui.setItem(27, four);

        ItemStack five = new ItemStack(Material.valueOf(config.getString("wall.gui.rang.05.gui-block")));
        ItemMeta fiveMeta = five.getItemMeta();
        fiveMeta.setDisplayName(config.getString("wall.gui.rang.05.name", "err"));
        five.setItemMeta(fiveMeta);
        this.wandgui.setItem(36, five);

        player.openInventory(this.wandgui);
    }

    public void Halter() {
        for (int i = 0; i < this.wandgui.getSize(); i++) {
            ItemStack Halter = new ItemStack(Material.valueOf(config("wall.gui.fill.gui-block")));
            ItemMeta HalterMeta = Halter.getItemMeta();
            HalterMeta.setDisplayName(config("wall.gui.fill.name"));
            Halter.setItemMeta(HalterMeta);
            this.wandgui.setItem(i, Halter);
        }
    }
}

