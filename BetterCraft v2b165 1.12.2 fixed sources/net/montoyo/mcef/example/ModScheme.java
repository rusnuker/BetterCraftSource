// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.example;

import java.io.IOException;
import net.montoyo.mcef.api.ISchemeResponseData;
import net.montoyo.mcef.api.ISchemeResponseHeaders;
import net.montoyo.mcef.MCEF;
import net.montoyo.mcef.utilities.Log;
import net.montoyo.mcef.api.SchemePreResponse;
import java.io.InputStream;
import net.montoyo.mcef.api.IScheme;

public class ModScheme implements IScheme
{
    private String contentType;
    private InputStream is;
    
    public ModScheme() {
        this.contentType = null;
        this.is = null;
    }
    
    @Override
    public SchemePreResponse processRequest(String url) {
        url = url.substring("mod://".length());
        int pos = url.indexOf(47);
        if (pos < 0) {
            return SchemePreResponse.NOT_HANDLED;
        }
        final String mod = this.removeSlashes(url.substring(0, pos));
        final String loc = this.removeSlashes(url.substring(pos + 1));
        if (mod.length() <= 0 || loc.length() <= 0 || mod.charAt(0) == '.' || loc.charAt(0) == '.') {
            Log.warning("Invalid URL " + url, new Object[0]);
            return SchemePreResponse.NOT_HANDLED;
        }
        this.is = ModScheme.class.getResourceAsStream("/assets/" + mod.toLowerCase() + "/html/" + loc.toLowerCase());
        if (this.is == null) {
            Log.warning("Resource " + url + " NOT found!", new Object[0]);
            return SchemePreResponse.NOT_HANDLED;
        }
        this.contentType = null;
        pos = loc.lastIndexOf(46);
        if (pos >= 0 && pos < loc.length() - 2) {
            this.contentType = MCEF.PROXY_CLIENT.mimeTypeFromExtension(loc.substring(pos + 1));
        }
        return SchemePreResponse.HANDLED_CONTINUE;
    }
    
    private String removeSlashes(final String loc) {
        int i;
        for (i = 0; i < loc.length() && loc.charAt(i) == '/'; ++i) {}
        return loc.substring(i);
    }
    
    @Override
    public void getResponseHeaders(final ISchemeResponseHeaders rep) {
        if (this.contentType != null) {
            rep.setMimeType(this.contentType);
        }
        rep.setStatus(200);
        rep.setStatusText("OK");
        rep.setResponseLength(-1);
    }
    
    @Override
    public boolean readResponse(final ISchemeResponseData data) {
        try {
            final int ret = this.is.read(data.getDataArray(), 0, data.getBytesToRead());
            if (ret <= 0) {
                this.is.close();
            }
            data.setAmountRead(Math.max(ret, 0));
            return ret > 0;
        }
        catch (final IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
