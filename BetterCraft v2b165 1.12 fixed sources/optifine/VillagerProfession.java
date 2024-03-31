// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

public class VillagerProfession
{
    private int profession;
    private int[] careers;
    
    public VillagerProfession(final int p_i100_1_) {
        this(p_i100_1_, null);
    }
    
    public VillagerProfession(final int p_i101_1_, final int p_i101_2_) {
        this(p_i101_1_, new int[] { p_i101_2_ });
    }
    
    public VillagerProfession(final int p_i102_1_, final int[] p_i102_2_) {
        this.profession = p_i102_1_;
        this.careers = p_i102_2_;
    }
    
    public boolean matches(final int p_matches_1_, final int p_matches_2_) {
        return this.profession == p_matches_1_ && (this.careers == null || Config.equalsOne(p_matches_2_, this.careers));
    }
    
    private boolean hasCareer(final int p_hasCareer_1_) {
        return this.careers != null && Config.equalsOne(p_hasCareer_1_, this.careers);
    }
    
    public boolean addCareer(final int p_addCareer_1_) {
        if (this.careers == null) {
            this.careers = new int[] { p_addCareer_1_ };
            return true;
        }
        if (this.hasCareer(p_addCareer_1_)) {
            return false;
        }
        this.careers = Config.addIntToArray(this.careers, p_addCareer_1_);
        return true;
    }
    
    public int getProfession() {
        return this.profession;
    }
    
    public int[] getCareers() {
        return this.careers;
    }
    
    @Override
    public String toString() {
        return (this.careers == null) ? new StringBuilder().append(this.profession).toString() : (this.profession + ":" + Config.arrayToString(this.careers));
    }
}
