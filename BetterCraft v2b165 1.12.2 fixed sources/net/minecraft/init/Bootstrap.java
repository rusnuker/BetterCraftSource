// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.init;

import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.LoggingPrintStream;
import java.io.OutputStream;
import net.minecraft.server.DebugLoggingPrintStream;
import net.minecraft.world.storage.loot.LootTableList;
import java.io.File;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.stats.StatList;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.biome.Biome;
import net.minecraft.entity.EntityList;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.potion.Potion;
import net.minecraft.block.BlockFire;
import net.minecraft.util.SoundEvent;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.item.ItemArmor;
import java.util.UUID;
import com.mojang.authlib.GameProfile;
import net.minecraft.util.StringUtils;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.block.BlockSkull;
import net.minecraft.util.SoundCategory;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.item.ItemDye;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.block.BlockTNT;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.item.ItemBucket;
import net.minecraft.entity.item.EntityBoat;
import java.util.Random;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.dispenser.IPosition;
import net.minecraft.world.World;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.block.BlockDispenser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.PrintStream;

public class Bootstrap
{
    public static final PrintStream SYSOUT;
    private static boolean alreadyRegistered;
    public static boolean field_194219_b;
    private static final Logger LOGGER;
    
    static {
        SYSOUT = System.out;
        LOGGER = LogManager.getLogger();
    }
    
    public static boolean isRegistered() {
        return Bootstrap.alreadyRegistered;
    }
    
