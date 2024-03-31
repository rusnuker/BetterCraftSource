// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.db.jpa.converter;

import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.util.Strings;
import javax.persistence.Converter;
import org.apache.logging.log4j.Marker;
import javax.persistence.AttributeConverter;

@Converter(autoApply = false)
public class MarkerAttributeConverter implements AttributeConverter<Marker, String>
{
    public String convertToDatabaseColumn(final Marker marker) {
        if (marker == null) {
            return null;
        }
        return marker.toString();
    }
    
    public Marker convertToEntityAttribute(final String s) {
        if (Strings.isEmpty(s)) {
            return null;
        }
        final int bracket = s.indexOf("[");
        return (bracket < 1) ? MarkerManager.getMarker(s) : MarkerManager.getMarker(s.substring(0, bracket));
    }
}
