// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network;

import net.minecraft.network.play.client.CPacketPlayerBlockPlacement;
import net.minecraft.network.play.client.CPacketRecipePlacement;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.Rotation;
import net.minecraft.util.Mirror;
import net.minecraft.util.math.MathHelper;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.init.Blocks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.item.ItemWrittenBook;
import java.io.IOException;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketClientSettings;
import java.util.Iterator;
import java.util.List;
import net.minecraft.network.play.server.SPacketTabComplete;
import com.google.common.collect.Lists;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketEnchantItem;
import net.minecraft.network.play.client.CPacketPlaceRecipe;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.inventory.Slot;
import net.minecraft.util.NonNullList;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.world.DimensionType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.item.ItemElytra;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatAllowedCharacters;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.client.CPacketSpectate;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.util.EnumHand;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import java.util.Set;
import java.util.Collections;
import net.minecraft.init.MobEffects;
import net.minecraft.world.GameType;
import net.minecraft.advancements.Advancement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.network.play.client.CPacketSeenAdvancements;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.MoverType;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.client.CPacketVehicleMove;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Doubles;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.network.play.client.CPacketInput;
import com.google.common.util.concurrent.Futures;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.network.play.server.SPacketKeepAlive;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.ServerRecipeBookHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.util.IntHashMap;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;
import net.minecraft.util.ITickable;
import net.minecraft.network.play.INetHandlerPlayServer;

public class NetHandlerPlayServer implements INetHandlerPlayServer, ITickable
{
    private static final Logger LOGGER;
    public final NetworkManager netManager;
    private final MinecraftServer serverController;
    public EntityPlayerMP playerEntity;
    private int networkTickCount;
    private long field_194402_f;
    private boolean field_194403_g;
    private long field_194404_h;
    private int chatSpamThresholdCount;
    private int itemDropThreshold;
    private final IntHashMap<Short> pendingTransactions;
    private double firstGoodX;
    private double firstGoodY;
    private double firstGoodZ;
    private double lastGoodX;
    private double lastGoodY;
    private double lastGoodZ;
    private Entity lowestRiddenEnt;
    private double lowestRiddenX;
    private double lowestRiddenY;
    private double lowestRiddenZ;
    private double lowestRiddenX1;
    private double lowestRiddenY1;
    private double lowestRiddenZ1;
    private Vec3d targetPos;
    private int teleportId;
    private int lastPositionUpdate;
    private boolean floating;
    private int floatingTickCount;
    private boolean vehicleFloating;
    private int vehicleFloatingTickCount;
    private int movePacketCounter;
    private int lastMovePacketCounter;
    private ServerRecipeBookHelper field_194309_H;
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public NetHandlerPlayServer(final MinecraftServer server, final NetworkManager networkManagerIn, final EntityPlayerMP playerIn) {
        this.pendingTransactions = new IntHashMap<Short>();
        this.field_194309_H = new ServerRecipeBookHelper();
        this.serverController = server;
        (this.netManager = networkManagerIn).setNetHandler(this);
        this.playerEntity = playerIn;
        playerIn.connection = this;
    }
    