    static void registerDispenserBehaviors() {
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.ARROW, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(final World worldIn, final IPosition position, final ItemStack stackIn) {
                final EntityTippedArrow entitytippedarrow = new EntityTippedArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entitytippedarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entitytippedarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.TIPPED_ARROW, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(final World worldIn, final IPosition position, final ItemStack stackIn) {
                final EntityTippedArrow entitytippedarrow = new EntityTippedArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entitytippedarrow.setPotionEffect(stackIn);
                entitytippedarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entitytippedarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.SPECTRAL_ARROW, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(final World worldIn, final IPosition position, final ItemStack stackIn) {
                final EntityArrow entityarrow = new EntitySpectralArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.EGG, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(final World worldIn, final IPosition position, final ItemStack stackIn) {
                return new EntityEgg(worldIn, position.getX(), position.getY(), position.getZ());
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.SNOWBALL, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(final World worldIn, final IPosition position, final ItemStack stackIn) {
                return new EntitySnowball(worldIn, position.getX(), position.getY(), position.getZ());
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.EXPERIENCE_BOTTLE, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(final World worldIn, final IPosition position, final ItemStack stackIn) {
                return new EntityExpBottle(worldIn, position.getX(), position.getY(), position.getZ());
            }
            
            @Override
            protected float getProjectileInaccuracy() {
                return super.getProjectileInaccuracy() * 0.5f;
            }
            
            @Override
            protected float getProjectileVelocity() {
                return super.getProjectileVelocity() * 1.25f;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.SPLASH_POTION, new IBehaviorDispenseItem() {
            @Override
            public ItemStack dispense(final IBlockSource source, final ItemStack stack) {
                return new BehaviorProjectileDispense() {
                    @Override
                    protected IProjectile getProjectileEntity(final World worldIn, final IPosition position, final ItemStack stackIn) {
                        return new EntityPotion(worldIn, position.getX(), position.getY(), position.getZ(), stack.copy());
                    }
                    
                    @Override
                    protected float getProjectileInaccuracy() {
                        return super.getProjectileInaccuracy() * 0.5f;
                    }
                    
                    @Override
                    protected float getProjectileVelocity() {
                        return super.getProjectileVelocity() * 1.25f;
                    }
                }.dispense(source, stack);
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.LINGERING_POTION, new IBehaviorDispenseItem() {
            @Override
            public ItemStack dispense(final IBlockSource source, final ItemStack stack) {
                return new BehaviorProjectileDispense() {
                    @Override
                    protected IProjectile getProjectileEntity(final World worldIn, final IPosition position, final ItemStack stackIn) {
                        return new EntityPotion(worldIn, position.getX(), position.getY(), position.getZ(), stack.copy());
                    }
                    
                    @Override
                    protected float getProjectileInaccuracy() {
                        return super.getProjectileInaccuracy() * 0.5f;
                    }
                    
                    @Override
                    protected float getProjectileVelocity() {
                        return super.getProjectileVelocity() * 1.25f;
                    }
                }.dispense(source, stack);
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.SPAWN_EGG, new BehaviorDefaultDispenseItem() {
            public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final EnumFacing enumfacing = source.getBlockState().getValue((IProperty<EnumFacing>)BlockDispenser.FACING);
                final double d0 = source.getX() + enumfacing.getFrontOffsetX();
                final double d2 = source.getBlockPos().getY() + enumfacing.getFrontOffsetY() + 0.2f;
                final double d3 = source.getZ() + enumfacing.getFrontOffsetZ();
                final Entity entity = ItemMonsterPlacer.spawnCreature(source.getWorld(), ItemMonsterPlacer.func_190908_h(stack), d0, d2, d3);
                if (entity instanceof EntityLivingBase && stack.hasDisplayName()) {
                    entity.setCustomNameTag(stack.getDisplayName());
                }
                ItemMonsterPlacer.applyItemEntityDataToEntity(source.getWorld(), null, stack, entity);
                stack.func_190918_g(1);
                return stack;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.FIREWORKS, new BehaviorDefaultDispenseItem() {
            public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final EnumFacing enumfacing = source.getBlockState().getValue((IProperty<EnumFacing>)BlockDispenser.FACING);
                final double d0 = source.getX() + enumfacing.getFrontOffsetX();
                final double d2 = source.getBlockPos().getY() + 0.2f;
                final double d3 = source.getZ() + enumfacing.getFrontOffsetZ();
                final EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(source.getWorld(), d0, d2, d3, stack);
                source.getWorld().spawnEntityInWorld(entityfireworkrocket);
                stack.func_190918_g(1);
                return stack;
            }
            
            @Override
            protected void playDispenseSound(final IBlockSource source) {
                source.getWorld().playEvent(1004, source.getBlockPos(), 0);
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.FIRE_CHARGE, new BehaviorDefaultDispenseItem() {
            public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final EnumFacing enumfacing = source.getBlockState().getValue((IProperty<EnumFacing>)BlockDispenser.FACING);
                final IPosition iposition = BlockDispenser.getDispensePosition(source);
                final double d0 = iposition.getX() + enumfacing.getFrontOffsetX() * 0.3f;
                final double d2 = iposition.getY() + enumfacing.getFrontOffsetY() * 0.3f;
                final double d3 = iposition.getZ() + enumfacing.getFrontOffsetZ() * 0.3f;
                final World world = source.getWorld();
                final Random random = world.rand;
                final double d4 = random.nextGaussian() * 0.05 + enumfacing.getFrontOffsetX();
                final double d5 = random.nextGaussian() * 0.05 + enumfacing.getFrontOffsetY();
                final double d6 = random.nextGaussian() * 0.05 + enumfacing.getFrontOffsetZ();
                world.spawnEntityInWorld(new EntitySmallFireball(world, d0, d2, d3, d4, d5, d6));
                stack.func_190918_g(1);
                return stack;
            }
            
            @Override
            protected void playDispenseSound(final IBlockSource source) {
                source.getWorld().playEvent(1018, source.getBlockPos(), 0);
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.BOAT, new BehaviorDispenseBoat(EntityBoat.Type.OAK));
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.SPRUCE_BOAT, new BehaviorDispenseBoat(EntityBoat.Type.SPRUCE));
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.BIRCH_BOAT, new BehaviorDispenseBoat(EntityBoat.Type.BIRCH));
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.JUNGLE_BOAT, new BehaviorDispenseBoat(EntityBoat.Type.JUNGLE));
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.DARK_OAK_BOAT, new BehaviorDispenseBoat(EntityBoat.Type.DARK_OAK));
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.ACACIA_BOAT, new BehaviorDispenseBoat(EntityBoat.Type.ACACIA));
        final IBehaviorDispenseItem ibehaviordispenseitem = new BehaviorDefaultDispenseItem() {
            private final BehaviorDefaultDispenseItem dispenseBehavior = new BehaviorDefaultDispenseItem();
            
            public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final ItemBucket itembucket = (ItemBucket)stack.getItem();
                final BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().getValue((IProperty<EnumFacing>)BlockDispenser.FACING));
                return itembucket.tryPlaceContainedLiquid(null, source.getWorld(), blockpos) ? new ItemStack(Items.BUCKET) : this.dispenseBehavior.dispense(source, stack);
            }
        };
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.LAVA_BUCKET, ibehaviordispenseitem);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.WATER_BUCKET, ibehaviordispenseitem);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.BUCKET, new BehaviorDefaultDispenseItem() {
            private final BehaviorDefaultDispenseItem dispenseBehavior = new BehaviorDefaultDispenseItem();
            
            public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final World world = source.getWorld();
                final BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().getValue((IProperty<EnumFacing>)BlockDispenser.FACING));
                final IBlockState iblockstate = world.getBlockState(blockpos);
                final Block block = iblockstate.getBlock();
                final Material material = iblockstate.getMaterial();
                Item item;
                if (Material.WATER.equals(material) && block instanceof BlockLiquid && iblockstate.getValue((IProperty<Integer>)BlockLiquid.LEVEL) == 0) {
                    item = Items.WATER_BUCKET;
                }
                else {
                    if (!Material.LAVA.equals(material) || !(block instanceof BlockLiquid) || iblockstate.getValue((IProperty<Integer>)BlockLiquid.LEVEL) != 0) {
                        return super.dispenseStack(source, stack);
                    }
                    item = Items.LAVA_BUCKET;
                }
                world.setBlockToAir(blockpos);
                stack.func_190918_g(1);
                if (stack.func_190926_b()) {
                    return new ItemStack(item);
                }
                if (source.getBlockTileEntity().addItemStack(new ItemStack(item)) < 0) {
                    this.dispenseBehavior.dispense(source, new ItemStack(item));
                }
                return stack;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.FLINT_AND_STEEL, new BehaviorDispenseOptional() {
            @Override
            protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final World world = source.getWorld();
                this.field_190911_b = true;
                final BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().getValue((IProperty<EnumFacing>)BlockDispenser.FACING));
                if (world.isAirBlock(blockpos)) {
                    world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
                    if (stack.attemptDamageItem(1, world.rand, null)) {
                        stack.func_190920_e(0);
                    }
                }
                else if (world.getBlockState(blockpos).getBlock() == Blocks.TNT) {
                    Blocks.TNT.onBlockDestroyedByPlayer(world, blockpos, Blocks.TNT.getDefaultState().withProperty((IProperty<Comparable>)BlockTNT.EXPLODE, true));
                    world.setBlockToAir(blockpos);
                }
                else {
                    this.field_190911_b = false;
                }
                return stack;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.DYE, new BehaviorDispenseOptional() {
            @Override
            protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                this.field_190911_b = true;
                if (EnumDyeColor.WHITE == EnumDyeColor.byDyeDamage(stack.getMetadata())) {
                    final World world = source.getWorld();
                    final BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().getValue((IProperty<EnumFacing>)BlockDispenser.FACING));
                    if (ItemDye.applyBonemeal(stack, world, blockpos)) {
                        if (!world.isRemote) {
                            world.playEvent(2005, blockpos, 0);
                        }
                    }
                    else {
                        this.field_190911_b = false;
                    }
                    return stack;
                }
                return super.dispenseStack(source, stack);
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Item.getItemFromBlock(Blocks.TNT), new BehaviorDefaultDispenseItem() {
            @Override
            protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final World world = source.getWorld();
                final BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().getValue((IProperty<EnumFacing>)BlockDispenser.FACING));
                final EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, blockpos.getX() + 0.5, blockpos.getY(), blockpos.getZ() + 0.5, null);
                world.spawnEntityInWorld(entitytntprimed);
                world.playSound(null, entitytntprimed.posX, entitytntprimed.posY, entitytntprimed.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
                stack.func_190918_g(1);
                return stack;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.SKULL, new BehaviorDispenseOptional() {
            @Override
            protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final World world = source.getWorld();
                final EnumFacing enumfacing = source.getBlockState().getValue((IProperty<EnumFacing>)BlockDispenser.FACING);
                final BlockPos blockpos = source.getBlockPos().offset(enumfacing);
                final BlockSkull blockskull = Blocks.SKULL;
                this.field_190911_b = true;
                if (world.isAirBlock(blockpos) && blockskull.canDispenserPlace(world, blockpos, stack)) {
                    if (!world.isRemote) {
                        world.setBlockState(blockpos, blockskull.getDefaultState().withProperty((IProperty<Comparable>)BlockSkull.FACING, EnumFacing.UP), 3);
                        final TileEntity tileentity = world.getTileEntity(blockpos);
                        if (tileentity instanceof TileEntitySkull) {
                            if (stack.getMetadata() == 3) {
                                GameProfile gameprofile = null;
                                if (stack.hasTagCompound()) {
                                    final NBTTagCompound nbttagcompound = stack.getTagCompound();
                                    if (nbttagcompound.hasKey("SkullOwner", 10)) {
                                        gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
                                    }
                                    else if (nbttagcompound.hasKey("SkullOwner", 8)) {
                                        final String s = nbttagcompound.getString("SkullOwner");
                                        if (!StringUtils.isNullOrEmpty(s)) {
                                            gameprofile = new GameProfile(null, s);
                                        }
                                    }
                                }
                                ((TileEntitySkull)tileentity).setPlayerProfile(gameprofile);
                            }
                            else {
                                ((TileEntitySkull)tileentity).setType(stack.getMetadata());
                            }
                            ((TileEntitySkull)tileentity).setSkullRotation(enumfacing.getOpposite().getHorizontalIndex() * 4);
                            Blocks.SKULL.checkWitherSpawn(world, blockpos, (TileEntitySkull)tileentity);
                        }
                        stack.func_190918_g(1);
                    }
                }
                else if (ItemArmor.dispenseArmor(source, stack).func_190926_b()) {
                    this.field_190911_b = false;
                }
                return stack;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Item.getItemFromBlock(Blocks.PUMPKIN), new BehaviorDispenseOptional() {
            @Override
            protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final World world = source.getWorld();
                final BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().getValue((IProperty<EnumFacing>)BlockDispenser.FACING));
                final BlockPumpkin blockpumpkin = (BlockPumpkin)Blocks.PUMPKIN;
                this.field_190911_b = true;
                if (world.isAirBlock(blockpos) && blockpumpkin.canDispenserPlace(world, blockpos)) {
                    if (!world.isRemote) {
                        world.setBlockState(blockpos, blockpumpkin.getDefaultState(), 3);
                    }
                    stack.func_190918_g(1);
                }
                else {
                    final ItemStack itemstack = ItemArmor.dispenseArmor(source, stack);
                    if (itemstack.func_190926_b()) {
                        this.field_190911_b = false;
                    }
                }
                return stack;
            }
        });
        EnumDyeColor[] values;
        for (int length = (values = EnumDyeColor.values()).length, i = 0; i < length; ++i) {
            final EnumDyeColor enumdyecolor = values[i];
            BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Item.getItemFromBlock(BlockShulkerBox.func_190952_a(enumdyecolor)), new BehaviorDispenseShulkerBox(null));
        }
    }
    
