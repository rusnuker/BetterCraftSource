// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements;

import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.item.Item;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public class DisplayInfo
{
    private final ITextComponent field_192300_a;
    private final ITextComponent field_193225_b;
    private final ItemStack field_192301_b;
    private final ResourceLocation field_192302_c;
    private final FrameType field_192303_d;
    private final boolean field_193226_f;
    private final boolean field_193227_g;
    private final boolean field_193228_h;
    private float field_192304_e;
    private float field_192305_f;
    
    public DisplayInfo(final ItemStack p_i47586_1_, final ITextComponent p_i47586_2_, final ITextComponent p_i47586_3_, @Nullable final ResourceLocation p_i47586_4_, final FrameType p_i47586_5_, final boolean p_i47586_6_, final boolean p_i47586_7_, final boolean p_i47586_8_) {
        this.field_192300_a = p_i47586_2_;
        this.field_193225_b = p_i47586_3_;
        this.field_192301_b = p_i47586_1_;
        this.field_192302_c = p_i47586_4_;
        this.field_192303_d = p_i47586_5_;
        this.field_193226_f = p_i47586_6_;
        this.field_193227_g = p_i47586_7_;
        this.field_193228_h = p_i47586_8_;
    }
    
    public void func_192292_a(final float p_192292_1_, final float p_192292_2_) {
        this.field_192304_e = p_192292_1_;
        this.field_192305_f = p_192292_2_;
    }
    
    public ITextComponent func_192297_a() {
        return this.field_192300_a;
    }
    
    public ITextComponent func_193222_b() {
        return this.field_193225_b;
    }
    
    public ItemStack func_192298_b() {
        return this.field_192301_b;
    }
    
    @Nullable
    public ResourceLocation func_192293_c() {
        return this.field_192302_c;
    }
    
    public FrameType func_192291_d() {
        return this.field_192303_d;
    }
    
    public float func_192299_e() {
        return this.field_192304_e;
    }
    
    public float func_192296_f() {
        return this.field_192305_f;
    }
    
    public boolean func_193223_h() {
        return this.field_193226_f;
    }
    
    public boolean func_193220_i() {
        return this.field_193227_g;
    }
    
    public boolean func_193224_j() {
        return this.field_193228_h;
    }
    
    public static DisplayInfo func_192294_a(final JsonObject p_192294_0_, final JsonDeserializationContext p_192294_1_) {
        final ITextComponent itextcomponent = JsonUtils.deserializeClass(p_192294_0_, "title", p_192294_1_, (Class<? extends ITextComponent>)ITextComponent.class);
        final ITextComponent itextcomponent2 = JsonUtils.deserializeClass(p_192294_0_, "description", p_192294_1_, (Class<? extends ITextComponent>)ITextComponent.class);
        if (itextcomponent != null && itextcomponent2 != null) {
            final ItemStack itemstack = func_193221_a(JsonUtils.getJsonObject(p_192294_0_, "icon"));
            final ResourceLocation resourcelocation = p_192294_0_.has("background") ? new ResourceLocation(JsonUtils.getString(p_192294_0_, "background")) : null;
            final FrameType frametype = p_192294_0_.has("frame") ? FrameType.func_192308_a(JsonUtils.getString(p_192294_0_, "frame")) : FrameType.TASK;
            final boolean flag = JsonUtils.getBoolean(p_192294_0_, "show_toast", true);
            final boolean flag2 = JsonUtils.getBoolean(p_192294_0_, "announce_to_chat", true);
            final boolean flag3 = JsonUtils.getBoolean(p_192294_0_, "hidden", false);
            return new DisplayInfo(itemstack, itextcomponent, itextcomponent2, resourcelocation, frametype, flag, flag2, flag3);
        }
        throw new JsonSyntaxException("Both title and description must be set");
    }
    
    private static ItemStack func_193221_a(final JsonObject p_193221_0_) {
        if (!p_193221_0_.has("item")) {
            throw new JsonSyntaxException("Unsupported icon type, currently only items are supported (add 'item' key)");
        }
        final Item item = JsonUtils.getItem(p_193221_0_, "item");
        final int i = JsonUtils.getInt(p_193221_0_, "data", 0);
        return new ItemStack(item, 1, i);
    }
    
    public void func_192290_a(final PacketBuffer p_192290_1_) {
        p_192290_1_.writeTextComponent(this.field_192300_a);
        p_192290_1_.writeTextComponent(this.field_193225_b);
        p_192290_1_.writeItemStackToBuffer(this.field_192301_b);
        p_192290_1_.writeEnumValue(this.field_192303_d);
        int i = 0;
        if (this.field_192302_c != null) {
            i |= 0x1;
        }
        if (this.field_193226_f) {
            i |= 0x2;
        }
        if (this.field_193228_h) {
            i |= 0x4;
        }
        p_192290_1_.writeInt(i);
        if (this.field_192302_c != null) {
            p_192290_1_.func_192572_a(this.field_192302_c);
        }
        p_192290_1_.writeFloat(this.field_192304_e);
        p_192290_1_.writeFloat(this.field_192305_f);
    }
    
    public static DisplayInfo func_192295_b(final PacketBuffer p_192295_0_) throws IOException {
        final ITextComponent itextcomponent = p_192295_0_.readTextComponent();
        final ITextComponent itextcomponent2 = p_192295_0_.readTextComponent();
        final ItemStack itemstack = p_192295_0_.readItemStackFromBuffer();
        final FrameType frametype = p_192295_0_.readEnumValue(FrameType.class);
        final int i = p_192295_0_.readInt();
        final ResourceLocation resourcelocation = ((i & 0x1) != 0x0) ? p_192295_0_.func_192575_l() : null;
        final boolean flag = (i & 0x2) != 0x0;
        final boolean flag2 = (i & 0x4) != 0x0;
        final DisplayInfo displayinfo = new DisplayInfo(itemstack, itextcomponent, itextcomponent2, resourcelocation, frametype, flag, false, flag2);
        displayinfo.func_192292_a(p_192295_0_.readFloat(), p_192295_0_.readFloat());
        return displayinfo;
    }
}
