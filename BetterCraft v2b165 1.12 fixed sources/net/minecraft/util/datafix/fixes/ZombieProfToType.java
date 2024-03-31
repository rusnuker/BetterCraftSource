// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import java.util.Random;
import net.minecraft.util.datafix.IFixableData;

public class ZombieProfToType implements IFixableData
{
    private static final Random RANDOM;
    
    static {
        RANDOM = new Random();
    }
    
    @Override
    public int getFixVersion() {
        return 502;
    }
    
    @Override
    public NBTTagCompound fixTagCompound(final NBTTagCompound compound) {
        if ("Zombie".equals(compound.getString("id")) && compound.getBoolean("IsVillager")) {
            if (!compound.hasKey("ZombieType", 99)) {
                int i = -1;
                if (compound.hasKey("VillagerProfession", 99)) {
                    try {
                        i = this.func_191277_a(compound.getInteger("VillagerProfession"));
                    }
                    catch (final RuntimeException ex) {}
                }
                if (i == -1) {
                    i = this.func_191277_a(ZombieProfToType.RANDOM.nextInt(6));
                }
                compound.setInteger("ZombieType", i);
            }
            compound.removeTag("IsVillager");
        }
        return compound;
    }
    
    private int func_191277_a(final int p_191277_1_) {
        return (p_191277_1_ >= 0 && p_191277_1_ < 6) ? p_191277_1_ : -1;
    }
}
