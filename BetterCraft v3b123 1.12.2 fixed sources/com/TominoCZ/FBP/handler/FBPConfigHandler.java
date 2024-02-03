// 
// Decompiled by Procyon v0.6.0
// 

package com.TominoCZ.FBP.handler;

import java.io.Reader;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.lang.reflect.Field;
import com.TominoCZ.FBP.util.FBPObfUtil;
import java.util.Iterator;
import java.io.PrintWriter;
import java.io.IOException;
import net.minecraft.block.material.Material;
import java.nio.file.Paths;
import com.TominoCZ.FBP.FBP;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;

public class FBPConfigHandler
{
    static FileInputStream fis;
    static InputStreamReader isr;
    static BufferedReader br;
    
    public static void init() {
        try {
            defaults(false);
            if (!Paths.get(FBP.config.getParent(), new String[0]).toFile().exists()) {
                Paths.get(FBP.config.getParent(), new String[0]).toFile().mkdirs();
            }
            if (!FBP.config.exists()) {
                FBP.config.createNewFile();
                write();
            }
            if (!FBP.particleBlacklistFile.exists()) {
                FBP.particleBlacklistFile.createNewFile();
            }
            if (!FBP.floatingMaterialsFile.exists()) {
                FBP.floatingMaterialsFile.createNewFile();
                FBP.getInstance().floatingMaterials.clear();
                FBP.getInstance().floatingMaterials.add(Material.leaves);
                FBP.getInstance().floatingMaterials.add(Material.plants);
                FBP.getInstance().floatingMaterials.add(Material.ice);
                FBP.getInstance().floatingMaterials.add(Material.packedIce);
                FBP.getInstance().floatingMaterials.add(Material.carpet);
                FBP.getInstance().floatingMaterials.add(Material.wood);
                FBP.getInstance().floatingMaterials.add(Material.web);
            }
            else {
                readFloatingMaterials();
            }
            read();
            readParticleBlacklist();
            write();
            writeParticleBlacklist();
            writeFloatingMaterials();
            closeStreams();
        }
        catch (final IOException e) {
            closeStreams();
            write();
        }
    }
    
    public static void write() {
        try {
            final PrintWriter writer = new PrintWriter(FBP.config.getPath(), "UTF-8");
            writer.println("enabled=" + FBP.enabled);
            writer.println("weatherParticleDensity=" + FBP.weatherParticleDensity);
            writer.println("particlesPerAxis=" + FBP.particlesPerAxis);
            writer.println("restOnFloor=" + FBP.restOnFloor);
            writer.println("waterPhysics=" + FBP.waterPhysics);
            writer.println("fancyFlame=" + FBP.fancyFlame);
            writer.println("fancySmoke=" + FBP.fancySmoke);
            writer.println("fancyRain=" + FBP.fancyRain);
            writer.println("fancySnow=" + FBP.fancySnow);
            writer.println("smartBreaking=" + FBP.smartBreaking);
            writer.println("lowTraction=" + FBP.lowTraction);
            writer.println("bounceOffWalls=" + FBP.bounceOffWalls);
            writer.println("showInMillis=" + FBP.showInMillis);
            writer.println("randomRotation=" + FBP.randomRotation);
            writer.println("cartoonMode=" + FBP.cartoonMode);
            writer.println("entityCollision=" + FBP.entityCollision);
            writer.println("randomizedScale=" + FBP.randomizedScale);
            writer.println("randomFadingSpeed=" + FBP.randomFadingSpeed);
            writer.println("spawnRedstoneBlockParticles=" + FBP.spawnRedstoneBlockParticles);
            writer.println("spawnWhileFrozen=" + FBP.spawnWhileFrozen);
            writer.println("infiniteDuration=" + FBP.infiniteDuration);
            writer.println("minAge=" + FBP.minAge);
            writer.println("maxAge=" + FBP.maxAge);
            writer.println("scaleMult=" + FBP.scaleMult);
            writer.println("gravityMult=" + FBP.gravityMult);
            writer.print("rotationMult=" + FBP.rotationMult);
            writer.close();
        }
        catch (final Exception e) {
            closeStreams();
            if (!FBP.config.exists()) {
                if (!Paths.get(FBP.config.getParent(), new String[0]).toFile().exists()) {
                    Paths.get(FBP.config.getParent(), new String[0]).toFile().mkdirs();
                }
                try {
                    FBP.config.createNewFile();
                }
                catch (final IOException e2) {
                    e2.printStackTrace();
                }
            }
            write();
        }
    }
    
