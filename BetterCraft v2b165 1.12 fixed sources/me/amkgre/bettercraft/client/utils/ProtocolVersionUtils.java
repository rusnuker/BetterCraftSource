// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

public class ProtocolVersionUtils
{
    public static ProtocolVersionUtils getInstance() {
        return new ProtocolVersionUtils();
    }
    
    public static String getKnownAs(final int protocolVersion) {
        if (protocolVersion == 0 || protocolVersion < 0) {
            return "Old Protocol";
        }
        if (protocolVersion < 6 && protocolVersion != 0) {
            return "1.7.X";
        }
        if (protocolVersion > 6 && protocolVersion < 48) {
            return "1.8.X";
        }
        if (protocolVersion == 48 || (protocolVersion > 48 && protocolVersion < 201)) {
            return "1.9.X";
        }
        if (protocolVersion == 201 || (protocolVersion > 201 && protocolVersion < 301)) {
            return "1.10.X";
        }
        if (protocolVersion == 301 || (protocolVersion > 301 && protocolVersion < 317)) {
            return "1.11.X";
        }
        if (protocolVersion == 317 || (protocolVersion > 317 && protocolVersion < 341)) {
            return "1.12.X";
        }
        if (protocolVersion == 341 || (protocolVersion > 341 && protocolVersion < 441)) {
            return "1.13.X";
        }
        if (protocolVersion == 441 || (protocolVersion > 441 && protocolVersion < 550)) {
            return "1.14.X";
        }
        if (protocolVersion == 550 || (protocolVersion > 550 && protocolVersion < 754)) {
            return "1.15.X";
        }
        if (protocolVersion == 754 || protocolVersion > 754) {
            return "1.16.X";
        }
        return "Wrong Protocol";
    }
}
