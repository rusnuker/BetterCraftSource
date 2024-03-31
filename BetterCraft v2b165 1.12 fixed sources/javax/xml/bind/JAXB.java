// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind;

import java.net.URLConnection;
import javax.xml.transform.stream.StreamResult;
import java.beans.Introspector;
import javax.xml.namespace.QName;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.Result;
import java.io.Writer;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.io.Reader;
import java.io.InputStream;
import java.net.URI;
import java.io.IOException;
import java.net.URL;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.lang.ref.WeakReference;

public final class JAXB
{
    private static volatile WeakReference<Cache> cache;
    
    private JAXB() {
    }
    
    private static <T> JAXBContext getContext(final Class<T> type) throws JAXBException {
        final WeakReference<Cache> c = JAXB.cache;
        if (c != null) {
            final Cache d = c.get();
            if (d != null && d.type == type) {
                return d.context;
            }
        }
        final Cache d = new Cache(type);
        JAXB.cache = new WeakReference<Cache>(d);
        return d.context;
    }
    
    public static <T> T unmarshal(final File xml, final Class<T> type) {
        try {
            final JAXBElement<T> item = getContext(type).createUnmarshaller().unmarshal(new StreamSource(xml), type);
            return item.getValue();
        }
        catch (final JAXBException e) {
            throw new DataBindingException(e);
        }
    }
    
    public static <T> T unmarshal(final URL xml, final Class<T> type) {
        try {
            final JAXBElement<T> item = getContext(type).createUnmarshaller().unmarshal(toSource(xml), type);
            return item.getValue();
        }
        catch (final JAXBException e) {
            throw new DataBindingException(e);
        }
        catch (final IOException e2) {
            throw new DataBindingException(e2);
        }
    }
    
    public static <T> T unmarshal(final URI xml, final Class<T> type) {
        try {
            final JAXBElement<T> item = getContext(type).createUnmarshaller().unmarshal(toSource(xml), type);
            return item.getValue();
        }
        catch (final JAXBException e) {
            throw new DataBindingException(e);
        }
        catch (final IOException e2) {
            throw new DataBindingException(e2);
        }
    }
    
    public static <T> T unmarshal(final String xml, final Class<T> type) {
        try {
            final JAXBElement<T> item = getContext(type).createUnmarshaller().unmarshal(toSource(xml), type);
            return item.getValue();
        }
        catch (final JAXBException e) {
            throw new DataBindingException(e);
        }
        catch (final IOException e2) {
            throw new DataBindingException(e2);
        }
    }
    
    public static <T> T unmarshal(final InputStream xml, final Class<T> type) {
        try {
            final JAXBElement<T> item = getContext(type).createUnmarshaller().unmarshal(toSource(xml), type);
            return item.getValue();
        }
        catch (final JAXBException e) {
            throw new DataBindingException(e);
        }
        catch (final IOException e2) {
            throw new DataBindingException(e2);
        }
    }
    
    public static <T> T unmarshal(final Reader xml, final Class<T> type) {
        try {
            final JAXBElement<T> item = getContext(type).createUnmarshaller().unmarshal(toSource(xml), type);
            return item.getValue();
        }
        catch (final JAXBException e) {
            throw new DataBindingException(e);
        }
        catch (final IOException e2) {
            throw new DataBindingException(e2);
        }
    }
    
    public static <T> T unmarshal(final Source xml, final Class<T> type) {
        try {
            final JAXBElement<T> item = getContext(type).createUnmarshaller().unmarshal(toSource(xml), type);
            return item.getValue();
        }
        catch (final JAXBException e) {
            throw new DataBindingException(e);
        }
        catch (final IOException e2) {
            throw new DataBindingException(e2);
        }
    }
    
