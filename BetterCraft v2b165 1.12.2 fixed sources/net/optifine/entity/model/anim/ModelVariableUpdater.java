// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.entity.model.anim;

import optifine.Config;

public class ModelVariableUpdater
{
    private String modelVariableName;
    private String expressionText;
    private ModelVariable modelVariable;
    private IExpression expression;
    
    public boolean initialize(final IModelResolver mr) {
        this.modelVariable = mr.getModelVariable(this.modelVariableName);
        if (this.modelVariable == null) {
            Config.warn("Model variable not found: " + this.modelVariableName);
            return false;
        }
        try {
            final ExpressionParser expressionparser = new ExpressionParser(mr);
            this.expression = expressionparser.parse(this.expressionText);
            return true;
        }
        catch (final ParseException parseexception) {
            Config.warn("Error parsing expression: " + this.expressionText);
            Config.warn(String.valueOf(parseexception.getClass().getName()) + ": " + parseexception.getMessage());
            return false;
        }
    }
    
    public ModelVariableUpdater(final String modelVariableName, final String expressionText) {
        this.modelVariableName = modelVariableName;
        this.expressionText = expressionText;
    }
    
    public void update() {
        final float f = this.expression.eval();
        this.modelVariable.setValue(f);
    }
}
