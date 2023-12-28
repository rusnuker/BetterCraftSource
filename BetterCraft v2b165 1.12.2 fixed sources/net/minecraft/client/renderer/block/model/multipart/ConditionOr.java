// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.block.model.multipart;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import javax.annotation.Nullable;
import com.google.common.base.Function;
import net.minecraft.block.state.IBlockState;
import com.google.common.base.Predicate;
import net.minecraft.block.state.BlockStateContainer;

public class ConditionOr implements ICondition
{
    final Iterable<ICondition> conditions;
    
    public ConditionOr(final Iterable<ICondition> conditionsIn) {
        this.conditions = conditionsIn;
    }
    
    @Override
    public Predicate<IBlockState> getPredicate(final BlockStateContainer blockState) {
        return Predicates.or(Iterables.transform(this.conditions, (Function<? super ICondition, ? extends Predicate<? super IBlockState>>)new Function<ICondition, Predicate<IBlockState>>() {
            @Nullable
            @Override
            public Predicate<IBlockState> apply(@Nullable final ICondition p_apply_1_) {
                return (p_apply_1_ == null) ? null : p_apply_1_.getPredicate(blockState);
            }
        }));
    }
}
