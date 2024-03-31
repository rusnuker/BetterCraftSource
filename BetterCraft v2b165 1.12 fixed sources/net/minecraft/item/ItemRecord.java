// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.util.text.translation.I18n;
import net.minecraft.client.util.ITooltipFlag;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.stats.StatList;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockJukebox;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;
import com.google.common.collect.Maps;
import net.minecraft.util.SoundEvent;
import java.util.Map;

public class ItemRecord extends Item
{
    private static final Map<SoundEvent, ItemRecord> RECORDS;
    private final SoundEvent sound;
    private final String displayName;
    
    static {
        RECORDS = Maps.newHashMap();
    }
    
    protected ItemRecord(final String p_i46742_1_, final SoundEvent soundIn) {
        this.displayName = "item.record." + p_i46742_1_ + ".desc";
        this.sound = soundIn;
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.MISC);
        ItemRecord.RECORDS.put(this.sound, this);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer stack, final World playerIn, final BlockPos worldIn, final EnumHand pos, final EnumFacing hand, final float facing, final float hitX, final float hitY) {
        final IBlockState iblockstate = playerIn.getBlockState(worldIn);
        if (iblockstate.getBlock() == Blocks.JUKEBOX && !iblockstate.getValue((IProperty<Boolean>)BlockJukebox.HAS_RECORD)) {
            if (!playerIn.isRemote) {
                final ItemStack itemstack = stack.getHeldItem(pos);
                ((BlockJukebox)Blocks.JUKEBOX).insertRecord(playerIn, worldIn, iblockstate, itemstack);
                playerIn.playEvent(null, 1010, worldIn, Item.getIdFromItem(this));
                itemstack.func_190918_g(1);
                stack.addStat(StatList.RECORD_PLAYED);
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }
    
    @Override
    public void addInformation(final ItemStack stack, @Nullable final World playerIn, final List<String> tooltip, final ITooltipFlag advanced) {
        tooltip.add(this.getRecordNameLocal());
    }
    
    public String getRecordNameLocal() {
        return I18n.translateToLocal(this.displayName);
    }
    
    @Override
    public EnumRarity getRarity(final ItemStack stack) {
        return EnumRarity.RARE;
    }
    
    @Nullable
    public static ItemRecord getBySound(final SoundEvent soundIn) {
        return ItemRecord.RECORDS.get(soundIn);
    }
    
    public SoundEvent getSound() {
        return this.sound;
    }
}
