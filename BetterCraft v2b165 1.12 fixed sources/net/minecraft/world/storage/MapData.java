// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.storage;

import net.minecraft.network.play.server.SPacketMaps;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.network.Packet;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Map;
import java.util.List;

public class MapData extends WorldSavedData
{
    public int xCenter;
    public int zCenter;
    public byte dimension;
    public boolean trackingPosition;
    public boolean field_191096_f;
    public byte scale;
    public byte[] colors;
    public List<MapInfo> playersArrayList;
    private final Map<EntityPlayer, MapInfo> playersHashMap;
    public Map<String, MapDecoration> mapDecorations;
    
    public MapData(final String mapname) {
        super(mapname);
        this.colors = new byte[16384];
        this.playersArrayList = (List<MapInfo>)Lists.newArrayList();
        this.playersHashMap = (Map<EntityPlayer, MapInfo>)Maps.newHashMap();
        this.mapDecorations = (Map<String, MapDecoration>)Maps.newLinkedHashMap();
    }
    
    public void calculateMapCenter(final double x, final double z, final int mapScale) {
        final int i = 128 * (1 << mapScale);
        final int j = MathHelper.floor((x + 64.0) / i);
        final int k = MathHelper.floor((z + 64.0) / i);
        this.xCenter = j * i + i / 2 - 64;
        this.zCenter = k * i + i / 2 - 64;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        this.dimension = nbt.getByte("dimension");
        this.xCenter = nbt.getInteger("xCenter");
        this.zCenter = nbt.getInteger("zCenter");
        this.scale = nbt.getByte("scale");
        this.scale = (byte)MathHelper.clamp(this.scale, 0, 4);
        if (nbt.hasKey("trackingPosition", 1)) {
            this.trackingPosition = nbt.getBoolean("trackingPosition");
        }
        else {
            this.trackingPosition = true;
        }
        this.field_191096_f = nbt.getBoolean("unlimitedTracking");
        final int i = nbt.getShort("width");
        final int j = nbt.getShort("height");
        if (i == 128 && j == 128) {
            this.colors = nbt.getByteArray("colors");
        }
        else {
            final byte[] abyte = nbt.getByteArray("colors");
            this.colors = new byte[16384];
            final int k = (128 - i) / 2;
            final int l = (128 - j) / 2;
            for (int i2 = 0; i2 < j; ++i2) {
                final int j2 = i2 + l;
                if (j2 >= 0 || j2 < 128) {
                    for (int k2 = 0; k2 < i; ++k2) {
                        final int l2 = k2 + k;
                        if (l2 >= 0 || l2 < 128) {
                            this.colors[l2 + j2 * 128] = abyte[k2 + i2 * i];
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        compound.setByte("dimension", this.dimension);
        compound.setInteger("xCenter", this.xCenter);
        compound.setInteger("zCenter", this.zCenter);
        compound.setByte("scale", this.scale);
        compound.setShort("width", (short)128);
        compound.setShort("height", (short)128);
        compound.setByteArray("colors", this.colors);
        compound.setBoolean("trackingPosition", this.trackingPosition);
        compound.setBoolean("unlimitedTracking", this.field_191096_f);
        return compound;
    }
    
    public void updateVisiblePlayers(final EntityPlayer player, final ItemStack mapStack) {
        if (!this.playersHashMap.containsKey(player)) {
            final MapInfo mapdata$mapinfo = new MapInfo(player);
            this.playersHashMap.put(player, mapdata$mapinfo);
            this.playersArrayList.add(mapdata$mapinfo);
        }
        if (!player.inventory.hasItemStack(mapStack)) {
            this.mapDecorations.remove(player.getName());
        }
        for (int i = 0; i < this.playersArrayList.size(); ++i) {
            final MapInfo mapdata$mapinfo2 = this.playersArrayList.get(i);
            if (!mapdata$mapinfo2.entityplayerObj.isDead && (mapdata$mapinfo2.entityplayerObj.inventory.hasItemStack(mapStack) || mapStack.isOnItemFrame())) {
                if (!mapStack.isOnItemFrame() && mapdata$mapinfo2.entityplayerObj.dimension == this.dimension && this.trackingPosition) {
                    this.func_191095_a(MapDecoration.Type.PLAYER, mapdata$mapinfo2.entityplayerObj.world, mapdata$mapinfo2.entityplayerObj.getName(), mapdata$mapinfo2.entityplayerObj.posX, mapdata$mapinfo2.entityplayerObj.posZ, mapdata$mapinfo2.entityplayerObj.rotationYaw);
                }
            }
            else {
                this.playersHashMap.remove(mapdata$mapinfo2.entityplayerObj);
                this.playersArrayList.remove(mapdata$mapinfo2);
            }
        }
        if (mapStack.isOnItemFrame() && this.trackingPosition) {
            final EntityItemFrame entityitemframe = mapStack.getItemFrame();
            final BlockPos blockpos = entityitemframe.getHangingPosition();
            this.func_191095_a(MapDecoration.Type.FRAME, player.world, "frame-" + entityitemframe.getEntityId(), blockpos.getX(), blockpos.getZ(), entityitemframe.facingDirection.getHorizontalIndex() * 90);
        }
        if (mapStack.hasTagCompound() && mapStack.getTagCompound().hasKey("Decorations", 9)) {
            final NBTTagList nbttaglist = mapStack.getTagCompound().getTagList("Decorations", 10);
            for (int j = 0; j < nbttaglist.tagCount(); ++j) {
                final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(j);
                if (!this.mapDecorations.containsKey(nbttagcompound.getString("id"))) {
                    this.func_191095_a(MapDecoration.Type.func_191159_a(nbttagcompound.getByte("type")), player.world, nbttagcompound.getString("id"), nbttagcompound.getDouble("x"), nbttagcompound.getDouble("z"), nbttagcompound.getDouble("rot"));
                }
            }
        }
    }
    
    public static void func_191094_a(final ItemStack p_191094_0_, final BlockPos p_191094_1_, final String p_191094_2_, final MapDecoration.Type p_191094_3_) {
        NBTTagList nbttaglist;
        if (p_191094_0_.hasTagCompound() && p_191094_0_.getTagCompound().hasKey("Decorations", 9)) {
            nbttaglist = p_191094_0_.getTagCompound().getTagList("Decorations", 10);
        }
        else {
            nbttaglist = new NBTTagList();
            p_191094_0_.setTagInfo("Decorations", nbttaglist);
        }
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setByte("type", p_191094_3_.func_191163_a());
        nbttagcompound.setString("id", p_191094_2_);
        nbttagcompound.setDouble("x", p_191094_1_.getX());
        nbttagcompound.setDouble("z", p_191094_1_.getZ());
        nbttagcompound.setDouble("rot", 180.0);
        nbttaglist.appendTag(nbttagcompound);
        if (p_191094_3_.func_191162_c()) {
            final NBTTagCompound nbttagcompound2 = p_191094_0_.func_190925_c("display");
            nbttagcompound2.setInteger("MapColor", p_191094_3_.func_191161_d());
        }
    }
    
    private void func_191095_a(MapDecoration.Type p_191095_1_, final World p_191095_2_, final String p_191095_3_, final double p_191095_4_, final double p_191095_6_, double p_191095_8_) {
        final int i = 1 << this.scale;
        final float f = (float)(p_191095_4_ - this.xCenter) / i;
        final float f2 = (float)(p_191095_6_ - this.zCenter) / i;
        byte b0 = (byte)(f * 2.0f + 0.5);
        byte b2 = (byte)(f2 * 2.0f + 0.5);
        final int j = 63;
        byte b3;
        if (f >= -63.0f && f2 >= -63.0f && f <= 63.0f && f2 <= 63.0f) {
            p_191095_8_ += ((p_191095_8_ < 0.0) ? -8.0 : 8.0);
            b3 = (byte)(p_191095_8_ * 16.0 / 360.0);
            if (this.dimension < 0) {
                final int l = (int)(p_191095_2_.getWorldInfo().getWorldTime() / 10L);
                b3 = (byte)(l * l * 34187121 + l * 121 >> 15 & 0xF);
            }
        }
        else {
            if (p_191095_1_ != MapDecoration.Type.PLAYER) {
                this.mapDecorations.remove(p_191095_3_);
                return;
            }
            final int k = 320;
            if (Math.abs(f) < 320.0f && Math.abs(f2) < 320.0f) {
                p_191095_1_ = MapDecoration.Type.PLAYER_OFF_MAP;
            }
            else {
                if (!this.field_191096_f) {
                    this.mapDecorations.remove(p_191095_3_);
                    return;
                }
                p_191095_1_ = MapDecoration.Type.PLAYER_OFF_LIMITS;
            }
            b3 = 0;
            if (f <= -63.0f) {
                b0 = -128;
            }
            if (f2 <= -63.0f) {
                b2 = -128;
            }
            if (f >= 63.0f) {
                b0 = 127;
            }
            if (f2 >= 63.0f) {
                b2 = 127;
            }
        }
        this.mapDecorations.put(p_191095_3_, new MapDecoration(p_191095_1_, b0, b2, b3));
    }
    
    @Nullable
    public Packet<?> getMapPacket(final ItemStack mapStack, final World worldIn, final EntityPlayer player) {
        final MapInfo mapdata$mapinfo = this.playersHashMap.get(player);
        return (mapdata$mapinfo == null) ? null : mapdata$mapinfo.getPacket(mapStack);
    }
    
    public void updateMapData(final int x, final int y) {
        super.markDirty();
        for (final MapInfo mapdata$mapinfo : this.playersArrayList) {
            mapdata$mapinfo.update(x, y);
        }
    }
    
    public MapInfo getMapInfo(final EntityPlayer player) {
        MapInfo mapdata$mapinfo = this.playersHashMap.get(player);
        if (mapdata$mapinfo == null) {
            mapdata$mapinfo = new MapInfo(player);
            this.playersHashMap.put(player, mapdata$mapinfo);
            this.playersArrayList.add(mapdata$mapinfo);
        }
        return mapdata$mapinfo;
    }
    
    public class MapInfo
    {
        public final EntityPlayer entityplayerObj;
        private boolean isDirty;
        private int minX;
        private int minY;
        private int maxX;
        private int maxY;
        private int tick;
        public int step;
        
        public MapInfo(final EntityPlayer player) {
            this.isDirty = true;
            this.maxX = 127;
            this.maxY = 127;
            this.entityplayerObj = player;
        }
        
        @Nullable
        public Packet<?> getPacket(final ItemStack stack) {
            if (this.isDirty) {
                this.isDirty = false;
                return new SPacketMaps(stack.getMetadata(), MapData.this.scale, MapData.this.trackingPosition, MapData.this.mapDecorations.values(), MapData.this.colors, this.minX, this.minY, this.maxX + 1 - this.minX, this.maxY + 1 - this.minY);
            }
            return (this.tick++ % 5 == 0) ? new SPacketMaps(stack.getMetadata(), MapData.this.scale, MapData.this.trackingPosition, MapData.this.mapDecorations.values(), MapData.this.colors, 0, 0, 0, 0) : null;
        }
        
        public void update(final int x, final int y) {
            if (this.isDirty) {
                this.minX = Math.min(this.minX, x);
                this.minY = Math.min(this.minY, y);
                this.maxX = Math.max(this.maxX, x);
                this.maxY = Math.max(this.maxY, y);
            }
            else {
                this.isDirty = true;
                this.minX = x;
                this.minY = y;
                this.maxX = x;
                this.maxY = y;
            }
        }
    }
}
