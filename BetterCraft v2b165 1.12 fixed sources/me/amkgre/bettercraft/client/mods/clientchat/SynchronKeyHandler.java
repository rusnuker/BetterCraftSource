// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.clientchat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;

public class SynchronKeyHandler
{
    DataInputStream in;
    DataOutputStream out;
    byte[] key;
    SimpleRandom random;
    
    public SynchronKeyHandler(final InputStream in, final OutputStream out) throws IOException {
        this.in = new DataInputStream(in);
        this.out = new DataOutputStream(out);
        (this.random = new SimpleRandom()).setSeed(System.currentTimeMillis());
        this.swapKeys();
    }
    
    private void swapKeys() throws IOException {
        final byte[] local_key = longToByteArr(this.random.nextLong());
        this.out.write(local_key);
        final byte[] remote_key = new byte[8];
        new DataInputStream(this.in).readFully(remote_key);
        XORbyteArr(local_key, remote_key);
        this.key = local_key;
    }
    
    private static void XORbyteArr(final byte[] arr, final byte[] xor) {
        if (arr == null || xor == null) {
            return;
        }
        if (arr.length != xor.length) {
            return;
        }
        for (int i = 0; i < arr.length; ++i) {
            final int n = i;
            arr[n] ^= xor[i];
        }
    }
    
    private static void applyKey(final byte[] arr, final byte[] xor) {
        if (arr == null || xor == null) {
            return;
        }
        if (xor.length == 0) {
            return;
        }
        for (int i = 0; i < arr.length; ++i) {
            final int n = i;
            arr[n] ^= xor[i % xor.length];
        }
    }
    
    private static byte[] longToByteArr(final long a) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(8);
        new DataOutputStream(baos).writeLong(a);
        return baos.toByteArray();
    }
    
    public byte[] readPacket() throws IOException {
        final byte[] b = new byte[4];
        this.in.readFully(b);
        applyKey(b, this.key);
        final byte[] a = new byte[new DataInputStream(new ByteArrayInputStream(b)).readInt()];
        this.in.readFully(a);
        applyKey(a, this.key);
        return a;
    }
    
    public void writePacket(final byte[] a) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new DataOutputStream(baos).writeInt(a.length);
        final byte[] b = baos.toByteArray();
        applyKey(b, this.key);
        this.out.write(b);
        applyKey(a, this.key);
        this.out.write(a);
    }
}
