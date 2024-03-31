// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation.interfaces;

import java.util.HashSet;
import java.util.Set;
import javax.tools.Diagnostic;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.annotation.processing.Messager;

public interface IMessagerEx extends Messager
{
    void printMessage(final MessageType p0, final CharSequence p1);
    
    void printMessage(final MessageType p0, final CharSequence p1, final Element p2);
    
    void printMessage(final MessageType p0, final CharSequence p1, final Element p2, final AnnotationMirror p3);
    
    void printMessage(final MessageType p0, final CharSequence p1, final Element p2, final AnnotationMirror p3, final AnnotationValue p4);
    
    public enum MessageType
    {
        INFO(Diagnostic.Kind.NOTE), 
        NOTE(Diagnostic.Kind.NOTE), 
        ERROR(Diagnostic.Kind.ERROR), 
        WARNING(Diagnostic.Kind.WARNING), 
        MIXIN_ON_INVALID_TYPE(Diagnostic.Kind.ERROR), 
        MIXIN_SOFT_TARGET_NOT_FOUND(Diagnostic.Kind.ERROR), 
        MIXIN_SOFT_TARGET_NOT_RESOLVED(Diagnostic.Kind.WARNING), 
        MIXIN_SOFT_TARGET_IS_PUBLIC(Diagnostic.Kind.WARNING), 
        MIXIN_NO_TARGETS(Diagnostic.Kind.ERROR), 
        PARENT_VALIDATOR(Diagnostic.Kind.ERROR), 
        TARGET_VALIDATOR(Diagnostic.Kind.ERROR), 
        ACCESSOR_ATTACH_ERROR(Diagnostic.Kind.ERROR), 
        ACCESSOR_TARGET_NOT_FOUND(Diagnostic.Kind.ERROR), 
        ACCESSOR_TYPE_UNSUPPORTED(Diagnostic.Kind.WARNING), 
        ACCESSOR_NAME_UNRESOLVED(Diagnostic.Kind.WARNING), 
        INVOKER_RAW_RETURN_TYPE(Diagnostic.Kind.WARNING), 
        FACTORY_INVOKER_GENERIC_ARGS(Diagnostic.Kind.ERROR), 
        FACTORY_INVOKER_RETURN_TYPE(Diagnostic.Kind.ERROR), 
        FACTORY_INVOKER_NONSTATIC(Diagnostic.Kind.ERROR), 
        CONSTRAINT_VIOLATION(Diagnostic.Kind.ERROR), 
        INVALID_CONSTRAINT(Diagnostic.Kind.WARNING), 
        ACCESSOR_MAPPING_CONFLICT(Diagnostic.Kind.ERROR), 
        INJECTOR_MAPPING_CONFLICT(Diagnostic.Kind.ERROR), 
        OVERWRITE_MAPPING_CONFLICT(Diagnostic.Kind.ERROR), 
        SHADOW_MAPPING_CONFLICT(Diagnostic.Kind.ERROR), 
        INJECTOR_IN_INTERFACE(Diagnostic.Kind.ERROR), 
        INJECTOR_ON_NON_METHOD_ELEMENT(Diagnostic.Kind.WARNING), 
        OVERWRITE_ON_NON_METHOD_ELEMENT(Diagnostic.Kind.ERROR), 
        ACCESSOR_ON_NON_METHOD_ELEMENT(Diagnostic.Kind.ERROR), 
        SHADOW_ON_INVALID_ELEMENT(Diagnostic.Kind.ERROR), 
        INJECTOR_ON_NON_MIXIN_METHOD(Diagnostic.Kind.ERROR), 
        OVERWRITE_ON_NON_MIXIN_METHOD(Diagnostic.Kind.ERROR), 
        ACCESSOR_ON_NON_MIXIN_METHOD(Diagnostic.Kind.ERROR), 
        SHADOW_ON_NON_MIXIN_ELEMENT(Diagnostic.Kind.ERROR), 
        SOFT_IMPLEMENTS_ON_INVALID_TYPE(Diagnostic.Kind.ERROR), 
        SOFT_IMPLEMENTS_ON_NON_MIXIN(Diagnostic.Kind.ERROR), 
        SOFT_IMPLEMENTS_EMPTY(Diagnostic.Kind.WARNING), 
        TARGET_SELECTOR_VALIDATION(Diagnostic.Kind.ERROR), 
        INJECTOR_TARGET_NOT_FULLY_QUALIFIED(Diagnostic.Kind.ERROR), 
        MISSING_INJECTOR_DESC_MULTITARGET(Diagnostic.Kind.ERROR), 
        MISSING_INJECTOR_DESC_SINGLETARGET(Diagnostic.Kind.WARNING), 
        MISSING_INJECTOR_DESC_SIMULATED(Diagnostic.Kind.OTHER), 
        TARGET_ELEMENT_NOT_FOUND(Diagnostic.Kind.WARNING), 
        METHOD_VISIBILITY(Diagnostic.Kind.WARNING), 
        NO_OBFDATA_FOR_ACCESSOR(Diagnostic.Kind.WARNING), 
        NO_OBFDATA_FOR_CLASS(Diagnostic.Kind.WARNING), 
        NO_OBFDATA_FOR_TARGET(Diagnostic.Kind.ERROR), 
        NO_OBFDATA_FOR_CTOR(Diagnostic.Kind.WARNING), 
        NO_OBFDATA_FOR_OVERWRITE(Diagnostic.Kind.ERROR), 
        NO_OBFDATA_FOR_STATIC_OVERWRITE(Diagnostic.Kind.WARNING), 
        NO_OBFDATA_FOR_FIELD(Diagnostic.Kind.WARNING), 
        NO_OBFDATA_FOR_METHOD(Diagnostic.Kind.WARNING), 
        NO_OBFDATA_FOR_SHADOW(Diagnostic.Kind.WARNING), 
        NO_OBFDATA_FOR_SIMULATED_SHADOW(Diagnostic.Kind.WARNING), 
        NO_OBFDATA_FOR_SOFT_IMPLEMENTS(Diagnostic.Kind.ERROR), 
        BARE_REFERENCE(Diagnostic.Kind.WARNING), 
        OVERWRITE_DOCS(Diagnostic.Kind.WARNING);
        
