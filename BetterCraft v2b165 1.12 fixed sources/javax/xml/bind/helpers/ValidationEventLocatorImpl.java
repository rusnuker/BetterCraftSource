// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind.helpers;

import java.text.MessageFormat;
import java.net.MalformedURLException;
import org.xml.sax.SAXParseException;
import org.xml.sax.Locator;
import org.w3c.dom.Node;
import java.net.URL;
import javax.xml.bind.ValidationEventLocator;

public class ValidationEventLocatorImpl implements ValidationEventLocator
{
    private URL url;
    private int offset;
    private int lineNumber;
    private int columnNumber;
    private Object object;
    private Node node;
    
    public ValidationEventLocatorImpl() {
        this.url = null;
        this.offset = -1;
        this.lineNumber = -1;
        this.columnNumber = -1;
        this.object = null;
        this.node = null;
    }
    
    public ValidationEventLocatorImpl(final Locator loc) {
        this.url = null;
        this.offset = -1;
        this.lineNumber = -1;
        this.columnNumber = -1;
        this.object = null;
        this.node = null;
        if (loc == null) {
            throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "loc"));
        }
        this.url = toURL(loc.getSystemId());
        this.columnNumber = loc.getColumnNumber();
        this.lineNumber = loc.getLineNumber();
    }
    
    public ValidationEventLocatorImpl(final SAXParseException e) {
        this.url = null;
        this.offset = -1;
        this.lineNumber = -1;
        this.columnNumber = -1;
        this.object = null;
        this.node = null;
        if (e == null) {
            throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "e"));
        }
        this.url = toURL(e.getSystemId());
        this.columnNumber = e.getColumnNumber();
        this.lineNumber = e.getLineNumber();
    }
    
    public ValidationEventLocatorImpl(final Node _node) {
        this.url = null;
        this.offset = -1;
        this.lineNumber = -1;
        this.columnNumber = -1;
        this.object = null;
        this.node = null;
        if (_node == null) {
            throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "_node"));
        }
        this.node = _node;
    }
    
    public ValidationEventLocatorImpl(final Object _object) {
        this.url = null;
        this.offset = -1;
        this.lineNumber = -1;
        this.columnNumber = -1;
        this.object = null;
        this.node = null;
        if (_object == null) {
            throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "_object"));
        }
        this.object = _object;
    }
    
    private static URL toURL(final String systemId) {
        try {
            return new URL(systemId);
        }
        catch (final MalformedURLException e) {
            return null;
        }
    }
    
    @Override
    public URL getURL() {
        return this.url;
    }
    
    public void setURL(final URL _url) {
        this.url = _url;
    }
    
    @Override
    public int getOffset() {
        return this.offset;
    }
    
    public void setOffset(final int _offset) {
        this.offset = _offset;
    }
    
    @Override
    public int getLineNumber() {
        return this.lineNumber;
    }
    
    public void setLineNumber(final int _lineNumber) {
        this.lineNumber = _lineNumber;
    }
    
    @Override
    public int getColumnNumber() {
        return this.columnNumber;
    }
    
    public void setColumnNumber(final int _columnNumber) {
        this.columnNumber = _columnNumber;
    }
    
    @Override
    public Object getObject() {
        return this.object;
    }
    
    public void setObject(final Object _object) {
        this.object = _object;
    }
    
    @Override
    public Node getNode() {
        return this.node;
    }
    
    public void setNode(final Node _node) {
        this.node = _node;
    }
    
    @Override
    public String toString() {
        return MessageFormat.format("[node={0},object={1},url={2},line={3},col={4},offset={5}]", this.getNode(), this.getObject(), this.getURL(), String.valueOf(this.getLineNumber()), String.valueOf(this.getColumnNumber()), String.valueOf(this.getOffset()));
    }
}
