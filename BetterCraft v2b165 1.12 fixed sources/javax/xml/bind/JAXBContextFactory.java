// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind;

import java.util.Map;

public interface JAXBContextFactory
{
    JAXBContext createContext(final Class<?>[] p0, final Map<String, ?> p1) throws JAXBException;
    
    JAXBContext createContext(final String p0, final ClassLoader p1, final Map<String, ?> p2) throws JAXBException;
}
