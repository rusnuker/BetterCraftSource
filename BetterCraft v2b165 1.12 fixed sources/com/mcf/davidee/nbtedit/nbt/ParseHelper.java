// 
// Decompiled by Procyon v0.6.0
// 

package com.mcf.davidee.nbtedit.nbt;

public class ParseHelper
{
    public static byte parseByte(final String s) throws NumberFormatException {
        try {
            return Byte.parseByte(s);
        }
        catch (final NumberFormatException e) {
            throw new NumberFormatException("Not a valid byte");
        }
    }
    
    public static short parseShort(final String s) throws NumberFormatException {
        try {
            return Short.parseShort(s);
        }
        catch (final NumberFormatException e) {
            throw new NumberFormatException("Not a valid short");
        }
    }
    
    public static int parseInt(final String s) throws NumberFormatException {
        try {
            return Integer.parseInt(s);
        }
        catch (final NumberFormatException e) {
            throw new NumberFormatException("Not a valid int");
        }
    }
    
    public static long parseLong(final String s) throws NumberFormatException {
        try {
            return Long.parseLong(s);
        }
        catch (final NumberFormatException e) {
            throw new NumberFormatException("Not a valid long");
        }
    }
    
    public static float parseFloat(final String s) throws NumberFormatException {
        try {
            return Float.parseFloat(s);
        }
        catch (final NumberFormatException e) {
            throw new NumberFormatException("Not a valid float");
        }
    }
    
    public static double parseDouble(final String s) throws NumberFormatException {
        try {
            return Double.parseDouble(s);
        }
        catch (final NumberFormatException e) {
            throw new NumberFormatException("Not a valid double");
        }
    }
    
    public static byte[] parseByteArray(final String s) throws NumberFormatException {
        try {
            final String[] input = s.split(" ");
            final byte[] arr = new byte[input.length];
            for (int i = 0; i < input.length; ++i) {
                arr[i] = parseByte(input[i]);
            }
            return arr;
        }
        catch (final NumberFormatException e) {
            throw new NumberFormatException("Not a valid byte array");
        }
    }
    
    public static int[] parseIntArray(final String s) throws NumberFormatException {
        try {
            final String[] input = s.split(" ");
            final int[] arr = new int[input.length];
            for (int i = 0; i < input.length; ++i) {
                arr[i] = parseInt(input[i]);
            }
            return arr;
        }
        catch (final NumberFormatException e) {
            throw new NumberFormatException("Not a valid int array");
        }
    }
}
