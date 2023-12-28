// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.remote;

import net.montoyo.mcef.utilities.Log;
import java.util.Collection;
import java.util.Arrays;
import net.montoyo.mcef.MCEF;
import java.util.Random;
import java.util.ArrayList;

public class MirrorManager
{
    private static final Mirror[] defaultMirrors;
    public static final MirrorManager INSTANCE;
    private final ArrayList<Mirror> mirrors;
    private final Random r;
    private Mirror current;
    
    static {
        defaultMirrors = new Mirror[] { new Mirror("montoyo.net (over HTTPS)", "https://montoyo.net/jcef", 3), new Mirror("montoyo.net (non-secure)", "http://montoyo.net/jcef", 0) };
        INSTANCE = new MirrorManager();
    }
    
    private MirrorManager() {
        this.mirrors = new ArrayList<Mirror>();
        this.r = new Random();
        this.markCurrentMirrorAsBroken();
    }
    
    private void reset() {
        this.mirrors.clear();
        if (MCEF.FORCE_MIRROR != null) {
            this.mirrors.add(new Mirror("user-forced", MCEF.FORCE_MIRROR, 4));
        }
        else {
            final ArrayList<Mirror> lst = new ArrayList<Mirror>(Arrays.asList(MirrorManager.defaultMirrors));
            while (!lst.isEmpty()) {
                final Mirror m = lst.remove(this.r.nextInt(lst.size()));
                if (m.isSecure()) {
                    this.mirrors.add(m);
                }
            }
            if (!MCEF.SECURE_MIRRORS_ONLY) {
                lst.addAll(Arrays.asList(MirrorManager.defaultMirrors));
                while (!lst.isEmpty()) {
                    final Mirror m = lst.remove(this.r.nextInt(lst.size()));
                    if (!m.isSecure()) {
                        this.mirrors.add(m);
                    }
                }
            }
        }
    }
    
    public Mirror getCurrent() {
        return this.current;
    }
    
    public boolean markCurrentMirrorAsBroken() {
        boolean ret = true;
        if (this.mirrors.isEmpty()) {
            this.reset();
            ret = false;
        }
        this.current = this.mirrors.remove(0);
        Log.info(this.current.getInformationString(), new Object[0]);
        return ret;
    }
}
