// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.mappings;

import net.labymod.main.Source;
import net.labymod.core.asm.MappingAdapter;

public class UnobfuscatedImplementation implements MappingAdapter
{
    private static final boolean MC18;
    
    static {
        MC18 = Source.ABOUT_MC_VERSION.startsWith("1.8");
    }
    
    @Override
    public String getDrawWorldBackgroundName() {
        return "drawWorldBackground";
    }
    
    @Override
    public String getInitGuiName() {
        return "initGui";
    }
    
    @Override
    public String getGuiDisconnectedMessageName() {
        return "message";
    }
    
    @Override
    public String getChatComponentClassName() {
        return UnobfuscatedImplementation.MC18 ? "net/minecraft/util/IChatComponent" : "net/minecraft/util/text/ITextComponent";
    }
    
    @Override
    public String getGuiDisconnectedName() {
        return "net/minecraft/client/gui/GuiDisconnected";
    }
    
    @Override
    public String getGetUnformattedTextForChatName() {
        return UnobfuscatedImplementation.MC18 ? "getUnformattedTextForChat" : "getUnformattedComponentText";
    }
    
    @Override
    public String getParentScreenName() {
        return "parentScreen";
    }
    
    @Override
    public String getGuiScreenName() {
        return "net/minecraft/client/gui/GuiScreen";
    }
    
    @Override
    public String getConnectName() {
        return "connect";
    }
    
    @Override
    public String getOptionsBackgroundName() {
        return UnobfuscatedImplementation.MC18 ? "optionsBackground" : "OPTIONS_BACKGROUND";
    }
    
    @Override
    public String getGuiName() {
        return "net/minecraft/client/gui/Gui";
    }
    
    @Override
    public String getBindTextureName() {
        return "bindTexture";
    }
    
    @Override
    public String getResourceLocationName() {
        return "net/minecraft/util/ResourceLocation";
    }
    
    @Override
    public String getTextureManagerName() {
        return "net/minecraft/client/renderer/texture/TextureManager";
    }
    
    @Override
    public String getTessellatorName() {
        return "net/minecraft/client/renderer/Tessellator";
    }
    
    @Override
    public String getDrawName() {
        return "draw";
    }
    
    @Override
    public String getGuiSlotOverlayBackgroundName() {
        return "overlayBackground";
    }
    
    @Override
    public String getGuiConnectingName() {
        return "net/minecraft/client/multiplayer/GuiConnecting";
    }
    
    @Override
    public String getGuiSlotName() {
        return "net/minecraft/client/gui/GuiSlot";
    }
    
    @Override
    public String getEntityPlayerSpName() {
        return "net/minecraft/client/entity/EntityPlayerSP";
    }
    
    @Override
    public String getEntityLivingBaseName() {
        return "net/minecraft/entity/EntityLivingBase";
    }
    
    @Override
    public String getEntityClassName() {
        return "net/minecraft/entity/Entity";
    }
    
    @Override
    public String getOrientCameraName() {
        return "orientCamera";
    }
    
    @Override
    public String getEyeHeightName() {
        return "getEyeHeight";
    }
    
    @Override
    public String getEntityRendererName() {
        return "net/minecraft/client/renderer/EntityRenderer";
    }
    
    @Override
    public String getServerListEntryNormalName() {
        return "net/minecraft/client/gui/ServerListEntryNormal";
    }
    
    @Override
    public String getServerName() {
        return UnobfuscatedImplementation.MC18 ? "field_148301_e" : "server";
    }
    
    @Override
    public String getServerMotdName() {
        return "serverMOTD";
    }
    
    @Override
    public String getPingToServerName() {
        return "pingToServer";
    }
    
    @Override
    public String getPopulationInfoName() {
        return "populationInfo";
    }
    
    @Override
    public String getDrawEntryName() {
        return "drawEntry";
    }
    
    @Override
    public String getServerDataName() {
        return "net/minecraft/client/multiplayer/ServerData";
    }
    
    @Override
    public String getGuiMultiplayerName() {
        return "net/minecraft/client/gui/GuiMultiplayer";
    }
    
    @Override
    public String getGuiScreenDrawScreenName() {
        return "drawScreen";
    }
    
    @Override
    public String getNetHandlerPlayClientName() {
        return "net/minecraft/client/network/NetHandlerPlayClient";
    }
    
    @Override
    public String getHandleCustomPayLoadName() {
        return "handleCustomPayload";
    }
    
    @Override
    public String getCustomPayLoadPacketName() {
        return UnobfuscatedImplementation.MC18 ? "net/minecraft/network/play/server/S3FPacketCustomPayload" : "net/minecraft/network/play/server/SPacketCustomPayload";
    }
    
    @Override
    public String getChannelNameName() {
        return "getChannelName";
    }
    
    @Override
    public String getBufferDataName() {
        return "getBufferData";
    }
    
    @Override
    public String getPacketBufferName() {
        return "net/minecraft/network/PacketBuffer";
    }
    
