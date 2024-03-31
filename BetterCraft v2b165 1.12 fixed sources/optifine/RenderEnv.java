// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.util.BlockRenderLayer;
import java.util.ArrayList;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.List;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.BlockModelRenderer;
import java.util.BitSet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.IBlockAccess;

public class RenderEnv
{
    private IBlockAccess blockAccess;
    private IBlockState blockState;
    private BlockPos blockPos;
    private int blockId;
    private int metadata;
    private int breakingAnimation;
    private int smartLeaves;
    private float[] quadBounds;
    private BitSet boundsFlags;
    private BlockModelRenderer.AmbientOcclusionFace aoFace;
    private BlockPosM colorizerBlockPosM;
    private boolean[] borderFlags;
    private boolean[] borderFlags2;
    private boolean[] borderFlags3;
    private EnumFacing[] borderDirections;
    private List<BakedQuad> listQuadsCustomizer;
    private List<BakedQuad> listQuadsCtmMultipass;
    private BakedQuad[] arrayQuadsCtm1;
    private BakedQuad[] arrayQuadsCtm2;
    private BakedQuad[] arrayQuadsCtm3;
    private BakedQuad[] arrayQuadsCtm4;
    private RegionRenderCacheBuilder regionRenderCacheBuilder;
    private ListQuadsOverlay[] listsQuadsOverlay;
    private boolean overlaysRendered;
    private static final int UNKNOWN = -1;
    private static final int FALSE = 0;
    private static final int TRUE = 1;
    
    public RenderEnv(final IBlockAccess p_i96_1_, final IBlockState p_i96_2_, final BlockPos p_i96_3_) {
        this.blockId = -1;
        this.metadata = -1;
        this.breakingAnimation = -1;
        this.smartLeaves = -1;
        this.quadBounds = new float[EnumFacing.VALUES.length * 2];
        this.boundsFlags = new BitSet(3);
        this.aoFace = new BlockModelRenderer.AmbientOcclusionFace();
        this.colorizerBlockPosM = null;
        this.borderFlags = null;
        this.borderFlags2 = null;
        this.borderFlags3 = null;
        this.borderDirections = null;
        this.listQuadsCustomizer = new ArrayList<BakedQuad>();
        this.listQuadsCtmMultipass = new ArrayList<BakedQuad>();
        this.arrayQuadsCtm1 = new BakedQuad[1];
        this.arrayQuadsCtm2 = new BakedQuad[2];
        this.arrayQuadsCtm3 = new BakedQuad[3];
        this.arrayQuadsCtm4 = new BakedQuad[4];
        this.regionRenderCacheBuilder = null;
        this.listsQuadsOverlay = new ListQuadsOverlay[BlockRenderLayer.values().length];
        this.overlaysRendered = false;
        this.blockAccess = p_i96_1_;
        this.blockState = p_i96_2_;
        this.blockPos = p_i96_3_;
    }
    
    public void reset(final IBlockAccess p_reset_1_, final IBlockState p_reset_2_, final BlockPos p_reset_3_) {
        if (this.blockAccess != p_reset_1_ || this.blockState != p_reset_2_ || this.blockPos != p_reset_3_) {
            this.blockAccess = p_reset_1_;
            this.blockState = p_reset_2_;
            this.blockPos = p_reset_3_;
            this.blockId = -1;
            this.metadata = -1;
            this.breakingAnimation = -1;
            this.smartLeaves = -1;
            this.boundsFlags.clear();
        }
    }
    
    public int getBlockId() {
        if (this.blockId < 0) {
            if (this.blockState instanceof BlockStateBase) {
                final BlockStateBase blockstatebase = (BlockStateBase)this.blockState;
                this.blockId = blockstatebase.getBlockId();
            }
            else {
                this.blockId = Block.getIdFromBlock(this.blockState.getBlock());
            }
        }
        return this.blockId;
    }
    
    public int getMetadata() {
        if (this.metadata < 0) {
            if (this.blockState instanceof BlockStateBase) {
                final BlockStateBase blockstatebase = (BlockStateBase)this.blockState;
                this.metadata = blockstatebase.getMetadata();
            }
            else {
                this.metadata = this.blockState.getBlock().getMetaFromState(this.blockState);
            }
        }
        return this.metadata;
    }
    
    public float[] getQuadBounds() {
        return this.quadBounds;
    }
    
    public BitSet getBoundsFlags() {
        return this.boundsFlags;
    }
    
    public BlockModelRenderer.AmbientOcclusionFace getAoFace() {
        return this.aoFace;
    }
    
    public boolean isBreakingAnimation(final List p_isBreakingAnimation_1_) {
        if (this.breakingAnimation == -1 && p_isBreakingAnimation_1_.size() > 0) {
            if (p_isBreakingAnimation_1_.get(0) instanceof BakedQuadRetextured) {
                this.breakingAnimation = 1;
            }
            else {
                this.breakingAnimation = 0;
            }
        }
        return this.breakingAnimation == 1;
    }
    
    public boolean isBreakingAnimation(final BakedQuad p_isBreakingAnimation_1_) {
        if (this.breakingAnimation < 0) {
            if (p_isBreakingAnimation_1_ instanceof BakedQuadRetextured) {
                this.breakingAnimation = 1;
            }
            else {
                this.breakingAnimation = 0;
            }
        }
        return this.breakingAnimation == 1;
    }
    
    public boolean isBreakingAnimation() {
        return this.breakingAnimation == 1;
    }
    
    public IBlockState getBlockState() {
        return this.blockState;
    }
    
