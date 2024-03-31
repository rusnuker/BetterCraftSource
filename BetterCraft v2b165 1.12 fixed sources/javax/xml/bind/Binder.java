// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind;

import javax.xml.validation.Schema;

public abstract class Binder<XmlNode>
{
    public abstract Object unmarshal(final XmlNode p0) throws JAXBException;
    
    public abstract <T> JAXBElement<T> unmarshal(final XmlNode p0, final Class<T> p1) throws JAXBException;
    
    public abstract void marshal(final Object p0, final XmlNode p1) throws JAXBException;
    
    public abstract XmlNode getXMLNode(final Object p0);
    
    public abstract Object getJAXBNode(final XmlNode p0);
    
    public abstract XmlNode updateXML(final Object p0) throws JAXBException;
    
    public abstract XmlNode updateXML(final Object p0, final XmlNode p1) throws JAXBException;
    
    public abstract Object updateJAXB(final XmlNode p0) throws JAXBException;
    
    public abstract void setSchema(final Schema p0);
    
    public abstract Schema getSchema();
    
    public abstract void setEventHandler(final ValidationEventHandler p0) throws JAXBException;
    
    public abstract ValidationEventHandler getEventHandler() throws JAXBException;
    
    public abstract void setProperty(final String p0, final Object p1) throws PropertyException;
    
    public abstract Object getProperty(final String p0) throws PropertyException;
}