    public static void register() {
        if (!Bootstrap.alreadyRegistered) {
            Bootstrap.alreadyRegistered = true;
            redirectOutputToLog();
            SoundEvent.registerSounds();
            Block.registerBlocks();
            BlockFire.init();
            Potion.registerPotions();
            Enchantment.registerEnchantments();
            Item.registerItems();
            PotionType.registerPotionTypes();
            PotionHelper.init();
            EntityList.init();
            Biome.registerBiomes();
            registerDispenserBehaviors();
            if (!CraftingManager.func_193377_a()) {
                Bootstrap.field_194219_b = true;
                Bootstrap.LOGGER.error("Errors with built-in recipes!");
            }
            StatList.init();
            if (Bootstrap.LOGGER.isDebugEnabled()) {
                if (new AdvancementManager(null).func_193767_b()) {
                    Bootstrap.field_194219_b = true;
                    Bootstrap.LOGGER.error("Errors with built-in advancements!");
                }
                if (!LootTableList.func_193579_b()) {
                    Bootstrap.field_194219_b = true;
                    Bootstrap.LOGGER.error("Errors with built-in loot tables");
                }
            }
        }
    }
    
    private static void redirectOutputToLog() {
        if (Bootstrap.LOGGER.isDebugEnabled()) {
            System.setErr(new DebugLoggingPrintStream("STDERR", System.err));
            System.setOut(new DebugLoggingPrintStream("STDOUT", Bootstrap.SYSOUT));
        }
        else {
            System.setErr(new LoggingPrintStream("STDERR", System.err));
            System.setOut(new LoggingPrintStream("STDOUT", Bootstrap.SYSOUT));
        }
    }
    
