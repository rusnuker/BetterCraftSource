// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.message;

public class FormattedMessageFactory extends AbstractMessageFactory
{
    @Override
    public Message newMessage(final String message, final Object... params) {
        return new FormattedMessage(message, params);
    }
}
