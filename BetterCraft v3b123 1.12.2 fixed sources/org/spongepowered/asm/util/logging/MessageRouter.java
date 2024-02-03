// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.util.logging;

import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.logging.Level;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import org.spongepowered.asm.logging.ILogger;
import javax.annotation.processing.Messager;

public final class MessageRouter
{
    private static Messager messager;
    
    private MessageRouter() {
    }
    
    public static Messager getMessager() {
        if (MessageRouter.messager == null) {
            MessageRouter.messager = new LoggingMessager();
        }
        return MessageRouter.messager;
    }
    
    public static void setMessager(final Messager messager) {
        MessageRouter.messager = ((messager == null) ? null : new DebugInterceptingMessager(messager));
    }
    
    static class LoggingMessager implements Messager
    {
        private static final ILogger logger;
        
        @Override
        public void printMessage(final Diagnostic.Kind kind, final CharSequence msg) {
            LoggingMessager.logger.log(messageKindToLoggingLevel(kind), msg.toString(), new Object[0]);
        }
        
        @Override
        public void printMessage(final Diagnostic.Kind kind, final CharSequence msg, final Element e) {
            LoggingMessager.logger.log(messageKindToLoggingLevel(kind), msg.toString(), new Object[0]);
        }
        
        @Override
        public void printMessage(final Diagnostic.Kind kind, final CharSequence msg, final Element e, final AnnotationMirror a) {
            LoggingMessager.logger.log(messageKindToLoggingLevel(kind), msg.toString(), new Object[0]);
        }
        
        @Override
        public void printMessage(final Diagnostic.Kind kind, final CharSequence msg, final Element e, final AnnotationMirror a, final AnnotationValue v) {
            LoggingMessager.logger.log(messageKindToLoggingLevel(kind), msg.toString(), new Object[0]);
        }
        
        private static Level messageKindToLoggingLevel(final Diagnostic.Kind kind) {
            switch (kind) {
                case ERROR: {
                    return Level.ERROR;
                }
                case WARNING:
                case MANDATORY_WARNING: {
                    return Level.WARN;
                }
                case NOTE: {
                    return Level.INFO;
                }
                default: {
                    return Level.DEBUG;
                }
            }
        }
        
        static {
            logger = MixinService.getService().getLogger("mixin");
        }
    }
    
    static class DebugInterceptingMessager implements Messager
    {
        private final Messager wrapped;
        
        DebugInterceptingMessager(final Messager messager) {
            this.wrapped = messager;
        }
        
        @Override
        public void printMessage(final Diagnostic.Kind kind, final CharSequence msg) {
            if (kind != Diagnostic.Kind.OTHER) {
                this.wrapped.printMessage(kind, msg);
            }
        }
        
        @Override
        public void printMessage(final Diagnostic.Kind kind, final CharSequence msg, final Element e) {
            if (kind != Diagnostic.Kind.OTHER) {
                this.wrapped.printMessage(kind, msg, e);
            }
        }
        
        @Override
        public void printMessage(final Diagnostic.Kind kind, final CharSequence msg, final Element e, final AnnotationMirror a) {
            if (kind != Diagnostic.Kind.OTHER) {
                this.wrapped.printMessage(kind, msg, e, a);
            }
        }
        
        @Override
        public void printMessage(final Diagnostic.Kind kind, final CharSequence msg, final Element e, final AnnotationMirror a, final AnnotationValue v) {
            if (kind != Diagnostic.Kind.OTHER) {
                this.wrapped.printMessage(kind, msg, e, a, v);
            }
        }
    }
}
