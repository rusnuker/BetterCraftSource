// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.client.gui.FontRenderer;
import java.util.Random;

public class EnchantmentNameParts
{
    private static final EnchantmentNameParts INSTANCE;
    private final Random rand;
    private final String[] namePartsArray;
    
    static {
        INSTANCE = new EnchantmentNameParts();
    }
    
    public EnchantmentNameParts() {
        this.rand = new Random();
        this.namePartsArray = "the elder scrolls klaatu berata niktu xyzzy bless curse light darkness fire air earth water hot dry cold wet ignite snuff embiggen twist shorten stretch fiddle destroy imbue galvanize enchant free limited range of towards inside sphere cube self other ball mental physical grow shrink demon elemental spirit animal creature beast humanoid undead fresh stale phnglui mglwnafh cthulhu rlyeh wgahnagl fhtagnbaguette".split(" ");
    }
    
    public static EnchantmentNameParts getInstance() {
        return EnchantmentNameParts.INSTANCE;
    }
    
    public String generateNewRandomName(final FontRenderer p_148334_1_, final int p_148334_2_) {
        final int i = this.rand.nextInt(2) + 3;
        String s = "";
        for (int j = 0; j < i; ++j) {
            if (j > 0) {
                s = String.valueOf(s) + " ";
            }
            s = String.valueOf(s) + this.namePartsArray[this.rand.nextInt(this.namePartsArray.length)];
        }
        final List<String> list = p_148334_1_.listFormattedStringToWidth(s, p_148334_2_);
        return StringUtils.join((list.size() >= 2) ? list.subList(0, 2) : list, " ");
    }
    
    public void reseedRandomGenerator(final long seed) {
        this.rand.setSeed(seed);
    }
}
