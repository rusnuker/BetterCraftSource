// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.db.jpa.converter;

import java.util.Iterator;
import org.apache.logging.log4j.util.StringMap;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;
import org.apache.logging.log4j.core.impl.ContextDataFactory;
import org.apache.logging.log4j.util.Strings;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import javax.persistence.PersistenceException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.util.BiConsumer;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.Converter;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import javax.persistence.AttributeConverter;

@Converter(autoApply = false)
public class ContextDataJsonAttributeConverter implements AttributeConverter<ReadOnlyStringMap, String>
{
    static final ObjectMapper OBJECT_MAPPER;
    
    public String convertToDatabaseColumn(final ReadOnlyStringMap contextData) {
        if (contextData == null) {
            return null;
        }
        try {
            final JsonNodeFactory factory = ContextDataJsonAttributeConverter.OBJECT_MAPPER.getNodeFactory();
            final ObjectNode root = factory.objectNode();
            contextData.forEach(new BiConsumer<String, Object>() {
                @Override
                public void accept(final String key, final Object value) {
                    root.put(key, String.valueOf(value));
                }
            });
            return ContextDataJsonAttributeConverter.OBJECT_MAPPER.writeValueAsString((Object)root);
        }
        catch (final Exception e) {
            throw new PersistenceException("Failed to convert contextData to JSON string.", (Throwable)e);
        }
    }
    
    public ReadOnlyStringMap convertToEntityAttribute(final String s) {
        if (Strings.isEmpty(s)) {
            return null;
        }
        try {
            final StringMap result = ContextDataFactory.createContextData();
            final ObjectNode root = (ObjectNode)ContextDataJsonAttributeConverter.OBJECT_MAPPER.readTree(s);
            final Iterator<Map.Entry<String, JsonNode>> entries = root.fields();
            while (entries.hasNext()) {
                final Map.Entry<String, JsonNode> entry = entries.next();
                final Object value = entry.getValue().textValue();
                result.putValue(entry.getKey(), value);
            }
            return result;
        }
        catch (final IOException e) {
            throw new PersistenceException("Failed to convert JSON string to map.", (Throwable)e);
        }
    }
    
    static {
        OBJECT_MAPPER = new ObjectMapper();
    }
}
