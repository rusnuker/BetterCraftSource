// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.entity;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.IBlockAccess;
import net.minecraft.entity.MoverType;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import net.minecraft.item.ItemElytra;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.init.MobEffects;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.entity.IMerchant;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.world.IWorldNameable;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.world.IInteractionObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.init.Items;
import net.minecraft.client.gui.inventory.GuiEditStructure;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.client.gui.inventory.GuiEditCommandBlockMinecart;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.client.audio.ElytraSound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.MathHelper;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.stats.StatBase;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.amkgre.bettercraft.client.commands.Command;
import me.amkgre.bettercraft.client.commands.CommandManager;
import me.amkgre.bettercraft.client.events.ChatMessageSendEvent;
import net.minecraft.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.EventManager;
import me.amkgre.bettercraft.client.events.EventUpdate;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.util.EnumHand;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.client.network.NetHandlerPlayClient;

public class EntityPlayerSP extends AbstractClientPlayer
{
    public final NetHandlerPlayClient connection;
    private final StatisticsManager statWriter;
    private final RecipeBook field_192036_cb;
    private int permissionLevel;
    private double lastReportedPosX;
    private double lastReportedPosY;
    private double lastReportedPosZ;
    private float lastReportedYaw;
    private float lastReportedPitch;
    private boolean prevOnGround;
    private boolean serverSneakState;
    private boolean serverSprintState;
    private int positionUpdateTicks;
    private boolean hasValidHealth;
    private String serverBrand;
    public MovementInput movementInput;
    protected Minecraft mc;
    protected int sprintToggleTimer;
    public int sprintingTicksLeft;
    public float renderArmYaw;
    public float renderArmPitch;
    public float prevRenderArmYaw;
    public float prevRenderArmPitch;
    private int horseJumpPowerCounter;
    private float horseJumpPower;
    public float timeInPortal;
    public float prevTimeInPortal;
    private boolean handActive;
    private EnumHand activeHand;
    private boolean rowingBoat;
    private boolean autoJumpEnabled;
    private int autoJumpTime;
    private boolean wasFallFlying;
    
    public EntityPlayerSP(final Minecraft p_i47378_1_, final World p_i47378_2_, final NetHandlerPlayClient p_i47378_3_, final StatisticsManager p_i47378_4_, final RecipeBook p_i47378_5_) {
        super(p_i47378_2_, p_i47378_3_.getGameProfile());
        this.permissionLevel = 0;
        this.autoJumpEnabled = true;
        this.connection = p_i47378_3_;
        this.statWriter = p_i47378_4_;
        this.field_192036_cb = p_i47378_5_;
        this.mc = p_i47378_1_;
        this.dimension = 0;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        return false;
    }
    
    @Override
    public void heal(final float healAmount) {
    }
    
    @Override
    public boolean startRiding(final Entity entityIn, final boolean force) {
        if (!super.startRiding(entityIn, force)) {
            return false;
        }
        if (entityIn instanceof EntityMinecart) {
            this.mc.getSoundHandler().playSound(new MovingSoundMinecartRiding(this, (EntityMinecart)entityIn));
        }
        if (entityIn instanceof EntityBoat) {
            this.prevRotationYaw = entityIn.rotationYaw;
            this.rotationYaw = entityIn.rotationYaw;
            this.setRotationYawHead(entityIn.rotationYaw);
        }
        return true;
    }
    
    @Override
    public void dismountRidingEntity() {
        super.dismountRidingEntity();
        this.rowingBoat = false;
    }
    
    @Override
    public Vec3d getLook(final float partialTicks) {
        return this.getVectorForRotation(this.rotationPitch, this.rotationYaw);
    }
    
