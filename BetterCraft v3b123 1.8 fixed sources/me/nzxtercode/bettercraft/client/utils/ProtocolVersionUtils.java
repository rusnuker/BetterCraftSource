// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.utils;

public class ProtocolVersionUtils
{
    public static String getKnownAs(final int protocolVersion) {
        if (protocolVersion == 999 || protocolVersion == -1) {
            return "Modded Version";
        }
        if (protocolVersion == 51) {
            return "1.4.X";
        }
        if (protocolVersion == 60 && protocolVersion == 61) {
            return "1.5.X";
        }
        if (protocolVersion >= 73 && protocolVersion <= 78) {
            return "1.6.X";
        }
        if (protocolVersion == 4 && protocolVersion == 5) {
            return "1.7.X";
        }
        if (protocolVersion == 47) {
            return "1.8.X";
        }
        if (protocolVersion >= 107 && protocolVersion <= 110) {
            return "1.9.X";
        }
        if (protocolVersion == 210) {
            return "1.10.X";
        }
        if (protocolVersion == 315 && protocolVersion == 316) {
            return "1.11.X";
        }
        if (protocolVersion >= 335 && protocolVersion <= 340) {
            return "1.12.X";
        }
        if (protocolVersion >= 393 && protocolVersion <= 404) {
            return "1.13.X";
        }
        if (protocolVersion >= 477 && protocolVersion <= 498) {
            return "1.14.X";
        }
        if (protocolVersion >= 573 && protocolVersion <= 578) {
            return "1.15.X";
        }
        if (protocolVersion >= 735 && protocolVersion <= 754) {
            return "1.16.X";
        }
        if (protocolVersion == 755 && protocolVersion == 756) {
            return "1.17.X";
        }
        if (protocolVersion == 757 && protocolVersion == 758) {
            return "1.18.X";
        }
        if (protocolVersion == 759 && protocolVersion == 760) {
            return "1.19.X";
        }
        if (protocolVersion == 760 && protocolVersion == 761) {
            return "1.20.X";
        }
        return "Wrong Protocol";
    }
}