    public static void writeParticleBlacklist() {
        try {
            final PrintWriter writer = new PrintWriter(FBP.particleBlacklistFile.getPath(), "UTF-8");
            for (final String ex : FBP.getInstance().blockParticleBlacklist) {
                writer.println(ex);
            }
            writer.close();
        }
        catch (final Exception e) {
            closeStreams();
            if (!FBP.particleBlacklistFile.exists()) {
                if (!Paths.get(FBP.particleBlacklistFile.getParent(), new String[0]).toFile().exists()) {
                    Paths.get(FBP.particleBlacklistFile.getParent(), new String[0]).toFile().mkdirs();
                }
                try {
                    FBP.particleBlacklistFile.createNewFile();
                }
                catch (final IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
    
    static void writeFloatingMaterials() {
        try {
            final PrintWriter writer = new PrintWriter(FBP.floatingMaterialsFile.getPath(), "UTF-8");
            final Field[] materials = Material.class.getDeclaredFields();
            Field[] array;
            for (int length = (array = materials).length, i = 0; i < length; ++i) {
                final Field f = array[i];
                final String fieldName = f.getName();
                if (f.getType() == Material.class) {
                    final String translated = FBPObfUtil.translateObfMaterialName(fieldName).toLowerCase();
                    try {
                        final Material mat = (Material)f.get(null);
                        if (mat != Material.air) {
                            final boolean flag = FBP.getInstance().doesMaterialFloat(mat);
                            writer.println(String.valueOf(translated) + "=" + flag);
                        }
                    }
                    catch (final Exception ex) {}
                }
            }
            writer.close();
        }
        catch (final Exception e) {
            closeStreams();
        }
    }
    
    static void read() {
        try {
            FBPConfigHandler.fis = new FileInputStream(FBP.config);
            FBPConfigHandler.isr = new InputStreamReader(FBPConfigHandler.fis, Charset.forName("UTF-8"));
            FBPConfigHandler.br = new BufferedReader(FBPConfigHandler.isr);
            String line;
            while ((line = FBPConfigHandler.br.readLine()) != null) {
                line = line.replaceAll(" ", "");
                if (line.contains("enabled=")) {
                    FBP.enabled = Boolean.valueOf(line.replace("enabled=", ""));
                }
                else if (line.contains("weatherParticleDensity=")) {
                    FBP.weatherParticleDensity = Double.valueOf(line.replace("weatherParticleDensity=", ""));
                }
                else if (line.contains("particlesPerAxis=")) {
                    FBP.particlesPerAxis = Integer.valueOf(line.replace("particlesPerAxis=", ""));
                }
                else if (line.contains("restOnFloor=")) {
                    FBP.restOnFloor = Boolean.valueOf(line.replace("restOnFloor=", ""));
                }
                else if (line.contains("waterPhysics=")) {
                    FBP.waterPhysics = Boolean.valueOf(line.replace("waterPhysics=", ""));
                }
                else if (line.contains("fancyFlame=")) {
                    FBP.fancyFlame = Boolean.valueOf(line.replace("fancyFlame=", ""));
                }
                else if (line.contains("fancySmoke=")) {
                    FBP.fancySmoke = Boolean.valueOf(line.replace("fancySmoke=", ""));
                }
                else if (line.contains("fancyRain=")) {
                    FBP.fancyRain = Boolean.valueOf(line.replace("fancyRain=", ""));
                }
                else if (line.contains("fancySnow=")) {
                    FBP.fancySnow = Boolean.valueOf(line.replace("fancySnow=", ""));
                }
                else if (line.contains("smartBreaking=")) {
                    FBP.smartBreaking = Boolean.valueOf(line.replace("smartBreaking=", ""));
                }
                else if (line.contains("lowTraction=")) {
                    FBP.lowTraction = Boolean.valueOf(line.replace("lowTraction=", ""));
                }
                else if (line.contains("bounceOffWalls=")) {
                    FBP.bounceOffWalls = Boolean.valueOf(line.replace("bounceOffWalls=", ""));
                }
                else if (line.contains("showInMillis=")) {
                    FBP.showInMillis = Boolean.valueOf(line.replace("showInMillis=", ""));
                }
                else if (line.contains("randomRotation=")) {
                    FBP.randomRotation = Boolean.valueOf(line.replace("randomRotation=", ""));
                }
                else if (line.contains("cartoonMode=")) {
                    FBP.cartoonMode = Boolean.valueOf(line.replace("cartoonMode=", ""));
                }
                else if (line.contains("entityCollision=")) {
                    FBP.entityCollision = Boolean.valueOf(line.replace("entityCollision=", ""));
                }
                else if (line.contains("randomFadingSpeed=")) {
                    FBP.randomFadingSpeed = Boolean.valueOf(line.replace("randomFadingSpeed=", ""));
                }
                else if (line.contains("smoothTransitions=")) {
                    FBP.randomizedScale = Boolean.valueOf(line.replace("randomizedScale=", ""));
                }
                else if (line.contains("spawnWhileFrozen=")) {
                    FBP.spawnWhileFrozen = Boolean.valueOf(line.replace("spawnWhileFrozen=", ""));
                }
                else if (line.contains("spawnRedstoneBlockParticles=")) {
                    FBP.spawnRedstoneBlockParticles = Boolean.valueOf(line.replace("spawnRedstoneBlockParticles=", ""));
                }
                else if (line.contains("infiniteDuration=")) {
                    FBP.infiniteDuration = Boolean.valueOf(line.replace("infiniteDuration=", ""));
                }
                else if (line.contains("minAge=")) {
                    FBP.minAge = Integer.valueOf(line.replace("minAge=", ""));
                }
                else if (line.contains("maxAge=")) {
                    FBP.maxAge = Integer.valueOf(line.replace("maxAge=", ""));
                }
                else if (line.contains("scaleMult=")) {
                    FBP.scaleMult = Double.valueOf(line.replace("scaleMult=", ""));
                }
                else if (line.contains("gravityMult=")) {
                    FBP.gravityMult = Double.valueOf(line.replace("gravityMult=", ""));
                }
                else {
                    if (!line.contains("rotationMult=")) {
                        continue;
                    }
                    FBP.rotationMult = Double.valueOf(line.replace("rotationMult=", ""));
                }
            }
            closeStreams();
        }
        catch (final Exception e) {
            closeStreams();
            write();
        }
    }
    
    static void readParticleBlacklist() {
        try {
            FBPConfigHandler.fis = new FileInputStream(FBP.particleBlacklistFile);
            FBPConfigHandler.isr = new InputStreamReader(FBPConfigHandler.fis, Charset.forName("UTF-8"));
            FBPConfigHandler.br = new BufferedReader(FBPConfigHandler.isr);
            FBP.getInstance().resetBlacklist();
            String line;
            while ((line = FBPConfigHandler.br.readLine()) != null) {
                if ((line = line.replaceAll(" ", "").toLowerCase()).equals("")) {
                    break;
                }
                FBP.getInstance().addToBlacklist(line);
            }
        }
        catch (final Exception ex) {}
        closeStreams();
    }
    
    static void readFloatingMaterials() {
        try {
            FBPConfigHandler.fis = new FileInputStream(FBP.floatingMaterialsFile);
            FBPConfigHandler.isr = new InputStreamReader(FBPConfigHandler.fis, Charset.forName("UTF-8"));
            FBPConfigHandler.br = new BufferedReader(FBPConfigHandler.isr);
            FBP.getInstance().floatingMaterials.clear();
            final Field[] materials = Material.class.getDeclaredFields();
            String line;
            while ((line = FBPConfigHandler.br.readLine()) != null) {
                line = line.trim().toLowerCase();
                final String[] split = line.split("=");
                if (split.length < 2) {
                    continue;
                }
                final String materialName = split[0].replace("_", "");
                final boolean flag = Boolean.parseBoolean(split[1]);
                if (!flag) {
                    continue;
                }
                boolean found = false;
                Field[] array;
                for (int length = (array = materials).length, i = 0; i < length; ++i) {
                    final Field f = array[i];
                    final String fieldName = f.getName();
                    if (f.getType() == Material.class) {
                        final String translated = FBPObfUtil.translateObfMaterialName(fieldName).toLowerCase().replace("_", "");
                        if (materialName.equals(translated)) {
                            try {
                                final Material mat = (Material)f.get(null);
                                if (!FBP.getInstance().floatingMaterials.contains(mat)) {
                                    FBP.getInstance().floatingMaterials.add(mat);
                                }
                                found = true;
                                break;
                            }
                            catch (final Exception ex) {}
                        }
                    }
                }
                if (found) {
                    continue;
                }
                System.out.println("[FBP]: Material not recognized: " + materialName);
            }
            closeStreams();
        }
        catch (final Exception e) {
            closeStreams();
            write();
        }
    }
    
    static void closeStreams() {
        try {
            FBPConfigHandler.br.close();
            FBPConfigHandler.isr.close();
            FBPConfigHandler.fis.close();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void defaults(final boolean write) {
        FBP.minAge = 10;
        FBP.maxAge = 55;
        FBP.scaleMult = 0.75;
        FBP.gravityMult = 1.0;
        FBP.rotationMult = 1.0;
        FBP.particlesPerAxis = 4;
        FBP.weatherParticleDensity = 1.0;
        FBP.lowTraction = false;
        FBP.bounceOffWalls = true;
        FBP.randomRotation = true;
        FBP.cartoonMode = false;
        FBP.entityCollision = false;
        FBP.randomizedScale = true;
        FBP.randomFadingSpeed = true;
        FBP.spawnRedstoneBlockParticles = false;
        FBP.infiniteDuration = false;
        FBP.spawnWhileFrozen = true;
        FBP.smartBreaking = true;
        FBP.fancyRain = true;
        FBP.fancySnow = true;
        FBP.fancySmoke = true;
        FBP.fancyFlame = true;
        FBP.waterPhysics = true;
        FBP.restOnFloor = true;
        if (write) {
            write();
        }
    }
}
