// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.db.jpa.converter;

import org.apache.logging.log4j.util.Strings;
import javax.persistence.Converter;
import org.apache.logging.log4j.Level;
import javax.persistence.AttributeConverter;

@Converter(autoApply = false)
public class LevelAttributeConverter implements AttributeConverter<Level, String>
{
    public String convertToDatabaseColumn(final Level level) {
        if (level == null) {
            return null;
        }
        return level.name();
    }
    
    public Level convertToEntityAttribute(final String s) {
        if (Strings.isEmpty(s)) {
            return null;
        }
        return Level.toLevel(s, null);
    }
}
