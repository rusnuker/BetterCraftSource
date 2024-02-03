// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.bukkit.listeners.protocol1_19_4To1_19_3;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.event.player.PlayerInteractEvent;
import com.viaversion.viaversion.api.protocol.Protocol;
import org.bukkit.plugin.Plugin;
import com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.Protocol1_19_4To1_19_3;
import com.viaversion.viaversion.ViaVersionPlugin;
import com.viaversion.viaversion.bukkit.listeners.ViaBukkitListener;

public final class ArmorToggleListener extends ViaBukkitListener
{
    public ArmorToggleListener(final ViaVersionPlugin plugin) {
        super((Plugin)plugin, Protocol1_19_4To1_19_3.class);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void itemUse(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem();
        if (item == null || event.getHand() == null || !this.isOnPipe(player)) {
            return;
        }
        final EquipmentSlot armorItemSlot = item.getType().getEquipmentSlot();
        if (armorItemSlot != EquipmentSlot.HAND && armorItemSlot != EquipmentSlot.OFF_HAND) {
            final PlayerInventory inventory = player.getInventory();
            final ItemStack armor = inventory.getItem(armorItemSlot);
            if (armor != null && armor.getType() != Material.AIR && !armor.equals((Object)item)) {
                inventory.setItem(event.getHand(), inventory.getItem(event.getHand()));
                inventory.setItem(armorItemSlot, armor);
            }
        }
    }
}
