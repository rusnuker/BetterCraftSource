// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.asm;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import net.labymod.core.asm.global.ClassEditor;
import net.labymod.main.Source;
import net.labymod.addons.resourcepacks24.asm.editors.ResourcePackRepositoryEditor;
import net.labymod.addons.resourcepacks24.asm.editors.AbstractResourcePackEditor;
import net.minecraft.launchwrapper.IClassTransformer;

public class Resourcepacks24Transformer implements IClassTransformer
{
    private static final AbstractResourcePackEditor ABSTRACT_RESOURCE_PACK_EDITOR;
    private static final ResourcePackRepositoryEditor RESOURCE_PACK_REPOSITORY_EDITOR;
    private static final boolean MC18;
    
    static {
        ABSTRACT_RESOURCE_PACK_EDITOR = new AbstractResourcePackEditor();
        RESOURCE_PACK_REPOSITORY_EDITOR = new ResourcePackRepositoryEditor();
        MC18 = Source.ABOUT_MC_VERSION.startsWith("1.8");
    }
    
    @Override
    public byte[] transform(final String name, final String transformedName, byte[] bytes) {
        bytes = this.transform(name, bytes, Resourcepacks24Transformer.ABSTRACT_RESOURCE_PACK_EDITOR, "net.minecraft.client.resources.AbstractResourcePack", "bmx", "ced");
        bytes = this.transform(name, bytes, Resourcepacks24Transformer.RESOURCE_PACK_REPOSITORY_EDITOR, "net.minecraft.client.resources.ResourcePackRepository", "bnm", "ceu");
        return bytes;
    }
    
    public byte[] transform(final String name, final byte[] bytes, final ClassEditor editor, final String mcp, final String mc18, final String mc112) {
        if (Resourcepacks24Transformer.MC18) {
            if (!name.equals(mcp) && !name.equals(mc18)) {
                return bytes;
            }
        }
        else if (!name.equals(mcp) && !name.equals(mc112)) {
            return bytes;
        }
        try {
            final ClassNode node = new ClassNode();
            final ClassReader reader = new ClassReader(bytes);
            reader.accept(node, 0);
            editor.accept(name, node);
            final ClassWriter writer = new ClassWriter(3);
            node.accept(writer);
            return writer.toByteArray();
        }
        catch (final Exception error) {
            error.printStackTrace();
            return bytes;
        }
    }
}
