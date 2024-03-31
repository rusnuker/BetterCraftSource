// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.io.File;
import java.net.URL;

public final class Files
{
    private Files() {
    }
    
    public static File toFile(final URL url) throws URISyntaxException {
        return (url != null) ? toFile(url.toURI()) : null;
    }
    
    public static File toFile(URI uri) {
        if (uri == null) {
            return null;
        }
        if ("file".equals(uri.getScheme()) && uri.getAuthority() != null) {
            final String strUri = uri.toString();
            if (strUri.startsWith("file://") && !strUri.startsWith("file:///")) {
                try {
                    uri = new URI("file:////" + strUri.substring(7));
                }
                catch (final URISyntaxException ex) {
                    throw new IllegalArgumentException(ex.getMessage());
                }
            }
        }
        return new File(uri);
    }
}
