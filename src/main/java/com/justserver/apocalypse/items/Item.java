package com.justserver.apocalypse.items;

import com.justserver.apocalypse.Apocalypse;
import com.justserver.apocalypse.Registry;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public abstract class Item extends ItemLoader implements IItem {
    protected Apocalypse plugin;
    protected int count = 1;
    protected final String id;
    public int minIronNuggets = 0;
    public int maxIronNuggets = 0;

    public Item(Apocalypse plugin){
        this.plugin = plugin;
        if(plugin == null){
            this.plugin = Apocalypse.getInstance();
        }
        String preId = getClass().getSimpleName().toUpperCase();
        for(Field field : Registry.class.getFields()){
            field.setAccessible(true);
            if(field.getType().equals(getClass())){
                preId = field.getName();
                break;
            }
        }
        this.id = preId;
        init();
    }
    public String getId(){
        return id;
    }

    public ItemStack createItemStack(Apocalypse plugin){
        ItemStack is = new ItemStack(getMaterial(), count);
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(getRarity().getColor() + customName());
        meta.setCustomModelData(getId().hashCode());
        int slowdown = getSlowdown();
        if(slowdown != 0){
            ArrayList<String> lore = new ArrayList<>();
            if(slowdown > 0){
                lore.add(ChatColor.RED + "-" + slowdown + "% скорости");
            } else {
                lore.add(ChatColor.GREEN + "+" + slowdown + "% скорости");
            }
            meta.setLore(lore);
        }
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "APO_ID"), PersistentDataType.STRING, getId());
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        is.setItemMeta(meta);
        return is;
    }

    public ItemStack createItemStack(Apocalypse plugin, int damage){
        ItemStack is = new ItemStack(getMaterial(), count);
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(getRarity().getColor() + customName());
        meta.setCustomModelData(getId().hashCode());
        int slowdown = getSlowdown();
        if(slowdown != 0){
            ArrayList<String> lore = new ArrayList<>();
            if(slowdown > 0){
                lore.add(ChatColor.RED + "-" + slowdown + "% скорости");
            } else {
                lore.add(ChatColor.GREEN + "+" + slowdown + "% скорости");
            }
            meta.setLore(lore);
        }
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "APO_ID"), PersistentDataType.STRING, getId());
        ((Damageable)meta).setDamage(damage);

        is.setItemMeta(meta);
        return is;
    }

    @Override
    protected void init() {

    }
    private static final SecureRandom random = new SecureRandom();

    public ItemStack generateRandomNuggets(int amount){
        return new ItemStack(Material.IRON_NUGGET, generateInRange(minIronNuggets, maxIronNuggets) * amount);
    }

    private int generateInRange(int min, int max){
        if(min == max) return min;
        return random.nextInt(max - min) + min;
    }
}
