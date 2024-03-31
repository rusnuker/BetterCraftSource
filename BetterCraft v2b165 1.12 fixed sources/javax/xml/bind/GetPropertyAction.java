// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind;

import java.security.PrivilegedAction;

final class GetPropertyAction implements PrivilegedAction<String>
{
    private final String propertyName;
    
    public GetPropertyAction(final String propertyName) {
        this.propertyName = propertyName;
    }
    
    @Override
    public String run() {
        return System.getProperty(this.propertyName);
    }
}
