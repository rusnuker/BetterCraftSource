// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind.helpers;

import javax.xml.bind.attachment.AttachmentUnmarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.validation.Schema;
import javax.xml.bind.JAXBElement;
import org.w3c.dom.Node;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLEventReader;
import javax.xml.bind.PropertyException;
import javax.xml.bind.UnmarshalException;
import java.io.Reader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.net.URL;
import org.xml.sax.InputSource;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.Source;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.XMLReader;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.Unmarshaller;

public abstract class AbstractUnmarshallerImpl implements Unmarshaller
{
    private ValidationEventHandler eventHandler;
    protected boolean validating;
    private XMLReader reader;
    
    public AbstractUnmarshallerImpl() {
        this.eventHandler = new DefaultValidationEventHandler();
        this.validating = false;
        this.reader = null;
    }
    
    protected XMLReader getXMLReader() throws JAXBException {
        if (this.reader == null) {
            try {
                final SAXParserFactory parserFactory = SAXParserFactory.newInstance();
                parserFactory.setNamespaceAware(true);
                parserFactory.setValidating(false);
                this.reader = parserFactory.newSAXParser().getXMLReader();
            }
            catch (final ParserConfigurationException e) {
                throw new JAXBException(e);
            }
            catch (final SAXException e2) {
                throw new JAXBException(e2);
            }
        }
        return this.reader;
    }
    
    @Override
    public Object unmarshal(final Source source) throws JAXBException {
        if (source == null) {
            throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "source"));
        }
        if (source instanceof SAXSource) {
            return this.unmarshal((SAXSource)source);
        }
        if (source instanceof StreamSource) {
            return this.unmarshal(streamSourceToInputSource((StreamSource)source));
        }
        if (source instanceof DOMSource) {
            return this.unmarshal(((DOMSource)source).getNode());
        }
        throw new IllegalArgumentException();
    }
    
    private Object unmarshal(final SAXSource source) throws JAXBException {
        XMLReader r = source.getXMLReader();
        if (r == null) {
            r = this.getXMLReader();
        }
        return this.unmarshal(r, source.getInputSource());
    }
    
    protected abstract Object unmarshal(final XMLReader p0, final InputSource p1) throws JAXBException;
    
    @Override
    public final Object unmarshal(final InputSource source) throws JAXBException {
        if (source == null) {
            throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "source"));
        }
        return this.unmarshal(this.getXMLReader(), source);
    }
    
    private Object unmarshal(final String url) throws JAXBException {
        return this.unmarshal(new InputSource(url));
    }
    
    @Override
    public final Object unmarshal(final URL url) throws JAXBException {
        if (url == null) {
            throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "url"));
        }
        return this.unmarshal(url.toExternalForm());
    }
    
    @Override
    public final Object unmarshal(final File f) throws JAXBException {
        if (f == null) {
            throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "file"));
        }
        try {
            return this.unmarshal(new BufferedInputStream(new FileInputStream(f)));
        }
        catch (final FileNotFoundException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    
    @Override
    public final Object unmarshal(final InputStream is) throws JAXBException {
        if (is == null) {
            throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "is"));
        }
        final InputSource isrc = new InputSource(is);
        return this.unmarshal(isrc);
    }
    
    @Override
    public final Object unmarshal(final Reader reader) throws JAXBException {
        if (reader == null) {
            throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "reader"));
        }
        final InputSource isrc = new InputSource(reader);
        return this.unmarshal(isrc);
    }
    
    private static InputSource streamSourceToInputSource(final StreamSource ss) {
        final InputSource is = new InputSource();
        is.setSystemId(ss.getSystemId());
        is.setByteStream(ss.getInputStream());
        is.setCharacterStream(ss.getReader());
        return is;
    }
    
    @Override
    public boolean isValidating() throws JAXBException {
        return this.validating;
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
    
    @Override
    public void setValidating(final boolean validating) throws JAXBException {
        this.validating = validating;
    }
    
    @Override
    public ValidationEventHandler getEventHandler() throws JAXBException {
        return this.eventHandler;
    }
    
    protected UnmarshalException createUnmarshalException(final SAXException e) {
        final Exception nested = e.getException();
        if (nested instanceof UnmarshalException) {
            return (UnmarshalException)nested;
        }
        if (nested instanceof RuntimeException) {
            throw (RuntimeException)nested;
        }
        if (nested != null) {
            return new UnmarshalException(nested);
        }
        return new UnmarshalException(e);
    }
    
    @Override
    public void setProperty(final String name, final Object value) throws PropertyException {
        if (name == null) {
            throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "name"));
        }
        throw new PropertyException(name, value);
    }
    
    @Override
    public Object getProperty(final String name) throws PropertyException {
        if (name == null) {
            throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "name"));
        }
        throw new PropertyException(name);
    }
    
    @Override
    public Object unmarshal(final XMLEventReader reader) throws JAXBException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Object unmarshal(final XMLStreamReader reader) throws JAXBException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public <T> JAXBElement<T> unmarshal(final Node node, final Class<T> expectedType) throws JAXBException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public <T> JAXBElement<T> unmarshal(final Source source, final Class<T> expectedType) throws JAXBException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public <T> JAXBElement<T> unmarshal(final XMLStreamReader reader, final Class<T> expectedType) throws JAXBException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public <T> JAXBElement<T> unmarshal(final XMLEventReader reader, final Class<T> expectedType) throws JAXBException {
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
    public void setAttachmentUnmarshaller(final AttachmentUnmarshaller au) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public AttachmentUnmarshaller getAttachmentUnmarshaller() {
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
