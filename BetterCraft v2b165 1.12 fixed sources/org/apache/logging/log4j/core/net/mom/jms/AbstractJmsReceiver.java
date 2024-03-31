// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.net.mom.jms;

import org.apache.logging.log4j.core.net.server.JmsServer;

public abstract class AbstractJmsReceiver
{
    protected abstract void usage();
    
    protected void doMain(final String... args) throws Exception {
        if (args.length != 4) {
            this.usage();
            System.exit(1);
        }
        final JmsServer server = new JmsServer(args[0], args[1], args[2], args[3]);
        server.run();
    }
}
