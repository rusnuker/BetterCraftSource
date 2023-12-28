// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef;

import net.montoyo.mcef.api.IScheme;
import net.montoyo.mcef.api.IJSQueryHandler;
import net.montoyo.mcef.api.IDisplayHandler;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.utilities.Log;
import net.montoyo.mcef.api.API;

public class BaseProxy implements API
{
    private static final int PUNYCODE_TMIN = 1;
    private static final int PUNYCODE_TMAX = 26;
    private static final int PUNYCODE_SKEW = 38;
    private static final int PUNYCODE_DAMP = 700;
    private static final int PUNYCODE_INITIAL_BIAS = 72;
    private static final int PUNYCODE_INITIAL_N = 128;
    
    public void onPreInit() {
    }
    
    public void onInit() {
        Log.info("MCEF is running on server. Nothing to do.", new Object[0]);
    }
    
    @Override
    public IBrowser createBrowser(final String url, final boolean transparent) {
        Log.warning("A mod called API.createBrowser() from server! Returning null...", new Object[0]);
        return null;
    }
    
    @Override
    public IBrowser createBrowser(final String url) {
        return this.createBrowser(url, false);
    }
    
    @Override
    public void registerDisplayHandler(final IDisplayHandler idh) {
        Log.warning("A mod called API.registerDisplayHandler() from server!", new Object[0]);
    }
    
    @Override
    public boolean isVirtual() {
        return true;
    }
    
    @Override
    public void openExampleBrowser(final String url) {
        Log.warning("A mod called API.openExampleBrowser() from server! URL: %s", url);
    }
    
    @Override
    public void registerJSQueryHandler(final IJSQueryHandler iqh) {
        Log.warning("A mod called API.registerJSQueryHandler() from server!", new Object[0]);
    }
    
    @Override
    public String mimeTypeFromExtension(final String ext) {
        Log.warning("A mod called API.mimeTypeFromExtension() from server!", new Object[0]);
        return null;
    }
    
    @Override
    public void registerScheme(final String name, final Class<? extends IScheme> schemeClass, final boolean std, final boolean local, final boolean displayIsolated, final boolean secure, final boolean corsEnabled, final boolean cspBypassing, final boolean fetchEnabled) {
        Log.warning("A mod called API.registerScheme() from server!", new Object[0]);
    }
    
    @Override
    public boolean isSchemeRegistered(final String name) {
        Log.warning("A mod called API.isSchemeRegistered() from server!", new Object[0]);
        return false;
    }
    
    public void onShutdown() {
    }
    
    private static int punycodeBiasAdapt(int delta, final int numPoints, final boolean firstTime) {
        if (firstTime) {
            delta /= 700;
        }
        else {
            delta /= 2;
        }
        int k;
        for (k = 0, delta += delta / numPoints; delta > 455; delta /= 35, k += 36) {}
        return k + 36 * delta / (delta + 38);
    }
    
    private static void punycodeEncodeNumber(final StringBuilder dst, int q, final int bias) {
        boolean keepGoing = true;
        int k = 36;
        while (keepGoing) {
            int t = k - bias;
            if (t < 1) {
                t = 1;
            }
            else if (t > 26) {
                t = 26;
            }
            int digit;
            if (q < t) {
                digit = q;
                keepGoing = false;
            }
            else {
                digit = t + (q - t) % (36 - t);
                q = (q - t) / (36 - t);
            }
            if (digit < 26) {
                dst.append((char)(97 + digit));
            }
            else {
                dst.append((char)(48 + digit - 26));
            }
            k += 36;
        }
    }
    
    private static String punycodeEncodeString(final int[] input) {
        final StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length; ++i) {
            if (input[i] < 128) {
                output.append((char)input[i]);
            }
        }
        int n = 128;
        int delta = 0;
        int bias = 72;
        final int b;
        int h = b = output.length();
        if (b > 0) {
            output.append('-');
        }
        while (h < input.length) {
            int m = Integer.MAX_VALUE;
            for (int j = 0; j < input.length; ++j) {
                if (input[j] >= n && input[j] < m) {
                    m = input[j];
                }
            }
            delta += (m - n) * (h + 1);
            n = m;
            for (int j = 0; j < input.length; ++j) {
                final int c = input[j];
                if (c < n) {
                    ++delta;
                }
                if (c == n) {
                    punycodeEncodeNumber(output, delta, bias);
                    bias = punycodeBiasAdapt(delta, h + 1, h == b);
                    delta = 0;
                    ++h;
                }
            }
            ++delta;
            ++n;
        }
        return "xn--" + output.toString();
    }
    
    @Override
    public String punycode(final String url) {
        int protoEnd = url.indexOf("://");
        if (protoEnd < 0) {
            protoEnd = 0;
        }
        else {
            protoEnd += 3;
        }
        int hostEnd = url.indexOf(47, protoEnd);
        if (hostEnd < 0) {
            hostEnd = url.length();
        }
        final String hostname = url.substring(protoEnd, hostEnd);
        boolean doTransform = false;
        for (int i = 0; i < hostname.length(); ++i) {
            if (hostname.charAt(i) >= '\u0080') {
                doTransform = true;
                break;
            }
        }
        if (!doTransform) {
            return url;
        }
        final String[] parts = hostname.split("\\.");
        final StringBuilder sb = new StringBuilder();
        boolean first = true;
        sb.append(url, 0, protoEnd);
        String[] array;
        for (int length = (array = parts).length, k = 0; k < length; ++k) {
            final String p = array[k];
            doTransform = false;
            for (int j = 0; j < p.length(); ++j) {
                if (p.charAt(j) >= '\u0080') {
                    doTransform = true;
                    break;
                }
            }
            if (first) {
                first = false;
            }
            else {
                sb.append('.');
            }
            if (doTransform) {
                sb.append(punycodeEncodeString(p.codePoints().toArray()));
            }
            else {
                sb.append(p);
            }
        }
        sb.append(url, hostEnd, url.length());
        return sb.toString();
    }
}
