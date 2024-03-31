// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.remote;

import java.util.Iterator;
import java.util.ArrayList;

public class ResourceList extends ArrayList<Resource>
{
    public Resource fromFileName(final String file) {
        for (final Resource f : this) {
            if (f.getFileName().equalsIgnoreCase(file)) {
                return f;
            }
        }
        return null;
    }
    
    public void removeExistings() {
        for (int i = 0; i < this.size(); ++i) {
            if (this.get(i).exists()) {
                this.remove(i--);
            }
        }
    }
}
