// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind;

import javax.xml.bind.attachment.AttachmentUnmarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.validation.Schema;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import java.net.URL;
import java.io.Reader;
import java.io.InputStream;
import java.io.File;

public interface Unmarshaller
{
    Object unmarshal(final File p0) throws JAXBException;
    
    Object unmarshal(final InputStream p0) throws JAXBException;
    
    Object unmarshal(final Reader p0) throws JAXBException;
    
    Object unmarshal(final URL p0) throws JAXBException;
    
    Object unmarshal(final InputSource p0) throws JAXBException;
    
    Object unmarshal(final Node p0) throws JAXBException;
    
     <T> JAXBElement<T> unmarshal(final Node p0, final Class<T> p1) throws JAXBException;
    
    Object unmarshal(final Source p0) throws JAXBException;
    
     <T> JAXBElement<T> unmarshal(final Source p0, final Class<T> p1) throws JAXBException;
    
    Object unmarshal(final XMLStreamReader p0) throws JAXBException;
    
     <T> JAXBElement<T> unmarshal(final XMLStreamReader p0, final Class<T> p1) throws JAXBException;
    
    Object unmarshal(final XMLEventReader p0) throws JAXBException;
    
     <T> JAXBElement<T> unmarshal(final XMLEventReader p0, final Class<T> p1) throws JAXBException;
    
    UnmarshallerHandler getUnmarshallerHandler();
    
    @Deprecated
    void setValidating(final boolean p0) throws JAXBException;
    
    @Deprecated
    boolean isValidating() throws JAXBException;
    
    void setEventHandler(final ValidationEventHandler p0) throws JAXBException;
    
    ValidationEventHandler getEventHandler() throws JAXBException;
    
    void setProperty(final String p0, final Object p1) throws PropertyException;
    
    Object getProperty(final String p0) throws PropertyException;
    
    void setSchema(final Schema p0);
    
    Schema getSchema();
    
    void setAdapter(final XmlAdapter p0);
    
     <A extends XmlAdapter> void setAdapter(final Class<A> p0, final A p1);
    
     <A extends XmlAdapter> A getAdapter(final Class<A> p0);
    
    void setAttachmentUnmarshaller(final AttachmentUnmarshaller p0);
    
    AttachmentUnmarshaller getAttachmentUnmarshaller();
    
    void setListener(final Listener p0);
    
    Listener getListener();
    
    public abstract static class Listener
    {
        public void beforeUnmarshal(final Object target, final Object parent) {
        }
        
        public void afterUnmarshal(final Object target, final Object parent) {
        }
    }
}
