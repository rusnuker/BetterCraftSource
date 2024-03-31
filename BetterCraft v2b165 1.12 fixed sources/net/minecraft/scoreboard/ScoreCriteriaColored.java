// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.scoreboard;

import net.minecraft.util.text.TextFormatting;

public class ScoreCriteriaColored implements IScoreCriteria
{
    private final String goalName;
    
    public ScoreCriteriaColored(final String name, final TextFormatting format) {
        this.goalName = String.valueOf(name) + format.getFriendlyName();
        IScoreCriteria.INSTANCES.put(this.goalName, this);
    }
    
    @Override
    public String getName() {
        return this.goalName;
    }
    
    @Override
    public boolean isReadOnly() {
        return false;
    }
    
    @Override
    public EnumRenderType getRenderType() {
        return EnumRenderType.INTEGER;
    }
}
