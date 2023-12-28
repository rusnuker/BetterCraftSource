// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.net.mom.jms;

public class JmsQueueReceiver extends AbstractJmsReceiver
{
    private JmsQueueReceiver() {
    }
    
    public static void main(final String[] args) throws Exception {
        final JmsQueueReceiver receiver = new JmsQueueReceiver();
        receiver.doMain(args);
    }
    
    @Override
    protected void usage() {
        System.err.println("Wrong number of arguments.");
        System.err.println("Usage: java " + JmsQueueReceiver.class.getName() + " QueueConnectionFactoryBindingName QueueBindingName username password");
    }
}
