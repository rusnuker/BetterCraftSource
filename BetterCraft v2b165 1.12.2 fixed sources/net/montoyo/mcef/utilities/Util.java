// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.utilities;

import java.nio.file.Files;
import java.nio.file.Path;
import java.lang.reflect.Method;
import net.montoyo.mcef.remote.Mirror;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import net.montoyo.mcef.MCEF;
import javax.net.ssl.HttpsURLConnection;
import net.montoyo.mcef.remote.MirrorManager;
import java.util.zip.GZIPInputStream;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.util.zip.ZipEntry;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;
import java.io.FileInputStream;
import java.io.File;

public class Util
{
    private static final DummyProgressListener DPH;
    private static final String HEX = "0123456789abcdef";
    
    static {
        DPH = new DummyProgressListener();
    }
    
    public static double clamp(final double d, final double min, final double max) {
        if (d < min) {
            return min;
        }
        if (d > max) {
            return max;
        }
        return d;
    }
    
    public static boolean extract(final File zip, final File out) {
        ZipInputStream zis;
        try {
            zis = new ZipInputStream(new FileInputStream(zip));
        }
        catch (final FileNotFoundException ex) {
            Log.error("Couldn't extract %s: File not found.", zip.getName());
            ex.printStackTrace();
            return false;
        }
        try {
            ZipEntry ze = null;
            do {
                if (ze.isDirectory()) {
                    continue;
                }
                final File dst = new File(out, ze.getName());
                delete(dst);
                mkdirs(dst);
                final FileOutputStream fos = new FileOutputStream(dst);
                final byte[] data = new byte[65536];
                int read;
                while ((read = zis.read(data)) > 0) {
                    fos.write(data, 0, read);
                }
                close(fos);
            } while ((ze = zis.getNextEntry()) != null);
            return true;
        }
        catch (final FileNotFoundException e) {
            Log.error("Couldn't extract a file from %s. Maybe you're missing some permissions?", zip.getName());
            e.printStackTrace();
            return false;
        }
        catch (final IOException e2) {
            Log.error("IOException while extracting %s.", zip.getName());
            e2.printStackTrace();
            return false;
        }
        finally {
            close(zis);
        }
    }
    
    public static String hash(final File fle) {
        FileInputStream fis;
        try {
            fis = new FileInputStream(fle);
        }
        catch (final FileNotFoundException e) {
            Log.error("Couldn't hash %s: File not found.", fle.getName());
            e.printStackTrace();
            return null;
        }
        try {
            final MessageDigest sha = MessageDigest.getInstance("SHA-1");
            sha.reset();
            int read = 0;
            final byte[] buffer = new byte[65536];
            while ((read = fis.read(buffer)) > 0) {
                sha.update(buffer, 0, read);
            }
            final byte[] digest = sha.digest();
            String hash = "";
            for (int i = 0; i < digest.length; ++i) {
                final int b = digest[i] & 0xFF;
                final int left = b >>> 4;
                final int right = b & 0xF;
                hash = String.valueOf(hash) + "0123456789abcdef".charAt(left);
                hash = String.valueOf(hash) + "0123456789abcdef".charAt(right);
            }
            return hash;
        }
        catch (final IOException e2) {
            Log.error("IOException while hashing file %s", fle.getName());
            e2.printStackTrace();
            return null;
        }
        catch (final NoSuchAlgorithmException e3) {
            Log.error("Holy crap this shouldn't happen. SHA-1 not found!!!!", new Object[0]);
            e3.printStackTrace();
            return null;
        }
        finally {
            close(fis);
        }
    }
    
