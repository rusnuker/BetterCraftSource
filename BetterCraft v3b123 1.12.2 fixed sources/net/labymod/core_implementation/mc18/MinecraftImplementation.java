// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core_implementation.mc18;

import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.labymod.core_implementation.mc18.util.prediction.JumpPrediction;
import java.util.Iterator;
import net.minecraft.entity.DataWatcher;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.util.Vec3;
import net.labymod.api.permissions.Permissions;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.gui.GuiChat;
import net.labymod.core_implementation.mc18.util.PacketBufOld;
import net.labymod.labyconnect.packets.PacketBuf;
import io.netty.buffer.ByteBuf;
import com.google.common.base.Objects;
import net.labymod.core.Vec3i;
import net.labymod.utils.manager.SignManager;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.ChatComponentText;
import net.labymod.utils.manager.TagManager;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreObjective;
import java.util.UUID;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.labymod.support.util.Debug;
import net.minecraft.network.PacketBuffer;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.labymod.core.LabyModCore;
import net.minecraft.potion.PotionEffect;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.labymod.core.BlockPosition;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.gui.ServerSelectionList;
import net.minecraft.item.ItemStack;
import net.labymod.core_implementation.mc18.gui.ModGuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.labymod.core_implementation.mc18.gui.GuiIngameCustom;
import net.minecraft.client.gui.GuiIngame;
import net.labymod.core_implementation.mc18.render.RenderManagerCustom;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.EnumAction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.labymod.core_implementation.mc18.util.StringMessage;
import net.minecraft.util.IChatComponent;
import net.labymod.core.ChatComponent;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.labymod.core_implementation.mc18.listener.ServerIncomingPacketListener;
import net.labymod.main.LabyMod;
import net.labymod.core.MinecraftAdapter;

public class MinecraftImplementation implements MinecraftAdapter
{
    @Override
    public void init(final LabyMod labymod) {
        new ServerIncomingPacketListener(labymod).register();
    }
    
