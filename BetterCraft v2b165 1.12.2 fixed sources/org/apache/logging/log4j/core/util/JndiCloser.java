// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.util;

import javax.naming.NamingException;
import javax.naming.Context;

public final class JndiCloser
{
    private JndiCloser() {
    }
    
    public static void close(final Context context) throws NamingException {
        if (context != null) {
            context.close();
        }
    }
    
    public static boolean closeSilently(final Context context) {
        try {
            close(context);
            return true;
        }
        catch (final NamingException ignored) {
            return false;
        }
    }
}
