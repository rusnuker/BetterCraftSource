// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind.helpers;

import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.validation.Schema;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.XMLEventWriter;
import javax.xml.bind.PropertyException;
import java.io.UnsupportedEncodingException;
import javax.xml.transform.dom.DOMResult;
import org.w3c.dom.Node;
import javax.xml.transform.sax.SAXResult;
import org.xml.sax.ContentHandler;
import java.io.Writer;
import java.io.IOException;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.OutputStream;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.Marshaller;

public abstract class AbstractMarshallerImpl implements Marshaller
{
    private ValidationEventHandler eventHandler;
    private String encoding;
    private String schemaLocation;
    private String noNSSchemaLocation;
    private boolean formattedOutput;
    private boolean fragment;
    static String[] aliases;
    
    static {
        AbstractMarshallerImpl.aliases = new String[] { "UTF-8", "UTF8", "UTF-16", "Unicode", "UTF-16BE", "UnicodeBigUnmarked", "UTF-16LE", "UnicodeLittleUnmarked", "US-ASCII", "ASCII", "TIS-620", "TIS620", "ISO-10646-UCS-2", "Unicode", "EBCDIC-CP-US", "cp037", "EBCDIC-CP-CA", "cp037", "EBCDIC-CP-NL", "cp037", "EBCDIC-CP-WT", "cp037", "EBCDIC-CP-DK", "cp277", "EBCDIC-CP-NO", "cp277", "EBCDIC-CP-FI", "cp278", "EBCDIC-CP-SE", "cp278", "EBCDIC-CP-IT", "cp280", "EBCDIC-CP-ES", "cp284", "EBCDIC-CP-GB", "cp285", "EBCDIC-CP-FR", "cp297", "EBCDIC-CP-AR1", "cp420", "EBCDIC-CP-HE", "cp424", "EBCDIC-CP-BE", "cp500", "EBCDIC-CP-CH", "cp500", "EBCDIC-CP-ROECE", "cp870", "EBCDIC-CP-YU", "cp870", "EBCDIC-CP-IS", "cp871", "EBCDIC-CP-AR2", "cp918" };
    }
    
    public AbstractMarshallerImpl() {
        this.eventHandler = new DefaultValidationEventHandler();
        this.encoding = "UTF-8";
        this.schemaLocation = null;
        this.noNSSchemaLocation = null;
        this.formattedOutput = false;
        this.fragment = false;
    }
    
    @Override
    public final void marshal(final Object obj, final OutputStream os) throws JAXBException {
        this.checkNotNull(obj, "obj", os, "os");
        this.marshal(obj, new StreamResult(os));
    }
    
    @Override
    public void marshal(final Object jaxbElement, final File output) throws JAXBException {
        this.checkNotNull(jaxbElement, "jaxbElement", output, "output");
        try {
            final OutputStream os = new BufferedOutputStream(new FileOutputStream(output));
            try {
                this.marshal(jaxbElement, new StreamResult(os));
            }
            finally {
                os.close();
            }
            os.close();
        }
        catch (final IOException e) {
            throw new JAXBException(e);
        }
    }
    
    @Override
    public final void marshal(final Object obj, final Writer w) throws JAXBException {
        this.checkNotNull(obj, "obj", w, "writer");
        this.marshal(obj, new StreamResult(w));
    }
    
    @Override
    public final void marshal(final Object obj, final ContentHandler handler) throws JAXBException {
        this.checkNotNull(obj, "obj", handler, "handler");
        this.marshal(obj, new SAXResult(handler));
    }
    
    @Override
    public final void marshal(final Object obj, final Node node) throws JAXBException {
        this.checkNotNull(obj, "obj", node, "node");
        this.marshal(obj, new DOMResult(node));
    }
    
    @Override
    public Node getNode(final Object obj) throws JAXBException {
        this.checkNotNull(obj, "obj", Boolean.TRUE, "foo");
        throw new UnsupportedOperationException();
    }
    
    protected String getEncoding() {
        return this.encoding;
    }
    
    protected void setEncoding(final String encoding) {
        this.encoding = encoding;
    }
    
    protected String getSchemaLocation() {
        return this.schemaLocation;
    }
    
    protected void setSchemaLocation(final String location) {
        this.schemaLocation = location;
    }
    
    protected String getNoNSSchemaLocation() {
        return this.noNSSchemaLocation;
    }
    
    protected void setNoNSSchemaLocation(final String location) {
        this.noNSSchemaLocation = location;
    }
    
    protected boolean isFormattedOutput() {
        return this.formattedOutput;
    }
    
