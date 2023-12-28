// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.entity.model.anim;

public class RenderResolverEntity implements IRenderResolver
{
    @Override
    public IExpression getParameter(final String name) {
        final EnumRenderParameterEntity enumrenderparameterentity = EnumRenderParameterEntity.parse(name);
        return enumrenderparameterentity;
    }
}
