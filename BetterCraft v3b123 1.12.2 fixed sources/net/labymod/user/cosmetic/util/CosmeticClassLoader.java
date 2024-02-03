// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.util;

import net.labymod.user.cosmetic.cosmetics.shop.head.CosmeticCatEars;
import net.labymod.user.cosmetic.cosmetics.shop.wings.CosmeticWingsButterfly;
import net.labymod.user.cosmetic.cosmetics.shop.head.masks.CosmeticMaskKawaii;
import net.labymod.user.cosmetic.cosmetics.partner.CosmeticReved;
import net.labymod.user.cosmetic.cosmetics.partner.CosmeticAbgegrieft;
import net.labymod.user.cosmetic.cosmetics.shop.head.CosmeticRoyalCrown;
import net.labymod.user.cosmetic.cosmetics.partner.CosmeticStegi;
import net.labymod.user.cosmetic.cosmetics.shop.body.CosmeticBackPack;
import net.labymod.user.cosmetic.cosmetics.shop.wings.CosmeticWingsSteampunk;
import net.labymod.user.cosmetic.cosmetics.shop.wings.CosmeticWingsCrystal;
import net.labymod.user.cosmetic.cosmetics.event.CosmeticBeard;
import net.labymod.user.cosmetic.cosmetics.event.CosmeticAntlers;
import net.labymod.user.cosmetic.cosmetics.event.CosmeticRednose;
import net.labymod.user.cosmetic.cosmetics.event.CosmeticXmasHat;
import net.labymod.user.cosmetic.cosmetics.shop.body.CosmeticWolfTail;
import net.labymod.user.cosmetic.cosmetics.shop.head.CosmeticWitchHat;
import net.labymod.user.cosmetic.cosmetics.shop.wings.CosmeticWingsDragon;
import net.labymod.user.cosmetic.cosmetics.shop.body.CosmeticTool;
import net.labymod.user.cosmetic.cosmetics.partner.CosmeticSnoxh;
import net.labymod.user.cosmetic.cosmetics.shop.body.CosmeticShoes;
import net.labymod.user.cosmetic.cosmetics.shop.head.CosmeticRabbit;
import net.labymod.user.cosmetic.cosmetics.staff.CosmeticMoehritz;
import net.labymod.user.cosmetic.cosmetics.shop.head.CosmeticDevilHorn;
import net.labymod.user.cosmetic.cosmetics.shop.head.CosmeticHeadset;
import net.labymod.user.cosmetic.cosmetics.shop.body.CosmeticHalo;
import net.labymod.user.cosmetic.cosmetics.event.CosmeticHalloween;
import net.labymod.user.cosmetic.cosmetics.shop.head.CosmeticFlower;
import net.labymod.user.cosmetic.cosmetics.shop.head.CosmeticDog;
import net.labymod.user.cosmetic.cosmetics.event.CosmeticMerchCrown;
import net.labymod.user.cosmetic.cosmetics.shop.body.CosmeticCloak;
import net.labymod.user.cosmetic.cosmetics.shop.body.CosmeticCatTail;
import net.labymod.user.cosmetic.cosmetics.event.CosmeticCap;
import net.labymod.user.cosmetic.cosmetics.shop.head.CosmeticBandana;
import net.labymod.user.cosmetic.cosmetics.shop.wings.CosmeticWingsAngel;
import net.labymod.support.util.Debug;
import java.util.ArrayList;
import java.util.List;

public class CosmeticClassLoader
{
    private static final String PACKAGE = "net.labymod.user.cosmetic.cosmetics";
    private List<Class<?>> cosmeticClasses;
    
    public List<Class<?>> getCosmeticClasses() {
        return this.cosmeticClasses;
    }
    
    public CosmeticClassLoader() {
        this.cosmeticClasses = new ArrayList<Class<?>>();
        final boolean getTopLevelClassesRecursiveFailed = true;
        try {
            if (getTopLevelClassesRecursiveFailed) {
                Debug.log(Debug.EnumDebugMode.GENERAL, "getTopLevelClassesRecursive failed! Adding backup classes..");
                Class[] array;
                for (int length = (array = new Class[] { CosmeticWingsAngel.class, CosmeticBandana.class, CosmeticCap.class, CosmeticCatTail.class, CosmeticCloak.class, CosmeticMerchCrown.class, CosmeticDog.class, CosmeticFlower.class, CosmeticHalloween.class, CosmeticHalo.class, CosmeticHeadset.class, CosmeticDevilHorn.class, CosmeticMoehritz.class, CosmeticRabbit.class, CosmeticShoes.class, CosmeticSnoxh.class, CosmeticTool.class, CosmeticWingsDragon.class, CosmeticWitchHat.class, CosmeticWolfTail.class, CosmeticXmasHat.class, CosmeticRednose.class, CosmeticAntlers.class, CosmeticBeard.class, CosmeticWingsCrystal.class, CosmeticWingsSteampunk.class, CosmeticBackPack.class, CosmeticStegi.class, CosmeticRoyalCrown.class, CosmeticAbgegrieft.class, CosmeticReved.class, CosmeticMaskKawaii.class, CosmeticWingsButterfly.class, CosmeticCatEars.class }).length, i = 0; i < length; ++i) {
                    final Class<?> cosmeticClass = array[i];
                    this.cosmeticClasses.add(cosmeticClass);
                }
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