        private static final String OPTION_PREFIX = "MSG_";
        private static boolean decorate;
        private static String prefix;
        private final Diagnostic.Kind originalKind;
        private Diagnostic.Kind kind;
        private boolean enabled;
        private boolean setByUser;
        
        private MessageType(final Diagnostic.Kind kind) {
            this.enabled = true;
            this.setByUser = false;
            this.kind = kind;
            this.originalKind = kind;
        }
        
        public boolean isError() {
            return this.kind == Diagnostic.Kind.ERROR;
        }
        
        public Diagnostic.Kind getKind() {
            return this.kind;
        }
        
        public void setKind(final Diagnostic.Kind kind) {
            this.kind = kind;
            this.setByUser = true;
        }
        
        public void quench(final Diagnostic.Kind kind) {
            if (!this.setByUser && kind.ordinal() > this.kind.ordinal()) {
                this.kind = kind;
            }
        }
        
        public boolean isEnabled() {
            return this.enabled;
        }
        
        public void setEnabled(final boolean enabled) {
            this.enabled = enabled;
            this.setByUser = true;
        }
        
        public void reset() {
            this.kind = this.originalKind;
            this.enabled = true;
            this.setByUser = false;
        }
        
        public CharSequence decorate(final CharSequence msg) {
            return MessageType.decorate ? String.format("%s[%s] %s", MessageType.prefix, this.name(), msg) : (MessageType.prefix + (Object)msg);
        }
        
        public static void setDecoration(final boolean enabled) {
            MessageType.decorate = enabled;
        }
        
        public static void setPrefix(final String prefix) {
            MessageType.prefix = prefix;
        }
        
        public static Set<String> getSupportedOptions() {
            final Set<String> supportedOptions = new HashSet<String>();
            for (final MessageType type : values()) {
                supportedOptions.add("MSG_" + type.name());
            }
            return supportedOptions;
        }
        
        public static void applyOptions(final IMixinAnnotationProcessor.CompilerEnvironment env, final IOptionProvider options) {
            setDecoration("true".equalsIgnoreCase(options.getOption("showMessageTypes")));
            MessageType.INFO.setEnabled(!env.isDevelopmentEnvironment() && !"true".equalsIgnoreCase(options.getOption("quiet")));
            if ("error".equalsIgnoreCase(options.getOption("overwriteErrorLevel"))) {
                MessageType.OVERWRITE_DOCS.setKind(Diagnostic.Kind.ERROR);
            }
            for (final MessageType type : values()) {
                final String option = options.getOption("MSG_" + type.name());
                if (option != null) {
                    if ("note".equalsIgnoreCase(option)) {
                        type.setKind(Diagnostic.Kind.NOTE);
                    }
                    else if ("warning".equalsIgnoreCase(option)) {
                        type.setKind(Diagnostic.Kind.WARNING);
                    }
                    else if ("error".equalsIgnoreCase(option)) {
                        type.setKind(Diagnostic.Kind.ERROR);
                    }
                    else if ("disabled".equalsIgnoreCase(option)) {
                        type.setEnabled(false);
                    }
                }
            }
        }
        
        static {
            MessageType.decorate = false;
            MessageType.prefix = "";
        }
    }
}