    public static boolean download(final String res, final File dst, final boolean gzip, IProgressListener ph) {
        final String err = "Couldn't download " + dst.getName() + "!";
        ph = secure(ph);
        ph.onTaskChanged("Downloading " + dst.getName());
        final SizedInputStream sis = openStream(res, err);
        if (sis == null) {
            return false;
        }
        InputStream is = null;
        Label_0116: {
            if (gzip) {
                try {
                    is = new GZIPInputStream(sis);
                    break Label_0116;
                }
                catch (final IOException e) {
                    Log.error("Couldn't create GZIPInputStream: IOException.", new Object[0]);
                    e.printStackTrace();
                    close(sis);
                    return false;
                }
            }
            is = sis;
        }
        delete(dst);
        mkdirs(dst);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(dst);
        }
        catch (final FileNotFoundException e2) {
            Log.error("%s Couldn't open the destination file. Maybe you're missing rights.", err);
            e2.printStackTrace();
            close(is);
            return false;
        }
        final byte[] data = new byte[65536];
        final double total = (double)sis.getContentLength();
        double cur = 0.0;
        try {
            int read;
            while ((read = is.read(data)) > 0) {
                fos.write(data, 0, read);
                cur += sis.resetLengthCounter();
                ph.onProgressed(cur / total * 100.0);
            }
            return true;
        }
        catch (final IOException e3) {
            Log.error("%s IOException while downloading.", err);
            e3.printStackTrace();
            return false;
        }
        finally {
            close(is);
            close(fos);
        }
    }
    
    public static boolean download(final String res, final File dst, final IProgressListener ph) {
        return download(res, dst, false, ph);
    }
    
    public static IProgressListener secure(final IProgressListener pl) {
        return (pl == null) ? Util.DPH : pl;
    }
    
    public static File rename(final File src, final String name) {
        final File ret = new File(src.getParentFile(), name);
        if (src.renameTo(ret)) {
            return ret;
        }
        return null;
    }
    
    public static void mkdirs(final File f) {
        final File p = f.getParentFile();
        if (!p.exists()) {
            p.mkdirs();
        }
    }
    
    public static void delete(final String f) {
        delete(new File(f));
    }
    
    public static void delete(final File f) {
        if (!f.exists() || f.delete()) {
            return;
        }
        final File mv = new File(f.getParentFile(), "deleteme" + (int)(Math.random() * 100000.0));
        if (f.renameTo(mv)) {
            if (!mv.delete()) {
                mv.deleteOnExit();
            }
            return;
        }
        Log.warning("Couldn't delete file! If there's any problems, please try to remove it yourself. Path: %s", f.getAbsolutePath());
    }
    
    public static SizedInputStream openStream(final String res, final String err) {
        while (true) {
            HttpURLConnection conn;
            try {
                final Mirror m = MirrorManager.INSTANCE.getCurrent();
                conn = m.getResource(res);
                if (conn instanceof HttpsURLConnection && m.usesLetsEncryptCertificate() && MCEF.SSL_SOCKET_FACTORY != null) {
                    ((HttpsURLConnection)conn).setSSLSocketFactory(MCEF.SSL_SOCKET_FACTORY);
                }
            }
            catch (final MalformedURLException e) {
                Log.error("%s Is the mirror list broken?", err);
                e.printStackTrace();
                return null;
            }
            catch (final IOException e2) {
                Log.error("%s Is your antivirus or firewall blocking the connection?", err);
                e2.printStackTrace();
                return null;
            }
            try {
                long len = -1L;
                boolean failed = true;
                try {
                    final Method i = HttpURLConnection.class.getMethod("getContentLengthLong", (Class<?>[])new Class[0]);
                    len = (long)i.invoke(conn, new Object[0]);
                    failed = false;
                }
                catch (final NoSuchMethodException ex) {}
                catch (final IllegalAccessException ex2) {}
                catch (final InvocationTargetException te) {
                    if (te.getTargetException() instanceof IOException) {
                        throw (IOException)te.getTargetException();
                    }
                }
                if (failed) {
                    len = conn.getContentLength();
                }
                return new SizedInputStream(conn.getInputStream(), len);
            }
            catch (final IOException e2) {
                int rc;
                try {
                    rc = conn.getResponseCode();
                }
                catch (final IOException ie) {
                    Log.error("%s Couldn't even get the HTTP response code!", err);
                    ie.printStackTrace();
                    return null;
                }
                Log.error("%s HTTP response is %d; trying with another mirror.", err, rc);
                if (!MirrorManager.INSTANCE.markCurrentMirrorAsBroken()) {
                    Log.error("%s All mirrors seems broken.", err);
                    return null;
                }
                continue;
            }
        }
    }
    
    public static void close(final Object o) {
        try {
            o.getClass().getMethod("close", (Class<?>[])new Class[0]).invoke(o, new Object[0]);
        }
        catch (final Throwable t) {}
    }
    
    public static boolean isSameFile(final Path p1, final Path p2) {
        try {
            return Files.isSameFile(p1, p2);
        }
        catch (final IOException e) {
            return false;
        }
    }
    
    public static String getenv(final String name) {
        final String ret = System.getenv(name);
        return (ret == null) ? "" : ret;
    }
}
