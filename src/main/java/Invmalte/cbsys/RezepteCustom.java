package Invmalte.cbsys;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class RezepteCustom

{
    private final JavaPlugin plugin;
    public RezepteCustom(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    public void registerRecipes() {

        //Custom Rezept für Leder
        ItemStack Leder = new ItemStack(Material.LEATHER);
        NamespacedKey key = new NamespacedKey(plugin, "Leder");
        ShapedRecipe Lederrezept = new ShapedRecipe(key, Leder);
        Lederrezept.shape("RFR", "FFF", "RFR");
        Lederrezept.setIngredient('R', Material.ROTTEN_FLESH);
        Lederrezept.setIngredient('F', Material.STRING);

        Bukkit.addRecipe(Lederrezept);

        //Custom Rezept XPBottels
        ItemStack xpBottle = new ItemStack(Material.EXPERIENCE_BOTTLE);
        NamespacedKey Xkey = new NamespacedKey(plugin, "XPBottle");
        ShapedRecipe XPRezept = new ShapedRecipe(Xkey, xpBottle);
        XPRezept.shape(" B ", "BEB", " B ");
        XPRezept.setIngredient('B', Material.GLASS_BOTTLE);
        XPRezept.setIngredient('E', Material.ENDER_EYE);

        Bukkit.addRecipe(XPRezept);

    }
    public void unregisterRecipes() {


        ItemStack Leder = new ItemStack(Material.LEATHER);
        NamespacedKey key = new NamespacedKey(plugin, "Leder");
        ShapedRecipe Lederrezept = new ShapedRecipe(key, Leder);
        Lederrezept.shape("RFR", "FFF", "RFR");
        Lederrezept.setIngredient('R', Material.ROTTEN_FLESH);
        Lederrezept.setIngredient('F', Material.STRING);



        //Custom Rezept XPBottels
        ItemStack xpBottle = new ItemStack(Material.EXPERIENCE_BOTTLE);
        NamespacedKey Xkey = new NamespacedKey(plugin, "XPBottle");
        ShapedRecipe XPRezept = new ShapedRecipe(Xkey, xpBottle);
        XPRezept.shape(" B ", "BEB", " B ");
        XPRezept.setIngredient('B', Material.GLASS_BOTTLE);
        XPRezept.setIngredient('E', Material.ENDER_EYE);

        Bukkit.removeRecipe(key);
        Bukkit.removeRecipe(Xkey);
    }
}
