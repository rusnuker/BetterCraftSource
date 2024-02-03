// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.version_18;

import net.labymod.core.asm.global.ClassEditor;

public class CapeUtilsVisitor extends ClassEditor
{
    public CapeUtilsVisitor() {
        super(ClassEditorType.CLASS_VISITOR_AND_REMAPPER);
    }
    
    @Override
    public String visitMapping(final String typeName) {
        if (typeName.equals("CapeUtils$1")) {
            return "net/labymod/core_implementation/mc18/of/CapeImageBuffer";
        }
        return super.visitMapping(typeName);
    }
}
