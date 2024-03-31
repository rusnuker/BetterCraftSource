// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.tutorial;

import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.item.Item;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.RayTraceResult;
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

public class FindTreeStep implements ITutorialStep
{
    private static final Set<Block> field_193268_a;
    private static final ITextComponent field_193269_b;
    private static final ITextComponent field_193270_c;
    private final Tutorial field_193271_d;
    private TutorialToast field_193272_e;
    private int field_193273_f;
    
    static {
        field_193268_a = Sets.newHashSet(Blocks.LOG, Blocks.LOG2, Blocks.LEAVES, Blocks.LEAVES2);
        field_193269_b = new TextComponentTranslation("tutorial.find_tree.title", new Object[0]);
        field_193270_c = new TextComponentTranslation("tutorial.find_tree.description", new Object[0]);
    }
    
    public FindTreeStep(final Tutorial p_i47582_1_) {
        this.field_193271_d = p_i47582_1_;
    }
    
    @Override
    public void func_193245_a() {
        ++this.field_193273_f;
        if (this.field_193271_d.func_194072_f() != GameType.SURVIVAL) {
            this.field_193271_d.func_193292_a(TutorialSteps.NONE);
        }
        else {
            if (this.field_193273_f == 1) {
                final EntityPlayerSP entityplayersp = this.field_193271_d.func_193295_e().player;
                if (entityplayersp != null) {
                    for (final Block block : FindTreeStep.field_193268_a) {
                        if (entityplayersp.inventory.hasItemStack(new ItemStack(block))) {
                            this.field_193271_d.func_193292_a(TutorialSteps.CRAFT_PLANKS);
                            return;
                        }
                    }
                    if (func_194070_a(entityplayersp)) {
                        this.field_193271_d.func_193292_a(TutorialSteps.CRAFT_PLANKS);
                        return;
                    }
                }
            }
            if (this.field_193273_f >= 6000 && this.field_193272_e == null) {
                this.field_193272_e = new TutorialToast(TutorialToast.Icons.TREE, FindTreeStep.field_193269_b, FindTreeStep.field_193270_c, false);
                this.field_193271_d.func_193295_e().func_193033_an().func_192988_a(this.field_193272_e);
            }
        }
    }
    
    @Override
    public void func_193248_b() {
        if (this.field_193272_e != null) {
            this.field_193272_e.func_193670_a();
            this.field_193272_e = null;
        }
    }
    
    @Override
    public void func_193246_a(final WorldClient p_193246_1_, final RayTraceResult p_193246_2_) {
        if (p_193246_2_.typeOfHit == RayTraceResult.Type.BLOCK && p_193246_2_.getBlockPos() != null) {
            final IBlockState iblockstate = p_193246_1_.getBlockState(p_193246_2_.getBlockPos());
            if (FindTreeStep.field_193268_a.contains(iblockstate.getBlock())) {
                this.field_193271_d.func_193292_a(TutorialSteps.PUNCH_TREE);
            }
        }
    }
    
    @Override
    public void func_193252_a(final ItemStack p_193252_1_) {
        for (final Block block : FindTreeStep.field_193268_a) {
            if (p_193252_1_.getItem() == Item.getItemFromBlock(block)) {
                this.field_193271_d.func_193292_a(TutorialSteps.CRAFT_PLANKS);
            }
        }
    }
    
    public static boolean func_194070_a(final EntityPlayerSP p_194070_0_) {
        for (final Block block : FindTreeStep.field_193268_a) {
            final StatBase statbase = StatList.getBlockStats(block);
            if (statbase != null && p_194070_0_.getStatFileWriter().readStat(statbase) > 0) {
                return true;
            }
        }
        return false;
    }
}
