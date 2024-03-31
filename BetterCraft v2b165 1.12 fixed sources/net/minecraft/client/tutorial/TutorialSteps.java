// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.tutorial;

import java.util.function.Function;

public enum TutorialSteps
{
    MOVEMENT("movement", (Function<Tutorial, T>)MovementStep::new), 
    FIND_TREE("find_tree", (Function<Tutorial, T>)FindTreeStep::new), 
    PUNCH_TREE("punch_tree", (Function<Tutorial, T>)PunchTreeStep::new), 
    OPEN_INVENTORY("open_inventory", (Function<Tutorial, T>)OpenInventoryStep::new), 
    CRAFT_PLANKS("craft_planks", (Function<Tutorial, T>)CraftPlanksStep::new), 
    NONE("none", (Function<Tutorial, T>)CompletedTutorialStep::new);
    
    private final String field_193316_g;
    private final Function<Tutorial, ? extends ITutorialStep> field_193317_h;
    
    private <T extends ITutorialStep> TutorialSteps(final String p_i47577_3_, final Function<Tutorial, T> p_i47577_4_) {
        this.field_193316_g = p_i47577_3_;
        this.field_193317_h = p_i47577_4_;
    }
    
    public ITutorialStep func_193309_a(final Tutorial p_193309_1_) {
        return (ITutorialStep)this.field_193317_h.apply(p_193309_1_);
    }
    
    public String func_193308_a() {
        return this.field_193316_g;
    }
    
    public static TutorialSteps func_193307_a(final String p_193307_0_) {
        TutorialSteps[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final TutorialSteps tutorialsteps = values[i];
            if (tutorialsteps.field_193316_g.equals(p_193307_0_)) {
                return tutorialsteps;
            }
        }
        return TutorialSteps.NONE;
    }
}