    @Override
    public void onUpdate() {
        if (this.mc.player.moveForward > 0.0f) {
            KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSprint.getKeyCode(), true);
        }
        final EventUpdate eventUpdate = new EventUpdate();
        EventManager.call(eventUpdate);
        if (this.world.isBlockLoaded(new BlockPos(this.posX, 0.0, this.posZ))) {
            super.onUpdate();
            if (this.isRiding()) {
                this.connection.sendPacket(new CPacketPlayer.Rotation(this.rotationYaw, this.rotationPitch, this.onGround));
                this.connection.sendPacket(new CPacketInput(this.moveStrafing, this.field_191988_bg, this.movementInput.jump, this.movementInput.sneak));
                final Entity entity = this.getLowestRidingEntity();
                if (entity != this && entity.canPassengerSteer()) {
                    this.connection.sendPacket(new CPacketVehicleMove(entity));
                }
            }
            else {
                this.onUpdateWalkingPlayer();
            }
        }
    }
    
    private void onUpdateWalkingPlayer() {
        final boolean flag = this.isSprinting();
        if (flag != this.serverSprintState) {
            if (flag) {
                this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.START_SPRINTING));
            }
            else {
                this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.STOP_SPRINTING));
            }
            this.serverSprintState = flag;
        }
        final boolean flag2 = this.isSneaking();
        if (flag2 != this.serverSneakState) {
            if (flag2) {
                this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.START_SNEAKING));
            }
            else {
                this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.STOP_SNEAKING));
            }
            this.serverSneakState = flag2;
        }
        if (this.isCurrentViewEntity()) {
            final AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
            final double d0 = this.posX - this.lastReportedPosX;
            final double d2 = axisalignedbb.minY - this.lastReportedPosY;
            final double d3 = this.posZ - this.lastReportedPosZ;
            final double d4 = this.rotationYaw - this.lastReportedYaw;
            final double d5 = this.rotationPitch - this.lastReportedPitch;
            ++this.positionUpdateTicks;
            boolean flag3 = d0 * d0 + d2 * d2 + d3 * d3 > 9.0E-4 || this.positionUpdateTicks >= 20;
            final boolean flag4 = d4 != 0.0 || d5 != 0.0;
            if (this.isRiding()) {
                this.connection.sendPacket(new CPacketPlayer.PositionRotation(this.motionX, -999.0, this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
                flag3 = false;
            }
            else if (flag3 && flag4) {
                this.connection.sendPacket(new CPacketPlayer.PositionRotation(this.posX, axisalignedbb.minY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround));
            }
            else if (flag3) {
                this.connection.sendPacket(new CPacketPlayer.Position(this.posX, axisalignedbb.minY, this.posZ, this.onGround));
            }
            else if (flag4) {
                this.connection.sendPacket(new CPacketPlayer.Rotation(this.rotationYaw, this.rotationPitch, this.onGround));
            }
            else if (this.prevOnGround != this.onGround) {
                this.connection.sendPacket(new CPacketPlayer(this.onGround));
            }
            if (flag3) {
                this.lastReportedPosX = this.posX;
                this.lastReportedPosY = axisalignedbb.minY;
                this.lastReportedPosZ = this.posZ;
                this.positionUpdateTicks = 0;
            }
            if (flag4) {
                this.lastReportedYaw = this.rotationYaw;
                this.lastReportedPitch = this.rotationPitch;
            }
            this.prevOnGround = this.onGround;
            this.autoJumpEnabled = this.mc.gameSettings.autoJump;
        }
    }
    
    @Nullable
    @Override
    public EntityItem dropItem(final boolean dropAll) {
        final CPacketPlayerDigging.Action cpacketplayerdigging$action = dropAll ? CPacketPlayerDigging.Action.DROP_ALL_ITEMS : CPacketPlayerDigging.Action.DROP_ITEM;
        this.connection.sendPacket(new CPacketPlayerDigging(cpacketplayerdigging$action, BlockPos.ORIGIN, EnumFacing.DOWN));
        return null;
    }
    
    @Override
    protected ItemStack dropItemAndGetStack(final EntityItem p_184816_1_) {
        return ItemStack.field_190927_a;
    }
    
    public void sendChatMessage(String message) {
        final ChatMessageSendEvent ev = new ChatMessageSendEvent(message);
        EventManager.call(ev);
        if (ev.isCancelled()) {
            return;
        }
        message = ev.getMessage();
        if (CommandManager.execute(message)) {
            return;
        }
        if (message.startsWith(CommandManager.syntax)) {
            Command.clientMSG("§7Type §d" + CommandManager.syntax + "help 1-2 §7to list all commands", true);
            return;
        }
        this.connection.sendPacket(new CPacketChatMessage(message));
    }
    
    @Override
    public void swingArm(final EnumHand hand) {
        super.swingArm(hand);
        this.connection.sendPacket(new CPacketAnimation(hand));
    }
    
    @Override
    public void respawnPlayer() {
        this.connection.sendPacket(new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN));
    }
    
    @Override
    protected void damageEntity(final DamageSource damageSrc, final float damageAmount) {
        if (!this.isEntityInvulnerable(damageSrc)) {
            this.setHealth(this.getHealth() - damageAmount);
        }
    }
    
    public void closeScreen() {
        this.connection.sendPacket(new CPacketCloseWindow(this.openContainer.windowId));
        this.closeScreenAndDropStack();
    }
    
    public void closeScreenAndDropStack() {
        this.inventory.setItemStack(ItemStack.field_190927_a);
        super.closeScreen();
        this.mc.displayGuiScreen(null);
    }
    
    public void setPlayerSPHealth(final float health) {
        if (this.hasValidHealth) {
            final float f = this.getHealth() - health;
            if (f <= 0.0f) {
                this.setHealth(health);
                if (f < 0.0f) {
                    this.hurtResistantTime = this.maxHurtResistantTime / 2;
                }
            }
            else {
                this.lastDamage = f;
                this.setHealth(this.getHealth());
                this.hurtResistantTime = this.maxHurtResistantTime;
                this.damageEntity(DamageSource.generic, f);
                this.maxHurtTime = 10;
                this.hurtTime = this.maxHurtTime;
            }
        }
        else {
            this.setHealth(health);
            this.hasValidHealth = true;
        }
    }
    
    @Override
    public void addStat(final StatBase stat, final int amount) {
        if (stat != null && stat.isIndependent) {
            super.addStat(stat, amount);
        }
    }
    
    @Override
    public void sendPlayerAbilities() {
        this.connection.sendPacket(new CPacketPlayerAbilities(this.capabilities));
    }
    
    @Override
    public boolean isUser() {
        return true;
    }
    
    protected void sendHorseJump() {
        this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.START_RIDING_JUMP, MathHelper.floor(this.getHorseJumpPower() * 100.0f)));
    }
    
    public void sendHorseInventory() {
        this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.OPEN_INVENTORY));
    }
    
    public void setServerBrand(final String brand) {
        this.serverBrand = brand;
    }
    
    public String getServerBrand() {
        return this.serverBrand;
    }
    
    public StatisticsManager getStatFileWriter() {
        return this.statWriter;
    }
    
    public RecipeBook func_192035_E() {
        return this.field_192036_cb;
    }
    
    public void func_193103_a(final IRecipe p_193103_1_) {
        if (this.field_192036_cb.func_194076_e(p_193103_1_)) {
            this.field_192036_cb.func_194074_f(p_193103_1_);
            this.connection.sendPacket(new CPacketRecipeInfo(p_193103_1_));
        }
    }
    
    public int getPermissionLevel() {
        return this.permissionLevel;
    }
    
    public void setPermissionLevel(final int p_184839_1_) {
        this.permissionLevel = p_184839_1_;
    }
    
    @Override
    public void addChatComponentMessage(final ITextComponent chatComponent, final boolean p_146105_2_) {
        if (p_146105_2_) {
            this.mc.ingameGUI.setRecordPlaying(chatComponent, false);
        }
        else {
            this.mc.ingameGUI.getChatGUI().printChatMessage(chatComponent);
        }
    }
    
    @Override
    protected boolean pushOutOfBlocks(final double x, final double y, final double z) {
        if (this.noClip) {
            return false;
        }
        final BlockPos blockpos = new BlockPos(x, y, z);
        final double d0 = x - blockpos.getX();
        final double d2 = z - blockpos.getZ();
        if (!this.isOpenBlockSpace(blockpos)) {
            int i = -1;
            double d3 = 9999.0;
            if (this.isOpenBlockSpace(blockpos.west()) && d0 < d3) {
                d3 = d0;
                i = 0;
            }
            if (this.isOpenBlockSpace(blockpos.east()) && 1.0 - d0 < d3) {
                d3 = 1.0 - d0;
                i = 1;
            }
            if (this.isOpenBlockSpace(blockpos.north()) && d2 < d3) {
                d3 = d2;
                i = 4;
            }
            if (this.isOpenBlockSpace(blockpos.south()) && 1.0 - d2 < d3) {
                d3 = 1.0 - d2;
                i = 5;
            }
            final float f = 0.1f;
            if (i == 0) {
                this.motionX = -0.10000000149011612;
            }
            if (i == 1) {
                this.motionX = 0.10000000149011612;
            }
            if (i == 4) {
                this.motionZ = -0.10000000149011612;
            }
            if (i == 5) {
                this.motionZ = 0.10000000149011612;
            }
        }
        return false;
    }
    
    private boolean isOpenBlockSpace(final BlockPos pos) {
        return !this.world.getBlockState(pos).isNormalCube() && !this.world.getBlockState(pos.up()).isNormalCube();
    }
    
    @Override
    public void setSprinting(final boolean sprinting) {
        super.setSprinting(sprinting);
        this.sprintingTicksLeft = 0;
    }
    
    public void setXPStats(final float currentXP, final int maxXP, final int level) {
        this.experience = currentXP;
        this.experienceTotal = maxXP;
        this.experienceLevel = level;
    }
    
    @Override
    public void addChatMessage(final ITextComponent component) {
        this.mc.ingameGUI.getChatGUI().printChatMessage(component);
    }
    
    @Override
    public boolean canCommandSenderUseCommand(final int permLevel, final String commandName) {
        return permLevel <= this.getPermissionLevel();
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id >= 24 && id <= 28) {
            this.setPermissionLevel(id - 24);
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    @Override
    public BlockPos getPosition() {
        return new BlockPos(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5);
    }
    
    @Override
    public void playSound(final SoundEvent soundIn, final float volume, final float pitch) {
        this.world.playSound(this.posX, this.posY, this.posZ, soundIn, this.getSoundCategory(), volume, pitch, false);
    }
    
    @Override
    public boolean isServerWorld() {
        return true;
    }
    
    @Override
    public void setActiveHand(final EnumHand hand) {
        final ItemStack itemstack = this.getHeldItem(hand);
        if (!itemstack.func_190926_b() && !this.isHandActive()) {
            super.setActiveHand(hand);
            this.handActive = true;
            this.activeHand = hand;
        }
    }
    
    @Override
    public boolean isHandActive() {
        return this.handActive;
    }
    
    @Override
    public void resetActiveHand() {
        super.resetActiveHand();
        this.handActive = false;
    }
    
    @Override
    public EnumHand getActiveHand() {
        return this.activeHand;
    }
    
    @Override
    public void notifyDataManagerChange(final DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if (EntityPlayerSP.HAND_STATES.equals(key)) {
            final boolean flag = (this.dataManager.get(EntityPlayerSP.HAND_STATES) & 0x1) > 0;
            final EnumHand enumhand = ((this.dataManager.get(EntityPlayerSP.HAND_STATES) & 0x2) > 0) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
            if (flag && !this.handActive) {
                this.setActiveHand(enumhand);
            }
            else if (!flag && this.handActive) {
                this.resetActiveHand();
            }
        }
        if (EntityPlayerSP.FLAGS.equals(key) && this.isElytraFlying() && !this.wasFallFlying) {
            this.mc.getSoundHandler().playSound(new ElytraSound(this));
        }
    }
    
    public boolean isRidingHorse() {
        final Entity entity = this.getRidingEntity();
        return this.isRiding() && entity instanceof IJumpingMount && ((IJumpingMount)entity).canJump();
    }
    
    public float getHorseJumpPower() {
        return this.horseJumpPower;
    }
    
    @Override
    public void openEditSign(final TileEntitySign signTile) {
        this.mc.displayGuiScreen(new GuiEditSign(signTile));
    }
    
    @Override
    public void displayGuiEditCommandCart(final CommandBlockBaseLogic commandBlock) {
        this.mc.displayGuiScreen(new GuiEditCommandBlockMinecart(commandBlock));
    }
    
    @Override
    public void displayGuiCommandBlock(final TileEntityCommandBlock commandBlock) {
        this.mc.displayGuiScreen(new GuiCommandBlock(commandBlock));
    }
    
    @Override
    public void openEditStructure(final TileEntityStructure structure) {
        this.mc.displayGuiScreen(new GuiEditStructure(structure));
    }
    
    @Override
    public void openBook(final ItemStack stack, final EnumHand hand) {
        final Item item = stack.getItem();
        if (item == Items.WRITABLE_BOOK) {
            this.mc.displayGuiScreen(new GuiScreenBook(this, stack, true));
        }
    }
    
    @Override
    public void displayGUIChest(final IInventory chestInventory) {
        final String s = (chestInventory instanceof IInteractionObject) ? ((IInteractionObject)chestInventory).getGuiID() : "minecraft:container";
        if ("minecraft:chest".equals(s)) {
            this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
        }
        else if ("minecraft:hopper".equals(s)) {
            this.mc.displayGuiScreen(new GuiHopper(this.inventory, chestInventory));
        }
        else if ("minecraft:furnace".equals(s)) {
            this.mc.displayGuiScreen(new GuiFurnace(this.inventory, chestInventory));
        }
        else if ("minecraft:brewing_stand".equals(s)) {
            this.mc.displayGuiScreen(new GuiBrewingStand(this.inventory, chestInventory));
        }
        else if ("minecraft:beacon".equals(s)) {
            this.mc.displayGuiScreen(new GuiBeacon(this.inventory, chestInventory));
        }
        else if (!"minecraft:dispenser".equals(s) && !"minecraft:dropper".equals(s)) {
            if ("minecraft:shulker_box".equals(s)) {
                this.mc.displayGuiScreen(new GuiShulkerBox(this.inventory, chestInventory));
            }
            else {
                this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
            }
        }
        else {
            this.mc.displayGuiScreen(new GuiDispenser(this.inventory, chestInventory));
        }
    }
    
    @Override
    public void openGuiHorseInventory(final AbstractHorse horse, final IInventory inventoryIn) {
        this.mc.displayGuiScreen(new GuiScreenHorseInventory(this.inventory, inventoryIn, horse));
    }
    
    @Override
    public void displayGui(final IInteractionObject guiOwner) {
        final String s = guiOwner.getGuiID();
        if ("minecraft:crafting_table".equals(s)) {
            this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.world));
        }
        else if ("minecraft:enchanting_table".equals(s)) {
            this.mc.displayGuiScreen(new GuiEnchantment(this.inventory, this.world, guiOwner));
        }
        else if ("minecraft:anvil".equals(s)) {
            this.mc.displayGuiScreen(new GuiRepair(this.inventory, this.world));
        }
    }
    
    @Override
    public void displayVillagerTradeGui(final IMerchant villager) {
        this.mc.displayGuiScreen(new GuiMerchant(this.inventory, villager, this.world));
    }
    
    @Override
    public void onCriticalHit(final Entity entityHit) {
        this.mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT);
    }
    
    @Override
    public void onEnchantmentCritical(final Entity entityHit) {
        this.mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT_MAGIC);
    }
    
    @Override
    public boolean isSneaking() {
        final boolean flag = this.movementInput != null && this.movementInput.sneak;
        return flag && !this.sleeping;
    }
    
    public void updateEntityActionState() {
        super.updateEntityActionState();
        if (this.isCurrentViewEntity()) {
            this.moveStrafing = this.movementInput.moveStrafe;
            this.field_191988_bg = this.movementInput.field_192832_b;
            this.isJumping = this.movementInput.jump;
            this.prevRenderArmYaw = this.renderArmYaw;
            this.prevRenderArmPitch = this.renderArmPitch;
            this.renderArmPitch += (float)((this.rotationPitch - this.renderArmPitch) * 0.5);
            this.renderArmYaw += (float)((this.rotationYaw - this.renderArmYaw) * 0.5);
        }
    }
    
    protected boolean isCurrentViewEntity() {
        return this.mc.getRenderViewEntity() == this;
    }
    
    @Override
    public void onLivingUpdate() {
        ++this.sprintingTicksLeft;
        if (this.sprintToggleTimer > 0) {
            --this.sprintToggleTimer;
        }
        this.prevTimeInPortal = this.timeInPortal;
        if (this.inPortal) {
            if (Minecraft.currentScreen != null && !Minecraft.currentScreen.doesGuiPauseGame()) {
                if (Minecraft.currentScreen instanceof GuiContainer) {
                    this.closeScreen();
                }
                this.mc.displayGuiScreen(null);
            }
            if (this.timeInPortal == 0.0f) {
                this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.BLOCK_PORTAL_TRIGGER, this.rand.nextFloat() * 0.4f + 0.8f));
            }
            this.timeInPortal += 0.0125f;
            if (this.timeInPortal >= 1.0f) {
                this.timeInPortal = 1.0f;
            }
            this.inPortal = false;
        }
        else if (this.isPotionActive(MobEffects.NAUSEA) && this.getActivePotionEffect(MobEffects.NAUSEA).getDuration() > 60) {
            this.timeInPortal += 0.006666667f;
            if (this.timeInPortal > 1.0f) {
                this.timeInPortal = 1.0f;
            }
        }
        else {
            if (this.timeInPortal > 0.0f) {
                this.timeInPortal -= 0.05f;
            }
            if (this.timeInPortal < 0.0f) {
                this.timeInPortal = 0.0f;
            }
        }
        if (this.timeUntilPortal > 0) {
            --this.timeUntilPortal;
        }
        final boolean flag = this.movementInput.jump;
        final boolean flag2 = this.movementInput.sneak;
        final float f = 0.8f;
        final boolean flag3 = this.movementInput.field_192832_b >= 0.8f;
        this.movementInput.updatePlayerMoveState();
        this.mc.func_193032_ao().func_193293_a(this.movementInput);
        if (this.isHandActive() && !this.isRiding()) {
            final MovementInput movementInput = this.movementInput;
            movementInput.moveStrafe *= 0.2f;
            final MovementInput movementInput2 = this.movementInput;
            movementInput2.field_192832_b *= 0.2f;
            this.sprintToggleTimer = 0;
        }
        boolean flag4 = false;
        if (this.autoJumpTime > 0) {
            --this.autoJumpTime;
            flag4 = true;
            this.movementInput.jump = true;
        }
        final AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        this.pushOutOfBlocks(this.posX - this.width * 0.35, axisalignedbb.minY + 0.5, this.posZ + this.width * 0.35);
        this.pushOutOfBlocks(this.posX - this.width * 0.35, axisalignedbb.minY + 0.5, this.posZ - this.width * 0.35);
        this.pushOutOfBlocks(this.posX + this.width * 0.35, axisalignedbb.minY + 0.5, this.posZ - this.width * 0.35);
        this.pushOutOfBlocks(this.posX + this.width * 0.35, axisalignedbb.minY + 0.5, this.posZ + this.width * 0.35);
        final boolean flag5 = this.getFoodStats().getFoodLevel() > 6.0f || this.capabilities.allowFlying;
        if (this.onGround && !flag2 && !flag3 && this.movementInput.field_192832_b >= 0.8f && !this.isSprinting() && flag5 && !this.isHandActive() && !this.isPotionActive(MobEffects.BLINDNESS)) {
            if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown()) {
                this.sprintToggleTimer = 7;
            }
            else {
                this.setSprinting(true);
            }
        }
        if (!this.isSprinting() && this.movementInput.field_192832_b >= 0.8f && flag5 && !this.isHandActive() && !this.isPotionActive(MobEffects.BLINDNESS) && this.mc.gameSettings.keyBindSprint.isKeyDown()) {
            this.setSprinting(true);
        }
        if (this.isSprinting() && (this.movementInput.field_192832_b < 0.8f || this.isCollidedHorizontally || !flag5)) {
            this.setSprinting(false);
        }
        if (this.capabilities.allowFlying) {
            if (this.mc.playerController.isSpectatorMode()) {
                if (!this.capabilities.isFlying) {
                    this.capabilities.isFlying = true;
                    this.sendPlayerAbilities();
                }
            }
            else if (!flag && this.movementInput.jump && !flag4) {
                if (this.flyToggleTimer == 0) {
                    this.flyToggleTimer = 7;
                }
                else {
                    this.capabilities.isFlying = !this.capabilities.isFlying;
                    this.sendPlayerAbilities();
                    this.flyToggleTimer = 0;
                }
            }
        }
        if (this.movementInput.jump && !flag && !this.onGround && this.motionY < 0.0 && !this.isElytraFlying() && !this.capabilities.isFlying) {
            final ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (itemstack.getItem() == Items.ELYTRA && ItemElytra.isBroken(itemstack)) {
                this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.START_FALL_FLYING));
            }
        }
        this.wasFallFlying = this.isElytraFlying();
        if (this.capabilities.isFlying && this.isCurrentViewEntity()) {
            if (this.movementInput.sneak) {
                this.movementInput.moveStrafe /= (float)0.3;
                this.movementInput.field_192832_b /= (float)0.3;
                this.motionY -= this.capabilities.getFlySpeed() * 3.0f;
            }
            if (this.movementInput.jump) {
                this.motionY += this.capabilities.getFlySpeed() * 3.0f;
            }
        }
        if (this.isRidingHorse()) {
            final IJumpingMount ijumpingmount = (IJumpingMount)this.getRidingEntity();
            if (this.horseJumpPowerCounter < 0) {
                ++this.horseJumpPowerCounter;
                if (this.horseJumpPowerCounter == 0) {
                    this.horseJumpPower = 0.0f;
                }
            }
            if (flag && !this.movementInput.jump) {
                this.horseJumpPowerCounter = -10;
                ijumpingmount.setJumpPower(MathHelper.floor(this.getHorseJumpPower() * 100.0f));
                this.sendHorseJump();
            }
            else if (!flag && this.movementInput.jump) {
                this.horseJumpPowerCounter = 0;
                this.horseJumpPower = 0.0f;
            }
            else if (flag) {
                ++this.horseJumpPowerCounter;
                if (this.horseJumpPowerCounter < 10) {
                    this.horseJumpPower = this.horseJumpPowerCounter * 0.1f;
                }
                else {
                    this.horseJumpPower = 0.8f + 2.0f / (this.horseJumpPowerCounter - 9) * 0.1f;
                }
            }
        }
        else {
            this.horseJumpPower = 0.0f;
        }
        super.onLivingUpdate();
        if (this.onGround && this.capabilities.isFlying && !this.mc.playerController.isSpectatorMode()) {
            this.capabilities.isFlying = false;
            this.sendPlayerAbilities();
        }
    }
    
    @Override
    public void updateRidden() {
        super.updateRidden();
        this.rowingBoat = false;
        if (this.getRidingEntity() instanceof EntityBoat) {
            final EntityBoat entityboat = (EntityBoat)this.getRidingEntity();
            entityboat.updateInputs(this.movementInput.leftKeyDown, this.movementInput.rightKeyDown, this.movementInput.forwardKeyDown, this.movementInput.backKeyDown);
            this.rowingBoat |= (this.movementInput.leftKeyDown || this.movementInput.rightKeyDown || this.movementInput.forwardKeyDown || this.movementInput.backKeyDown);
        }
    }
    
    public boolean isRowingBoat() {
        return this.rowingBoat;
    }
    
    @Nullable
    @Override
    public PotionEffect removeActivePotionEffect(@Nullable final Potion potioneffectin) {
        if (potioneffectin == MobEffects.NAUSEA) {
            this.prevTimeInPortal = 0.0f;
            this.timeInPortal = 0.0f;
        }
        return super.removeActivePotionEffect(potioneffectin);
    }
    
    @Override
    public void moveEntity(final MoverType x, final double p_70091_2_, final double p_70091_4_, final double p_70091_6_) {
        final double d0 = this.posX;
        final double d2 = this.posZ;
        super.moveEntity(x, p_70091_2_, p_70091_4_, p_70091_6_);
        this.updateAutoJump((float)(this.posX - d0), (float)(this.posZ - d2));
    }
    
    public boolean isAutoJumpEnabled() {
        return this.autoJumpEnabled;
    }
    
    protected void updateAutoJump(final float p_189810_1_, final float p_189810_2_) {
        if (this.isAutoJumpEnabled() && this.autoJumpTime <= 0 && this.onGround && !this.isSneaking() && !this.isRiding()) {
            final Vec2f vec2f = this.movementInput.getMoveVector();
            if (vec2f.x != 0.0f || vec2f.y != 0.0f) {
                final Vec3d vec3d = new Vec3d(this.posX, this.getEntityBoundingBox().minY, this.posZ);
                final double d0 = this.posX + p_189810_1_;
                final double d2 = this.posZ + p_189810_2_;
                final Vec3d vec3d2 = new Vec3d(d0, this.getEntityBoundingBox().minY, d2);
                Vec3d vec3d3 = new Vec3d(p_189810_1_, 0.0, p_189810_2_);
                final float f = this.getAIMoveSpeed();
                float f2 = (float)vec3d3.lengthSquared();
                if (f2 <= 0.001f) {
                    final float f3 = f * vec2f.x;
                    final float f4 = f * vec2f.y;
                    final float f5 = MathHelper.sin(this.rotationYaw * 0.017453292f);
                    final float f6 = MathHelper.cos(this.rotationYaw * 0.017453292f);
                    vec3d3 = new Vec3d(f3 * f6 - f4 * f5, vec3d3.yCoord, f4 * f6 + f3 * f5);
                    f2 = (float)vec3d3.lengthSquared();
                    if (f2 <= 0.001f) {
                        return;
                    }
                }
                final float f7 = (float)MathHelper.fastInvSqrt(f2);
                final Vec3d vec3d4 = vec3d3.scale(f7);
                final Vec3d vec3d5 = this.getForward();
                final float f8 = (float)(vec3d5.xCoord * vec3d4.xCoord + vec3d5.zCoord * vec3d4.zCoord);
                if (f8 >= -0.15f) {
                    BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().maxY, this.posZ);
                    final IBlockState iblockstate = this.world.getBlockState(blockpos);
                    if (iblockstate.getCollisionBoundingBox(this.world, blockpos) == null) {
                        blockpos = blockpos.up();
                        final IBlockState iblockstate2 = this.world.getBlockState(blockpos);
                        if (iblockstate2.getCollisionBoundingBox(this.world, blockpos) == null) {
                            final float f9 = 7.0f;
                            float f10 = 1.2f;
                            if (this.isPotionActive(MobEffects.JUMP_BOOST)) {
                                f10 += (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.75f;
                            }
                            final float f11 = Math.max(f * 7.0f, 1.0f / f7);
                            Vec3d vec3d6 = vec3d2.add(vec3d4.scale(f11));
                            final float f12 = this.width;
                            final float f13 = this.height;
                            final AxisAlignedBB axisalignedbb = new AxisAlignedBB(vec3d, vec3d6.addVector(0.0, f13, 0.0)).expand(f12, 0.0, f12);
                            final Vec3d lvt_19_1_ = vec3d.addVector(0.0, 0.5099999904632568, 0.0);
                            vec3d6 = vec3d6.addVector(0.0, 0.5099999904632568, 0.0);
                            final Vec3d vec3d7 = vec3d4.crossProduct(new Vec3d(0.0, 1.0, 0.0));
                            final Vec3d vec3d8 = vec3d7.scale(f12 * 0.5f);
                            final Vec3d vec3d9 = lvt_19_1_.subtract(vec3d8);
                            final Vec3d vec3d10 = vec3d6.subtract(vec3d8);
                            final Vec3d vec3d11 = lvt_19_1_.add(vec3d8);
                            final Vec3d vec3d12 = vec3d6.add(vec3d8);
                            final List<AxisAlignedBB> list = this.world.getCollisionBoxes(this, axisalignedbb);
                            if (!list.isEmpty()) {}
                            float f14 = Float.MIN_VALUE;
                            for (final AxisAlignedBB axisalignedbb2 : list) {
                                if (axisalignedbb2.intersects(vec3d9, vec3d10) || axisalignedbb2.intersects(vec3d11, vec3d12)) {
                                    f14 = (float)axisalignedbb2.maxY;
                                    final Vec3d vec3d13 = axisalignedbb2.getCenter();
                                    final BlockPos blockpos2 = new BlockPos(vec3d13);
                                    for (int i = 1; i < f10; ++i) {
                                        final BlockPos blockpos3 = blockpos2.up(i);
                                        final IBlockState iblockstate3 = this.world.getBlockState(blockpos3);
                                        final AxisAlignedBB axisalignedbb3;
                                        if ((axisalignedbb3 = iblockstate3.getCollisionBoundingBox(this.world, blockpos3)) != null) {
                                            f14 = (float)axisalignedbb3.maxY + blockpos3.getY();
                                            if (f14 - this.getEntityBoundingBox().minY > f10) {
                                                return;
                                            }
                                        }
                                        if (i > 1) {
                                            blockpos = blockpos.up();
                                            final IBlockState iblockstate4 = this.world.getBlockState(blockpos);
                                            if (iblockstate4.getCollisionBoundingBox(this.world, blockpos) != null) {
                                                return;
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                            if (f14 != Float.MIN_VALUE) {
                                final float f15 = (float)(f14 - this.getEntityBoundingBox().minY);
                                if (f15 > 0.5f && f15 <= f10) {
                                    this.autoJumpTime = 1;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
