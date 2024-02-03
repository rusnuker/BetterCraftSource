// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import com.viaversion.viaversion.api.protocol.Protocol;
import org.bukkit.plugin.Plugin;
import com.viaversion.viabackwards.protocol.protocol1_13to1_13_1.Protocol1_13To1_13_1;
import com.viaversion.viabackwards.BukkitPlugin;
import com.viaversion.viaversion.bukkit.listeners.ViaBukkitListener;

public class PlayerItemDropListener extends ViaBukkitListener
{
    public PlayerItemDropListener(final BukkitPlugin plugin) {
        super((Plugin)plugin, Protocol1_13To1_13_1.class);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemDrop(final PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        if (!this.isOnPipe(player)) {
            return;
        }
        final int slot = player.getInventory().getHeldItemSlot();
        final ItemStack item = player.getInventory().getItem(slot);
        player.getInventory().setItem(slot, item);
    }
}
