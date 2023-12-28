// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.db.jpa;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.lang.reflect.Constructor;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseAppender;

@Plugin(name = "JPA", category = "Core", elementType = "appender", printObject = true)
public final class JpaAppender extends AbstractDatabaseAppender<JpaDatabaseManager>
{
    private final String description;
    
    private JpaAppender(final String name, final Filter filter, final boolean ignoreExceptions, final JpaDatabaseManager manager) {
        super(name, filter, ignoreExceptions, manager);
        this.description = this.getName() + "{ manager=" + ((AbstractDatabaseAppender<Object>)this).getManager() + " }";
    }
    
    @Override
    public String toString() {
        return this.description;
    }
    
    @PluginFactory
    public static JpaAppender createAppender(@PluginAttribute("name") final String name, @PluginAttribute("ignoreExceptions") final String ignore, @PluginElement("Filter") final Filter filter, @PluginAttribute("bufferSize") final String bufferSize, @PluginAttribute("entityClassName") final String entityClassName, @PluginAttribute("persistenceUnitName") final String persistenceUnitName) {
        if (Strings.isEmpty(entityClassName) || Strings.isEmpty(persistenceUnitName)) {
            JpaAppender.LOGGER.error("Attributes entityClassName and persistenceUnitName are required for JPA Appender.");
            return null;
        }
        final int bufferSizeInt = AbstractAppender.parseInt(bufferSize, 0);
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        try {
            final Class<? extends AbstractLogEventWrapperEntity> entityClass = LoaderUtil.loadClass(entityClassName).asSubclass(AbstractLogEventWrapperEntity.class);
            try {
                entityClass.getConstructor((Class<?>[])new Class[0]);
            }
            catch (final NoSuchMethodException e) {
                JpaAppender.LOGGER.error("Entity class [{}] does not have a no-arg constructor. The JPA provider will reject it.", entityClassName);
                return null;
            }
            final Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor = entityClass.getConstructor(LogEvent.class);
            final String managerName = "jpaManager{ description=" + name + ", bufferSize=" + bufferSizeInt + ", persistenceUnitName=" + persistenceUnitName + ", entityClass=" + entityClass.getName() + '}';
            final JpaDatabaseManager manager = JpaDatabaseManager.getJPADatabaseManager(managerName, bufferSizeInt, entityClass, entityConstructor, persistenceUnitName);
            if (manager == null) {
                return null;
            }
            return new JpaAppender(name, filter, ignoreExceptions, manager);
        }
        catch (final ClassNotFoundException e2) {
            JpaAppender.LOGGER.error("Could not load entity class [{}].", entityClassName, e2);
            return null;
        }
        catch (final NoSuchMethodException e3) {
            JpaAppender.LOGGER.error("Entity class [{}] does not have a constructor with a single argument of type LogEvent.", entityClassName);
            return null;
        }
        catch (final ClassCastException e4) {
            JpaAppender.LOGGER.error("Entity class [{}] does not extend AbstractLogEventWrapperEntity.", entityClassName);
            return null;
        }
    }
}
