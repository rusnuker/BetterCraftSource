// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class Request
{
    private String command;
    private List<Parameter> params;
    
    public Request(final String command, final Parameter... params) {
        this.params = new ArrayList<Parameter>();
        this.command = command;
        Collections.addAll(this.params, params);
    }
    
    protected void addParam(final Parameter parameter) {
        this.params.add(parameter);
    }
    
    protected static ArrayParameter array(final String... array) {
        return new ArrayParameter(array);
    }
    
    protected static OptionParameter option(final String option) {
        return new OptionParameter(option);
    }
    
    protected static ValueParameter value(final String key, final Object value) {
        return new ValueParameter(key, value);
    }
    
    public List<Parameter> getParams() {
        return this.params;
    }
    
    @Override
    public String toString() {
        String result = this.command;
        for (final Parameter parameter : this.params) {
            result = String.valueOf(result) + " " + parameter.serialize();
        }
        return result;
    }
}
