// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind;

import javax.xml.namespace.QName;

public abstract class JAXBIntrospector
{
    public abstract boolean isElement(final Object p0);
    
    public abstract QName getElementName(final Object p0);
    
    public static Object getValue(final Object jaxbElement) {
        if (jaxbElement instanceof JAXBElement) {
            return ((JAXBElement)jaxbElement).getValue();
        }
        return jaxbElement;
    }
}
