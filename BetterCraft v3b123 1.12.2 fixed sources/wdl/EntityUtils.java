// 
// Decompiled by Procyon v0.6.0
// 

package wdl;

import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.Entity;
import java.util.List;
import wdl.api.IEntityAdder;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.Iterator;
import java.util.Collection;
import wdl.api.WDLApi;
import wdl.api.ISpecialEntityHandler;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.lang.reflect.Field;
import net.minecraft.crash.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityList;
import org.apache.logging.log4j.LogManager;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class EntityUtils
{
    private static final Logger logger;
    public static final Map<String, Class<?>> stringToClassMapping;
    public static final Map<Class<?>, String> classToStringMapping;
    
    static {
        logger = LogManager.getLogger();
        try {
            Map<String, Class<?>> mappingSTC = null;
            Map<Class<?>, String> mappingCTS = null;
            Field[] declaredFields;
            for (int length = (declaredFields = EntityList.class.getDeclaredFields()).length, i = 0; i < length; ++i) {
                final Field field = declaredFields[i];
                if (field.getType().equals(Map.class)) {
                    field.setAccessible(true);
                    final Map<?, ?> m = (Map<?, ?>)field.get(null);
                    final Map.Entry<?, ?> e = (Map.Entry<?, ?>)m.entrySet().toArray()[0];
                    if (e.getKey() instanceof String && e.getValue() instanceof Class) {
                        final Map<String, Class<?>> temp = mappingSTC = (Map<String, Class<?>>)m;
                    }
                    if (e.getKey() instanceof Class && e.getValue() instanceof String) {
                        final Map<Class<?>, String> temp2 = mappingCTS = (Map<Class<?>, String>)m;
                    }
                }
            }
            if (mappingSTC == null) {
                throw new Exception("WDL: Failed to find stringToClassMapping!");
            }
            if (mappingCTS == null) {
                throw new Exception("WDL: Failed to find classToStringMapping!");
            }
            stringToClassMapping = mappingSTC;
            classToStringMapping = mappingCTS;
        }
        catch (final Exception e2) {
            Minecraft.getMinecraft().crashed(new CrashReport("World Downloader Mod: failed to set up entity ranges!", e2));
            throw new Error("World Downloader Mod: failed to set up entity ranges!", e2);
        }
    }
    
    public static Set<String> getEntityTypes() {
        final Set<String> returned = new HashSet<String>();
        for (final Map.Entry<String, Class<?>> e : EntityUtils.stringToClassMapping.entrySet()) {
            if (Modifier.isAbstract(e.getValue().getModifiers())) {
                continue;
            }
            returned.add(e.getKey());
        }
        for (final WDLApi.ModInfo<ISpecialEntityHandler> info : WDLApi.getImplementingExtensions(ISpecialEntityHandler.class)) {
            returned.addAll(info.mod.getSpecialEntities().values());
        }
        return returned;
    }
    
    public static Multimap<String, String> getEntitiesByGroup() {
        final Multimap<String, String> returned = (Multimap<String, String>)HashMultimap.create();
        final Set<String> types = getEntityTypes();
        for (final String type : types) {
            returned.put(getEntityGroup(type), type);
        }
        return returned;
    }
    
    public static int getDefaultEntityRange(final String type) {
        if (type == null) {
            return -1;
        }
        for (final WDLApi.ModInfo<IEntityAdder> info : WDLApi.getImplementingExtensions(IEntityAdder.class)) {
            final List<String> names = info.mod.getModEntities();
            if (names == null) {
                EntityUtils.logger.warn(String.valueOf(info.toString()) + " returned null for getModEntities()!");
            }
            else {
                if (names.contains(type)) {
                    return info.mod.getDefaultEntityTrackDistance(type);
                }
                continue;
            }
        }
        for (final WDLApi.ModInfo<ISpecialEntityHandler> info2 : WDLApi.getImplementingExtensions(ISpecialEntityHandler.class)) {
            final Multimap<String, String> specialEntities = info2.mod.getSpecialEntities();
            if (specialEntities == null) {
                EntityUtils.logger.warn(String.valueOf(info2.toString()) + " returned null for getSpecialEntities()!");
            }
            else {
                for (final Map.Entry<String, String> e : specialEntities.entries()) {
                    if (e.getValue().equals(type)) {
                        int trackDistance = info2.mod.getSpecialEntityTrackDistance(e.getValue());
                        if (trackDistance < 0) {
                            trackDistance = getMostLikelyEntityTrackDistance(e.getKey());
                        }
                        return trackDistance;
                    }
                }
            }
        }
        return getVanillaEntityRange(EntityUtils.stringToClassMapping.get(type));
    }
    
    public static int getEntityTrackDistance(final Entity e) {
        return getEntityTrackDistance(getTrackDistanceMode(), e);
    }
    
    public static int getEntityTrackDistance(final String mode, final Entity e) {
        if ("default".equals(mode)) {
            return getMostLikelyEntityTrackDistance(e);
        }
        if ("server".equals(mode)) {
            final int serverDistance = WDLPluginChannels.getEntityRange(getEntityType(e));
            if (serverDistance >= 0) {
                return serverDistance;
            }
            final int mostLikelyRange = getMostLikelyEntityTrackDistance(e);
            if (mostLikelyRange < 0) {
                return WDLPluginChannels.getEntityRange(EntityList.getEntityString(e));
            }
            return mostLikelyRange;
        }
        else {
            if (!"user".equals(mode)) {
                throw new IllegalArgumentException("Mode is not a valid mode: " + mode);
            }
            final String prop = WDL.worldProps.getProperty("Entity." + getEntityType(e) + ".TrackDistance", "-1");
            final int value = Integer.valueOf(prop);
            if (value == -1) {
                return getEntityTrackDistance("server", e);
            }
            return value;
        }
    }
    
    public static int getEntityTrackDistance(final String type) {
        return getEntityTrackDistance(getTrackDistanceMode(), type);
    }
    
    public static int getEntityTrackDistance(final String mode, final String type) {
        if ("default".equals(mode)) {
            final int mostLikelyDistance = getMostLikelyEntityTrackDistance(type);
            if (mostLikelyDistance < 0) {
                for (final WDLApi.ModInfo<ISpecialEntityHandler> info : WDLApi.getImplementingExtensions(ISpecialEntityHandler.class)) {
                    final Multimap<String, String> specialEntities = info.mod.getSpecialEntities();
                    for (final Map.Entry<String, String> mapping : specialEntities.entries()) {
                        if (type.equals(mapping.getValue())) {
                            return getEntityTrackDistance(mode, mapping.getKey());
                        }
                    }
                }
            }
            return mostLikelyDistance;
        }
        if ("server".equals(mode)) {
            final int serverDistance = WDLPluginChannels.getEntityRange(type);
            if (serverDistance < 0) {
                final int mostLikelyDistance2 = getMostLikelyEntityTrackDistance(type);
                if (mostLikelyDistance2 >= 0) {
                    return mostLikelyDistance2;
                }
                for (final WDLApi.ModInfo<ISpecialEntityHandler> info2 : WDLApi.getImplementingExtensions(ISpecialEntityHandler.class)) {
                    final Multimap<String, String> specialEntities2 = info2.mod.getSpecialEntities();
                    for (final Map.Entry<String, String> mapping2 : specialEntities2.entries()) {
                        if (type.equals(mapping2.getValue())) {
                            return getEntityTrackDistance(mode, mapping2.getKey());
                        }
                    }
                }
            }
            return serverDistance;
        }
        if (!"user".equals(mode)) {
            throw new IllegalArgumentException("Mode is not a valid mode: " + mode);
        }
        final String prop = WDL.worldProps.getProperty("Entity." + type + ".TrackDistance", "-1");
        final int value = Integer.valueOf(prop);
        if (value == -1) {
            return getEntityTrackDistance("server", type);
        }
        return value;
    }
    
    public static String getEntityGroup(final String type) {
        if (type == null) {
            return null;
        }
        for (final WDLApi.ModInfo<IEntityAdder> info : WDLApi.getImplementingExtensions(IEntityAdder.class)) {
            final List<String> names = info.mod.getModEntities();
            if (names == null) {
                EntityUtils.logger.warn(String.valueOf(info.toString()) + " returned null for getModEntities()!");
            }
            else {
                if (names.contains(type)) {
                    return info.mod.getEntityCategory(type);
                }
                continue;
            }
        }
        for (final WDLApi.ModInfo<ISpecialEntityHandler> info2 : WDLApi.getImplementingExtensions(ISpecialEntityHandler.class)) {
            final Multimap<String, String> specialEntities = info2.mod.getSpecialEntities();
            if (specialEntities == null) {
                EntityUtils.logger.warn(String.valueOf(info2.toString()) + " returned null for getSpecialEntities()!");
            }
            else {
                if (specialEntities.containsValue(type)) {
                    return info2.mod.getSpecialEntityCategory(type);
                }
                continue;
            }
        }
        if (!EntityUtils.stringToClassMapping.containsKey(type)) {
            return null;
        }
        final Class<?> clazz = EntityUtils.stringToClassMapping.get(type);
        if (IMob.class.isAssignableFrom(clazz)) {
            return "Hostile";
        }
        if (IAnimals.class.isAssignableFrom(clazz)) {
            return "Passive";
        }
        return "Other";
    }
    
    public static boolean isEntityEnabled(final Entity e) {
        return isEntityEnabled(getEntityType(e));
    }
    
    public static boolean isEntityEnabled(final String type) {
        final boolean groupEnabled = WDL.worldProps.getProperty("EntityGroup." + getEntityGroup(type) + ".Enabled", "true").equals("true");
        final boolean singleEnabled = WDL.worldProps.getProperty("Entity." + type + ".Enabled", "true").equals("true");
        return groupEnabled && singleEnabled;
    }
    
    public static String getEntityType(final Entity e) {
        final String vanillaName = EntityList.getEntityString(e);
        for (final WDLApi.ModInfo<ISpecialEntityHandler> info : WDLApi.getImplementingExtensions(ISpecialEntityHandler.class)) {
            if (info.mod.getSpecialEntities().containsKey(vanillaName)) {
                final String specialName = info.mod.getSpecialEntityName(e);
                if (specialName != null) {
                    return specialName;
                }
                continue;
            }
        }
        return vanillaName;
    }
    
    public static int getMostLikelyEntityTrackDistance(final Entity e) {
        if (WDL.isSpigot()) {
            return getDefaultSpigotEntityRange(e.getClass());
        }
        return getDefaultEntityRange(getEntityType(e));
    }
    
    public static int getMostLikelyEntityTrackDistance(final String type) {
        if (!WDL.isSpigot()) {
            return getDefaultEntityRange(type);
        }
        final Class<?> c = EntityUtils.stringToClassMapping.get(type);
        if (c != null) {
            return getDefaultSpigotEntityRange(c);
        }
        return getDefaultEntityRange(type);
    }
    
    public static int getVanillaEntityRange(final String type) {
        return getVanillaEntityRange(EntityUtils.classToStringMapping.get(type));
    }
    
    public static String getTrackDistanceMode() {
        return WDL.worldProps.getProperty("Entity.TrackDistanceMode", "server");
    }
    
    public static int getVanillaEntityRange(final Class<?> c) {
        if (c == null) {
            return -1;
        }
        if (EntityFishHook.class.isAssignableFrom(c) || EntityArrow.class.isAssignableFrom(c) || EntitySmallFireball.class.isAssignableFrom(c) || EntityFireball.class.isAssignableFrom(c) || EntitySnowball.class.isAssignableFrom(c) || EntityEnderPearl.class.isAssignableFrom(c) || EntityEnderEye.class.isAssignableFrom(c) || EntityEgg.class.isAssignableFrom(c) || EntityPotion.class.isAssignableFrom(c) || EntityExpBottle.class.isAssignableFrom(c) || EntityFireworkRocket.class.isAssignableFrom(c) || EntityItem.class.isAssignableFrom(c) || EntitySquid.class.isAssignableFrom(c)) {
            return 64;
        }
        if (EntityMinecart.class.isAssignableFrom(c) || EntityBoat.class.isAssignableFrom(c) || EntityWither.class.isAssignableFrom(c) || EntityBat.class.isAssignableFrom(c) || IAnimals.class.isAssignableFrom(c)) {
            return 80;
        }
        if (EntityDragon.class.isAssignableFrom(c) || EntityTNTPrimed.class.isAssignableFrom(c) || EntityFallingBlock.class.isAssignableFrom(c) || EntityHanging.class.isAssignableFrom(c) || EntityArmorStand.class.isAssignableFrom(c) || EntityXPOrb.class.isAssignableFrom(c)) {
            return 160;
        }
        if (EntityEnderCrystal.class.isAssignableFrom(c)) {
            return 256;
        }
        return -1;
    }
    
    public static int getDefaultSpigotEntityRange(final Class<?> c) {
        final int monsterRange = 48;
        final int animalRange = 48;
        final int miscRange = 32;
        final int otherRange = 64;
        if (EntityMob.class.isAssignableFrom(c) || EntitySlime.class.isAssignableFrom(c)) {
            return 48;
        }
        if (EntityCreature.class.isAssignableFrom(c) || EntityAmbientCreature.class.isAssignableFrom(c)) {
            return 48;
        }
        if (EntityItemFrame.class.isAssignableFrom(c) || EntityPainting.class.isAssignableFrom(c) || EntityItem.class.isAssignableFrom(c) || EntityXPOrb.class.isAssignableFrom(c)) {
            return 32;
        }
        return 64;
    }
}
