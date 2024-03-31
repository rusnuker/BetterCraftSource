// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type.types;

import com.viaversion.viaversion.api.type.Type;
import java.util.LinkedList;
import java.util.List;

public class Particle
{
    private int id;
    private List<ParticleData> arguments;
    
    public Particle(final int id) {
        this.arguments = new LinkedList<ParticleData>();
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public List<ParticleData> getArguments() {
        return this.arguments;
    }
    
    public void setArguments(final List<ParticleData> arguments) {
        this.arguments = arguments;
    }
    
    public static class ParticleData
    {
        private Type type;
        private Object value;
        
        public ParticleData(final Type type, final Object value) {
            this.type = type;
            this.value = value;
        }
        
        public Type getType() {
            return this.type;
        }
        
        public void setType(final Type type) {
            this.type = type;
        }
        
        public Object getValue() {
            return this.value;
        }
        
        public <T> T get() {
            return (T)this.value;
        }
        
        public void setValue(final Object value) {
            this.value = value;
        }
    }
}
