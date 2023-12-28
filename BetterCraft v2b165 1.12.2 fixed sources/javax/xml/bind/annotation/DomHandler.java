// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind.annotation;

import javax.xml.transform.Source;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.transform.Result;

public interface DomHandler<ElementT, ResultT extends Result>
{
    ResultT createUnmarshaller(final ValidationEventHandler p0);
    
    ElementT getElement(final ResultT p0);
    
    Source marshal(final ElementT p0, final ValidationEventHandler p1);
}