    public BlockPosM getColorizerBlockPosM() {
        if (this.colorizerBlockPosM == null) {
            this.colorizerBlockPosM = new BlockPosM(0, 0, 0);
        }
        return this.colorizerBlockPosM;
    }
    
    public boolean[] getBorderFlags() {
        if (this.borderFlags == null) {
            this.borderFlags = new boolean[4];
        }
        return this.borderFlags;
    }
    
    public boolean[] getBorderFlags2() {
        if (this.borderFlags2 == null) {
            this.borderFlags2 = new boolean[4];
        }
        return this.borderFlags2;
    }
    
    public boolean[] getBorderFlags3() {
        if (this.borderFlags3 == null) {
            this.borderFlags3 = new boolean[4];
        }
        return this.borderFlags3;
    }
    
    public EnumFacing[] getBorderDirections() {
        if (this.borderDirections == null) {
            this.borderDirections = new EnumFacing[4];
        }
        return this.borderDirections;
    }
    
    public EnumFacing[] getBorderDirections(final EnumFacing p_getBorderDirections_1_, final EnumFacing p_getBorderDirections_2_, final EnumFacing p_getBorderDirections_3_, final EnumFacing p_getBorderDirections_4_) {
        final EnumFacing[] aenumfacing = this.getBorderDirections();
        aenumfacing[0] = p_getBorderDirections_1_;
        aenumfacing[1] = p_getBorderDirections_2_;
        aenumfacing[2] = p_getBorderDirections_3_;
        aenumfacing[3] = p_getBorderDirections_4_;
        return aenumfacing;
    }
    
    public boolean isSmartLeaves() {
        if (this.smartLeaves == -1) {
            if (Config.isTreesSmart() && this.blockState.getBlock() instanceof BlockLeaves) {
                this.smartLeaves = 1;
            }
            else {
                this.smartLeaves = 0;
            }
        }
        return this.smartLeaves == 1;
    }
    
    public List<BakedQuad> getListQuadsCustomizer() {
        return this.listQuadsCustomizer;
    }
    
    public BakedQuad[] getArrayQuadsCtm(final BakedQuad p_getArrayQuadsCtm_1_) {
        this.arrayQuadsCtm1[0] = p_getArrayQuadsCtm_1_;
        return this.arrayQuadsCtm1;
    }
    
    public BakedQuad[] getArrayQuadsCtm(final BakedQuad p_getArrayQuadsCtm_1_, final BakedQuad p_getArrayQuadsCtm_2_) {
        this.arrayQuadsCtm2[0] = p_getArrayQuadsCtm_1_;
        this.arrayQuadsCtm2[1] = p_getArrayQuadsCtm_2_;
        return this.arrayQuadsCtm2;
    }
    
    public BakedQuad[] getArrayQuadsCtm(final BakedQuad p_getArrayQuadsCtm_1_, final BakedQuad p_getArrayQuadsCtm_2_, final BakedQuad p_getArrayQuadsCtm_3_) {
        this.arrayQuadsCtm3[0] = p_getArrayQuadsCtm_1_;
        this.arrayQuadsCtm3[1] = p_getArrayQuadsCtm_2_;
        this.arrayQuadsCtm3[2] = p_getArrayQuadsCtm_3_;
        return this.arrayQuadsCtm3;
    }
    
    public BakedQuad[] getArrayQuadsCtm(final BakedQuad p_getArrayQuadsCtm_1_, final BakedQuad p_getArrayQuadsCtm_2_, final BakedQuad p_getArrayQuadsCtm_3_, final BakedQuad p_getArrayQuadsCtm_4_) {
        this.arrayQuadsCtm4[0] = p_getArrayQuadsCtm_1_;
        this.arrayQuadsCtm4[1] = p_getArrayQuadsCtm_2_;
        this.arrayQuadsCtm4[2] = p_getArrayQuadsCtm_3_;
        this.arrayQuadsCtm4[3] = p_getArrayQuadsCtm_4_;
        return this.arrayQuadsCtm4;
    }
    
    public List<BakedQuad> getListQuadsCtmMultipass(final BakedQuad[] p_getListQuadsCtmMultipass_1_) {
        this.listQuadsCtmMultipass.clear();
        if (p_getListQuadsCtmMultipass_1_ != null) {
            for (int i = 0; i < p_getListQuadsCtmMultipass_1_.length; ++i) {
                final BakedQuad bakedquad = p_getListQuadsCtmMultipass_1_[i];
                this.listQuadsCtmMultipass.add(bakedquad);
            }
        }
        return this.listQuadsCtmMultipass;
    }
    
    public RegionRenderCacheBuilder getRegionRenderCacheBuilder() {
        return this.regionRenderCacheBuilder;
    }
    
    public void setRegionRenderCacheBuilder(final RegionRenderCacheBuilder p_setRegionRenderCacheBuilder_1_) {
        this.regionRenderCacheBuilder = p_setRegionRenderCacheBuilder_1_;
    }
    
    public ListQuadsOverlay getListQuadsOverlay(final BlockRenderLayer p_getListQuadsOverlay_1_) {
        ListQuadsOverlay listquadsoverlay = this.listsQuadsOverlay[p_getListQuadsOverlay_1_.ordinal()];
        if (listquadsoverlay == null) {
            listquadsoverlay = new ListQuadsOverlay();
            this.listsQuadsOverlay[p_getListQuadsOverlay_1_.ordinal()] = listquadsoverlay;
        }
        return listquadsoverlay;
    }
    
    public boolean isOverlaysRendered() {
        return this.overlaysRendered;
    }
    
    public void setOverlaysRendered(final boolean p_setOverlaysRendered_1_) {
        this.overlaysRendered = p_setOverlaysRendered_1_;
    }
}