    public static void printToSYSOUT(final String message) {
        Bootstrap.SYSOUT.println(message);
    }
    
    public static class BehaviorDispenseBoat extends BehaviorDefaultDispenseItem
    {
        private final BehaviorDefaultDispenseItem dispenseBehavior;
        private final EntityBoat.Type boatType;
        
        public BehaviorDispenseBoat(final EntityBoat.Type boatTypeIn) {
            this.dispenseBehavior = new BehaviorDefaultDispenseItem();
            this.boatType = boatTypeIn;
        }
        
        public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
            final EnumFacing enumfacing = source.getBlockState().getValue((IProperty<EnumFacing>)BlockDispenser.FACING);
            final World world = source.getWorld();
            final double d0 = source.getX() + enumfacing.getFrontOffsetX() * 1.125f;
            final double d2 = source.getY() + enumfacing.getFrontOffsetY() * 1.125f;
            final double d3 = source.getZ() + enumfacing.getFrontOffsetZ() * 1.125f;
            final BlockPos blockpos = source.getBlockPos().offset(enumfacing);
            final Material material = world.getBlockState(blockpos).getMaterial();
            double d4;
            if (Material.WATER.equals(material)) {
                d4 = 1.0;
            }
            else {
                if (!Material.AIR.equals(material) || !Material.WATER.equals(world.getBlockState(blockpos.down()).getMaterial())) {
                    return this.dispenseBehavior.dispense(source, stack);
                }
                d4 = 0.0;
            }
            final EntityBoat entityboat = new EntityBoat(world, d0, d2 + d4, d3);
            entityboat.setBoatType(this.boatType);
            entityboat.rotationYaw = enumfacing.getHorizontalAngle();
            world.spawnEntityInWorld(entityboat);
            stack.func_190918_g(1);
            return stack;
        }
        
