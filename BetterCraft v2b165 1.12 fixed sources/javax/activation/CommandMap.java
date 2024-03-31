// 
// Decompiled by Procyon v0.6.0
// 

package javax.activation;

public abstract class CommandMap
{
    private static CommandMap defaultCommandMap;
    
    static {
        CommandMap.defaultCommandMap = new MailcapCommandMap();
    }
    
    public static CommandMap getDefaultCommandMap() {
        return CommandMap.defaultCommandMap;
    }
    
    public static void setDefaultCommandMap(final CommandMap commandMap) {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkSetFactory();
        }
        CommandMap.defaultCommandMap = ((commandMap == null) ? new MailcapCommandMap() : commandMap);
    }
    
    public CommandInfo[] getPreferredCommands(final String mimeType, final DataSource ds) {
        return this.getPreferredCommands(mimeType);
    }
    
    public abstract CommandInfo[] getPreferredCommands(final String p0);
    
    public CommandInfo[] getAllCommands(final String mimeType, final DataSource ds) {
        return this.getAllCommands(mimeType);
    }
    
    public abstract CommandInfo[] getAllCommands(final String p0);
    
    public CommandInfo getCommand(final String mimeType, final String cmdName, final DataSource ds) {
        return this.getCommand(mimeType, cmdName);
    }
    
    public abstract CommandInfo getCommand(final String p0, final String p1);
    
    public DataContentHandler createDataContentHandler(final String mimeType, final DataSource ds) {
        return this.createDataContentHandler(mimeType);
    }
    
    public abstract DataContentHandler createDataContentHandler(final String p0);
    
    public String[] getMimeTypes() {
        return null;
    }
}
