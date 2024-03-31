// 
// Decompiled by Procyon v0.6.0
// 

package joptsimple.util;

import joptsimple.internal.Messages;
import java.util.Locale;
import java.net.UnknownHostException;
import joptsimple.ValueConversionException;
import java.net.InetAddress;
import joptsimple.ValueConverter;

public class InetAddressConverter implements ValueConverter<InetAddress>
{
    @Override
    public InetAddress convert(final String value) {
        try {
            return InetAddress.getByName(value);
        }
        catch (final UnknownHostException e) {
            throw new ValueConversionException(this.message(value));
        }
    }
    
    @Override
    public Class<InetAddress> valueType() {
        return InetAddress.class;
    }
    
    @Override
    public String valuePattern() {
        return null;
    }
    
    private String message(final String value) {
        return Messages.message(Locale.getDefault(), "joptsimple.ExceptionMessages", InetAddressConverter.class, "message", value);
    }
}
