// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.creative;

import me.amkgre.bettercraft.client.mods.creative.impl.exploits.CrashSkullItem;
import me.amkgre.bettercraft.client.mods.creative.impl.exploits.CrashMapItem;
import me.amkgre.bettercraft.client.mods.creative.impl.potions.StackUpPotionItem;
import me.amkgre.bettercraft.client.mods.creative.impl.potions.CreativeKillerPotionItem;
import me.amkgre.bettercraft.client.mods.creative.impl.potions.NoMovePotionItem;
import me.amkgre.bettercraft.client.mods.creative.impl.potions.AntiRespawnPotionItem;
import me.amkgre.bettercraft.client.mods.creative.impl.potions.AntiJumpPotionItem;
import me.amkgre.bettercraft.client.mods.creative.impl.potions.StealthPotionItem;
import me.amkgre.bettercraft.client.mods.creative.impl.potions.BonusPotionItem;
import me.amkgre.bettercraft.client.mods.creative.impl.equip.DiamondSwordItem;
import me.amkgre.bettercraft.client.mods.creative.impl.equip.DiamondLeggingsItem;
import me.amkgre.bettercraft.client.mods.creative.impl.equip.DiamondHelmetItem;
import me.amkgre.bettercraft.client.mods.creative.impl.equip.DiamondChestplateItem;
import me.amkgre.bettercraft.client.mods.creative.impl.equip.DiamondBootsItem;
import me.amkgre.bettercraft.client.mods.creative.impl.otheritems.SpawnMagicFogerItem;
import me.amkgre.bettercraft.client.mods.creative.impl.otheritems.MultiColorFirework2Item;
import me.amkgre.bettercraft.client.mods.creative.impl.otheritems.MultiColorFirework1Item;
import me.amkgre.bettercraft.client.mods.creative.impl.otheritems.UltraEnchantedBookItem;
import me.amkgre.bettercraft.client.mods.creative.impl.otheritems.BootsOfDoomItem;
import me.amkgre.bettercraft.client.mods.creative.impl.otheritems.SpeedyItem;
import me.amkgre.bettercraft.client.mods.creative.impl.otheritems.BetterArmorstandItem;
import me.amkgre.bettercraft.client.mods.creative.impl.otheritems.FrostWalkerItem;
import java.util.ArrayList;

public class InventoryManager
{
    public ArrayList<InventoryItem> items;
    
    public InventoryManager() {
        this.items = new ArrayList<InventoryItem>();
    }
    
    public void loadItems() {
        this.loadItemWithSection(new FrostWalkerItem(), InventorySection.SONSTIGEITEMS);
        this.loadItemWithSection(new BetterArmorstandItem(), InventorySection.SONSTIGEITEMS);
        this.loadItemWithSection(new SpeedyItem(), InventorySection.SONSTIGEITEMS);
        this.loadItemWithSection(new BootsOfDoomItem(), InventorySection.SONSTIGEITEMS);
        this.loadItemWithSection(new UltraEnchantedBookItem(), InventorySection.SONSTIGEITEMS);
        this.loadItemWithSection(new MultiColorFirework1Item(), InventorySection.SONSTIGEITEMS);
        this.loadItemWithSection(new MultiColorFirework2Item(), InventorySection.SONSTIGEITEMS);
        this.loadItemWithSection(new SpawnMagicFogerItem(), InventorySection.SONSTIGEITEMS);
        this.loadItemWithSection(new DiamondBootsItem(), InventorySection.EQUIP);
        this.loadItemWithSection(new DiamondChestplateItem(), InventorySection.EQUIP);
        this.loadItemWithSection(new DiamondHelmetItem(), InventorySection.EQUIP);
        this.loadItemWithSection(new DiamondLeggingsItem(), InventorySection.EQUIP);
        this.loadItemWithSection(new DiamondSwordItem(), InventorySection.EQUIP);
        this.loadItemWithSection(new BonusPotionItem(), InventorySection.POTIONS);
        this.loadItemWithSection(new StealthPotionItem(), InventorySection.POTIONS);
        this.loadItemWithSection(new AntiJumpPotionItem(), InventorySection.POTIONS);
        this.loadItemWithSection(new AntiRespawnPotionItem(), InventorySection.POTIONS);
        this.loadItemWithSection(new NoMovePotionItem(), InventorySection.POTIONS);
        this.loadItemWithSection(new CreativeKillerPotionItem(), InventorySection.POTIONS);
        this.loadItemWithSection(new StackUpPotionItem(), InventorySection.POTIONS);
        this.loadItemWithSection(new CrashMapItem(), InventorySection.EXPLOITS);
        this.loadItemWithSection(new CrashSkullItem(), InventorySection.EXPLOITS);
    }
    
    public void emptySlot() {
    }
    
    public void loadItemWithSection(final InventoryItem item, final InventorySection section) {
        item.setSection(section);
        this.items.add(item);
    }
}
