// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind;

public interface Validator
{
    @Deprecated
    void setEventHandler(final ValidationEventHandler p0) throws JAXBException;
    
    @Deprecated
    ValidationEventHandler getEventHandler() throws JAXBException;
    
    @Deprecated
    boolean validate(final Object p0) throws JAXBException;
    
    @Deprecated
    boolean validateRoot(final Object p0) throws JAXBException;
    
    @Deprecated
    void setProperty(final String p0, final Object p1) throws PropertyException;
    
    @Deprecated
    Object getProperty(final String p0) throws PropertyException;
}
