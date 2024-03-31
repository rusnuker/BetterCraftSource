// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements;

import com.google.gson.JsonParseException;
import net.minecraft.item.crafting.IRecipe;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.crafting.CraftingManager;
import com.google.gson.JsonArray;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import java.util.Arrays;
import net.minecraft.entity.item.EntityItem;
import java.util.Iterator;
import net.minecraft.command.CommandResultStats;
import net.minecraft.world.World;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.command.FunctionObject;
import net.minecraft.util.ResourceLocation;

public class AdvancementRewards
{
    public static final AdvancementRewards field_192114_a;
    private final int field_192115_b;
    private final ResourceLocation[] field_192116_c;
    private final ResourceLocation[] field_192117_d;
    private final FunctionObject.CacheableFunction field_193129_e;
    
    static {
        field_192114_a = new AdvancementRewards(0, new ResourceLocation[0], new ResourceLocation[0], FunctionObject.CacheableFunction.field_193519_a);
    }
    
    public AdvancementRewards(final int p_i47587_1_, final ResourceLocation[] p_i47587_2_, final ResourceLocation[] p_i47587_3_, final FunctionObject.CacheableFunction p_i47587_4_) {
        this.field_192115_b = p_i47587_1_;
        this.field_192116_c = p_i47587_2_;
        this.field_192117_d = p_i47587_3_;
        this.field_193129_e = p_i47587_4_;
    }
    
    public void func_192113_a(final EntityPlayerMP p_192113_1_) {
        p_192113_1_.addExperience(this.field_192115_b);
        final LootContext lootcontext = new LootContext.Builder(p_192113_1_.getServerWorld()).withLootedEntity(p_192113_1_).build();
        boolean flag = false;
        ResourceLocation[] field_192116_c;
        for (int length = (field_192116_c = this.field_192116_c).length, i = 0; i < length; ++i) {
            final ResourceLocation resourcelocation = field_192116_c[i];
            for (final ItemStack itemstack : p_192113_1_.world.getLootTableManager().getLootTableFromLocation(resourcelocation).generateLootForPools(p_192113_1_.getRNG(), lootcontext)) {
                if (p_192113_1_.func_191521_c(itemstack)) {
                    p_192113_1_.world.playSound(null, p_192113_1_.posX, p_192113_1_.posY, p_192113_1_.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, ((p_192113_1_.getRNG().nextFloat() - p_192113_1_.getRNG().nextFloat()) * 0.7f + 1.0f) * 2.0f);
                    flag = true;
                }
                else {
                    final EntityItem entityitem = p_192113_1_.dropItem(itemstack, false);
                    if (entityitem == null) {
                        continue;
                    }
                    entityitem.setNoPickupDelay();
                    entityitem.setOwner(p_192113_1_.getName());
                }
            }
        }
        if (flag) {
            p_192113_1_.inventoryContainer.detectAndSendChanges();
        }
        if (this.field_192117_d.length > 0) {
            p_192113_1_.func_193102_a(this.field_192117_d);
        }
        final MinecraftServer minecraftserver = p_192113_1_.mcServer;
        final FunctionObject functionobject = this.field_193129_e.func_193518_a(minecraftserver.func_193030_aL());
        if (functionobject != null) {
            final ICommandSender icommandsender = new ICommandSender() {
                @Override
                public String getName() {
                    return p_192113_1_.getName();
                }
                
                @Override
                public ITextComponent getDisplayName() {
                    return p_192113_1_.getDisplayName();
                }
                
                @Override
                public void addChatMessage(final ITextComponent component) {
                }
                
                @Override
                public boolean canCommandSenderUseCommand(final int permLevel, final String commandName) {
                    return permLevel <= 2;
                }
                
                @Override
                public BlockPos getPosition() {
                    return p_192113_1_.getPosition();
                }
                
                @Override
                public Vec3d getPositionVector() {
                    return p_192113_1_.getPositionVector();
                }
                
                @Override
                public World getEntityWorld() {
                    return p_192113_1_.world;
                }
                
                @Override
                public Entity getCommandSenderEntity() {
                    return p_192113_1_;
                }
                
                @Override
                public boolean sendCommandFeedback() {
                    return minecraftserver.worldServers[0].getGameRules().getBoolean("commandBlockOutput");
                }
                
                @Override
                public void setCommandStat(final CommandResultStats.Type type, final int amount) {
                    p_192113_1_.setCommandStat(type, amount);
                }
                
                @Override
                public MinecraftServer getServer() {
                    return p_192113_1_.getServer();
                }
            };
            minecraftserver.func_193030_aL().func_194019_a(functionobject, icommandsender);
        }
    }
    
    @Override
    public String toString() {
        return "AdvancementRewards{experience=" + this.field_192115_b + ", loot=" + Arrays.toString(this.field_192116_c) + ", recipes=" + Arrays.toString(this.field_192117_d) + ", function=" + this.field_193129_e + '}';
    }
    
    public static class Deserializer implements JsonDeserializer<AdvancementRewards>
    {
        @Override
        public AdvancementRewards deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            final JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "rewards");
            final int i = JsonUtils.getInt(jsonobject, "experience", 0);
            final JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "loot", new JsonArray());
            final ResourceLocation[] aresourcelocation = new ResourceLocation[jsonarray.size()];
            for (int j = 0; j < aresourcelocation.length; ++j) {
                aresourcelocation[j] = new ResourceLocation(JsonUtils.getString(jsonarray.get(j), "loot[" + j + "]"));
            }
            final JsonArray jsonarray2 = JsonUtils.getJsonArray(jsonobject, "recipes", new JsonArray());
            final ResourceLocation[] aresourcelocation2 = new ResourceLocation[jsonarray2.size()];
            for (int k = 0; k < aresourcelocation2.length; ++k) {
                aresourcelocation2[k] = new ResourceLocation(JsonUtils.getString(jsonarray2.get(k), "recipes[" + k + "]"));
                final IRecipe irecipe = CraftingManager.func_193373_a(aresourcelocation2[k]);
                if (irecipe == null) {
                    throw new JsonSyntaxException("Unknown recipe '" + aresourcelocation2[k] + "'");
                }
            }
            FunctionObject.CacheableFunction functionobject$cacheablefunction;
            if (jsonobject.has("function")) {
                functionobject$cacheablefunction = new FunctionObject.CacheableFunction(new ResourceLocation(JsonUtils.getString(jsonobject, "function")));
            }
            else {
                functionobject$cacheablefunction = FunctionObject.CacheableFunction.field_193519_a;
            }
            return new AdvancementRewards(i, aresourcelocation, aresourcelocation2, functionobject$cacheablefunction);
        }
    }
}
