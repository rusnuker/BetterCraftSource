// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind;

import java.io.IOException;
import javax.xml.transform.Result;

public abstract class SchemaOutputResolver
{
    public abstract Result createOutput(final String p0, final String p1) throws IOException;
}
