// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.creativetab;

import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.util.NonNullList;
import net.minecraft.potion.PotionUtils;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.Items;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.EnumEnchantmentType;

public abstract class CreativeTabs
{
    public static final CreativeTabs[] CREATIVE_TAB_ARRAY;
    public static final CreativeTabs BUILDING_BLOCKS;
    public static final CreativeTabs DECORATIONS;
    public static final CreativeTabs REDSTONE;
    public static final CreativeTabs TRANSPORTATION;
    public static final CreativeTabs MISC;
    public static final CreativeTabs SEARCH;
    public static final CreativeTabs FOOD;
    public static final CreativeTabs TOOLS;
    public static final CreativeTabs COMBAT;
    public static final CreativeTabs BREWING;
    public static final CreativeTabs MATERIALS;
    public static final CreativeTabs field_192395_m;
    public static final CreativeTabs INVENTORY;
    private final int tabIndex;
    private final String tabLabel;
    private String theTexture;
    private boolean hasScrollbar;
    private boolean drawTitle;
    private EnumEnchantmentType[] enchantmentTypes;
    private ItemStack iconItemStack;
    
    static {
        CREATIVE_TAB_ARRAY = new CreativeTabs[12];
        BUILDING_BLOCKS = new CreativeTabs("buildingBlocks") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(Item.getItemFromBlock(Blocks.BRICK_BLOCK));
            }
        };
        DECORATIONS = new CreativeTabs("decorations") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(Item.getItemFromBlock(Blocks.DOUBLE_PLANT), 1, BlockDoublePlant.EnumPlantType.PAEONIA.getMeta());
            }
        };
        REDSTONE = new CreativeTabs("redstone") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(Items.REDSTONE);
            }
        };
        TRANSPORTATION = new CreativeTabs("transportation") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(Item.getItemFromBlock(Blocks.GOLDEN_RAIL));
            }
        };
        MISC = new CreativeTabs("misc") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(Items.LAVA_BUCKET);
            }
        };
        SEARCH = new CreativeTabs("search") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(Items.COMPASS);
            }
        }.setBackgroundImageName("item_search.png");
        FOOD = new CreativeTabs("food") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(Items.APPLE);
            }
        };
        TOOLS = new CreativeTabs("tools") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(Items.IRON_AXE);
            }
        }.setRelevantEnchantmentTypes(EnumEnchantmentType.ALL, EnumEnchantmentType.DIGGER, EnumEnchantmentType.FISHING_ROD, EnumEnchantmentType.BREAKABLE);
        COMBAT = new CreativeTabs("combat") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(Items.GOLDEN_SWORD);
            }
        }.setRelevantEnchantmentTypes(EnumEnchantmentType.ALL, EnumEnchantmentType.ARMOR, EnumEnchantmentType.ARMOR_FEET, EnumEnchantmentType.ARMOR_HEAD, EnumEnchantmentType.ARMOR_LEGS, EnumEnchantmentType.ARMOR_CHEST, EnumEnchantmentType.BOW, EnumEnchantmentType.WEAPON, EnumEnchantmentType.WEARABLE, EnumEnchantmentType.BREAKABLE);
        BREWING = new CreativeTabs("brewing") {
            @Override
            public ItemStack getTabIconItem() {
                return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER);
            }
        };
        MATERIALS = CreativeTabs.MISC;
        field_192395_m = new CreativeTabs("hotbar") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(Blocks.BOOKSHELF);
            }
            
            @Override
            public void displayAllRelevantItems(final NonNullList<ItemStack> p_78018_1_) {
                throw new RuntimeException("Implement exception client-side.");
            }
            
            @Override
            public boolean func_192394_m() {
                return true;
            }
        };
        INVENTORY = new CreativeTabs("inventory") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(Item.getItemFromBlock(Blocks.CHEST));
            }
        }.setBackgroundImageName("inventory.png").setNoScrollbar().setNoTitle();
    }
    
    public CreativeTabs(final int index, final String label) {
        this.theTexture = "items.png";
        this.hasScrollbar = true;
        this.drawTitle = true;
        this.enchantmentTypes = new EnumEnchantmentType[0];
        this.tabIndex = index;
        this.tabLabel = label;
        this.iconItemStack = ItemStack.field_190927_a;
        CreativeTabs.CREATIVE_TAB_ARRAY[index] = this;
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
    
    public CreativeTabs setBackgroundImageName(final String texture) {
        this.theTexture = texture;
        return this;
    }
    
    public boolean drawInForegroundOfTab() {
        return this.drawTitle;
    }
    
    public CreativeTabs setNoTitle() {
        this.drawTitle = false;
        return this;
    }
    
    public boolean shouldHidePlayerInventory() {
        return this.hasScrollbar;
    }
    
    public CreativeTabs setNoScrollbar() {
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
    
    public CreativeTabs setRelevantEnchantmentTypes(final EnumEnchantmentType... types) {
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
        for (final Item item : Item.REGISTRY) {
            item.getSubItems(this, p_78018_1_);
        }
    }
}
