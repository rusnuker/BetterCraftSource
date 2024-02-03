// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.creativetabs;

import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.Enchantment;
import java.util.Iterator;
import java.util.List;
import net.minecraft.init.Items;
import me.nzxtercode.bettercraft.client.misc.creativetabs.impl.TabOwn;
import me.nzxtercode.bettercraft.client.misc.creativetabs.impl.TabExploits;
import net.minecraft.block.Block;
import me.nzxtercode.bettercraft.client.misc.creativetabs.impl.TabCustom;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import me.nzxtercode.bettercraft.client.misc.creativetabs.impl.TabMisc;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.EnumEnchantmentType;

public abstract class TabsCreative
{
    public static TabsCreative[] creativeTabArray;
    public static final TabsCreative tabMisc;
    public static final TabsCreative tabCustom;
    public static final TabsCreative tabExploits;
    public static final TabsCreative tabOwn;
    public static final TabsCreative tabAllSearch;
    public static final TabsCreative tabInventory;
    private final int tabIndex;
    private final String tabLabel;
    private String theTexture;
    private boolean hasScrollbar;
    private boolean drawTitle;
    private EnumEnchantmentType[] enchantmentTypes;
    private ItemStack iconItemStack;
    
    static {
        TabsCreative.creativeTabArray = new TabsCreative[6];
        tabMisc = new TabMisc("misc") {
            @Override
            public Item getTabIconItem() {
                return Item.getItemFromBlock(Blocks.anvil);
            }
        };
        tabCustom = new TabCustom("custom") {
            @Override
            public Item getTabIconItem() {
                return Item.getItemFromBlock(Blocks.chest);
            }
        };
        tabExploits = new TabExploits("exploits") {
            @Override
            public Item getTabIconItem() {
                return Item.getItemFromBlock(Blocks.command_block);
            }
        };
        tabOwn = new TabOwn("own") {
            @Override
            public Item getTabIconItem() {
                return Item.getItemFromBlock(Blocks.barrier);
            }
        };
        tabAllSearch = new TabsCreative("search") {
            @Override
            public Item getTabIconItem() {
                return Items.compass;
            }
        }.setBackgroundImageName("item_search.png");
        tabInventory = new TabsCreative("inventory") {
            @Override
            public Item getTabIconItem() {
                return Item.getItemFromBlock(Blocks.chest);
            }
        }.setBackgroundImageName("inventory.png").setNoScrollbar().setNoTitle();
    }
    
    public TabsCreative(final int index, final String label) {
        this.theTexture = "items.png";
        this.hasScrollbar = true;
        this.drawTitle = true;
        if (index >= TabsCreative.creativeTabArray.length) {
            final TabsCreative[] tmp = new TabsCreative[index + 1];
            for (int x = 0; x < TabsCreative.creativeTabArray.length; ++x) {
                tmp[x] = TabsCreative.creativeTabArray[x];
            }
            TabsCreative.creativeTabArray = tmp;
        }
        this.tabIndex = index;
        this.tabLabel = label;
        TabsCreative.creativeTabArray[index] = this;
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
        if (this.iconItemStack == null) {
            this.iconItemStack = new ItemStack(this.getTabIconItem(), 1, this.getIconItemDamage());
        }
        return this.iconItemStack;
    }
    
    public abstract Item getTabIconItem();
    
    public int getIconItemDamage() {
        return 0;
    }
    
    public String getBackgroundImageName() {
        return this.theTexture;
    }
    
    public TabsCreative setBackgroundImageName(final String texture) {
        this.theTexture = texture;
        return this;
    }
    
    public boolean drawInForegroundOfTab() {
        return this.drawTitle;
    }
    
    public TabsCreative setNoTitle() {
        this.drawTitle = false;
        return this;
    }
    
    public boolean shouldHidePlayerInventory() {
        return this.hasScrollbar;
    }
    
    public TabsCreative setNoScrollbar() {
        this.hasScrollbar = false;
        return this;
    }
    
    public int getTabColumn() {
        if (this.tabIndex > 11) {
            return (this.tabIndex - 12) % 10 % 5;
        }
        return this.tabIndex % 6;
    }
    
    public boolean isTabInFirstRow() {
        if (this.tabIndex > 11) {
            return (this.tabIndex - 12) % 10 < 5;
        }
        return this.tabIndex < 6;
    }
    
    public EnumEnchantmentType[] getRelevantEnchantmentTypes() {
        return this.enchantmentTypes;
    }
    
    public TabsCreative setRelevantEnchantmentTypes(final EnumEnchantmentType... types) {
        this.enchantmentTypes = types;
        return this;
    }
    
    public boolean hasRelevantEnchantmentType(final EnumEnchantmentType enchantmentType) {
        if (this.enchantmentTypes == null) {
            return false;
        }
        EnumEnchantmentType[] enchantmentTypes;
        for (int length = (enchantmentTypes = this.enchantmentTypes).length, i = 0; i < length; ++i) {
            final EnumEnchantmentType enumenchantmenttype = enchantmentTypes[i];
            if (enumenchantmenttype == enchantmentType) {
                return true;
            }
        }
        return false;
    }
    
    public void displayAllReleventItems(final List<ItemStack> p_78018_1_) {
        for (final Item item : Item.itemRegistry) {
            if (item != null && item.getTabCreative() == this) {
                item.getSubItems(item, this, p_78018_1_);
            }
        }
        if (this.getRelevantEnchantmentTypes() != null) {
            this.addEnchantmentBooksToList(p_78018_1_, this.getRelevantEnchantmentTypes());
        }
    }
    
    public void addEnchantmentBooksToList(final List<ItemStack> itemList, final EnumEnchantmentType... enchantmentType) {
        Enchantment[] enchantmentsBookList;
        for (int length = (enchantmentsBookList = Enchantment.enchantmentsBookList).length, j = 0; j < length; ++j) {
            final Enchantment enchantment = enchantmentsBookList[j];
            if (enchantment != null && enchantment.type != null) {
                boolean flag = false;
                for (int i = 0; i < enchantmentType.length && !flag; ++i) {
                    if (enchantment.type == enchantmentType[i]) {
                        flag = true;
                    }
                }
                if (flag) {
                    itemList.add(Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, enchantment.getMaxLevel())));
                }
            }
        }
    }
    
    public int getTabPage() {
        if (this.tabIndex > 11) {
            return (this.tabIndex - 12) / 10 + 1;
        }
        return 0;
    }
    
    public static int getNextID() {
        return TabsCreative.creativeTabArray.length;
    }
    
    public boolean hasSearchBar() {
        return this.tabIndex == TabsCreative.tabAllSearch.tabIndex;
    }
    
    public int getSearchbarWidth() {
        return 89;
    }
}
