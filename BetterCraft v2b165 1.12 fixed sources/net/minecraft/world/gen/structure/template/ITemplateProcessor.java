// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.gen.structure.template;

import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITemplateProcessor
{
    @Nullable
    Template.BlockInfo processBlock(final World p0, final BlockPos p1, final Template.BlockInfo p2);
}
