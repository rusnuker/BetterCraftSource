// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind.util;

import org.xml.sax.ContentHandler;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.UnmarshallerHandler;
import javax.xml.transform.sax.SAXResult;

public class JAXBResult extends SAXResult
{
    private final UnmarshallerHandler unmarshallerHandler;
    
    public JAXBResult(final JAXBContext context) throws JAXBException {
        this((context == null) ? assertionFailed() : context.createUnmarshaller());
    }
    
    public JAXBResult(final Unmarshaller _unmarshaller) throws JAXBException {
        if (_unmarshaller == null) {
            throw new JAXBException(Messages.format("JAXBResult.NullUnmarshaller"));
        }
        super.setHandler(this.unmarshallerHandler = _unmarshaller.getUnmarshallerHandler());
    }
    
    public Object getResult() throws JAXBException {
        return this.unmarshallerHandler.getResult();
    }
    
    private static Unmarshaller assertionFailed() throws JAXBException {
        throw new JAXBException(Messages.format("JAXBResult.NullContext"));
    }
}
