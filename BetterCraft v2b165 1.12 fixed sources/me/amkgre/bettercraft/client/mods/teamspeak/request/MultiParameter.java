// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

public class MultiParameter extends Parameter
{
    private Parameter[][] parameters;
    
    public MultiParameter(final Parameter[][] parameters) {
        this.parameters = parameters;
    }
    
    @Override
    public String serialize() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int parametersLength = this.parameters.length, i = 0; i < parametersLength; ++i) {
            final Parameter[] parameter = this.parameters[i];
            if (i != 0) {
                stringBuilder.append("|");
            }
            for (int parameterLength = parameter.length, j = 0; j < parameterLength; ++j) {
                final Parameter parameter2 = parameter[j];
                if (j != 0) {
                    stringBuilder.append(" ");
                }
                stringBuilder.append(parameter2.serialize());
            }
        }
        return stringBuilder.toString();
    }
}
