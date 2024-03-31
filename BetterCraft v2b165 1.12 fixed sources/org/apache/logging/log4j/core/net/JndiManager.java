// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.net;

import java.util.Hashtable;
import javax.naming.InitialContext;
import org.apache.logging.log4j.Logger;
import javax.naming.NamingException;
import org.apache.logging.log4j.core.util.JndiCloser;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.Properties;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.LoggerContext;
import javax.naming.Context;
import org.apache.logging.log4j.core.appender.AbstractManager;

public class JndiManager extends AbstractManager
{
    private static final JndiManagerFactory FACTORY;
    private final Context context;
    
    private JndiManager(final String name, final Context context) {
        super(null, name);
        this.context = context;
    }
    
    public static JndiManager getDefaultManager() {
        return AbstractManager.getManager(JndiManager.class.getName(), (ManagerFactory<JndiManager, Object>)JndiManager.FACTORY, null);
    }
    
    public static JndiManager getDefaultManager(final String name) {
        return AbstractManager.getManager(name, (ManagerFactory<JndiManager, Object>)JndiManager.FACTORY, null);
    }
    
    public static JndiManager getJndiManager(final String initialContextFactoryName, final String providerURL, final String urlPkgPrefixes, final String securityPrincipal, final String securityCredentials, final Properties additionalProperties) {
        final String name = JndiManager.class.getName() + '@' + JndiManager.class.hashCode();
        if (initialContextFactoryName == null) {
            return AbstractManager.getManager(name, (ManagerFactory<JndiManager, Object>)JndiManager.FACTORY, null);
        }
        final Properties properties = new Properties();
        properties.setProperty("java.naming.factory.initial", initialContextFactoryName);
        if (providerURL != null) {
            properties.setProperty("java.naming.provider.url", providerURL);
        }
        else {
            JndiManager.LOGGER.warn("The JNDI InitialContextFactory class name [{}] was provided, but there was no associated provider URL. This is likely to cause problems.", initialContextFactoryName);
        }
        if (urlPkgPrefixes != null) {
            properties.setProperty("java.naming.factory.url.pkgs", urlPkgPrefixes);
        }
        if (securityPrincipal != null) {
            properties.setProperty("java.naming.security.principal", securityPrincipal);
            if (securityCredentials != null) {
                properties.setProperty("java.naming.security.credentials", securityCredentials);
            }
            else {
                JndiManager.LOGGER.warn("A security principal [{}] was provided, but with no corresponding security credentials.", securityPrincipal);
            }
        }
        if (additionalProperties != null) {
            properties.putAll(additionalProperties);
        }
        return AbstractManager.getManager(name, (ManagerFactory<JndiManager, Properties>)JndiManager.FACTORY, properties);
    }
    
    @Override
    protected boolean releaseSub(final long timeout, final TimeUnit timeUnit) {
        return JndiCloser.closeSilently(this.context);
    }
    
    public <T> T lookup(final String name) throws NamingException {
        return (T)this.context.lookup(name);
    }
    
    static {
        FACTORY = new JndiManagerFactory();
    }
    
    private static class JndiManagerFactory implements ManagerFactory<JndiManager, Properties>
    {
        @Override
        public JndiManager createManager(final String name, final Properties data) {
            try {
                return new JndiManager(name, new InitialContext(data), null);
            }
            catch (final NamingException e) {
                JndiManager.LOGGER.error("Error creating JNDI InitialContext.", e);
                return null;
            }
        }
    }
}
