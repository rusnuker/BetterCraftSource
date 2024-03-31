// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import java.security.GeneralSecurityException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import javax.crypto.Cipher;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.security.MessageDigest;
import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import net.labymod.support.util.Debug;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class CryptManager
{
    public static SecretKey createNewSharedKey() {
        try {
            final KeyGenerator key = KeyGenerator.getInstance("AES");
            key.init(128);
            return key.generateKey();
        }
        catch (final NoSuchAlgorithmException var1) {
            throw new Error(var1);
        }
    }
    
    public static KeyPair createNewKeyPair() {
        try {
            final KeyPairGenerator keyPair = KeyPairGenerator.getInstance("RSA");
            keyPair.initialize(1024);
            return keyPair.generateKeyPair();
        }
        catch (final NoSuchAlgorithmException var1) {
            var1.printStackTrace();
            Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, "Key pair generation failed!");
            return null;
        }
    }
    
    public static byte[] getServerIdHash(final String input, final PublicKey publicKey, final SecretKey secretKey) {
        try {
            return digestOperation("SHA-1", new byte[][] { input.getBytes("ISO_8859_1"), secretKey.getEncoded(), publicKey.getEncoded() });
        }
        catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static byte[] digestOperation(final String type, final byte[]... bytes) {
        try {
            final MessageDigest disgest = MessageDigest.getInstance(type);
            final byte[][] byts = bytes;
            for (int length = bytes.length, i = 0; i < length; ++i) {
                final byte[] b = byts[i];
                disgest.update(b);
            }
            return disgest.digest();
        }
        catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static PublicKey decodePublicKey(final byte[] p_75896_0_) {
        try {
            final X509EncodedKeySpec var1 = new X509EncodedKeySpec(p_75896_0_);
            final KeyFactory var2 = KeyFactory.getInstance("RSA");
            return var2.generatePublic(var1);
        }
        catch (final NoSuchAlgorithmException ex) {}
        catch (final InvalidKeySpecException ex2) {}
        Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, "Public key reconstitute failed!");
        return null;
    }
    
    public static SecretKey decryptSharedKey(final PrivateKey p_75887_0_, final byte[] p_75887_1_) {
        return new SecretKeySpec(decryptData(p_75887_0_, p_75887_1_), "AES");
    }
    
    public static byte[] encryptData(final Key p_75894_0_, final byte[] p_75894_1_) {
        return cipherOperation(1, p_75894_0_, p_75894_1_);
    }
    
    public static byte[] decryptData(final Key p_75889_0_, final byte[] p_75889_1_) {
        return cipherOperation(2, p_75889_0_, p_75889_1_);
    }
    
    private static byte[] cipherOperation(final int p_75885_0_, final Key p_75885_1_, final byte[] p_75885_2_) {
        try {
            return createTheCipherInstance(p_75885_0_, p_75885_1_.getAlgorithm(), p_75885_1_).doFinal(p_75885_2_);
        }
        catch (final IllegalBlockSizeException var4) {
            var4.printStackTrace();
        }
        catch (final BadPaddingException var5) {
            var5.printStackTrace();
        }
        Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, "Cipher data failed!");
        return null;
    }
    
    private static Cipher createTheCipherInstance(final int p_75886_0_, final String p_75886_1_, final Key p_75886_2_) {
        try {
            final Cipher var3 = Cipher.getInstance(p_75886_1_);
            var3.init(p_75886_0_, p_75886_2_);
            return var3;
        }
        catch (final InvalidKeyException var4) {
            var4.printStackTrace();
        }
        catch (final NoSuchAlgorithmException var5) {
            var5.printStackTrace();
        }
        catch (final NoSuchPaddingException var6) {
            var6.printStackTrace();
        }
        Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, "Cipher creation failed!");
        return null;
    }
    
    public static Cipher func_151229_a(final int p_151229_0_, final Key p_151229_1_) {
        try {
            final Cipher var2 = Cipher.getInstance("AES/CFB8/NoPadding");
            var2.init(p_151229_0_, p_151229_1_, new IvParameterSpec(p_151229_1_.getEncoded()));
            return var2;
        }
        catch (final GeneralSecurityException var3) {
            throw new RuntimeException(var3);
        }
    }
}
