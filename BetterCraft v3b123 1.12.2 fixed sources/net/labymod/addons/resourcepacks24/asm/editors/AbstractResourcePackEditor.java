// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.asm.editors;

import java.util.Iterator;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.ClassNode;
import net.labymod.core.asm.LabyModCoreMod;
import net.labymod.main.Source;
import net.labymod.core.asm.global.ClassEditor;

public class AbstractResourcePackEditor extends ClassEditor
{
    private String abstractResourcePackName;
    private String resourcePackFileName;
    
    public AbstractResourcePackEditor() {
        super(ClassEditorType.CLASS_NODE);
        final boolean mc18 = Source.ABOUT_MC_VERSION.startsWith("1.8");
        if (LabyModCoreMod.isObfuscated()) {
            this.abstractResourcePackName = (mc18 ? "bmx" : "ced");
            this.resourcePackFileName = (mc18 ? "a" : "a");
        }
        else {
            this.abstractResourcePackName = "net/minecraft/client/resources/AbstractResourcePack";
            this.resourcePackFileName = (LabyModCoreMod.isForge() ? (mc18 ? "field_110597_b" : "field_110597_b") : "resourcePackFile");
        }
    }
    
    @Override
    public void accept(final String name, final ClassNode node) {
        for (final MethodNode m : node.methods) {
            if (!m.desc.equals("()Ljava/lang/String;")) {
                continue;
            }
            AbstractInsnNode[] array;
            for (int length = (array = m.instructions.toArray()).length, i = 0; i < length; ++i) {
                final AbstractInsnNode abstractInsnNode = array[i];
                if (abstractInsnNode instanceof InsnNode) {
                    final InsnNode ldc = (InsnNode)abstractInsnNode;
                    if (ldc.getOpcode() == 176) {
                        final InsnList list = new InsnList();
                        list.add(new IntInsnNode(25, 0));
                        list.add(new FieldInsnNode(180, this.abstractResourcePackName, this.resourcePackFileName, "Ljava/io/File;"));
                        list.add(new MethodInsnNode(184, "net/labymod/addons/resourcepacks24/asm/ResourcepackMethods", "modifyName", "(Ljava/lang/String;Ljava/io/File;)Ljava/lang/String;", false));
                        m.instructions.insertBefore(ldc, list);
                        return;
                    }
                }
            }
        }
    }
}