    protected void setFormattedOutput(final boolean v) {
        this.formattedOutput = v;
    }
    
    protected boolean isFragment() {
        return this.fragment;
    }
    
    protected void setFragment(final boolean v) {
        this.fragment = v;
    }
    
    protected String getJavaEncoding(final String encoding) throws UnsupportedEncodingException {
        try {
            "1".getBytes(encoding);
            return encoding;
        }
        catch (final UnsupportedEncodingException e) {
            for (int i = 0; i < AbstractMarshallerImpl.aliases.length; i += 2) {
                if (encoding.equals(AbstractMarshallerImpl.aliases[i])) {
                    "1".getBytes(AbstractMarshallerImpl.aliases[i + 1]);
                    return AbstractMarshallerImpl.aliases[i + 1];
                }
            }
            throw new UnsupportedEncodingException(encoding);
        }
    }
    
    @Override
    public void setProperty(final String name, final Object value) throws PropertyException {
        if (name == null) {
            throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "name"));
        }
        if ("jaxb.encoding".equals(name)) {
            this.checkString(name, value);
            this.setEncoding((String)value);
            return;
        }
        if ("jaxb.formatted.output".equals(name)) {
            this.checkBoolean(name, value);
            this.setFormattedOutput((boolean)value);
            return;
        }
        if ("jaxb.noNamespaceSchemaLocation".equals(name)) {
            this.checkString(name, value);
            this.setNoNSSchemaLocation((String)value);
            return;
        }
        if ("jaxb.schemaLocation".equals(name)) {
            this.checkString(name, value);
            this.setSchemaLocation((String)value);
            return;
        }
        if ("jaxb.fragment".equals(name)) {
            this.checkBoolean(name, value);
            this.setFragment((boolean)value);
            return;
        }
        throw new PropertyException(name, value);
    }
    
    @Override
    public Object getProperty(final String name) throws PropertyException {
        if (name == null) {
            throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "name"));
        }
        if ("jaxb.encoding".equals(name)) {
            return this.getEncoding();
        }
        if ("jaxb.formatted.output".equals(name)) {
            return this.isFormattedOutput() ? Boolean.TRUE : Boolean.FALSE;
        }
        if ("jaxb.noNamespaceSchemaLocation".equals(name)) {
            return this.getNoNSSchemaLocation();
        }
        if ("jaxb.schemaLocation".equals(name)) {
            return this.getSchemaLocation();
        }
        if ("jaxb.fragment".equals(name)) {
            return this.isFragment() ? Boolean.TRUE : Boolean.FALSE;
        }
        throw new PropertyException(name);
    }
    
    @Override
    public ValidationEventHandler getEventHandler() throws JAXBException {
        return this.eventHandler;
    }
    
    @Override
    public void setEventHandler(final ValidationEventHandler handler) throws JAXBException {
        if (handler == null) {
            this.eventHandler = new DefaultValidationEventHandler();
        }
        else {
            this.eventHandler = handler;
        }
    }
    
    private void checkBoolean(final String name, final Object value) throws PropertyException {
        if (!(value instanceof Boolean)) {
            throw new PropertyException(Messages.format("AbstractMarshallerImpl.MustBeBoolean", name));
        }
    }
    
    private void checkString(final String name, final Object value) throws PropertyException {
        if (!(value instanceof String)) {
            throw new PropertyException(Messages.format("AbstractMarshallerImpl.MustBeString", name));
        }
    }
    
    private void checkNotNull(final Object o1, final String o1Name, final Object o2, final String o2Name) {
        if (o1 == null) {
            throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", o1Name));
        }
        if (o2 == null) {
            throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", o2Name));
        }
    }
    
    @Override
    public void marshal(final Object obj, final XMLEventWriter writer) throws JAXBException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void marshal(final Object obj, final XMLStreamWriter writer) throws JAXBException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void setSchema(final Schema schema) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Schema getSchema() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void setAdapter(final XmlAdapter adapter) {
        if (adapter == null) {
            throw new IllegalArgumentException();
        }
        this.setAdapter((Class<XmlAdapter>)adapter.getClass(), adapter);
    }
    
    @Override
    public <A extends XmlAdapter> void setAdapter(final Class<A> type, final A adapter) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public <A extends XmlAdapter> A getAdapter(final Class<A> type) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void setAttachmentMarshaller(final AttachmentMarshaller am) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public AttachmentMarshaller getAttachmentMarshaller() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void setListener(final Listener listener) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Listener getListener() {
        throw new UnsupportedOperationException();
    }
}
