// 
// Decompiled by Procyon v0.6.0
// 

package org.json;

import java.io.StringWriter;

public class JSONStringer extends JSONWriter
{
    public JSONStringer() {
        super(new StringWriter());
    }
    
    @Override
    public String toString() {
        return (this.mode == 'd') ? this.writer.toString() : null;
    }
}
