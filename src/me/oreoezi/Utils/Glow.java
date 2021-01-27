package me.oreoezi.Utils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class Glow {
    public static ItemStack addGlow(ItemStack item){
    	ItemStack its = item;
    	its.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
    	ItemMeta meta = (ItemMeta) its.getItemMeta();
    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    	its.setItemMeta(meta);
    	return its;
    }
    public static ItemStack removeGlow(ItemStack item){
    	ItemStack its = item;
    	its.removeEnchantment(Enchantment.OXYGEN);
    	return its;
    }
}
