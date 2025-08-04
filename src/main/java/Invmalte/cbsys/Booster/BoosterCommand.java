package Invmalte.cbsys.Booster;

import Invmalte.cbsys.CBSys;
import Invmalte.cbsys.Util.MySQLManager;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BoosterCommand implements CommandExecutor {
    private final MySQLManager sql;
    private final Map<String, Long> activeBoosters;
    private final int boosterDuration = CBSys.getInstance().getConfig().getInt("booster.duration") * 20;
    private String message(String path) {return (String) CBSys.getInstance().getMessageConfig().getString(path, "err");}
    private String config(String path) {return (String) CBSys.getInstance().getConfig().getString(path, "err");}
    public BoosterCommand(MySQLManager sql, Map<String, Long> activeBoosters) {this.sql = sql;this.activeBoosters = activeBoosters;}

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(CBSys.PREFIX + message("booster.help.player").replace("<newline>", "\n"));
            if (sender.hasPermission("cbsys.booster.admin")) {
                sender.sendMessage(message("booster.help.admin").replace("<newline>", "\n"));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("status")) {
            sender.sendMessage(CBSys.PREFIX + message("booster.status.1"));
            String[] boosters = { "haste", "speed", "regeneration", "fly" };
            for (String booster : boosters) {
                boolean active = (this.activeBoosters.containsKey(booster) && ((Long)this.activeBoosters.get(booster)).longValue() > System.currentTimeMillis());
                sender.sendMessage(message("booster.status.2").replace("%booster-name%", booster).replace("%booster-state%", (active ? message("booster.status.true") : message("booster.status.false"))));
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("use")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CBSys.PREFIX + message("all.no.player"));
                return true;
            }
            Player player = (Player) sender;
            if (args.length < 2) {
                sender.sendMessage(CBSys.PREFIX + message("booster.use.use"));
                return true;
            }
            String booster = args[1].toLowerCase();
            UUID uuid = player.getUniqueId();
            if (this.activeBoosters.containsKey(booster) && ((Long)this.activeBoosters.get(booster)).longValue() > System.currentTimeMillis()) {
                player.sendMessage(CBSys.PREFIX + message("booster.use.already_active").replace("%booster%", booster));
                return true;
            }
            Map<String, Integer> boosters = this.sql.loadBoosterData(uuid);
            if (!boosters.containsKey(booster) || ((Integer)boosters.get(booster)).intValue() <= 0) {
                player.sendMessage(CBSys.PREFIX + message("booster.use.no_boost").replace("%booster%", booster));
                return true;
            }
            boosters.put(booster, Integer.valueOf(((Integer)boosters.get(booster)).intValue() - 1));
            this.sql.setBoosterAmount(uuid, booster, ((Integer)boosters.get(booster)).intValue());
            long endTime = System.currentTimeMillis() + (this.boosterDuration / 20) * 1000L;
            this.activeBoosters.put(booster, Long.valueOf(endTime));
            this.sql.saveActiveBooster(booster, endTime);
            Bukkit.broadcastMessage(CBSys.PREFIX + message("booster.use.activated").replace("%booster%", booster).replace("%name%", player.getName()));
            for (Player p : Bukkit.getOnlinePlayers()) {
                switch (booster) {
                    case "haste":
                        p.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, this.boosterDuration, 0));
                    case "speed":
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, this.boosterDuration, 0));
                    case "regeneration":
                        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, this.boosterDuration, 0));
                    case "fly":
                        p.setAllowFlight(true);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, this.boosterDuration, 0));
                }
            }
            Bukkit.getScheduler().runTaskLater((Plugin) CBSys.getInstance(), () -> {
                Bukkit.broadcastMessage(CBSys.PREFIX + message("booster.use.timesup10"));
            }, (this.boosterDuration - 200));
            Bukkit.getScheduler().runTaskLater((Plugin) CBSys.getInstance(), () -> {
                this.activeBoosters.remove(booster);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    switch (booster) {
                        case "haste":
                            p.removePotionEffect(PotionEffectType.HASTE);
                        case "speed":
                            p.removePotionEffect(PotionEffectType.SPEED);
                        case "regeneration":
                            p.removePotionEffect(PotionEffectType.REGENERATION);
                        case "fly":
                            if (player.hasPermission("cmi.command.fly") || player.hasPermission("*")) { player.sendMessage(CBSys.PREFIX + message("booster.use.end_flybypass")); } else { p.setAllowFlight(false); }
                            p.removePotionEffect(PotionEffectType.SLOW_FALLING);
                    }
                }
                Bukkit.broadcastMessage(CBSys.PREFIX + message("booster.use.boost_end").replace("%booster%", booster));
            }, this.boosterDuration);
            return true;
        }
        if (args[0].equalsIgnoreCase("give")) {
            int amount;
            if (!sender.hasPermission("cbsys.booster.admin")) {
                sender.sendMessage(CBSys.PREFIX + message("all.no.perms"));
                return true;
            }
            if (args.length < 4) {
                sender.sendMessage(CBSys.PREFIX + message("booster.give.use"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(CBSys.PREFIX + message("all.player_not_found"));
                return true;
            }
            String booster = args[2].toLowerCase();
            try {
                amount = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                sender.sendMessage(CBSys.PREFIX + message("all.invalid.amount"));
                return true;
            }
            UUID tid = target.getUniqueId();
            Map<String, Integer> boosters = this.sql.loadBoosterData(tid);
            int newAmount = (((Integer)boosters.getOrDefault(booster, Integer.valueOf(0))).intValue() + amount);
            this.sql.setBoosterAmount(tid, booster, newAmount);
            sender.sendMessage(CBSys.PREFIX + message("booster.give.success"));
            return true;
        }
        if (args[0].equalsIgnoreCase("set")) {
            int amount;
            if (!sender.hasPermission("cbsys.booster.admin")) {
                sender.sendMessage(CBSys.PREFIX + message("all.no.perms"));
                return true;
            }
            if (args.length < 4) {
                sender.sendMessage(CBSys.PREFIX + message("booster.set.use"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(CBSys.PREFIX + message("all.player_not_found"));
                return true;
            }
            String booster = args[2].toLowerCase();
            try {
                amount = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                sender.sendMessage(CBSys.PREFIX + message("all.invalid.amount"));
                return true;
            }
            UUID tid = target.getUniqueId();
            int newAmount = amount;
            this.sql.setBoosterAmount(tid, booster, newAmount);
            sender.sendMessage(CBSys.PREFIX + message("booster.set.success"));
            return true;
        }
        if (args[0].equalsIgnoreCase("see")) {
            if (!sender.hasPermission("cbsys.booster.admin")) {
                sender.sendMessage(CBSys.PREFIX + message("all.no.perms"));
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage(CBSys.PREFIX + message("booster.see.use"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(CBSys.PREFIX + message("all.player_not_found"));
                return true;
            }
            String booster = args[2].toLowerCase();
            UUID tid = target.getUniqueId();
            int amount = ((Integer)this.sql.loadBoosterData(tid).getOrDefault(booster, Integer.valueOf(0))).intValue();
            sender.sendMessage(CBSys.PREFIX + message("booster.see.ans").replace("%name%", target.getName()).replace("%amount%", String.valueOf(amount)).replace("%booster%", booster));
            return true;
        }
        return false;
    }
}
