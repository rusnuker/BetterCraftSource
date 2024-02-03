// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core;

import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.entity.EntityLivingBase;
import net.labymod.labyconnect.packets.PacketBuf;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.scoreboard.ScoreObjective;
import java.util.UUID;
import net.minecraft.network.PacketBuffer;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.minecraft.block.material.Material;
import net.minecraft.potion.PotionEffect;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.Item;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.gui.ServerSelectionList;
import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.entity.EntityPlayerSP;
import net.labymod.main.LabyMod;

public interface MinecraftAdapter
{
    void init(final LabyMod p0);
    
    EntityPlayerSP getPlayer();
    
    WorldClient getWorld();
    
    ChatComponent getChatComponent(final Object p0);
    
    String getBiome();
    
    void displayMessageInChat(final String p0);
    
    void displayMessageInChatURL(final String p0, final String p1);
    
    void displayMessageInChatCustomAction(final String p0, final int p1, final String p2);
    
    boolean isBlocking(final EntityPlayer p0);
    
    void playSound(final ResourceLocation p0, final float p1);
    
    RenderManager getCustomRenderManager();
    
    GuiIngame getCustomIngameGui();
    
    GuiScreen getCustomMainMenu();
    
    int getItemInUseMaxCount();
    
    boolean isHandActive();
    
    ItemStack getItemInUse();
    
    void updateOnlineServers(final ServerSelectionList p0, final ServerList p1);
    
    NetHandlerPlayClient getConnection();
    
    Potion getInvisiblityPotion();
    
    Entity getRidingEntity(final AbstractClientPlayer p0);
    
    ItemStack getItem(final InventoryPlayer p0, final int p1);
    
    int getAnimationsToGo(final ItemStack p0);
    
    BlockPosition getPosition(final Object p0);
    
    String getClickEventValue(final int p0, final int p1);
    
    Item getItemBow();
    
    List<?> splitText(final Object p0, final int p1, final FontRenderer p2, final boolean p3, final boolean p4);
    
    ItemStack getMainHandItem();
    
    ItemStack getOffHandItem();
    
    int getStackSize(final ItemStack p0);
    
    String getPotionDurationString(final PotionEffect p0);
    
    Potion getPotion(final PotionEffect p0);
    
    Item getTargetBlockItem();
    
    Material getLavaMaterial();
    
    boolean isRightArmPoseBow(final ModelCosmetics p0);
    
    boolean isAimedBow(final ModelCosmetics p0);
    
    PotionEffect getPotionEffect(final Potion p0, final int p1, final int p2);
    
    boolean isSelected(final ServerSelectionList p0, final int p1);
    
    int getSelectedServerInSelectionList(final ServerSelectionList p0);
    
    void sendPluginMessage(final String p0, final PacketBuffer p1);
    
    void updateServerList(final ServerSelectionList p0, final ServerList p1);
    
    void writeUniqueIdToBuffer(final PacketBuffer p0, final UUID p1);
    
    String readStringFromBuffer(final PacketBuffer p0);
    
    ScoreObjective getDummyScoreObjective();
    
    Object getTaggedChatComponent(final Object p0);
    
    FontRenderer getFontRenderer();
    
    void setButtonXPosition(final GuiButton p0, final int p1);
    
    void setButtonYPosition(final GuiButton p0, final int p1);
    
    void setTextFieldXPosition(final GuiTextField p0, final int p1);
    
    void setTextFieldYPosition(final GuiTextField p0, final int p1);
    
    int getXPosition(final GuiButton p0);
    
    int getYPosition(final GuiButton p0);
    
    int getXPosition(final GuiTextField p0);
    
    int getYPosition(final GuiTextField p0);
    
    int getTeamColorIndex(final ScorePlayerTeam p0);
    
    void drawButton(final GuiButton p0, final int p1, final int p2);
    
    TileEntitySignRenderer getCustomSignRenderer();
    
    String vectoString(final Vec3i p0);
    
    PacketBuf createPacketBuf(final ByteBuf p0);
    
    boolean hasInGameFocus();
    
    boolean isCurrentScreenNull();
    
    boolean isMinecraftChatOpen();
    
    long getLastAttackTime();
    
    EntityLivingBase getLastAttackedEntity();
    
    void handleBlockBuild();
    
    double calculateEyeMovement(final Entity p0, final Entity p1);
    
    void setSecondLayerBit(final AbstractClientPlayer p0, final int p1, final byte p2);
    
    void sendClientSettings(final String p0, final int p1, final EntityPlayer.EnumChatVisibility p2, final boolean p3, final int p4);
    
    UUID isEmotePacket(final Object p0);
    
    boolean isJumpPredicted();
    
    int getGameMode(final UUID p0);
    
    Entity getEntityMouseOver();
    
    boolean isElytraFlying(final Entity p0);
    
    boolean isWearingElytra(final Entity p0);
    
    Entity getRenderViewEntity();
    
    boolean isAchievementGui(final GuiScreen p0);
    
    String getBossBarMessage();
    
    void setUseLeftHand(final boolean p0);
    
    boolean isUsingLeftHand();
    
    PropertyMap getPropertyMap();
}