    @Override
    public EntityPlayerSP getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }
    
    @Override
    public WorldClient getWorld() {
        return Minecraft.getMinecraft().theWorld;
    }
    
    @Override
    public ChatComponent getChatComponent(final Object chatComponent) {
        if (chatComponent == null) {
            return null;
        }
        return new ChatComponent(((IChatComponent)chatComponent).getUnformattedText(), ((IChatComponent)chatComponent).getFormattedText(), IChatComponent.Serializer.componentToJson((IChatComponent)chatComponent));
    }
    
    @Override
    public String getBiome() {
        return (this.getPlayer() == null || this.getWorld() == null || this.getPlayer().getPosition() == null) ? "?" : this.getWorld().getBiomeGenForCoords(this.getPlayer().getPosition()).biomeName;
    }
    
    @Override
    public void displayMessageInChat(final String text) {
        IChatComponent[] output;
        for (int length = (output = new StringMessage(text, (boolean)(0 != 0)).getOutput()).length, i = 0; i < length; ++i) {
            final IChatComponent line = output[i];
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(line);
        }
    }
    
    @Override
    public void displayMessageInChatURL(final String text, final String url) {
        this.displayMessageInChatCustomAction(text, ClickEvent.Action.OPEN_URL.ordinal(), url);
    }
    
    @Override
    public void displayMessageInChatCustomAction(final String text, final int actionId, final String command) {
        final ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.values()[actionId], command);
        IChatComponent[] output;
        for (int length = (output = new StringMessage(text, (boolean)(0 != 0)).getOutput()).length, i = 0; i < length; ++i) {
            final IChatComponent line = output[i];
            line.getChatStyle().setChatClickEvent(clickEvent);
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(line);
        }
    }
    
    @Override
    public boolean isBlocking(final EntityPlayer player) {
        return player.isUsingItem() && player.getItemInUse().getItem().getItemUseAction(player.getItemInUse()) == EnumAction.BLOCK;
    }
    
    @Override
    public void playSound(final ResourceLocation resourceLocation, final float pitch) {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(resourceLocation, pitch));
    }
    
    @Override
    public RenderManager getCustomRenderManager() {
        return new RenderManagerCustom(Minecraft.getMinecraft().getTextureManager(), Minecraft.getMinecraft().getRenderItem());
    }
    
    @Override
    public GuiIngame getCustomIngameGui() {
        return new GuiIngameCustom(Minecraft.getMinecraft());
    }
    
    @Override
    public GuiScreen getCustomMainMenu() {
        return new ModGuiMainMenu();
    }
    
    @Override
    public int getItemInUseMaxCount() {
        return this.getPlayer().getItemInUseDuration();
    }
    
    @Override
    public boolean isHandActive() {
        return this.getPlayer().isUsingItem();
    }
    
    @Override
    public ItemStack getItemInUse() {
        return this.getPlayer().getItemInUse();
    }
    
    @Override
    public void updateOnlineServers(final ServerSelectionList serverSelectionList, final ServerList serverList) {
        serverSelectionList.func_148195_a(serverList);
    }
    
    @Override
    public NetHandlerPlayClient getConnection() {
        return Minecraft.getMinecraft().getNetHandler();
    }
    
    @Override
    public Potion getInvisiblityPotion() {
        return Potion.invisibility;
    }
    
    @Override
    public Entity getRidingEntity(final AbstractClientPlayer player) {
        return player.ridingEntity;
    }
    
    @Override
    public ItemStack getItem(final InventoryPlayer inventoryPlayer, final int index) {
        return inventoryPlayer.mainInventory[index];
    }
    
    @Override
    public int getAnimationsToGo(final ItemStack itemStack) {
        return itemStack.animationsToGo;
    }
    
    @Override
    public BlockPosition getPosition(final Object blockPosition) {
        final BlockPos pos = (BlockPos)blockPosition;
        return new BlockPosition(pos.getX(), pos.getY(), pos.getZ());
    }
    
    @Override
    public String getClickEventValue(final int x, final int y) {
        final IChatComponent iChatClickHover = Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatComponent(x, y);
        if (iChatClickHover == null || iChatClickHover.getChatStyle() == null || iChatClickHover.getChatStyle().getChatClickEvent() == null) {
            return null;
        }
        final String value = iChatClickHover.getChatStyle().getChatClickEvent().getValue();
        return value;
    }
    
    @Override
    public Item getItemBow() {
        return Items.bow;
    }
    
    @Override
    public List<?> splitText(final Object textComponent, final int maxTextLenght, final FontRenderer fontRendererIn, final boolean p_178908_3_, final boolean forceTextColor) {
        return GuiUtilRenderComponents.splitText((IChatComponent)textComponent, maxTextLenght, fontRendererIn, p_178908_3_, forceTextColor);
    }
    
    @Override
    public ItemStack getMainHandItem() {
        return (this.getPlayer() == null) ? null : this.getPlayer().getHeldItem();
    }
    
    @Override
    public ItemStack getOffHandItem() {
        return null;
    }
    
    @Override
    public int getStackSize(final ItemStack itemStack) {
        return itemStack.stackSize;
    }
    
    @Override
    public String getPotionDurationString(final PotionEffect potion) {
        return Potion.getDurationString(potion);
    }
    
    @Override
    public Potion getPotion(final PotionEffect potionEffect) {
        return Potion.potionTypes[potionEffect.getPotionID()];
    }
    
    @Override
    public Item getTargetBlockItem() {
        final WorldClient world = LabyModCore.getMinecraft().getWorld();
        if (world != null && Minecraft.getMinecraft().objectMouseOver != null) {
            final BlockPos current = Minecraft.getMinecraft().objectMouseOver.getBlockPos();
            if (current != null) {
                final IBlockState blockstate = world.getBlockState(current);
                if (blockstate != null) {
                    return blockstate.getBlock().getItem(world, current);
                }
            }
        }
        return null;
    }
    
    @Override
    public Material getLavaMaterial() {
        return Material.lava;
    }
    
    @Override
    public boolean isRightArmPoseBow(final ModelCosmetics modelCosmetics) {
        return modelCosmetics.heldItemRight == 3;
    }
    
    @Override
    public boolean isAimedBow(final ModelCosmetics modelCosmetics) {
        return modelCosmetics.aimedBow;
    }
    
    @Override
    public PotionEffect getPotionEffect(final Potion potion, final int duration, final int amplifier) {
        return new PotionEffect(potion.getId(), duration, amplifier);
    }
    
    @Override
    public boolean isSelected(final ServerSelectionList serverSelectionList, final int index) {
        return serverSelectionList.func_148193_k() == index;
    }
    
    @Override
    public void sendPluginMessage(final String channelName, final PacketBuffer packetBuffer) {
        if (this.getConnection() != null) {
            if (!channelName.equals("CCP") && !channelName.equals("LMC") && !channelName.equals("SHADOW")) {
                Debug.log(Debug.EnumDebugMode.PLUGINMESSAGE, "[OUT] " + channelName);
            }
            this.getConnection().addToSendQueue(new C17PacketCustomPayload(channelName, packetBuffer));
        }
    }
    
    @Override
    public void updateServerList(final ServerSelectionList serverSelectionList, final ServerList serverList) {
        serverSelectionList.func_148195_a(serverList);
    }
    
    @Override
    public void writeUniqueIdToBuffer(final PacketBuffer buffer, final UUID uuid) {
        buffer.writeUuid(uuid);
    }
    
    @Override
    public String readStringFromBuffer(final PacketBuffer buffer) {
        return buffer.readStringFromBuffer(buffer.readVarIntFromBuffer());
    }
    
    @Override
    public ScoreObjective getDummyScoreObjective() {
        final ScoreObjective dummyScoreObjective = new ScoreObjective(new Scoreboard(), "Displayname", new IScoreObjectiveCriteria() {
            @Override
            public boolean isReadOnly() {
                return true;
            }
            
            @Override
            public EnumRenderType getRenderType() {
                return EnumRenderType.INTEGER;
            }
            
            @Override
            public String getName() {
                return "NAME";
            }
            
            @Override
            public int setScore(final List<EntityPlayer> p_96635_1_) {
                return 1;
            }
        });
        dummyScoreObjective.getScoreboard().setObjectiveInDisplaySlot(1, dummyScoreObjective);
        for (int i = 1; i < 4; ++i) {
            dummyScoreObjective.getScoreboard().getValueFromObjective("Player " + i, dummyScoreObjective);
        }
        return dummyScoreObjective;
    }
    
    @Override
    public Object getTaggedChatComponent(final Object textComponent) {
        IChatComponent chatComponent = (IChatComponent)textComponent;
        try {
            final String json = IChatComponent.Serializer.componentToJson(chatComponent);
            final String editedJson = TagManager.getTaggedMessage(json);
            if (editedJson != null) {
                chatComponent = IChatComponent.Serializer.jsonToComponent(editedJson);
                chatComponent.appendSibling(new ChatComponentText(" \u270e").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
            }
        }
        catch (final Exception ex) {}
        return chatComponent;
    }
    
    @Override
    public FontRenderer getFontRenderer() {
        return Minecraft.getMinecraft().fontRendererObj;
    }
    
    @Override
    public void setButtonXPosition(final GuiButton button, final int x) {
        button.xPosition = x;
    }
    
    @Override
    public void setButtonYPosition(final GuiButton button, final int y) {
        button.yPosition = y;
    }
    
    @Override
    public void setTextFieldXPosition(final GuiTextField field, final int x) {
        field.xPosition = x;
    }
    
    @Override
    public void setTextFieldYPosition(final GuiTextField field, final int y) {
        field.yPosition = y;
    }
    
    @Override
    public int getXPosition(final GuiButton button) {
        return button.xPosition;
    }
    
    @Override
    public int getYPosition(final GuiButton button) {
        return button.yPosition;
    }
    
    @Override
    public int getXPosition(final GuiTextField field) {
        return field.xPosition;
    }
    
    @Override
    public int getYPosition(final GuiTextField field) {
        return field.yPosition;
    }
    
    @Override
    public int getTeamColorIndex(final ScorePlayerTeam team) {
        return team.getChatFormat().getColorIndex();
    }
    
    @Override
    public void drawButton(final GuiButton button, final int mouseX, final int mouseY) {
        button.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
    }
    
    @Override
    public TileEntitySignRenderer getCustomSignRenderer() {
        return new TileEntitySignRenderer() {
            @Override
            public void renderTileEntityAt(final TileEntitySign te, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
                SignManager.render(te);
                super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
            }
        };
    }
    
    @Override
    public String vectoString(final Vec3i vec3i) {
        return Objects.toStringHelper(vec3i).add("x", vec3i.getX()).add("y", vec3i.getY()).add("z", vec3i.getZ()).toString();
    }
    
    @Override
    public PacketBuf createPacketBuf(final ByteBuf byteBuf) {
        return new PacketBufOld(byteBuf);
    }
    
    @Override
    public int getSelectedServerInSelectionList(final ServerSelectionList serverSelectionList) {
        return serverSelectionList.func_148193_k();
    }
    
    @Override
    public boolean hasInGameFocus() {
        return Minecraft.getMinecraft().inGameHasFocus;
    }
    
    @Override
    public boolean isCurrentScreenNull() {
        return Minecraft.getMinecraft().currentScreen == null;
    }
    
    @Override
    public boolean isMinecraftChatOpen() {
        return Minecraft.getMinecraft().currentScreen instanceof GuiChat;
    }
    
    @Override
    public long getLastAttackTime() {
        return Minecraft.getMinecraft().thePlayer.getLastAttackerTime();
    }
    
    @Override
    public EntityLivingBase getLastAttackedEntity() {
        return Minecraft.getMinecraft().thePlayer.getLastAttacker();
    }
    
    @Override
    public void handleBlockBuild() {
        if (LabyMod.getSettings().oldBlockbuild) {
            final EntityPlayerSP player = LabyModCore.getMinecraft().getPlayer();
            final Minecraft mc = Minecraft.getMinecraft();
            if (player != null && player.isUsingItem() && mc.gameSettings.keyBindAttack.isKeyDown() && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                final BlockPos blockpos = mc.objectMouseOver.getBlockPos();
                mc.effectRenderer.addBlockHitEffects(blockpos, mc.objectMouseOver.sideHit);
                Boolean blockBuildAllowedByServer = LabyMod.getInstance().getServerManager().getPermissionMap().get(Permissions.Permission.BLOCKBUILD);
                if (blockBuildAllowedByServer == null) {
                    blockBuildAllowedByServer = false;
                }
                LabyMod.getInstance().getClientTickListener().setCancelSwingAnimation(!blockBuildAllowedByServer);
                player.swingItem();
                LabyMod.getInstance().getClientTickListener().setCancelSwingAnimation(false);
            }
        }
    }
    
    @Override
    public double calculateEyeMovement(final Entity entityIn, final Entity targetEntity) {
        final Vec3 eyePositionClient = targetEntity.getPositionEyes(0.0f);
        final Vec3 eyePositionEntity = entityIn.getPositionEyes(0.0f);
        Vec3 eyeLookVector = entityIn.getLook(0.0f);
        eyeLookVector = new Vec3(eyeLookVector.xCoord, 0.0, eyeLookVector.zCoord);
        final Vec3 eyeVector = new Vec3(eyePositionEntity.xCoord - eyePositionClient.xCoord, 0.0, eyePositionEntity.zCoord - eyePositionClient.zCoord).normalize().rotateYaw(1.5707964f);
        return eyeLookVector.dotProduct(eyeVector);
    }
    
    @Override
    public void setSecondLayerBit(final AbstractClientPlayer player, final int index, final byte value) {
        byte mask = player.getDataWatcher().getWatchableObjectByte(10);
        if ((mask >> index & 0x1) != value) {
            mask &= 0xFFFFFFBF;
            player.getDataWatcher().updateObject(10, mask);
        }
    }
    
    @Override
    public void sendClientSettings(final String langIn, final int viewIn, final EntityPlayer.EnumChatVisibility chatVisibilityIn, final boolean enableColorsIn, final int modelPartFlagsIn) {
        if (!LabyMod.getInstance().isInGame()) {
            return;
        }
        final NetHandlerPlayClient client = LabyModCore.getMinecraft().getPlayer().sendQueue;
        if (client != null) {
            client.addToSendQueue(new C15PacketClientSettings(langIn, viewIn, chatVisibilityIn, enableColorsIn, modelPartFlagsIn));
        }
    }
    
    @Override
    public UUID isEmotePacket(final Object packet) {
        if (!(packet instanceof S1CPacketEntityMetadata)) {
            return null;
        }
        try {
            final S1CPacketEntityMetadata metaPacket = (S1CPacketEntityMetadata)packet;
            Byte value = null;
            for (final DataWatcher.WatchableObject watchableObject : metaPacket.func_149376_c()) {
                if (watchableObject.getDataValueId() == 10) {
                    if (!(watchableObject.getObject() instanceof Byte)) {
                        continue;
                    }
                    value = (byte)watchableObject.getObject();
                }
            }
            if (value == null || value >= 0) {
                return null;
            }
            final World world = Minecraft.getMinecraft().theWorld;
            if (world == null) {
                return null;
            }
            final Entity entity = world.getEntityByID(metaPacket.getEntityId());
            return (entity == null || entity.getUniqueID().equals(LabyMod.getInstance().getPlayerUUID()) || !(entity instanceof EntityPlayer)) ? null : entity.getUniqueID();
        }
        catch (final Exception error) {
            error.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean isJumpPredicted() {
        final EntityPlayerSP player = this.getPlayer();
        return player != null && JumpPrediction.isJumpPredicted(player);
    }
    
    @Override
    public int getGameMode(final UUID uuid) {
        final NetHandlerPlayClient netHandler = LabyModCore.getMinecraft().getPlayer().sendQueue;
        if (netHandler == null) {
            return -1;
        }
        final NetworkPlayerInfo info = netHandler.getPlayerInfo(uuid);
        if (info == null) {
            return -1;
        }
        return info.getGameType().getID();
    }
    
    @Override
    public Entity getEntityMouseOver() {
        final MovingObjectPosition objectMouseOver = Minecraft.getMinecraft().objectMouseOver;
        if (objectMouseOver != null && objectMouseOver.entityHit != null) {
            return objectMouseOver.entityHit;
        }
        return null;
    }
    
    @Override
    public boolean isElytraFlying(final Entity entity) {
        return false;
    }
    
    @Override
    public boolean isWearingElytra(final Entity entity) {
        return false;
    }
    
    @Override
    public Entity getRenderViewEntity() {
        return Minecraft.getMinecraft().getRenderViewEntity();
    }
    
    @Override
    public boolean isAchievementGui(final GuiScreen screen) {
        return screen instanceof GuiAchievements;
    }
    
    @Override
    public String getBossBarMessage() {
        return BossStatus.bossName;
    }
    
    @Override
    public void setUseLeftHand(final boolean value) {
    }
    
    @Override
    public boolean isUsingLeftHand() {
        return false;
    }
    
    @Override
    public PropertyMap getPropertyMap() {
        return Minecraft.getMinecraft().getProfileProperties();
    }
}
