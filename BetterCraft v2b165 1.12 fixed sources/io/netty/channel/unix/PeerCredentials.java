// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.unix;

public final class PeerCredentials
{
    private final int pid;
    private final int uid;
    private final int gid;
    
    PeerCredentials(final int p, final int u, final int g) {
        this.pid = p;
        this.uid = u;
        this.gid = g;
    }
    
    public int pid() {
        return this.pid;
    }
    
    public int uid() {
        return this.uid;
    }
    
    public int gid() {
        return this.gid;
    }
    
    @Override
    public String toString() {
        return "UserCredentials[pid=" + this.pid + "; uid=" + this.uid + "; gid=" + this.gid + "]";
    }
}
