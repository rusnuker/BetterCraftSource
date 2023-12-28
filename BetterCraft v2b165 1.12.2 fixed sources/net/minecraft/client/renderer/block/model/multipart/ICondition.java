// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.block.model.multipart;

import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import com.google.common.base.Predicate;
import net.minecraft.block.state.BlockStateContainer;

public interface ICondition
{
    public static final ICondition TRUE = new ICondition() {
        @Override
        public Predicate<IBlockState> getPredicate(final BlockStateContainer blockState) {
            return new Predicate<IBlockState>() {
                @Override
                public boolean apply(@Nullable final IBlockState p_apply_1_) {
                    return true;
                }
            };
        }
    };
    public static final ICondition FALSE = new ICondition() {
        @Override
        public Predicate<IBlockState> getPredicate(final BlockStateContainer blockState) {
            return new Predicate<IBlockState>() {
                @Override
                public boolean apply(@Nullable final IBlockState p_apply_1_) {
                    return false;
                }
            };
        }
    };
    
    Predicate<IBlockState> getPredicate(final BlockStateContainer p0);
}
