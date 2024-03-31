// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.net.server;

import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.AbstractLifeCycle;
import org.apache.logging.log4j.LoggingException;
import javax.jms.JMSException;
import org.apache.logging.log4j.core.LogEvent;
import javax.jms.ObjectMessage;
import javax.jms.Message;
import org.apache.logging.log4j.core.net.JndiManager;
import javax.jms.MessageConsumer;
import org.apache.logging.log4j.core.appender.mom.JmsManager;
import org.apache.logging.log4j.core.LifeCycle;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.core.LifeCycle2;
import javax.jms.MessageListener;
import org.apache.logging.log4j.core.LogEventListener;

public class JmsServer extends LogEventListener implements MessageListener, LifeCycle2
{
    private final AtomicReference<LifeCycle.State> state;
    private final JmsManager jmsManager;
    private MessageConsumer messageConsumer;
    
    public JmsServer(final String connectionFactoryBindingName, final String destinationBindingName, final String username, final String password) {
        this.state = new AtomicReference<LifeCycle.State>(LifeCycle.State.INITIALIZED);
        final String managerName = JmsServer.class.getName() + '@' + JmsServer.class.hashCode();
        final JndiManager jndiManager = JndiManager.getDefaultManager(managerName);
        this.jmsManager = JmsManager.getJmsManager(managerName, jndiManager, connectionFactoryBindingName, destinationBindingName, username, password);
    }
    
    public LifeCycle.State getState() {
        return this.state.get();
    }
    
    public void onMessage(final Message message) {
        try {
            if (message instanceof ObjectMessage) {
                final Object body = ((ObjectMessage)message).getObject();
                if (body instanceof LogEvent) {
                    this.log((LogEvent)body);
                }
                else {
                    JmsServer.LOGGER.warn("Expected ObjectMessage to contain LogEvent. Got type {} instead.", body.getClass());
                }
            }
            else {
                JmsServer.LOGGER.warn("Received message of type {} and JMSType {} which cannot be handled.", message.getClass(), message.getJMSType());
            }
        }
        catch (final JMSException e) {
            JmsServer.LOGGER.catching((Throwable)e);
        }
    }
    
    public void initialize() {
    }
    
    public void start() {
        if (this.state.compareAndSet(LifeCycle.State.INITIALIZED, LifeCycle.State.STARTING)) {
            try {
                (this.messageConsumer = this.jmsManager.createMessageConsumer()).setMessageListener((MessageListener)this);
            }
            catch (final JMSException e) {
                throw new LoggingException((Throwable)e);
            }
        }
    }
    
    public void stop() {
        this.stop(0L, AbstractLifeCycle.DEFAULT_STOP_TIMEUNIT);
    }
    
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        boolean stopped = true;
        try {
            this.messageConsumer.close();
        }
        catch (final JMSException e) {
            JmsServer.LOGGER.debug("Exception closing {}", this.messageConsumer, e);
            stopped = false;
        }
        return stopped && this.jmsManager.stop(timeout, timeUnit);
    }
    
    public boolean isStarted() {
        return this.state.get() == LifeCycle.State.STARTED;
    }
    
    public boolean isStopped() {
        return this.state.get() == LifeCycle.State.STOPPED;
    }
    
    public void run() throws IOException {
        this.start();
        System.out.println("Type \"exit\" to quit.");
        final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, Charset.defaultCharset()));
        String line;
        do {
            line = stdin.readLine();
        } while (line != null && !line.equalsIgnoreCase("exit"));
        System.out.println("Exiting. Kill the application if it does not exit due to daemon threads.");
        this.stop();
    }
}
