// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.ingamechat.namehistory;

import org.apache.commons.lang3.ArrayUtils;
import net.labymod.utils.UUIDFetcher;
import java.util.UUID;

public class NameHistory
{
    private UUID uuid;
    private UUIDFetcher[] changes;
    
    public NameHistory(final UUID uuid, final UUIDFetcher[] changes) {
        this.uuid = uuid;
        ArrayUtils.reverse(this.changes = changes);
    }
    
    public UUIDFetcher[] getChanges() {
        return this.changes;
    }
    
    public UUID getUUID() {
        return this.uuid;
    }
}
