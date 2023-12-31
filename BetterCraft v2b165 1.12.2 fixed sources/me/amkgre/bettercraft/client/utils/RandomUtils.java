// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import java.util.Iterator;
import java.util.ArrayList;

public class RandomUtils
{
    private static RandomUtils instance;
    
    static {
        RandomUtils.instance = new RandomUtils();
    }
    
    public static RandomUtils getRandomUtils() {
        return RandomUtils.instance;
    }
    
    public String getRandomChar(final boolean UpperCase) {
        int randomInt = 0;
        String randomChar = "null";
        randomInt = this.getRandomInt(0, 25);
        if (randomInt == 0) {
            randomChar = "a";
        }
        if (randomInt == 1) {
            randomChar = "b";
        }
        if (randomInt == 2) {
            randomChar = "c";
        }
        if (randomInt == 3) {
            randomChar = "d";
        }
        if (randomInt == 4) {
            randomChar = "e";
        }
        if (randomInt == 5) {
            randomChar = "f";
        }
        if (randomInt == 6) {
            randomChar = "g";
        }
        if (randomInt == 7) {
            randomChar = "h";
        }
        if (randomInt == 8) {
            randomChar = "i";
        }
        if (randomInt == 9) {
            randomChar = "j";
        }
        if (randomInt == 10) {
            randomChar = "k";
        }
        if (randomInt == 11) {
            randomChar = "l";
        }
        if (randomInt == 12) {
            randomChar = "m";
        }
        if (randomInt == 13) {
            randomChar = "n";
        }
        if (randomInt == 14) {
            randomChar = "o";
        }
        if (randomInt == 15) {
            randomChar = "p";
        }
        if (randomInt == 16) {
            randomChar = "q";
        }
        if (randomInt == 17) {
            randomChar = "r";
        }
        if (randomInt == 18) {
            randomChar = "s";
        }
        if (randomInt == 19) {
            randomChar = "t";
        }
        if (randomInt == 20) {
            randomChar = "u";
        }
        if (randomInt == 21) {
            randomChar = "v";
        }
        if (randomInt == 22) {
            randomChar = "w";
        }
        if (randomInt == 23) {
            randomChar = "x";
        }
        if (randomInt == 24) {
            randomChar = "y";
        }
        if (randomInt == 25) {
            randomChar = "z";
        }
        if (UpperCase) {
            return randomChar.toUpperCase();
        }
        return randomChar;
    }
    
    public boolean getRandomBoolean() {
        int randomInt = 0;
        boolean randomBoolean = false;
        randomInt = this.getRandomInt(0, 1);
        if (randomInt == 0) {
            randomBoolean = true;
        }
        if (randomInt == 1) {
            randomBoolean = false;
        }
        return randomBoolean;
    }
    
    public String getRandomStringWithRandomUpperCase(final int length) {
        int randomInt = 0;
        String randomString = "";
        randomInt = this.getRandomInt(0, 25);
        for (int i = 0; i <= length - 1; ++i) {
            randomString = String.valueOf(String.valueOf(randomString)) + this.getRandomChar(this.getRandomBoolean());
        }
        return randomString;
    }
    
    public String getRandomString(final int length, final boolean UpperCase) {
        int randomInt = 0;
        String randomString = "";
        randomInt = this.getRandomInt(0, 25);
        for (int i = 0; i <= length - 1; ++i) {
            randomString = String.valueOf(String.valueOf(randomString)) + this.getRandomChar(UpperCase);
        }
        return randomString;
    }
    
    public String getRandomName() {
        final String RealLiveNames = StringUtils.getStringUtils().RealLiveNames;
        final ArrayList<String> namesArray = new ArrayList<String>();
        String[] split;
        for (int length = (split = RealLiveNames.split(",")).length, i = 0; i < length; ++i) {
            final String name = split[i];
            namesArray.add(name);
        }
        return this.getRandomStringFromArrayList(namesArray, "Tim");
    }
    
    public String getRandomIGN() {
        int randomInt = 0;
        String Prefix = "";
        String Sufix = "";
        randomInt = this.getRandomInt(0, 12);
        if (randomInt == 0) {
            Sufix = "_HD";
        }
        if (randomInt == 1) {
            Sufix = "_TV";
        }
        if (randomInt == 2) {
            Sufix = "_YT";
        }
        if (randomInt == 3) {
            Sufix = "Xx";
            Prefix = "xX";
        }
        if (randomInt == 4) {
            Prefix = "__";
            Sufix = "__";
        }
        if (randomInt == 5) {
            Sufix = "2019";
        }
        if (randomInt == 6) {
            Sufix = "LP";
        }
        if (randomInt == 7) {
            Sufix = "_LP";
        }
        if (randomInt == 8) {
            Sufix = "HD";
        }
        if (randomInt == 9) {
            Sufix = "YT";
        }
        if (randomInt == 10) {
            Sufix = "TV";
        }
        if (randomInt == 11) {
            Sufix = new StringBuilder().append(this.getRandomInt(100, 500)).toString();
        }
        if (randomInt == 12) {
            Sufix = "Oo";
            Prefix = "oO";
        }
        if (randomInt == 5) {
            Sufix = "2018";
        }
        if (randomInt == 5) {
            Sufix = "2017";
        }
        return String.valueOf(String.valueOf(Prefix)) + this.getRandomName().replace("o", "0").replace("h", "4") + Sufix;
    }
    
    public String getRandomStringFromArrayList(final ArrayList<String> array, final String abweichung) {
        int randomInt = 0;
        int nextInt = 0;
        randomInt = this.getRandomInt(0, array.size());
        for (final String string : array) {
            if (nextInt == randomInt) {
                return string;
            }
            ++nextInt;
        }
        return abweichung;
    }
    
    public float getRandomFloat(final int min, final int max) {
        return (float)(min + Math.random() * (max - min + 1));
    }
    
    public double getRandomDouble(final int min, final int max) {
        return min + Math.random() * (max - min + 1);
    }
    
    public int getRandomInt(final int min, final int max) {
        return (int)(min + Math.random() * (max - min + 1));
    }
}