    @Override
    public String getScaledResolutionName() {
        return "net/minecraft/client/gui/ScaledResolution";
    }
    
    @Override
    public String getStartGameName() {
        return UnobfuscatedImplementation.MC18 ? "startGame" : "init";
    }
    
    @Override
    public String getDrawSplashScreenName() {
        return "drawSplashScreen";
    }
    
    @Override
    public String getCreateDisplayName() {
        return "createDisplay";
    }
    
    @Override
    public String getRenderEntitiesName() {
        return "renderEntities";
    }
    
    @Override
    public String getRenderGlobalName() {
        return "net/minecraft/client/renderer/RenderGlobal";
    }
    
    @Override
    public String getCountEntitiesRenderedName() {
        return "countEntitiesRendered";
    }
    
    @Override
    public String getCountEntitiesTotalName() {
        return "countEntitiesTotal";
    }
    
    @Override
    public String getICameraClassName() {
        return "net/minecraft/client/renderer/culling/ICamera";
    }
    
    @Override
    public String getMinecraftName() {
        return "net/minecraft/client/Minecraft";
    }
    
    @Override
    public String getFullscreenName() {
        return "fullscreen";
    }
    
    @Override
    public String getToggleFullscreenName() {
        return "toggleFullscreen";
    }
    
    @Override
    public String getSetInitialDisplayModeName() {
        return "setInitialDisplayMode";
    }
    
    @Override
    public String getItemRendererName() {
        return "net/minecraft/client/renderer/ItemRenderer";
    }
    
    @Override
    public String getTransformFirstPersonItemName() {
        return UnobfuscatedImplementation.MC18 ? "transformFirstPersonItem" : "transformFirstPerson";
    }
    
    @Override
    public String getItemStackName() {
        return "net/minecraft/item/ItemStack";
    }
    
    @Override
    public String getRenderItemInFirstPersonName() {
        return "renderItemInFirstPerson";
    }
    
    @Override
    public String getItemToRenderName() {
        return UnobfuscatedImplementation.MC18 ? "itemToRender" : "itemStackMainHand";
    }
    
    @Override
    public String getPushMatrixName() {
        return "pushMatrix";
    }
    
    @Override
    public String getModelPlayerName() {
        return "net/minecraft/client/model/ModelPlayer";
    }
    
    @Override
    public String getServerPingerName() {
        return UnobfuscatedImplementation.MC18 ? "net/minecraft/client/network/OldServerPinger" : "net/minecraft/client/network/ServerPinger";
    }
    
    @Override
    public String getGuiContainerName() {
        return "net/minecraft/client/gui/inventory/GuiContainer";
    }
    
    @Override
    public String getGuiContainerMouseReleasedName() {
        return "mouseReleased";
    }
    
    @Override
    public String getSlotName() {
        return "net/minecraft/inventory/Slot";
    }
    
    @Override
    public String getSlotGetStackName() {
        return "getStack";
    }
    
    @Override
    public String getItemName() {
        return "net/minecraft/item/Item";
    }
    
    @Override
    public String getItemBucketName() {
        return "net/minecraft/item/ItemBucket";
    }
    
    @Override
    public String getLastAttackerTimeName() {
        return UnobfuscatedImplementation.MC18 ? "lastAttackerTime" : "lastAttackedEntityTime";
    }
    
    @Override
    public String getRightClickMouseName() {
        return "rightClickMouse";
    }
    
    @Override
    public String getEntityPlayerName() {
        return "net/minecraft/entity/player/EntityPlayer";
    }
    
    @Override
    public String getS21PacketChunkDataName() {
        return UnobfuscatedImplementation.MC18 ? "net/minecraft/network/play/server/S21PacketChunkData" : "net/minecraft/network/play/server/SPacketChunkData";
    }
    
    @Override
    public String getS26PacketMapChunkBulkName() {
        return "net/minecraft/network/play/server/S26PacketMapChunkBulk";
    }
    
    @Override
    public String getReadPacketDataName() {
        return "readPacketData";
    }
    
    @Override
    public String getBootstrapName() {
        return "net/minecraft/init/Bootstrap";
    }
    
    @Override
    public String getPrintToSYSOUTName() {
        return "printToSYSOUT";
    }
    
    @Override
    public String getMessageDeserializerName() {
        return UnobfuscatedImplementation.MC18 ? "net/minecraft/util/MessageDeserializer" : "net/minecraft/network/NettyPacketDecoder";
    }
    
    @Override
    public String getModelBipedName() {
        return "net/minecraft/client/model/ModelBiped";
    }
    
    @Override
    public String getHandleResourcePackName() {
        return "handleResourcePack";
    }
    
    @Override
    public String getPacketResourcePackSendName() {
        return "net/minecraft/network/play/server/S48PacketResourcePackSend";
    }
    
    @Override
    public String getRunTickName() {
        return "runTick";
    }
    
    @Override
    public String getThirdPersonViewName() {
        return "thirdPersonView";
    }
    
    @Override
    public String getMCDefaultResourcePack() {
        return "mcDefaultResourcePack";
    }
}
