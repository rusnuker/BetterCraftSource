// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.db.jpa.converter;

import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.status.StatusLogger;
import javax.persistence.Converter;
import org.apache.logging.log4j.message.Message;
import javax.persistence.AttributeConverter;

@Converter(autoApply = false)
public class MessageAttributeConverter implements AttributeConverter<Message, String>
{
    private static final StatusLogger LOGGER;
    
    public String convertToDatabaseColumn(final Message message) {
        if (message == null) {
            return null;
        }
        return message.getFormattedMessage();
    }
    
    public Message convertToEntityAttribute(final String s) {
        if (Strings.isEmpty(s)) {
            return null;
        }
        return MessageAttributeConverter.LOGGER.getMessageFactory().newMessage(s);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