    @Override
    public void update() {
        this.captureCurrentPosition();
        this.playerEntity.onUpdateEntity();
        this.playerEntity.setPositionAndRotation(this.firstGoodX, this.firstGoodY, this.firstGoodZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
        ++this.networkTickCount;
        this.lastMovePacketCounter = this.movePacketCounter;
        if (this.floating) {
            if (++this.floatingTickCount > 80) {
                NetHandlerPlayServer.LOGGER.warn("{} was kicked for floating too long!", this.playerEntity.getName());
                this.func_194028_b(new TextComponentTranslation("multiplayer.disconnect.flying", new Object[0]));
                return;
            }
        }
        else {
            this.floating = false;
            this.floatingTickCount = 0;
        }
        this.lowestRiddenEnt = this.playerEntity.getLowestRidingEntity();
        if (this.lowestRiddenEnt != this.playerEntity && this.lowestRiddenEnt.getControllingPassenger() == this.playerEntity) {
            this.lowestRiddenX = this.lowestRiddenEnt.posX;
            this.lowestRiddenY = this.lowestRiddenEnt.posY;
            this.lowestRiddenZ = this.lowestRiddenEnt.posZ;
            this.lowestRiddenX1 = this.lowestRiddenEnt.posX;
            this.lowestRiddenY1 = this.lowestRiddenEnt.posY;
            this.lowestRiddenZ1 = this.lowestRiddenEnt.posZ;
            if (this.vehicleFloating && this.playerEntity.getLowestRidingEntity().getControllingPassenger() == this.playerEntity) {
                if (++this.vehicleFloatingTickCount > 80) {
                    NetHandlerPlayServer.LOGGER.warn("{} was kicked for floating a vehicle too long!", this.playerEntity.getName());
                    this.func_194028_b(new TextComponentTranslation("multiplayer.disconnect.flying", new Object[0]));
                    return;
                }
            }
            else {
                this.vehicleFloating = false;
                this.vehicleFloatingTickCount = 0;
            }
        }
        else {
            this.lowestRiddenEnt = null;
            this.vehicleFloating = false;
            this.vehicleFloatingTickCount = 0;
        }
        this.serverController.theProfiler.startSection("keepAlive");
        final long i = this.currentTimeMillis();
        if (i - this.field_194402_f >= 15000L) {
            if (this.field_194403_g) {
                this.func_194028_b(new TextComponentTranslation("disconnect.timeout", new Object[0]));
            }
            else {
                this.field_194403_g = true;
                this.field_194402_f = i;
                this.field_194404_h = i;
                this.sendPacket(new SPacketKeepAlive(this.field_194404_h));
            }
        }
        this.serverController.theProfiler.endSection();
        if (this.chatSpamThresholdCount > 0) {
            --this.chatSpamThresholdCount;
        }
        if (this.itemDropThreshold > 0) {
            --this.itemDropThreshold;
        }
        if (this.playerEntity.getLastActiveTime() > 0L && this.serverController.getMaxPlayerIdleMinutes() > 0 && MinecraftServer.getCurrentTimeMillis() - this.playerEntity.getLastActiveTime() > this.serverController.getMaxPlayerIdleMinutes() * 1000 * 60) {
            this.func_194028_b(new TextComponentTranslation("multiplayer.disconnect.idling", new Object[0]));
        }
    }
    
    private void captureCurrentPosition() {
        this.firstGoodX = this.playerEntity.posX;
        this.firstGoodY = this.playerEntity.posY;
        this.firstGoodZ = this.playerEntity.posZ;
        this.lastGoodX = this.playerEntity.posX;
        this.lastGoodY = this.playerEntity.posY;
        this.lastGoodZ = this.playerEntity.posZ;
    }
    
    public NetworkManager getNetworkManager() {
        return this.netManager;
    }
    
    public void func_194028_b(final ITextComponent p_194028_1_) {
        this.netManager.sendPacket(new SPacketDisconnect(p_194028_1_), new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(final Future<? super Void> p_operationComplete_1_) throws Exception {
                NetHandlerPlayServer.this.netManager.closeChannel(p_194028_1_);
            }
        }, (GenericFutureListener<? extends Future<? super Void>>[])new GenericFutureListener[0]);
        this.netManager.disableAutoRead();
        Futures.getUnchecked(this.serverController.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                NetHandlerPlayServer.this.netManager.checkDisconnected();
            }
        }));
    }
    
    @Override
    public void processInput(final CPacketInput packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        this.playerEntity.setEntityActionState(packetIn.getStrafeSpeed(), packetIn.func_192620_b(), packetIn.isJumping(), packetIn.isSneaking());
    }
    
    private static boolean isMovePlayerPacketInvalid(final CPacketPlayer packetIn) {
        return !Doubles.isFinite(packetIn.getX(0.0)) || !Doubles.isFinite(packetIn.getY(0.0)) || !Doubles.isFinite(packetIn.getZ(0.0)) || !Floats.isFinite(packetIn.getPitch(0.0f)) || !Floats.isFinite(packetIn.getYaw(0.0f)) || Math.abs(packetIn.getX(0.0)) > 3.0E7 || Math.abs(packetIn.getY(0.0)) > 3.0E7 || Math.abs(packetIn.getZ(0.0)) > 3.0E7;
    }
    
    private static boolean isMoveVehiclePacketInvalid(final CPacketVehicleMove packetIn) {
        return !Doubles.isFinite(packetIn.getX()) || !Doubles.isFinite(packetIn.getY()) || !Doubles.isFinite(packetIn.getZ()) || !Floats.isFinite(packetIn.getPitch()) || !Floats.isFinite(packetIn.getYaw());
    }
    
    @Override
    public void processVehicleMove(final CPacketVehicleMove packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        if (isMoveVehiclePacketInvalid(packetIn)) {
            this.func_194028_b(new TextComponentTranslation("multiplayer.disconnect.invalid_vehicle_movement", new Object[0]));
        }
        else {
            final Entity entity = this.playerEntity.getLowestRidingEntity();
            if (entity != this.playerEntity && entity.getControllingPassenger() == this.playerEntity && entity == this.lowestRiddenEnt) {
                final WorldServer worldserver = this.playerEntity.getServerWorld();
                final double d0 = entity.posX;
                final double d2 = entity.posY;
                final double d3 = entity.posZ;
                final double d4 = packetIn.getX();
                final double d5 = packetIn.getY();
                final double d6 = packetIn.getZ();
                final float f = packetIn.getYaw();
                final float f2 = packetIn.getPitch();
                double d7 = d4 - this.lowestRiddenX;
                double d8 = d5 - this.lowestRiddenY;
                double d9 = d6 - this.lowestRiddenZ;
                final double d10 = entity.motionX * entity.motionX + entity.motionY * entity.motionY + entity.motionZ * entity.motionZ;
                double d11 = d7 * d7 + d8 * d8 + d9 * d9;
                if (d11 - d10 > 100.0 && (!this.serverController.isSinglePlayer() || !this.serverController.getServerOwner().equals(entity.getName()))) {
                    NetHandlerPlayServer.LOGGER.warn("{} (vehicle of {}) moved too quickly! {},{},{}", entity.getName(), this.playerEntity.getName(), d7, d8, d9);
                    this.netManager.sendPacket(new SPacketMoveVehicle(entity));
                    return;
                }
                final boolean flag = worldserver.getCollisionBoxes(entity, entity.getEntityBoundingBox().contract(0.0625)).isEmpty();
                d7 = d4 - this.lowestRiddenX1;
                d8 = d5 - this.lowestRiddenY1 - 1.0E-6;
                d9 = d6 - this.lowestRiddenZ1;
                entity.moveEntity(MoverType.PLAYER, d7, d8, d9);
                final double d12 = d8;
                d7 = d4 - entity.posX;
                d8 = d5 - entity.posY;
                if (d8 > -0.5 || d8 < 0.5) {
                    d8 = 0.0;
                }
                d9 = d6 - entity.posZ;
                d11 = d7 * d7 + d8 * d8 + d9 * d9;
                boolean flag2 = false;
                if (d11 > 0.0625) {
                    flag2 = true;
                    NetHandlerPlayServer.LOGGER.warn("{} moved wrongly!", entity.getName());
                }
                entity.setPositionAndRotation(d4, d5, d6, f, f2);
                final boolean flag3 = worldserver.getCollisionBoxes(entity, entity.getEntityBoundingBox().contract(0.0625)).isEmpty();
                if (flag && (flag2 || !flag3)) {
                    entity.setPositionAndRotation(d0, d2, d3, f, f2);
                    this.netManager.sendPacket(new SPacketMoveVehicle(entity));
                    return;
                }
                this.serverController.getPlayerList().serverUpdateMovingPlayer(this.playerEntity);
                this.playerEntity.addMovementStat(this.playerEntity.posX - d0, this.playerEntity.posY - d2, this.playerEntity.posZ - d3);
                this.vehicleFloating = (d12 >= -0.03125 && !this.serverController.isFlightAllowed() && !worldserver.checkBlockCollision(entity.getEntityBoundingBox().expandXyz(0.0625).addCoord(0.0, -0.55, 0.0)));
                this.lowestRiddenX1 = entity.posX;
                this.lowestRiddenY1 = entity.posY;
                this.lowestRiddenZ1 = entity.posZ;
            }
        }
    }
    
    @Override
    public void processConfirmTeleport(final CPacketConfirmTeleport packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        if (packetIn.getTeleportId() == this.teleportId) {
            this.playerEntity.setPositionAndRotation(this.targetPos.xCoord, this.targetPos.yCoord, this.targetPos.zCoord, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
            if (this.playerEntity.isInvulnerableDimensionChange()) {
                this.lastGoodX = this.targetPos.xCoord;
                this.lastGoodY = this.targetPos.yCoord;
                this.lastGoodZ = this.targetPos.zCoord;
                this.playerEntity.clearInvulnerableDimensionChange();
            }
            this.targetPos = null;
        }
    }
    
    @Override
    public void func_191984_a(final CPacketRecipeInfo p_191984_1_) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)p_191984_1_, this, this.playerEntity.getServerWorld());
        if (p_191984_1_.func_194156_a() == CPacketRecipeInfo.Purpose.SHOWN) {
            this.playerEntity.func_192037_E().func_194074_f(p_191984_1_.func_193648_b());
        }
        else if (p_191984_1_.func_194156_a() == CPacketRecipeInfo.Purpose.SETTINGS) {
            this.playerEntity.func_192037_E().func_192813_a(p_191984_1_.func_192624_c());
            this.playerEntity.func_192037_E().func_192810_b(p_191984_1_.func_192625_d());
        }
    }
    
    @Override
    public void func_194027_a(final CPacketSeenAdvancements p_194027_1_) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)p_194027_1_, this, this.playerEntity.getServerWorld());
        if (p_194027_1_.func_194162_b() == CPacketSeenAdvancements.Action.OPENED_TAB) {
            final ResourceLocation resourcelocation = p_194027_1_.func_194165_c();
            final Advancement advancement = this.serverController.func_191949_aK().func_192778_a(resourcelocation);
            if (advancement != null) {
                this.playerEntity.func_192039_O().func_194220_a(advancement);
            }
        }
    }
    
    @Override
    public void processPlayer(final CPacketPlayer packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        if (isMovePlayerPacketInvalid(packetIn)) {
            this.func_194028_b(new TextComponentTranslation("multiplayer.disconnect.invalid_player_movement", new Object[0]));
        }
        else {
            final WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
            if (!this.playerEntity.playerConqueredTheEnd) {
                if (this.networkTickCount == 0) {
                    this.captureCurrentPosition();
                }
                if (this.targetPos != null) {
                    if (this.networkTickCount - this.lastPositionUpdate > 20) {
                        this.lastPositionUpdate = this.networkTickCount;
                        this.setPlayerLocation(this.targetPos.xCoord, this.targetPos.yCoord, this.targetPos.zCoord, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
                    }
                }
                else {
                    this.lastPositionUpdate = this.networkTickCount;
                    if (this.playerEntity.isRiding()) {
                        this.playerEntity.setPositionAndRotation(this.playerEntity.posX, this.playerEntity.posY, this.playerEntity.posZ, packetIn.getYaw(this.playerEntity.rotationYaw), packetIn.getPitch(this.playerEntity.rotationPitch));
                        this.serverController.getPlayerList().serverUpdateMovingPlayer(this.playerEntity);
                    }
                    else {
                        final double d0 = this.playerEntity.posX;
                        final double d2 = this.playerEntity.posY;
                        final double d3 = this.playerEntity.posZ;
                        final double d4 = this.playerEntity.posY;
                        final double d5 = packetIn.getX(this.playerEntity.posX);
                        final double d6 = packetIn.getY(this.playerEntity.posY);
                        final double d7 = packetIn.getZ(this.playerEntity.posZ);
                        final float f = packetIn.getYaw(this.playerEntity.rotationYaw);
                        final float f2 = packetIn.getPitch(this.playerEntity.rotationPitch);
                        double d8 = d5 - this.firstGoodX;
                        double d9 = d6 - this.firstGoodY;
                        double d10 = d7 - this.firstGoodZ;
                        final double d11 = this.playerEntity.motionX * this.playerEntity.motionX + this.playerEntity.motionY * this.playerEntity.motionY + this.playerEntity.motionZ * this.playerEntity.motionZ;
                        double d12 = d8 * d8 + d9 * d9 + d10 * d10;
                        if (this.playerEntity.isPlayerSleeping()) {
                            if (d12 > 1.0) {
                                this.setPlayerLocation(this.playerEntity.posX, this.playerEntity.posY, this.playerEntity.posZ, packetIn.getYaw(this.playerEntity.rotationYaw), packetIn.getPitch(this.playerEntity.rotationPitch));
                            }
                        }
                        else {
                            ++this.movePacketCounter;
                            int i = this.movePacketCounter - this.lastMovePacketCounter;
                            if (i > 5) {
                                NetHandlerPlayServer.LOGGER.debug("{} is sending move packets too frequently ({} packets since last tick)", this.playerEntity.getName(), i);
                                i = 1;
                            }
                            if (!this.playerEntity.isInvulnerableDimensionChange() && (!this.playerEntity.getServerWorld().getGameRules().getBoolean("disableElytraMovementCheck") || !this.playerEntity.isElytraFlying())) {
                                final float f3 = this.playerEntity.isElytraFlying() ? 300.0f : 100.0f;
                                if (d12 - d11 > f3 * i && (!this.serverController.isSinglePlayer() || !this.serverController.getServerOwner().equals(this.playerEntity.getName()))) {
                                    NetHandlerPlayServer.LOGGER.warn("{} moved too quickly! {},{},{}", this.playerEntity.getName(), d8, d9, d10);
                                    this.setPlayerLocation(this.playerEntity.posX, this.playerEntity.posY, this.playerEntity.posZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
                                    return;
                                }
                            }
                            final boolean flag2 = worldserver.getCollisionBoxes(this.playerEntity, this.playerEntity.getEntityBoundingBox().contract(0.0625)).isEmpty();
                            d8 = d5 - this.lastGoodX;
                            d9 = d6 - this.lastGoodY;
                            d10 = d7 - this.lastGoodZ;
                            if (this.playerEntity.onGround && !packetIn.isOnGround() && d9 > 0.0) {
                                this.playerEntity.jump();
                            }
                            this.playerEntity.moveEntity(MoverType.PLAYER, d8, d9, d10);
                            this.playerEntity.onGround = packetIn.isOnGround();
                            final double d13 = d9;
                            d8 = d5 - this.playerEntity.posX;
                            d9 = d6 - this.playerEntity.posY;
                            if (d9 > -0.5 || d9 < 0.5) {
                                d9 = 0.0;
                            }
                            d10 = d7 - this.playerEntity.posZ;
                            d12 = d8 * d8 + d9 * d9 + d10 * d10;
                            boolean flag3 = false;
                            if (!this.playerEntity.isInvulnerableDimensionChange() && d12 > 0.0625 && !this.playerEntity.isPlayerSleeping() && !this.playerEntity.interactionManager.isCreative() && this.playerEntity.interactionManager.getGameType() != GameType.SPECTATOR) {
                                flag3 = true;
                                NetHandlerPlayServer.LOGGER.warn("{} moved wrongly!", this.playerEntity.getName());
                            }
                            this.playerEntity.setPositionAndRotation(d5, d6, d7, f, f2);
                            this.playerEntity.addMovementStat(this.playerEntity.posX - d0, this.playerEntity.posY - d2, this.playerEntity.posZ - d3);
                            if (!this.playerEntity.noClip && !this.playerEntity.isPlayerSleeping()) {
                                final boolean flag4 = worldserver.getCollisionBoxes(this.playerEntity, this.playerEntity.getEntityBoundingBox().contract(0.0625)).isEmpty();
                                if (flag2 && (flag3 || !flag4)) {
                                    this.setPlayerLocation(d0, d2, d3, f, f2);
                                    return;
                                }
                            }
                            this.floating = (d13 >= -0.03125);
                            this.floating &= (!this.serverController.isFlightAllowed() && !this.playerEntity.capabilities.allowFlying);
                            this.floating &= (!this.playerEntity.isPotionActive(MobEffects.LEVITATION) && !this.playerEntity.isElytraFlying() && !worldserver.checkBlockCollision(this.playerEntity.getEntityBoundingBox().expandXyz(0.0625).addCoord(0.0, -0.55, 0.0)));
                            this.playerEntity.onGround = packetIn.isOnGround();
                            this.serverController.getPlayerList().serverUpdateMovingPlayer(this.playerEntity);
                            this.playerEntity.handleFalling(this.playerEntity.posY - d4, packetIn.isOnGround());
                            this.lastGoodX = this.playerEntity.posX;
                            this.lastGoodY = this.playerEntity.posY;
                            this.lastGoodZ = this.playerEntity.posZ;
                        }
                    }
                }
            }
        }
    }
    
    public void setPlayerLocation(final double x, final double y, final double z, final float yaw, final float pitch) {
        this.setPlayerLocation(x, y, z, yaw, pitch, Collections.emptySet());
    }
    
    public void setPlayerLocation(final double x, final double y, final double z, final float yaw, final float pitch, final Set<SPacketPlayerPosLook.EnumFlags> relativeSet) {
        final double d0 = relativeSet.contains(SPacketPlayerPosLook.EnumFlags.X) ? this.playerEntity.posX : 0.0;
        final double d2 = relativeSet.contains(SPacketPlayerPosLook.EnumFlags.Y) ? this.playerEntity.posY : 0.0;
        final double d3 = relativeSet.contains(SPacketPlayerPosLook.EnumFlags.Z) ? this.playerEntity.posZ : 0.0;
        this.targetPos = new Vec3d(x + d0, y + d2, z + d3);
        float f = yaw;
        float f2 = pitch;
        if (relativeSet.contains(SPacketPlayerPosLook.EnumFlags.Y_ROT)) {
            f = yaw + this.playerEntity.rotationYaw;
        }
        if (relativeSet.contains(SPacketPlayerPosLook.EnumFlags.X_ROT)) {
            f2 = pitch + this.playerEntity.rotationPitch;
        }
        if (++this.teleportId == Integer.MAX_VALUE) {
            this.teleportId = 0;
        }
        this.lastPositionUpdate = this.networkTickCount;
        this.playerEntity.setPositionAndRotation(this.targetPos.xCoord, this.targetPos.yCoord, this.targetPos.zCoord, f, f2);
        this.playerEntity.connection.sendPacket(new SPacketPlayerPosLook(x, y, z, yaw, pitch, relativeSet, this.teleportId));
    }
    
    @Override
    public void processPlayerDigging(final CPacketPlayerDigging packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        final WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        final BlockPos blockpos = packetIn.getPosition();
        this.playerEntity.markPlayerActive();
        switch (packetIn.getAction()) {
            case SWAP_HELD_ITEMS: {
                if (!this.playerEntity.isSpectator()) {
                    final ItemStack itemstack = this.playerEntity.getHeldItem(EnumHand.OFF_HAND);
                    this.playerEntity.setHeldItem(EnumHand.OFF_HAND, this.playerEntity.getHeldItem(EnumHand.MAIN_HAND));
                    this.playerEntity.setHeldItem(EnumHand.MAIN_HAND, itemstack);
                }
                return;
            }
            case DROP_ITEM: {
                if (!this.playerEntity.isSpectator()) {
                    this.playerEntity.dropItem(false);
                }
                return;
            }
            case DROP_ALL_ITEMS: {
                if (!this.playerEntity.isSpectator()) {
                    this.playerEntity.dropItem(true);
                }
                return;
            }
            case RELEASE_USE_ITEM: {
                this.playerEntity.stopActiveHand();
                return;
            }
            case START_DESTROY_BLOCK:
            case ABORT_DESTROY_BLOCK:
            case STOP_DESTROY_BLOCK: {
                final double d0 = this.playerEntity.posX - (blockpos.getX() + 0.5);
                final double d2 = this.playerEntity.posY - (blockpos.getY() + 0.5) + 1.5;
                final double d3 = this.playerEntity.posZ - (blockpos.getZ() + 0.5);
                final double d4 = d0 * d0 + d2 * d2 + d3 * d3;
                if (d4 > 36.0) {
                    return;
                }
                if (blockpos.getY() >= this.serverController.getBuildLimit()) {
                    return;
                }
                if (packetIn.getAction() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                    if (!this.serverController.isBlockProtected(worldserver, blockpos, this.playerEntity) && worldserver.getWorldBorder().contains(blockpos)) {
                        this.playerEntity.interactionManager.onBlockClicked(blockpos, packetIn.getFacing());
                    }
                    else {
                        this.playerEntity.connection.sendPacket(new SPacketBlockChange(worldserver, blockpos));
                    }
                }
                else {
                    if (packetIn.getAction() == CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                        this.playerEntity.interactionManager.blockRemoving(blockpos);
                    }
                    else if (packetIn.getAction() == CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK) {
                        this.playerEntity.interactionManager.cancelDestroyingBlock();
                    }
                    if (worldserver.getBlockState(blockpos).getMaterial() != Material.AIR) {
                        this.playerEntity.connection.sendPacket(new SPacketBlockChange(worldserver, blockpos));
                    }
                }
                return;
            }
            default: {
                throw new IllegalArgumentException("Invalid player action");
            }
        }
    }
    
    @Override
    public void processRightClickBlock(final CPacketPlayerTryUseItemOnBlock packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        final WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        final EnumHand enumhand = packetIn.getHand();
        final ItemStack itemstack = this.playerEntity.getHeldItem(enumhand);
        final BlockPos blockpos = packetIn.getPos();
        final EnumFacing enumfacing = packetIn.getDirection();
        this.playerEntity.markPlayerActive();
        if (blockpos.getY() < this.serverController.getBuildLimit() - 1 || (enumfacing != EnumFacing.UP && blockpos.getY() < this.serverController.getBuildLimit())) {
            if (this.targetPos == null && this.playerEntity.getDistanceSq(blockpos.getX() + 0.5, blockpos.getY() + 0.5, blockpos.getZ() + 0.5) < 64.0 && !this.serverController.isBlockProtected(worldserver, blockpos, this.playerEntity) && worldserver.getWorldBorder().contains(blockpos)) {
                this.playerEntity.interactionManager.processRightClickBlock(this.playerEntity, worldserver, itemstack, enumhand, blockpos, enumfacing, packetIn.getFacingX(), packetIn.getFacingY(), packetIn.getFacingZ());
            }
        }
        else {
            final TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("build.tooHigh", new Object[] { this.serverController.getBuildLimit() });
            textcomponenttranslation.getStyle().setColor(TextFormatting.RED);
            this.playerEntity.connection.sendPacket(new SPacketChat(textcomponenttranslation, ChatType.GAME_INFO));
        }
        this.playerEntity.connection.sendPacket(new SPacketBlockChange(worldserver, blockpos));
        this.playerEntity.connection.sendPacket(new SPacketBlockChange(worldserver, blockpos.offset(enumfacing)));
    }
    
    @Override
    public void processPlayerBlockPlacement(final CPacketPlayerTryUseItem packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        final WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        final EnumHand enumhand = packetIn.getHand();
        final ItemStack itemstack = this.playerEntity.getHeldItem(enumhand);
        this.playerEntity.markPlayerActive();
        if (!itemstack.func_190926_b()) {
            this.playerEntity.interactionManager.processRightClick(this.playerEntity, worldserver, itemstack, enumhand);
        }
    }
    
    @Override
    public void handleSpectate(final CPacketSpectate packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        if (this.playerEntity.isSpectator()) {
            Entity entity = null;
            WorldServer[] worldServers;
            for (int length = (worldServers = this.serverController.worldServers).length, i = 0; i < length; ++i) {
                final WorldServer worldserver = worldServers[i];
                if (worldserver != null) {
                    entity = packetIn.getEntity(worldserver);
                    if (entity != null) {
                        break;
                    }
                }
            }
            if (entity != null) {
                this.playerEntity.setSpectatingEntity(this.playerEntity);
                this.playerEntity.dismountRidingEntity();
                if (entity.world == this.playerEntity.world) {
                    this.playerEntity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);
                }
                else {
                    final WorldServer worldserver2 = this.playerEntity.getServerWorld();
                    final WorldServer worldserver3 = (WorldServer)entity.world;
                    this.playerEntity.dimension = entity.dimension;
                    this.sendPacket(new SPacketRespawn(this.playerEntity.dimension, worldserver2.getDifficulty(), worldserver2.getWorldInfo().getTerrainType(), this.playerEntity.interactionManager.getGameType()));
                    this.serverController.getPlayerList().updatePermissionLevel(this.playerEntity);
                    worldserver2.removeEntityDangerously(this.playerEntity);
                    this.playerEntity.isDead = false;
                    this.playerEntity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
                    if (this.playerEntity.isEntityAlive()) {
                        worldserver2.updateEntityWithOptionalForce(this.playerEntity, false);
                        worldserver3.spawnEntityInWorld(this.playerEntity);
                        worldserver3.updateEntityWithOptionalForce(this.playerEntity, false);
                    }
                    this.playerEntity.setWorld(worldserver3);
                    this.serverController.getPlayerList().preparePlayer(this.playerEntity, worldserver2);
                    this.playerEntity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);
                    this.playerEntity.interactionManager.setWorld(worldserver3);
                    this.serverController.getPlayerList().updateTimeAndWeatherForPlayer(this.playerEntity, worldserver3);
                    this.serverController.getPlayerList().syncPlayerInventory(this.playerEntity);
                }
            }
        }
    }
    
    @Override
    public void handleResourcePackStatus(final CPacketResourcePackStatus packetIn) {
    }
    
    @Override
    public void processSteerBoat(final CPacketSteerBoat packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        final Entity entity = this.playerEntity.getRidingEntity();
        if (entity instanceof EntityBoat) {
            ((EntityBoat)entity).setPaddleState(packetIn.getLeft(), packetIn.getRight());
        }
    }
    
    @Override
    public void onDisconnect(final ITextComponent reason) {
        NetHandlerPlayServer.LOGGER.info("{} lost connection: {}", this.playerEntity.getName(), reason.getUnformattedText());
        this.serverController.refreshStatusNextTick();
        final TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("multiplayer.player.left", new Object[] { this.playerEntity.getDisplayName() });
        textcomponenttranslation.getStyle().setColor(TextFormatting.YELLOW);
        this.serverController.getPlayerList().sendChatMsg(textcomponenttranslation);
        this.playerEntity.mountEntityAndWakeUp();
        this.serverController.getPlayerList().playerLoggedOut(this.playerEntity);
        if (this.serverController.isSinglePlayer() && this.playerEntity.getName().equals(this.serverController.getServerOwner())) {
            NetHandlerPlayServer.LOGGER.info("Stopping singleplayer server as player logged out");
            this.serverController.initiateShutdown();
        }
    }
    
    public void sendPacket(final Packet<?> packetIn) {
        if (packetIn instanceof SPacketChat) {
            final SPacketChat spacketchat = (SPacketChat)packetIn;
            final EntityPlayer.EnumChatVisibility entityplayer$enumchatvisibility = this.playerEntity.getChatVisibility();
            if (entityplayer$enumchatvisibility == EntityPlayer.EnumChatVisibility.HIDDEN && spacketchat.func_192590_c() != ChatType.GAME_INFO) {
                return;
            }
            if (entityplayer$enumchatvisibility == EntityPlayer.EnumChatVisibility.SYSTEM && !spacketchat.isSystem()) {
                return;
            }
        }
        try {
            this.netManager.sendPacket(packetIn);
        }
        catch (final Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Sending packet");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Packet being sent");
            crashreportcategory.setDetail("Packet class", new ICrashReportDetail<String>() {
                @Override
                public String call() throws Exception {
                    return packetIn.getClass().getCanonicalName();
                }
            });
            throw new ReportedException(crashreport);
        }
    }
    
    @Override
    public void processHeldItemChange(final CPacketHeldItemChange packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        if (packetIn.getSlotId() >= 0 && packetIn.getSlotId() < InventoryPlayer.getHotbarSize()) {
            this.playerEntity.inventory.currentItem = packetIn.getSlotId();
            this.playerEntity.markPlayerActive();
        }
        else {
            NetHandlerPlayServer.LOGGER.warn("{} tried to set an invalid carried item", this.playerEntity.getName());
        }
    }
    
    @Override
    public void processChatMessage(final CPacketChatMessage packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        if (this.playerEntity.getChatVisibility() == EntityPlayer.EnumChatVisibility.HIDDEN) {
            final TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("chat.cannotSend", new Object[0]);
            textcomponenttranslation.getStyle().setColor(TextFormatting.RED);
            this.sendPacket(new SPacketChat(textcomponenttranslation));
        }
        else {
            this.playerEntity.markPlayerActive();
            String s = packetIn.getMessage();
            s = StringUtils.normalizeSpace(s);
            for (int i = 0; i < s.length(); ++i) {
                if (!ChatAllowedCharacters.isAllowedCharacter(s.charAt(i))) {
                    this.func_194028_b(new TextComponentTranslation("multiplayer.disconnect.illegal_characters", new Object[0]));
                    return;
                }
            }
            if (s.startsWith("/")) {
                this.handleSlashCommand(s);
            }
            else {
                final ITextComponent itextcomponent = new TextComponentTranslation("chat.type.text", new Object[] { this.playerEntity.getDisplayName(), s });
                this.serverController.getPlayerList().sendChatMsgImpl(itextcomponent, false);
            }
            this.chatSpamThresholdCount += 20;
            if (this.chatSpamThresholdCount > 200 && !this.serverController.getPlayerList().canSendCommands(this.playerEntity.getGameProfile())) {
                this.func_194028_b(new TextComponentTranslation("disconnect.spam", new Object[0]));
            }
        }
    }
    
    private void handleSlashCommand(final String command) {
        this.serverController.getCommandManager().executeCommand(this.playerEntity, command);
    }
    
    @Override
    public void handleAnimation(final CPacketAnimation packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        this.playerEntity.markPlayerActive();
        this.playerEntity.swingArm(packetIn.getHand());
    }
    
    @Override
    public void processEntityAction(final CPacketEntityAction packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        this.playerEntity.markPlayerActive();
        switch (packetIn.getAction()) {
            case START_SNEAKING: {
                this.playerEntity.setSneaking(true);
                break;
            }
            case STOP_SNEAKING: {
                this.playerEntity.setSneaking(false);
                break;
            }
            case START_SPRINTING: {
                this.playerEntity.setSprinting(true);
                break;
            }
            case STOP_SPRINTING: {
                this.playerEntity.setSprinting(false);
                break;
            }
            case STOP_SLEEPING: {
                if (this.playerEntity.isPlayerSleeping()) {
                    this.playerEntity.wakeUpPlayer(false, true, true);
                    this.targetPos = new Vec3d(this.playerEntity.posX, this.playerEntity.posY, this.playerEntity.posZ);
                    break;
                }
                break;
            }
            case START_RIDING_JUMP: {
                if (!(this.playerEntity.getRidingEntity() instanceof IJumpingMount)) {
                    break;
                }
                final IJumpingMount ijumpingmount1 = (IJumpingMount)this.playerEntity.getRidingEntity();
                final int i = packetIn.getAuxData();
                if (ijumpingmount1.canJump() && i > 0) {
                    ijumpingmount1.handleStartJump(i);
                    break;
                }
                break;
            }
            case STOP_RIDING_JUMP: {
                if (this.playerEntity.getRidingEntity() instanceof IJumpingMount) {
                    final IJumpingMount ijumpingmount2 = (IJumpingMount)this.playerEntity.getRidingEntity();
                    ijumpingmount2.handleStopJump();
                    break;
                }
                break;
            }
            case OPEN_INVENTORY: {
                if (this.playerEntity.getRidingEntity() instanceof AbstractHorse) {
                    ((AbstractHorse)this.playerEntity.getRidingEntity()).openGUI(this.playerEntity);
                    break;
                }
                break;
            }
            case START_FALL_FLYING: {
                if (this.playerEntity.onGround || this.playerEntity.motionY >= 0.0 || this.playerEntity.isElytraFlying() || this.playerEntity.isInWater()) {
                    this.playerEntity.clearElytraFlying();
                    break;
                }
                final ItemStack itemstack = this.playerEntity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                if (itemstack.getItem() == Items.ELYTRA && ItemElytra.isBroken(itemstack)) {
                    this.playerEntity.setElytraFlying();
                    break;
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid client command!");
            }
        }
    }
    
    @Override
    public void processUseEntity(final CPacketUseEntity packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        final WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        final Entity entity = packetIn.getEntityFromWorld(worldserver);
        this.playerEntity.markPlayerActive();
        if (entity != null) {
            final boolean flag = this.playerEntity.canEntityBeSeen(entity);
            double d0 = 36.0;
            if (!flag) {
                d0 = 9.0;
            }
            if (this.playerEntity.getDistanceSqToEntity(entity) < d0) {
                if (packetIn.getAction() == CPacketUseEntity.Action.INTERACT) {
                    final EnumHand enumhand = packetIn.getHand();
                    this.playerEntity.func_190775_a(entity, enumhand);
                }
                else if (packetIn.getAction() == CPacketUseEntity.Action.INTERACT_AT) {
                    final EnumHand enumhand2 = packetIn.getHand();
                    entity.applyPlayerInteraction(this.playerEntity, packetIn.getHitVec(), enumhand2);
                }
                else if (packetIn.getAction() == CPacketUseEntity.Action.ATTACK) {
                    if (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow || entity == this.playerEntity) {
                        this.func_194028_b(new TextComponentTranslation("multiplayer.disconnect.invalid_entity_attacked", new Object[0]));
                        this.serverController.logWarning("Player " + this.playerEntity.getName() + " tried to attack an invalid entity");
                        return;
                    }
                    this.playerEntity.attackTargetEntityWithCurrentItem(entity);
                }
            }
        }
    }
    
    @Override
    public void processClientStatus(final CPacketClientStatus packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        this.playerEntity.markPlayerActive();
        final CPacketClientStatus.State cpacketclientstatus$state = packetIn.getStatus();
        switch (cpacketclientstatus$state) {
            case PERFORM_RESPAWN: {
                if (this.playerEntity.playerConqueredTheEnd) {
                    this.playerEntity.playerConqueredTheEnd = false;
                    this.playerEntity = this.serverController.getPlayerList().recreatePlayerEntity(this.playerEntity, 0, true);
                    CriteriaTriggers.field_193134_u.func_193143_a(this.playerEntity, DimensionType.THE_END, DimensionType.OVERWORLD);
                    break;
                }
                if (this.playerEntity.getHealth() > 0.0f) {
                    return;
                }
                this.playerEntity = this.serverController.getPlayerList().recreatePlayerEntity(this.playerEntity, 0, false);
                if (this.serverController.isHardcore()) {
                    this.playerEntity.setGameType(GameType.SPECTATOR);
                    this.playerEntity.getServerWorld().getGameRules().setOrCreateGameRule("spectatorsGenerateChunks", "false");
                    break;
                }
                break;
            }
            case REQUEST_STATS: {
                this.playerEntity.getStatFile().sendStats(this.playerEntity);
                break;
            }
        }
    }
    
    @Override
    public void processCloseWindow(final CPacketCloseWindow packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        this.playerEntity.closeContainer();
    }
    
    @Override
    public void processClickWindow(final CPacketClickWindow packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        this.playerEntity.markPlayerActive();
        if (this.playerEntity.openContainer.windowId == packetIn.getWindowId() && this.playerEntity.openContainer.getCanCraft(this.playerEntity)) {
            if (this.playerEntity.isSpectator()) {
                final NonNullList<ItemStack> nonnulllist = NonNullList.func_191196_a();
                for (int i = 0; i < this.playerEntity.openContainer.inventorySlots.size(); ++i) {
                    nonnulllist.add(this.playerEntity.openContainer.inventorySlots.get(i).getStack());
                }
                this.playerEntity.updateCraftingInventory(this.playerEntity.openContainer, nonnulllist);
            }
            else {
                final ItemStack itemstack2 = this.playerEntity.openContainer.slotClick(packetIn.getSlotId(), packetIn.getUsedButton(), packetIn.getClickType(), this.playerEntity);
                if (ItemStack.areItemStacksEqual(packetIn.getClickedItem(), itemstack2)) {
                    this.playerEntity.connection.sendPacket(new SPacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true));
                    this.playerEntity.isChangingQuantityOnly = true;
                    this.playerEntity.openContainer.detectAndSendChanges();
                    this.playerEntity.updateHeldItem();
                    this.playerEntity.isChangingQuantityOnly = false;
                }
                else {
                    this.pendingTransactions.addKey(this.playerEntity.openContainer.windowId, packetIn.getActionNumber());
                    this.playerEntity.connection.sendPacket(new SPacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), false));
                    this.playerEntity.openContainer.setCanCraft(this.playerEntity, false);
                    final NonNullList<ItemStack> nonnulllist2 = NonNullList.func_191196_a();
                    for (int j = 0; j < this.playerEntity.openContainer.inventorySlots.size(); ++j) {
                        final ItemStack itemstack3 = this.playerEntity.openContainer.inventorySlots.get(j).getStack();
                        final ItemStack itemstack4 = itemstack3.func_190926_b() ? ItemStack.field_190927_a : itemstack3;
                        nonnulllist2.add(itemstack4);
                    }
                    this.playerEntity.updateCraftingInventory(this.playerEntity.openContainer, nonnulllist2);
                }
            }
        }
    }
    
    @Override
    public void func_194308_a(final CPacketPlaceRecipe p_194308_1_) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)p_194308_1_, this, this.playerEntity.getServerWorld());
        this.playerEntity.markPlayerActive();
        if (!this.playerEntity.isSpectator() && this.playerEntity.openContainer.windowId == p_194308_1_.func_194318_a() && this.playerEntity.openContainer.getCanCraft(this.playerEntity)) {
            this.field_194309_H.func_194327_a(this.playerEntity, p_194308_1_.func_194317_b(), p_194308_1_.func_194319_c());
        }
    }
    
    @Override
    public void processEnchantItem(final CPacketEnchantItem packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        this.playerEntity.markPlayerActive();
        if (this.playerEntity.openContainer.windowId == packetIn.getWindowId() && this.playerEntity.openContainer.getCanCraft(this.playerEntity) && !this.playerEntity.isSpectator()) {
            this.playerEntity.openContainer.enchantItem(this.playerEntity, packetIn.getButton());
            this.playerEntity.openContainer.detectAndSendChanges();
        }
    }
    
    @Override
    public void processCreativeInventoryAction(final CPacketCreativeInventoryAction packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        if (this.playerEntity.interactionManager.isCreative()) {
            final boolean flag = packetIn.getSlotId() < 0;
            final ItemStack itemstack = packetIn.getStack();
            if (!itemstack.func_190926_b() && itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("BlockEntityTag", 10)) {
                final NBTTagCompound nbttagcompound = itemstack.getTagCompound().getCompoundTag("BlockEntityTag");
                if (nbttagcompound.hasKey("x") && nbttagcompound.hasKey("y") && nbttagcompound.hasKey("z")) {
                    final BlockPos blockpos = new BlockPos(nbttagcompound.getInteger("x"), nbttagcompound.getInteger("y"), nbttagcompound.getInteger("z"));
                    final TileEntity tileentity = this.playerEntity.world.getTileEntity(blockpos);
                    if (tileentity != null) {
                        final NBTTagCompound nbttagcompound2 = tileentity.writeToNBT(new NBTTagCompound());
                        nbttagcompound2.removeTag("x");
                        nbttagcompound2.removeTag("y");
                        nbttagcompound2.removeTag("z");
                        itemstack.setTagInfo("BlockEntityTag", nbttagcompound2);
                    }
                }
            }
            final boolean flag2 = packetIn.getSlotId() >= 1 && packetIn.getSlotId() <= 45;
            final boolean flag3 = itemstack.func_190926_b() || (itemstack.getMetadata() >= 0 && itemstack.func_190916_E() <= 64 && !itemstack.func_190926_b());
            if (flag2 && flag3) {
                if (itemstack.func_190926_b()) {
                    this.playerEntity.inventoryContainer.putStackInSlot(packetIn.getSlotId(), ItemStack.field_190927_a);
                }
                else {
                    this.playerEntity.inventoryContainer.putStackInSlot(packetIn.getSlotId(), itemstack);
                }
                this.playerEntity.inventoryContainer.setCanCraft(this.playerEntity, true);
            }
            else if (flag && flag3 && this.itemDropThreshold < 200) {
                this.itemDropThreshold += 20;
                final EntityItem entityitem = this.playerEntity.dropItem(itemstack, true);
                if (entityitem != null) {
                    entityitem.setAgeToCreativeDespawnTime();
                }
            }
        }
    }
    
    @Override
    public void processConfirmTransaction(final CPacketConfirmTransaction packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        final Short oshort = this.pendingTransactions.lookup(this.playerEntity.openContainer.windowId);
        if (oshort != null && packetIn.getUid() == oshort && this.playerEntity.openContainer.windowId == packetIn.getWindowId() && !this.playerEntity.openContainer.getCanCraft(this.playerEntity) && !this.playerEntity.isSpectator()) {
            this.playerEntity.openContainer.setCanCraft(this.playerEntity, true);
        }
    }
    
    @Override
    public void processUpdateSign(final CPacketUpdateSign packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        this.playerEntity.markPlayerActive();
        final WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        final BlockPos blockpos = packetIn.getPosition();
        if (worldserver.isBlockLoaded(blockpos)) {
            final IBlockState iblockstate = worldserver.getBlockState(blockpos);
            final TileEntity tileentity = worldserver.getTileEntity(blockpos);
            if (!(tileentity instanceof TileEntitySign)) {
                return;
            }
            final TileEntitySign tileentitysign = (TileEntitySign)tileentity;
            if (!tileentitysign.getIsEditable() || tileentitysign.getPlayer() != this.playerEntity) {
                this.serverController.logWarning("Player " + this.playerEntity.getName() + " just tried to change non-editable sign");
                return;
            }
            final String[] astring = packetIn.getLines();
            for (int i = 0; i < astring.length; ++i) {
                tileentitysign.signText[i] = new TextComponentString(TextFormatting.getTextWithoutFormattingCodes(astring[i]));
            }
            tileentitysign.markDirty();
            worldserver.notifyBlockUpdate(blockpos, iblockstate, iblockstate, 3);
        }
    }
    
    @Override
    public void processKeepAlive(final CPacketKeepAlive packetIn) {
        if (this.field_194403_g && packetIn.getKey() == this.field_194404_h) {
            final int i = (int)(this.currentTimeMillis() - this.field_194402_f);
            this.playerEntity.ping = (this.playerEntity.ping * 3 + i) / 4;
            this.field_194403_g = false;
        }
        else if (!this.playerEntity.getName().equals(this.serverController.getServerOwner())) {
            this.func_194028_b(new TextComponentTranslation("disconnect.timeout", new Object[0]));
        }
    }
    
    private long currentTimeMillis() {
        return System.nanoTime() / 1000000L;
    }
    
    @Override
    public void processPlayerAbilities(final CPacketPlayerAbilities packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        this.playerEntity.capabilities.isFlying = (packetIn.isFlying() && this.playerEntity.capabilities.allowFlying);
    }
    
    @Override
    public void processTabComplete(final CPacketTabComplete packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        final List<String> list = (List<String>)Lists.newArrayList();
        for (final String s : this.serverController.getTabCompletions(this.playerEntity, packetIn.getMessage(), packetIn.getTargetBlock(), packetIn.hasTargetBlock())) {
            list.add(s);
        }
        this.playerEntity.connection.sendPacket(new SPacketTabComplete(list.toArray(new String[list.size()])));
    }
    
    @Override
    public void processClientSettings(final CPacketClientSettings packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        this.playerEntity.handleClientSettings(packetIn);
    }
    
    @Override
    public void processCustomPayload(final CPacketCustomPayload packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue((Packet<NetHandlerPlayServer>)packetIn, this, this.playerEntity.getServerWorld());
        final String s = packetIn.getChannelName();
        if ("MC|BEdit".equals(s)) {
            final PacketBuffer packetbuffer = packetIn.getBufferData();
            try {
                final ItemStack itemstack = packetbuffer.readItemStackFromBuffer();
                if (itemstack.func_190926_b()) {
                    return;
                }
                if (!ItemWritableBook.isNBTValid(itemstack.getTagCompound())) {
                    throw new IOException("Invalid book tag!");
                }
                final ItemStack itemstack2 = this.playerEntity.getHeldItemMainhand();
                if (itemstack2.func_190926_b()) {
                    return;
                }
                if (itemstack.getItem() == Items.WRITABLE_BOOK && itemstack.getItem() == itemstack2.getItem()) {
                    itemstack2.setTagInfo("pages", itemstack.getTagCompound().getTagList("pages", 8));
                }
            }
            catch (final Exception exception6) {
                NetHandlerPlayServer.LOGGER.error("Couldn't handle book info", exception6);
            }
        }
        else if ("MC|BSign".equals(s)) {
            final PacketBuffer packetbuffer2 = packetIn.getBufferData();
            try {
                final ItemStack itemstack3 = packetbuffer2.readItemStackFromBuffer();
                if (itemstack3.func_190926_b()) {
                    return;
                }
                if (!ItemWrittenBook.validBookTagContents(itemstack3.getTagCompound())) {
                    throw new IOException("Invalid book tag!");
                }
                final ItemStack itemstack4 = this.playerEntity.getHeldItemMainhand();
                if (itemstack4.func_190926_b()) {
                    return;
                }
                if (itemstack3.getItem() == Items.WRITABLE_BOOK && itemstack4.getItem() == Items.WRITABLE_BOOK) {
                    final ItemStack itemstack5 = new ItemStack(Items.WRITTEN_BOOK);
                    itemstack5.setTagInfo("author", new NBTTagString(this.playerEntity.getName()));
                    itemstack5.setTagInfo("title", new NBTTagString(itemstack3.getTagCompound().getString("title")));
                    final NBTTagList nbttaglist = itemstack3.getTagCompound().getTagList("pages", 8);
                    for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                        String s2 = nbttaglist.getStringTagAt(i);
                        final ITextComponent itextcomponent = new TextComponentString(s2);
                        s2 = ITextComponent.Serializer.componentToJson(itextcomponent);
                        nbttaglist.set(i, new NBTTagString(s2));
                    }
                    itemstack5.setTagInfo("pages", nbttaglist);
                    this.playerEntity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, itemstack5);
                }
            }
            catch (final Exception exception7) {
                NetHandlerPlayServer.LOGGER.error("Couldn't sign book", exception7);
            }
        }
        else if ("MC|TrSel".equals(s)) {
            try {
                final int k = packetIn.getBufferData().readInt();
                final Container container = this.playerEntity.openContainer;
                if (container instanceof ContainerMerchant) {
                    ((ContainerMerchant)container).setCurrentRecipeIndex(k);
                }
            }
            catch (final Exception exception8) {
                NetHandlerPlayServer.LOGGER.error("Couldn't select trade", exception8);
            }
        }
        else if ("MC|AdvCmd".equals(s)) {
            if (!this.serverController.isCommandBlockEnabled()) {
                this.playerEntity.addChatMessage(new TextComponentTranslation("advMode.notEnabled", new Object[0]));
                return;
            }
            if (!this.playerEntity.canUseCommandBlock()) {
                this.playerEntity.addChatMessage(new TextComponentTranslation("advMode.notAllowed", new Object[0]));
                return;
            }
            final PacketBuffer packetbuffer3 = packetIn.getBufferData();
            try {
                final int l = packetbuffer3.readByte();
                CommandBlockBaseLogic commandblockbaselogic1 = null;
                if (l == 0) {
                    final TileEntity tileentity = this.playerEntity.world.getTileEntity(new BlockPos(packetbuffer3.readInt(), packetbuffer3.readInt(), packetbuffer3.readInt()));
                    if (tileentity instanceof TileEntityCommandBlock) {
                        commandblockbaselogic1 = ((TileEntityCommandBlock)tileentity).getCommandBlockLogic();
                    }
                }
                else if (l == 1) {
                    final Entity entity = this.playerEntity.world.getEntityByID(packetbuffer3.readInt());
                    if (entity instanceof EntityMinecartCommandBlock) {
                        commandblockbaselogic1 = ((EntityMinecartCommandBlock)entity).getCommandBlockLogic();
                    }
                }
                final String s3 = packetbuffer3.readStringFromBuffer(packetbuffer3.readableBytes());
                final boolean flag2 = packetbuffer3.readBoolean();
                if (commandblockbaselogic1 != null) {
                    commandblockbaselogic1.setCommand(s3);
                    commandblockbaselogic1.setTrackOutput(flag2);
                    if (!flag2) {
                        commandblockbaselogic1.setLastOutput(null);
                    }
                    commandblockbaselogic1.updateCommand();
                    this.playerEntity.addChatMessage(new TextComponentTranslation("advMode.setCommand.success", new Object[] { s3 }));
                }
            }
            catch (final Exception exception9) {
                NetHandlerPlayServer.LOGGER.error("Couldn't set command block", exception9);
            }
        }
        else if ("MC|AutoCmd".equals(s)) {
            if (!this.serverController.isCommandBlockEnabled()) {
                this.playerEntity.addChatMessage(new TextComponentTranslation("advMode.notEnabled", new Object[0]));
                return;
            }
            if (!this.playerEntity.canUseCommandBlock()) {
                this.playerEntity.addChatMessage(new TextComponentTranslation("advMode.notAllowed", new Object[0]));
                return;
            }
            final PacketBuffer packetbuffer4 = packetIn.getBufferData();
            try {
                CommandBlockBaseLogic commandblockbaselogic2 = null;
                TileEntityCommandBlock tileentitycommandblock = null;
                final BlockPos blockpos1 = new BlockPos(packetbuffer4.readInt(), packetbuffer4.readInt(), packetbuffer4.readInt());
                final TileEntity tileentity2 = this.playerEntity.world.getTileEntity(blockpos1);
                if (tileentity2 instanceof TileEntityCommandBlock) {
                    tileentitycommandblock = (TileEntityCommandBlock)tileentity2;
                    commandblockbaselogic2 = tileentitycommandblock.getCommandBlockLogic();
                }
                final String s4 = packetbuffer4.readStringFromBuffer(packetbuffer4.readableBytes());
                final boolean flag3 = packetbuffer4.readBoolean();
                final TileEntityCommandBlock.Mode tileentitycommandblock$mode = TileEntityCommandBlock.Mode.valueOf(packetbuffer4.readStringFromBuffer(16));
                final boolean flag4 = packetbuffer4.readBoolean();
                final boolean flag5 = packetbuffer4.readBoolean();
                if (commandblockbaselogic2 != null) {
                    final EnumFacing enumfacing = this.playerEntity.world.getBlockState(blockpos1).getValue((IProperty<EnumFacing>)BlockCommandBlock.FACING);
                    switch (tileentitycommandblock$mode) {
                        case SEQUENCE: {
                            final IBlockState iblockstate3 = Blocks.CHAIN_COMMAND_BLOCK.getDefaultState();
                            this.playerEntity.world.setBlockState(blockpos1, iblockstate3.withProperty((IProperty<Comparable>)BlockCommandBlock.FACING, enumfacing).withProperty((IProperty<Comparable>)BlockCommandBlock.CONDITIONAL, flag4), 2);
                            break;
                        }
                        case AUTO: {
                            final IBlockState iblockstate4 = Blocks.REPEATING_COMMAND_BLOCK.getDefaultState();
                            this.playerEntity.world.setBlockState(blockpos1, iblockstate4.withProperty((IProperty<Comparable>)BlockCommandBlock.FACING, enumfacing).withProperty((IProperty<Comparable>)BlockCommandBlock.CONDITIONAL, flag4), 2);
                            break;
                        }
                        case REDSTONE: {
                            final IBlockState iblockstate5 = Blocks.COMMAND_BLOCK.getDefaultState();
                            this.playerEntity.world.setBlockState(blockpos1, iblockstate5.withProperty((IProperty<Comparable>)BlockCommandBlock.FACING, enumfacing).withProperty((IProperty<Comparable>)BlockCommandBlock.CONDITIONAL, flag4), 2);
                            break;
                        }
                    }
                    tileentity2.validate();
                    this.playerEntity.world.setTileEntity(blockpos1, tileentity2);
                    commandblockbaselogic2.setCommand(s4);
                    commandblockbaselogic2.setTrackOutput(flag3);
                    if (!flag3) {
                        commandblockbaselogic2.setLastOutput(null);
                    }
                    tileentitycommandblock.setAuto(flag5);
                    commandblockbaselogic2.updateCommand();
                    if (!net.minecraft.util.StringUtils.isNullOrEmpty(s4)) {
                        this.playerEntity.addChatMessage(new TextComponentTranslation("advMode.setCommand.success", new Object[] { s4 }));
                    }
                }
            }
            catch (final Exception exception10) {
                NetHandlerPlayServer.LOGGER.error("Couldn't set command block", exception10);
            }
        }
        else if ("MC|Beacon".equals(s)) {
            if (this.playerEntity.openContainer instanceof ContainerBeacon) {
                try {
                    final PacketBuffer packetbuffer5 = packetIn.getBufferData();
                    final int i2 = packetbuffer5.readInt();
                    final int k2 = packetbuffer5.readInt();
                    final ContainerBeacon containerbeacon = (ContainerBeacon)this.playerEntity.openContainer;
                    final Slot slot = containerbeacon.getSlot(0);
                    if (slot.getHasStack()) {
                        slot.decrStackSize(1);
                        final IInventory iinventory = containerbeacon.getTileEntity();
                        iinventory.setField(1, i2);
                        iinventory.setField(2, k2);
                        iinventory.markDirty();
                    }
                }
                catch (final Exception exception11) {
                    NetHandlerPlayServer.LOGGER.error("Couldn't set beacon", exception11);
                }
            }
        }
        else if ("MC|ItemName".equals(s)) {
            if (this.playerEntity.openContainer instanceof ContainerRepair) {
                final ContainerRepair containerrepair = (ContainerRepair)this.playerEntity.openContainer;
                if (packetIn.getBufferData() != null && packetIn.getBufferData().readableBytes() >= 1) {
                    final String s5 = ChatAllowedCharacters.filterAllowedCharacters(packetIn.getBufferData().readStringFromBuffer(32767));
                    if (s5.length() <= 35) {
                        containerrepair.updateItemName(s5);
                    }
                }
                else {
                    containerrepair.updateItemName("");
                }
            }
        }
        else if ("MC|Struct".equals(s)) {
            if (!this.playerEntity.canUseCommandBlock()) {
                return;
            }
            final PacketBuffer packetbuffer6 = packetIn.getBufferData();
            try {
                final BlockPos blockpos2 = new BlockPos(packetbuffer6.readInt(), packetbuffer6.readInt(), packetbuffer6.readInt());
                final IBlockState iblockstate6 = this.playerEntity.world.getBlockState(blockpos2);
                final TileEntity tileentity3 = this.playerEntity.world.getTileEntity(blockpos2);
                if (tileentity3 instanceof TileEntityStructure) {
                    final TileEntityStructure tileentitystructure = (TileEntityStructure)tileentity3;
                    final int l2 = packetbuffer6.readByte();
                    final String s6 = packetbuffer6.readStringFromBuffer(32);
                    tileentitystructure.setMode(TileEntityStructure.Mode.valueOf(s6));
                    tileentitystructure.setName(packetbuffer6.readStringFromBuffer(64));
                    final int i3 = MathHelper.clamp(packetbuffer6.readInt(), -32, 32);
                    final int j2 = MathHelper.clamp(packetbuffer6.readInt(), -32, 32);
                    final int k3 = MathHelper.clamp(packetbuffer6.readInt(), -32, 32);
                    tileentitystructure.setPosition(new BlockPos(i3, j2, k3));
                    final int l3 = MathHelper.clamp(packetbuffer6.readInt(), 0, 32);
                    final int i4 = MathHelper.clamp(packetbuffer6.readInt(), 0, 32);
                    final int m = MathHelper.clamp(packetbuffer6.readInt(), 0, 32);
                    tileentitystructure.setSize(new BlockPos(l3, i4, m));
                    final String s7 = packetbuffer6.readStringFromBuffer(32);
                    tileentitystructure.setMirror(Mirror.valueOf(s7));
                    final String s8 = packetbuffer6.readStringFromBuffer(32);
                    tileentitystructure.setRotation(Rotation.valueOf(s8));
                    tileentitystructure.setMetadata(packetbuffer6.readStringFromBuffer(128));
                    tileentitystructure.setIgnoresEntities(packetbuffer6.readBoolean());
                    tileentitystructure.setShowAir(packetbuffer6.readBoolean());
                    tileentitystructure.setShowBoundingBox(packetbuffer6.readBoolean());
                    tileentitystructure.setIntegrity(MathHelper.clamp(packetbuffer6.readFloat(), 0.0f, 1.0f));
                    tileentitystructure.setSeed(packetbuffer6.readVarLong());
                    final String s9 = tileentitystructure.getName();
                    if (l2 == 2) {
                        if (tileentitystructure.save()) {
                            this.playerEntity.addChatComponentMessage(new TextComponentTranslation("structure_block.save_success", new Object[] { s9 }), false);
                        }
                        else {
                            this.playerEntity.addChatComponentMessage(new TextComponentTranslation("structure_block.save_failure", new Object[] { s9 }), false);
                        }
                    }
                    else if (l2 == 3) {
                        if (!tileentitystructure.isStructureLoadable()) {
                            this.playerEntity.addChatComponentMessage(new TextComponentTranslation("structure_block.load_not_found", new Object[] { s9 }), false);
                        }
                        else if (tileentitystructure.load()) {
                            this.playerEntity.addChatComponentMessage(new TextComponentTranslation("structure_block.load_success", new Object[] { s9 }), false);
                        }
                        else {
                            this.playerEntity.addChatComponentMessage(new TextComponentTranslation("structure_block.load_prepare", new Object[] { s9 }), false);
                        }
                    }
                    else if (l2 == 4) {
                        if (tileentitystructure.detectSize()) {
                            this.playerEntity.addChatComponentMessage(new TextComponentTranslation("structure_block.size_success", new Object[] { s9 }), false);
                        }
                        else {
                            this.playerEntity.addChatComponentMessage(new TextComponentTranslation("structure_block.size_failure", new Object[0]), false);
                        }
                    }
                    tileentitystructure.markDirty();
                    this.playerEntity.world.notifyBlockUpdate(blockpos2, iblockstate6, iblockstate6, 3);
                }
            }
            catch (final Exception exception12) {
                NetHandlerPlayServer.LOGGER.error("Couldn't set structure block", exception12);
            }
        }
        else if ("MC|PickItem".equals(s)) {
            final PacketBuffer packetbuffer7 = packetIn.getBufferData();
            try {
                final int j3 = packetbuffer7.readVarIntFromBuffer();
                this.playerEntity.inventory.pickItem(j3);
                this.playerEntity.connection.sendPacket(new SPacketSetSlot(-2, this.playerEntity.inventory.currentItem, this.playerEntity.inventory.getStackInSlot(this.playerEntity.inventory.currentItem)));
                this.playerEntity.connection.sendPacket(new SPacketSetSlot(-2, j3, this.playerEntity.inventory.getStackInSlot(j3)));
                this.playerEntity.connection.sendPacket(new SPacketHeldItemChange(this.playerEntity.inventory.currentItem));
            }
            catch (final Exception exception13) {
                NetHandlerPlayServer.LOGGER.error("Couldn't pick item", exception13);
            }
        }
    }
    
    @Override
    public void func_191985_a(final CPacketRecipePlacement cPacketRecipePlacement) {
    }
    
    @Override
    public void processPlayerBlockPlacement(final CPacketPlayerBlockPlacement packetIn) {
    }
}
