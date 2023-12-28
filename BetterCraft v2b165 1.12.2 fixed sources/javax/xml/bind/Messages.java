// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind;

import java.text.MessageFormat;
import java.util.ResourceBundle;

class Messages
{
    static final String PROVIDER_NOT_FOUND = "ContextFinder.ProviderNotFound";
    static final String DEFAULT_PROVIDER_NOT_FOUND = "ContextFinder.DefaultProviderNotFound";
    static final String COULD_NOT_INSTANTIATE = "ContextFinder.CouldNotInstantiate";
    static final String CANT_FIND_PROPERTIES_FILE = "ContextFinder.CantFindPropertiesFile";
    static final String CANT_MIX_PROVIDERS = "ContextFinder.CantMixProviders";
    static final String MISSING_PROPERTY = "ContextFinder.MissingProperty";
    static final String NO_PACKAGE_IN_CONTEXTPATH = "ContextFinder.NoPackageInContextPath";
    static final String NAME_VALUE = "PropertyException.NameValue";
    static final String CONVERTER_MUST_NOT_BE_NULL = "DatatypeConverter.ConverterMustNotBeNull";
    static final String ILLEGAL_CAST = "JAXBContext.IllegalCast";
    static final String ERROR_LOAD_CLASS = "ContextFinder.ErrorLoadClass";
    static final String JAXB_CLASSES_NOT_OPEN = "JAXBClasses.notOpen";
    
    static String format(final String property) {
        return format(property, null);
    }
    
    static String format(final String property, final Object arg1) {
        return format(property, new Object[] { arg1 });
    }
    
    static String format(final String property, final Object arg1, final Object arg2) {
        return format(property, new Object[] { arg1, arg2 });
    }
    
    static String format(final String property, final Object arg1, final Object arg2, final Object arg3) {
        return format(property, new Object[] { arg1, arg2, arg3 });
    }
    
    static String format(final String property, final Object[] args) {
        final String text = ResourceBundle.getBundle(Messages.class.getName()).getString(property);
        return MessageFormat.format(text, args);
    }
}