    private static Source toSource(Object xml) throws IOException {
        if (xml == null) {
            throw new IllegalArgumentException("no XML is given");
        }
        if (xml instanceof String) {
            try {
                xml = new URI((String)xml);
            }
            catch (final URISyntaxException e) {
                xml = new File((String)xml);
            }
        }
        if (xml instanceof File) {
            final File file = (File)xml;
            return new StreamSource(file);
        }
        if (xml instanceof URI) {
            final URI uri = (URI)xml;
            xml = uri.toURL();
        }
        if (xml instanceof URL) {
            final URL url = (URL)xml;
            return new StreamSource(url.toExternalForm());
        }
        if (xml instanceof InputStream) {
            final InputStream in = (InputStream)xml;
            return new StreamSource(in);
        }
        if (xml instanceof Reader) {
            final Reader r = (Reader)xml;
            return new StreamSource(r);
        }
        if (xml instanceof Source) {
            return (Source)xml;
        }
        throw new IllegalArgumentException("I don't understand how to handle " + xml.getClass());
    }
    
    public static void marshal(final Object jaxbObject, final File xml) {
        _marshal(jaxbObject, xml);
    }
    
    public static void marshal(final Object jaxbObject, final URL xml) {
        _marshal(jaxbObject, xml);
    }
    
    public static void marshal(final Object jaxbObject, final URI xml) {
        _marshal(jaxbObject, xml);
    }
    
    public static void marshal(final Object jaxbObject, final String xml) {
        _marshal(jaxbObject, xml);
    }
    
    public static void marshal(final Object jaxbObject, final OutputStream xml) {
        _marshal(jaxbObject, xml);
    }
    
    public static void marshal(final Object jaxbObject, final Writer xml) {
        _marshal(jaxbObject, xml);
    }
    
    public static void marshal(final Object jaxbObject, final Result xml) {
        _marshal(jaxbObject, xml);
    }
    
    private static void _marshal(Object jaxbObject, final Object xml) {
        try {
            JAXBContext context;
            if (jaxbObject instanceof JAXBElement) {
                context = getContext(((JAXBElement)jaxbObject).getDeclaredType());
            }
            else {
                final Class<?> clazz = jaxbObject.getClass();
                final XmlRootElement r = clazz.getAnnotation(XmlRootElement.class);
                context = getContext(clazz);
                if (r == null) {
                    jaxbObject = new JAXBElement(new QName(inferName(clazz)), (Class<Object>)clazz, jaxbObject);
                }
            }
            final Marshaller m = context.createMarshaller();
            m.setProperty("jaxb.formatted.output", true);
            m.marshal(jaxbObject, toResult(xml));
        }
        catch (final JAXBException e) {
            throw new DataBindingException(e);
        }
        catch (final IOException e2) {
            throw new DataBindingException(e2);
        }
    }
    
    private static String inferName(final Class clazz) {
        return Introspector.decapitalize(clazz.getSimpleName());
    }
    
    private static Result toResult(Object xml) throws IOException {
        if (xml == null) {
            throw new IllegalArgumentException("no XML is given");
        }
        if (xml instanceof String) {
            try {
                xml = new URI((String)xml);
            }
            catch (final URISyntaxException e) {
                xml = new File((String)xml);
            }
        }
        if (xml instanceof File) {
            final File file = (File)xml;
            return new StreamResult(file);
        }
        if (xml instanceof URI) {
            final URI uri = (URI)xml;
            xml = uri.toURL();
        }
        if (xml instanceof URL) {
            final URL url = (URL)xml;
            final URLConnection con = url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(false);
            con.connect();
            return new StreamResult(con.getOutputStream());
        }
        if (xml instanceof OutputStream) {
            final OutputStream os = (OutputStream)xml;
            return new StreamResult(os);
        }
        if (xml instanceof Writer) {
            final Writer w = (Writer)xml;
            return new StreamResult(w);
        }
        if (xml instanceof Result) {
            return (Result)xml;
        }
        throw new IllegalArgumentException("I don't understand how to handle " + xml.getClass());
    }
    
    private static final class Cache
    {
        final Class type;
        final JAXBContext context;
        
        public Cache(final Class type) throws JAXBException {
            this.type = type;
            this.context = JAXBContext.newInstance(type);
        }
    }
}
