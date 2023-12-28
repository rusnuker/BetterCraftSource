// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.net.mom.jms;

public class JmsTopicReceiver extends AbstractJmsReceiver
{
    private JmsTopicReceiver() {
    }
    
    public static void main(final String[] args) throws Exception {
        final JmsTopicReceiver receiver = new JmsTopicReceiver();
        receiver.doMain(args);
    }
    
    @Override
    protected void usage() {
        System.err.println("Wrong number of arguments.");
        System.err.println("Usage: java " + JmsTopicReceiver.class.getName() + " TopicConnectionFactoryBindingName TopicBindingName username password");
    }
}
