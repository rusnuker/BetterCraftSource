// 
// Decompiled by Procyon v0.6.0
// 

package joptsimple.internal;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.Locale;

public class Messages
{
    private Messages() {
        throw new UnsupportedOperationException();
    }
    
    public static String message(final Locale locale, final String bundleName, final Class<?> type, final String key, final Object... args) {
        final ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);
        final String template = bundle.getString(type.getName() + '.' + key);
        final MessageFormat format = new MessageFormat(template);
        format.setLocale(locale);
        return format.format(args);
    }
}