        @Override
        protected void playDispenseSound(final IBlockSource source) {
            source.getWorld().playEvent(1000, source.getBlockPos(), 0);
        }
    }
    
    public abstract static class BehaviorDispenseOptional extends BehaviorDefaultDispenseItem
    {
        protected boolean field_190911_b;
        
        public BehaviorDispenseOptional() {
            this.field_190911_b = true;
        }
        
        @Override
        protected void playDispenseSound(final IBlockSource source) {
            source.getWorld().playEvent(this.field_190911_b ? 1000 : 1001, source.getBlockPos(), 0);
        }
    }
    
    static class BehaviorDispenseShulkerBox extends BehaviorDispenseOptional
    {
        private BehaviorDispenseShulkerBox() {
        }
        
        @Override
        protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
            final Block block = Block.getBlockFromItem(stack.getItem());
            final World world = source.getWorld();
            final EnumFacing enumfacing = source.getBlockState().getValue((IProperty<EnumFacing>)BlockDispenser.FACING);
            final BlockPos blockpos = source.getBlockPos().offset(enumfacing);
            this.field_190911_b = world.func_190527_a(block, blockpos, false, EnumFacing.DOWN, null);
            if (this.field_190911_b) {
                final EnumFacing enumfacing2 = world.isAirBlock(blockpos.down()) ? enumfacing : EnumFacing.UP;
                final IBlockState iblockstate = block.getDefaultState().withProperty(BlockShulkerBox.field_190957_a, enumfacing2);
                world.setBlockState(blockpos, iblockstate);
                final TileEntity tileentity = world.getTileEntity(blockpos);
                final ItemStack itemstack = stack.splitStack(1);
                if (itemstack.hasTagCompound()) {
                    ((TileEntityShulkerBox)tileentity).func_190586_e(itemstack.getTagCompound().getCompoundTag("BlockEntityTag"));
                }
                if (itemstack.hasDisplayName()) {
                    ((TileEntityShulkerBox)tileentity).func_190575_a(itemstack.getDisplayName());
                }
                world.updateComparatorOutputLevel(blockpos, iblockstate.getBlock());
            }
            return stack;
        }
    }
}
