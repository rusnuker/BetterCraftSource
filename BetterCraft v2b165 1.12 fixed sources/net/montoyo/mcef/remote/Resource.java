// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.remote;

import net.montoyo.mcef.utilities.IProgressListener;
import net.montoyo.mcef.utilities.Log;
import net.montoyo.mcef.utilities.Util;
import java.io.File;
import net.montoyo.mcef.client.ClientProxy;

public class Resource
{
    private String platform;
    private String name;
    private String sum;
    private boolean shouldExtract;
    
    public Resource(final String name, final String sum, final String platform) {
        this.shouldExtract = false;
        this.name = name;
        this.sum = sum.trim();
        this.platform = platform;
    }
    
    public boolean exists() {
        final File f = new File(ClientProxy.ROOT, this.name);
        if (!f.exists()) {
            return false;
        }
        final String hash = Util.hash(f);
        if (hash == null) {
            Log.warning("Couldn't hash file %s; assuming it doesn't exist.", f.getAbsolutePath());
            return false;
        }
        return hash.equalsIgnoreCase(this.sum);
    }
    
    public boolean download(final IProgressListener ipl) {
        String end = "";
        if (this.shouldExtract) {
            end = String.valueOf(end) + ".gz";
        }
        final File dst = new File(ClientProxy.ROOT, this.name);
        final File parent = dst.getParentFile();
        if (!parent.exists() && !parent.mkdirs()) {
            Log.warning("Couldn't create directory %s... ignoring this error, but this might cause some issues later...", parent.getAbsolutePath());
        }
        return Util.download("1.11/" + this.platform + '/' + this.name + end, dst, this.shouldExtract, ipl);
    }
    
    public boolean extract(final IProgressListener ipl) {
        Util.secure(ipl).onTaskChanged("Extracting " + this.name);
        return Util.extract(new File(ClientProxy.ROOT, this.name), new File(ClientProxy.ROOT));
    }
    
    public void setShouldExtract() {
        this.shouldExtract = true;
        this.name = this.name.substring(0, this.name.length() - 3);
    }
    
    public String getFileName() {
        return this.name;
    }
    
    public static File getLocationOf(final String resName) {
        return new File(ClientProxy.ROOT, resName);
    }
}
