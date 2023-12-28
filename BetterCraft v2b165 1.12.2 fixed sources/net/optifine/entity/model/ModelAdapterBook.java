// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.entity.model;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import optifine.Config;
import optifine.Reflector;
import net.minecraft.client.renderer.tileentity.TileEntityEnchantmentTableRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.model.ModelBase;
import net.minecraft.tileentity.TileEntityEnchantmentTable;

public class ModelAdapterBook extends ModelAdapter
{
    public ModelAdapterBook() {
        super(TileEntityEnchantmentTable.class, "book", 0.0f);
    }
    
    @Override
    public ModelBase makeModel() {
        return new ModelBook();
    }
    
    @Override
    public ModelRenderer getModelRenderer(final ModelBase model, final String modelPart) {
        if (!(model instanceof ModelBook)) {
            return null;
        }
        final ModelBook modelbook = (ModelBook)model;
        if (modelPart.equals("cover_right")) {
            return modelbook.coverRight;
        }
        if (modelPart.equals("cover_left")) {
            return modelbook.coverLeft;
        }
        if (modelPart.equals("pages_right")) {
            return modelbook.pagesRight;
        }
        if (modelPart.equals("pages_left")) {
            return modelbook.pagesLeft;
        }
        if (modelPart.equals("flipping_page_right")) {
            return modelbook.flippingPageRight;
        }
        if (modelPart.equals("flipping_page_left")) {
            return modelbook.flippingPageLeft;
        }
        return modelPart.equals("book_spine") ? modelbook.bookSpine : null;
    }
    
    @Override
    public IEntityRenderer makeEntityRender(final ModelBase modelBase, final float shadowSize) {
        final TileEntityRendererDispatcher tileentityrendererdispatcher = TileEntityRendererDispatcher.instance;
        TileEntitySpecialRenderer tileentityspecialrenderer = tileentityrendererdispatcher.getSpecialRendererByClass(TileEntityEnchantmentTable.class);
        if (!(tileentityspecialrenderer instanceof TileEntityEnchantmentTableRenderer)) {
            return null;
        }
        if (tileentityspecialrenderer.getEntityClass() == null) {
            tileentityspecialrenderer = new TileEntityEnchantmentTableRenderer();
            tileentityspecialrenderer.setRendererDispatcher(tileentityrendererdispatcher);
        }
        if (!Reflector.TileEntityEnchantmentTableRenderer_modelBook.exists()) {
            Config.warn("Field not found: TileEntityEnchantmentTableRenderer.modelBook");
            return null;
        }
        Reflector.setFieldValue(tileentityspecialrenderer, Reflector.TileEntityEnchantmentTableRenderer_modelBook, modelBase);
        return tileentityspecialrenderer;
    }
}
