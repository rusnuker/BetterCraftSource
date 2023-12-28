// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

import java.util.Vector;
import java.util.Map;

class CefCommandLine_N extends CefNativeAdapter implements CefCommandLine
{
    @Override
    public void reset() {
        try {
            this.N_reset();
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
        }
    }
    
    @Override
    public String getProgram() {
        try {
            return this.N_getProgram();
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void setProgram(final String program) {
        try {
            this.N_setProgram(program);
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
        }
    }
    
    @Override
    public boolean hasSwitches() {
        try {
            return this.N_hasSwitches();
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean hasSwitch(final String name) {
        try {
            return this.N_hasSwitch(name);
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
            return false;
        }
    }
    
    @Override
    public String getSwitchValue(final String name) {
        try {
            return this.N_getSwitchValue(name);
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Map<String, String> getSwitches() {
        try {
            return this.N_getSwitches();
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void appendSwitch(final String name) {
        try {
            this.N_appendSwitch(name);
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
        }
    }
    
    @Override
    public void appendSwitchWithValue(final String name, final String value) {
        try {
            this.N_appendSwitchWithValue(name, value);
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
        }
    }
    
    @Override
    public boolean hasArguments() {
        try {
            return this.N_hasArguments();
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Vector<String> getArguments() {
        try {
            return this.N_getArguments();
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void appendArgument(final String argument) {
        try {
            this.N_appendArgument(argument);
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
        }
    }
    
    @Override
    public String toString() {
        String result = "CefCommandLine [program='" + this.getProgram() + "'";
        if (this.hasSwitches()) {
            final Map<String, String> switches = this.getSwitches();
            result = String.valueOf(result) + ", switches=" + switches;
        }
        if (this.hasArguments()) {
            final Vector<String> arguments = this.getArguments();
            result = String.valueOf(result) + ", arguments=" + arguments;
        }
        return String.valueOf(result) + "]";
    }
    
    private final native void N_reset();
    
    private final native String N_getProgram();
    
    private final native void N_setProgram(final String p0);
    
    private final native boolean N_hasSwitches();
    
    private final native boolean N_hasSwitch(final String p0);
    
    private final native String N_getSwitchValue(final String p0);
    
    private final native Map<String, String> N_getSwitches();
    
    private final native void N_appendSwitch(final String p0);
    
    private final native void N_appendSwitchWithValue(final String p0, final String p1);
    
    private final native boolean N_hasArguments();
    
    private final native Vector<String> N_getArguments();
    
    private final native void N_appendArgument(final String p0);
}
