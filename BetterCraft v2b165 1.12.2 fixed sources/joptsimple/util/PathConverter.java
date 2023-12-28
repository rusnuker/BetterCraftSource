// 
// Decompiled by Procyon v0.6.0
// 

package joptsimple.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import joptsimple.ValueConversionException;
import java.nio.file.Paths;
import java.nio.file.Path;
import joptsimple.ValueConverter;

public class PathConverter implements ValueConverter<Path>
{
    private final PathProperties[] pathProperties;
    
    public PathConverter(final PathProperties... pathProperties) {
        this.pathProperties = pathProperties;
    }
    
    @Override
    public Path convert(final String value) {
        final Path path = Paths.get(value, new String[0]);
        if (this.pathProperties != null) {
            for (final PathProperties each : this.pathProperties) {
                if (!each.accept(path)) {
                    throw new ValueConversionException(this.message(each.getMessageKey(), path.toString()));
                }
            }
        }
        return path;
    }
    
    @Override
    public Class<Path> valueType() {
        return Path.class;
    }
    
    @Override
    public String valuePattern() {
        return null;
    }
    
    private String message(final String errorKey, final String value) {
        final ResourceBundle bundle = ResourceBundle.getBundle("joptsimple.ExceptionMessages");
        final Object[] arguments = { value, this.valuePattern() };
        final String template = bundle.getString(PathConverter.class.getName() + "." + errorKey + ".message");
        return new MessageFormat(template).format(arguments);
    }
}
