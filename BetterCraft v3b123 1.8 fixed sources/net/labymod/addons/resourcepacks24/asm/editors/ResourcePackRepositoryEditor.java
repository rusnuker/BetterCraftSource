// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.asm.editors;

import org.objectweb.asm.tree.AbstractInsnNode;
import java.util.Iterator;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.ClassNode;
import net.labymod.core.asm.LabyModCoreMod;
import net.labymod.main.Source;
import net.labymod.core.asm.global.ClassEditor;

public class ResourcePackRepositoryEditor extends ClassEditor
{
    private String updateRepositoryEntriesAllName;
    
    public ResourcePackRepositoryEditor() {
        super(ClassEditorType.CLASS_NODE);
        final boolean mc18 = Source.ABOUT_MC_VERSION.startsWith("1.8");
        if (LabyModCoreMod.isObfuscated()) {
            this.updateRepositoryEntriesAllName = (mc18 ? "a" : "b");
        }
        else {
            this.updateRepositoryEntriesAllName = (LabyModCoreMod.isForge() ? (mc18 ? "a" : "b") : "updateRepositoryEntriesAll");
        }
    }
    
    @Override
    public void accept(final String name, final ClassNode node) {
        for (final MethodNode m : node.methods) {
            if (m.name.equals(this.updateRepositoryEntriesAllName)) {
                if (!m.desc.equals("()V")) {
                    continue;
                }
                AbstractInsnNode[] array;
                for (int length = (array = m.instructions.toArray()).length, i = 0; i < length; ++i) {
                    final AbstractInsnNode abstractInsnNode = array[i];
                    if (abstractInsnNode instanceof MethodInsnNode) {
                        final MethodInsnNode method = (MethodInsnNode)abstractInsnNode;
                        if (method.getOpcode() == 183 && method.desc.equals("()Ljava/util/List;")) {
                            method.setOpcode(184);
                            method.owner = "net/labymod/addons/resourcepacks24/asm/ResourcepackMethods";
                            method.name = "getFiles";
                            method.desc = "(Ljava/lang/Object;)Ljava/util/List;";
                            return;
                        }
                    }
                }
            }
        }
    }
}
