// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.tutorial;

import net.minecraft.item.Item;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.multiplayer.WorldClient;
import java.util.Iterator;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameType;
import net.minecraft.util.text.TextComponentTranslation;
import com.google.common.collect.Sets;
import net.minecraft.init.Blocks;
import net.minecraft.client.gui.toasts.TutorialToast;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.block.Block;
import java.util.Set;

public class PunchTreeStep implements ITutorialStep
{
    private static final Set<Block> field_193274_a;
    private static final ITextComponent field_193275_b;
    private static final ITextComponent field_193276_c;
    private final Tutorial field_193277_d;
    private TutorialToast field_193278_e;
    private int field_193279_f;
    private int field_193280_g;
    
    static {
        field_193274_a = Sets.newHashSet(Blocks.LOG, Blocks.LOG2);
        field_193275_b = new TextComponentTranslation("tutorial.punch_tree.title", new Object[0]);
        field_193276_c = new TextComponentTranslation("tutorial.punch_tree.description", new Object[] { Tutorial.func_193291_a("attack") });
    }
    
    public PunchTreeStep(final Tutorial p_i47579_1_) {
        this.field_193277_d = p_i47579_1_;
    }
    
    @Override
    public void func_193245_a() {
        ++this.field_193279_f;
        if (this.field_193277_d.func_194072_f() != GameType.SURVIVAL) {
            this.field_193277_d.func_193292_a(TutorialSteps.NONE);
        }
        else {
            if (this.field_193279_f == 1) {
                final EntityPlayerSP entityplayersp = this.field_193277_d.func_193295_e().player;
                if (entityplayersp != null) {
                    for (final Block block : PunchTreeStep.field_193274_a) {
                        if (entityplayersp.inventory.hasItemStack(new ItemStack(block))) {
                            this.field_193277_d.func_193292_a(TutorialSteps.CRAFT_PLANKS);
                            return;
                        }
                    }
                    if (FindTreeStep.func_194070_a(entityplayersp)) {
                        this.field_193277_d.func_193292_a(TutorialSteps.CRAFT_PLANKS);
                        return;
                    }
                }
            }
            if ((this.field_193279_f >= 600 || this.field_193280_g > 3) && this.field_193278_e == null) {
                this.field_193278_e = new TutorialToast(TutorialToast.Icons.TREE, PunchTreeStep.field_193275_b, PunchTreeStep.field_193276_c, true);
                this.field_193277_d.func_193295_e().func_193033_an().func_192988_a(this.field_193278_e);
            }
        }
    }
    
    @Override
    public void func_193248_b() {
        if (this.field_193278_e != null) {
            this.field_193278_e.func_193670_a();
            this.field_193278_e = null;
        }
    }
    
    @Override
    public void func_193250_a(final WorldClient p_193250_1_, final BlockPos p_193250_2_, final IBlockState p_193250_3_, final float p_193250_4_) {
        final boolean flag = PunchTreeStep.field_193274_a.contains(p_193250_3_.getBlock());
        if (flag && p_193250_4_ > 0.0f) {
            if (this.field_193278_e != null) {
                this.field_193278_e.func_193669_a(p_193250_4_);
            }
            if (p_193250_4_ >= 1.0f) {
                this.field_193277_d.func_193292_a(TutorialSteps.OPEN_INVENTORY);
            }
        }
        else if (this.field_193278_e != null) {
            this.field_193278_e.func_193669_a(0.0f);
        }
        else if (flag) {
            ++this.field_193280_g;
        }
    }
    
    @Override
    public void func_193252_a(final ItemStack p_193252_1_) {
        for (final Block block : PunchTreeStep.field_193274_a) {
            if (p_193252_1_.getItem() == Item.getItemFromBlock(block)) {
                this.field_193277_d.func_193292_a(TutorialSteps.CRAFT_PLANKS);
            }
        }
    }
}
