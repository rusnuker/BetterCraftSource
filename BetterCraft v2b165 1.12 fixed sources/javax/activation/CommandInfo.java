// 
// Decompiled by Procyon v0.6.0
// 

package javax.activation;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Externalizable;
import java.beans.Beans;

public class CommandInfo
{
    private final String commandName;
    private final String commandClass;
    
    public CommandInfo(final String commandName, final String commandClass) {
        this.commandName = commandName;
        this.commandClass = commandClass;
    }
    
    public String getCommandName() {
        return this.commandName;
    }
    
    public String getCommandClass() {
        return this.commandClass;
    }
    
    public Object getCommandObject(final DataHandler dh, final ClassLoader loader) throws IOException, ClassNotFoundException {
        final Object bean = Beans.instantiate(loader, this.commandClass);
        if (bean instanceof CommandObject) {
            ((CommandObject)bean).setCommandContext(this.commandName, dh);
        }
        else if (bean instanceof Externalizable && dh != null) {
            ((Externalizable)bean).readExternal(new ObjectInputStream(dh.getInputStream()));
        }
        return bean;
    }
}
