// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind;

import javax.xml.validation.Schema;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import java.io.Writer;
import java.io.File;
import java.io.OutputStream;
import javax.xml.transform.Result;

public interface Marshaller
{
    public static final String JAXB_ENCODING = "jaxb.encoding";
    public static final String JAXB_FORMATTED_OUTPUT = "jaxb.formatted.output";
    public static final String JAXB_SCHEMA_LOCATION = "jaxb.schemaLocation";
    public static final String JAXB_NO_NAMESPACE_SCHEMA_LOCATION = "jaxb.noNamespaceSchemaLocation";
    public static final String JAXB_FRAGMENT = "jaxb.fragment";
    
    void marshal(final Object p0, final Result p1) throws JAXBException;
    
    void marshal(final Object p0, final OutputStream p1) throws JAXBException;
    
    void marshal(final Object p0, final File p1) throws JAXBException;
    
    void marshal(final Object p0, final Writer p1) throws JAXBException;
    
    void marshal(final Object p0, final ContentHandler p1) throws JAXBException;
    
    void marshal(final Object p0, final Node p1) throws JAXBException;
    
    void marshal(final Object p0, final XMLStreamWriter p1) throws JAXBException;
    
    void marshal(final Object p0, final XMLEventWriter p1) throws JAXBException;
    
    Node getNode(final Object p0) throws JAXBException;
    
    void setProperty(final String p0, final Object p1) throws PropertyException;
    
    Object getProperty(final String p0) throws PropertyException;
    
    void setEventHandler(final ValidationEventHandler p0) throws JAXBException;
    
    ValidationEventHandler getEventHandler() throws JAXBException;
    
    void setAdapter(final XmlAdapter p0);
    
     <A extends XmlAdapter> void setAdapter(final Class<A> p0, final A p1);
    
     <A extends XmlAdapter> A getAdapter(final Class<A> p0);
    
    void setAttachmentMarshaller(final AttachmentMarshaller p0);
    
    AttachmentMarshaller getAttachmentMarshaller();
    
    void setSchema(final Schema p0);
    
    Schema getSchema();
    
    void setListener(final Listener p0);
    
    Listener getListener();
    
    public abstract static class Listener
    {
        public void beforeMarshal(final Object source) {
        }
        
        public void afterMarshal(final Object source) {
        }
    }
}
