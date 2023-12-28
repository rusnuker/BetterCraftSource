// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.entity.projectile.EntityFireball;
import javax.annotation.Nullable;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class DamageSource
{
    public static final DamageSource inFire;
    public static final DamageSource lightningBolt;
    public static final DamageSource onFire;
    public static final DamageSource lava;
    public static final DamageSource hotFloor;
    public static final DamageSource inWall;
    public static final DamageSource field_191291_g;
    public static final DamageSource drown;
    public static final DamageSource starve;
    public static final DamageSource cactus;
    public static final DamageSource fall;
    public static final DamageSource flyIntoWall;
    public static final DamageSource outOfWorld;
    public static final DamageSource generic;
    public static final DamageSource magic;
    public static final DamageSource wither;
    public static final DamageSource anvil;
    public static final DamageSource fallingBlock;
    public static final DamageSource dragonBreath;
    public static final DamageSource field_191552_t;
    private boolean isUnblockable;
    private boolean isDamageAllowedInCreativeMode;
    private boolean damageIsAbsolute;
    private float hungerDamage;
    private boolean fireDamage;
    private boolean projectile;
    private boolean difficultyScaled;
    private boolean magicDamage;
    private boolean explosion;
    public String damageType;
    
    static {
        inFire = new DamageSource("inFire").setFireDamage();
        lightningBolt = new DamageSource("lightningBolt");
        onFire = new DamageSource("onFire").setDamageBypassesArmor().setFireDamage();
        lava = new DamageSource("lava").setFireDamage();
        hotFloor = new DamageSource("hotFloor").setFireDamage();
        inWall = new DamageSource("inWall").setDamageBypassesArmor();
        field_191291_g = new DamageSource("cramming").setDamageBypassesArmor();
        drown = new DamageSource("drown").setDamageBypassesArmor();
        starve = new DamageSource("starve").setDamageBypassesArmor().setDamageIsAbsolute();
        cactus = new DamageSource("cactus");
        fall = new DamageSource("fall").setDamageBypassesArmor();
        flyIntoWall = new DamageSource("flyIntoWall").setDamageBypassesArmor();
        outOfWorld = new DamageSource("outOfWorld").setDamageBypassesArmor().setDamageAllowedInCreativeMode();
        generic = new DamageSource("generic").setDamageBypassesArmor();
        magic = new DamageSource("magic").setDamageBypassesArmor().setMagicDamage();
        wither = new DamageSource("wither").setDamageBypassesArmor();
        anvil = new DamageSource("anvil");
        fallingBlock = new DamageSource("fallingBlock");
        dragonBreath = new DamageSource("dragonBreath").setDamageBypassesArmor();
        field_191552_t = new DamageSource("fireworks").setExplosion();
    }
    
    public static DamageSource causeMobDamage(final EntityLivingBase mob) {
        return new EntityDamageSource("mob", mob);
    }
    
    public static DamageSource causeIndirectDamage(final Entity source, final EntityLivingBase indirectEntityIn) {
        return new EntityDamageSourceIndirect("mob", source, indirectEntityIn);
    }
    
    public static DamageSource causePlayerDamage(final EntityPlayer player) {
        return new EntityDamageSource("player", player);
    }
    
    public static DamageSource causeArrowDamage(final EntityArrow arrow, @Nullable final Entity indirectEntityIn) {
        return new EntityDamageSourceIndirect("arrow", arrow, indirectEntityIn).setProjectile();
    }
    
    public static DamageSource causeFireballDamage(final EntityFireball fireball, @Nullable final Entity indirectEntityIn) {
        return (indirectEntityIn == null) ? new EntityDamageSourceIndirect("onFire", fireball, fireball).setFireDamage().setProjectile() : new EntityDamageSourceIndirect("fireball", fireball, indirectEntityIn).setFireDamage().setProjectile();
    }
    
    public static DamageSource causeThrownDamage(final Entity source, @Nullable final Entity indirectEntityIn) {
        return new EntityDamageSourceIndirect("thrown", source, indirectEntityIn).setProjectile();
    }
    
    public static DamageSource causeIndirectMagicDamage(final Entity source, @Nullable final Entity indirectEntityIn) {
        return new EntityDamageSourceIndirect("indirectMagic", source, indirectEntityIn).setDamageBypassesArmor().setMagicDamage();
    }
    
    public static DamageSource causeThornsDamage(final Entity source) {
        return new EntityDamageSource("thorns", source).setIsThornsDamage().setMagicDamage();
    }
    
    public static DamageSource causeExplosionDamage(@Nullable final Explosion explosionIn) {
        return (explosionIn != null && explosionIn.getExplosivePlacedBy() != null) ? new EntityDamageSource("explosion.player", explosionIn.getExplosivePlacedBy()).setDifficultyScaled().setExplosion() : new DamageSource("explosion").setDifficultyScaled().setExplosion();
    }
    
    public static DamageSource causeExplosionDamage(@Nullable final EntityLivingBase entityLivingBaseIn) {
        return (entityLivingBaseIn != null) ? new EntityDamageSource("explosion.player", entityLivingBaseIn).setDifficultyScaled().setExplosion() : new DamageSource("explosion").setDifficultyScaled().setExplosion();
    }
    
    public boolean isProjectile() {
        return this.projectile;
    }
    
    public DamageSource setProjectile() {
        this.projectile = true;
        return this;
    }
    
    public boolean isExplosion() {
        return this.explosion;
    }
    
    public DamageSource setExplosion() {
        this.explosion = true;
        return this;
    }
    
    public boolean isUnblockable() {
        return this.isUnblockable;
    }
    
    public float getHungerDamage() {
        return this.hungerDamage;
    }
    
    public boolean canHarmInCreative() {
        return this.isDamageAllowedInCreativeMode;
    }
    
    public boolean isDamageAbsolute() {
        return this.damageIsAbsolute;
    }
    
    protected DamageSource(final String damageTypeIn) {
        this.hungerDamage = 0.1f;
        this.damageType = damageTypeIn;
    }
    
    @Nullable
    public Entity getSourceOfDamage() {
        return this.getEntity();
    }
    
    @Nullable
    public Entity getEntity() {
        return null;
    }
    
    protected DamageSource setDamageBypassesArmor() {
        this.isUnblockable = true;
        this.hungerDamage = 0.0f;
        return this;
    }
    
    protected DamageSource setDamageAllowedInCreativeMode() {
        this.isDamageAllowedInCreativeMode = true;
        return this;
    }
    
    protected DamageSource setDamageIsAbsolute() {
        this.damageIsAbsolute = true;
        this.hungerDamage = 0.0f;
        return this;
    }
    
    protected DamageSource setFireDamage() {
        this.fireDamage = true;
        return this;
    }
    
    public ITextComponent getDeathMessage(final EntityLivingBase entityLivingBaseIn) {
        final EntityLivingBase entitylivingbase = entityLivingBaseIn.getAttackingEntity();
        final String s = "death.attack." + this.damageType;
        final String s2 = String.valueOf(s) + ".player";
        return (entitylivingbase != null && I18n.canTranslate(s2)) ? new TextComponentTranslation(s2, new Object[] { entityLivingBaseIn.getDisplayName(), entitylivingbase.getDisplayName() }) : new TextComponentTranslation(s, new Object[] { entityLivingBaseIn.getDisplayName() });
    }
    
    public boolean isFireDamage() {
        return this.fireDamage;
    }
    
    public String getDamageType() {
        return this.damageType;
    }
    
    public DamageSource setDifficultyScaled() {
        this.difficultyScaled = true;
        return this;
    }
    
    public boolean isDifficultyScaled() {
        return this.difficultyScaled;
    }
    
    public boolean isMagicDamage() {
        return this.magicDamage;
    }
    
    public DamageSource setMagicDamage() {
        this.magicDamage = true;
        return this;
    }
    
    public boolean isCreativePlayer() {
        final Entity entity = this.getEntity();
        return entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode;
    }
    
    @Nullable
    public Vec3d getDamageLocation() {
        return null;
    }
}
