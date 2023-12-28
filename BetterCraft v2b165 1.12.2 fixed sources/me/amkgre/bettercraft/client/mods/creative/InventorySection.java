// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.creative;

import net.minecraft.util.NonNullList;
import javax.annotation.Nullable;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.EnumEnchantmentType;

public abstract class InventorySection
{
    public static final InventorySection[] CREATIVE_TAB_ARRAY;
    public static final InventorySection SONSTIGEITEMS;
    public static final InventorySection SKULLS;
    public static final InventorySection POTIONS;
    public static final InventorySection EQUIP;
    public static final InventorySection EXPLOITS;
    public static final InventorySection ADDONS;
    private final int tabIndex;
    private final String tabLabel;
    private String theTexture;
    private boolean hasScrollbar;
    private boolean drawTitle;
    private EnumEnchantmentType[] enchantmentTypes;
    private ItemStack iconItemStack;
    
    static {
        CREATIVE_TAB_ARRAY = new InventorySection[6];
        SONSTIGEITEMS = new InventorySection("Other Items") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(Items.BREWING_STAND);
            }
        };
        SKULLS = new InventorySection("Skulls") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(Item.getItemById(397));
            }
        };
        POTIONS = new InventorySection("Potions") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(Items.SPLASH_POTION);
            }
        };
        EQUIP = new InventorySection("Equip") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(Items.DIAMOND_SWORD);
            }
        };
        EXPLOITS = new InventorySection("Exploits") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(Items.BOOK);
            }
        };
        ADDONS = new InventorySection("Addons") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(Item.getItemFromBlock(Blocks.BARRIER));
            }
        };
    }
    
    public InventorySection(final int index2, final String label) {
        this.theTexture = "items.png";
        this.hasScrollbar = true;
        this.drawTitle = true;
        this.enchantmentTypes = new EnumEnchantmentType[0];
        this.tabIndex = index2;
        this.tabLabel = label;
        this.iconItemStack = ItemStack.field_190927_a;
        InventorySection.CREATIVE_TAB_ARRAY[index2] = this;
    }
    
    public int getTabIndex() {
        return this.tabIndex;
    }
    
    public String getTabLabel() {
        return this.tabLabel;
    }
    
    public String getTranslatedTabLabel() {
        return "itemGroup." + this.getTabLabel();
    }
    
    public ItemStack getIconItemStack() {
        if (this.iconItemStack.func_190926_b()) {
            this.iconItemStack = this.getTabIconItem();
        }
        return this.iconItemStack;
    }
    
    public abstract ItemStack getTabIconItem();
    
    public String getBackgroundImageName() {
        return this.theTexture;
    }
    
    public InventorySection setBackgroundImageName(final String texture) {
        this.theTexture = texture;
        return this;
    }
    
    public boolean drawInForegroundOfTab() {
        return this.drawTitle;
    }
    
    public InventorySection setNoTitle() {
        this.drawTitle = false;
        return this;
    }
    
    public boolean shouldHidePlayerInventory() {
        return this.hasScrollbar;
    }
    
    public InventorySection setNoScrollbar() {
        this.hasScrollbar = false;
        return this;
    }
    
    public int getTabColumn() {
        return this.tabIndex % 6;
    }
    
    public boolean isTabInFirstRow() {
        return this.tabIndex < 6;
    }
    
    public boolean func_192394_m() {
        return this.getTabColumn() == 5;
    }
    
    public EnumEnchantmentType[] getRelevantEnchantmentTypes() {
        return this.enchantmentTypes;
    }
    
    public InventorySection setRelevantEnchantmentTypes(final EnumEnchantmentType... types) {
        this.enchantmentTypes = types;
        return this;
    }
    
    public boolean hasRelevantEnchantmentType(@Nullable final EnumEnchantmentType enchantmentType) {
        if (enchantmentType != null) {
            EnumEnchantmentType[] enchantmentTypes;
            for (int length = (enchantmentTypes = this.enchantmentTypes).length, i = 0; i < length; ++i) {
                final EnumEnchantmentType enumenchantmenttype = enchantmentTypes[i];
                if (enumenchantmenttype == enchantmentType) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void displayAllRelevantItems(final NonNullList<ItemStack> p_78018_1_) {
        Item.getSubItems(this, p_78018_1_);
    }
}
