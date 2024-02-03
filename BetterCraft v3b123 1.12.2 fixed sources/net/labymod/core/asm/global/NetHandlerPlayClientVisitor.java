// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.global;

import java.util.Iterator;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.ClassNode;
import net.labymod.core.asm.LabyModTransformer;
import net.labymod.main.Source;

public class NetHandlerPlayClientVisitor extends ClassEditor
{
    private static final boolean MC_18;
    private String packetCustomPayloadName;
    private String handleCustomPayloadName;
    private String getChannelNameName;
    private String getBufferDataName;
    private String packetBufferName;
    private String handleResourcePackName;
    private String packetResourcePackSendName;
    
    static {
        MC_18 = Source.ABOUT_MC_VERSION.startsWith("1.8");
    }
    
    public NetHandlerPlayClientVisitor() {
        super(ClassEditorType.CLASS_NODE);
        this.packetCustomPayloadName = LabyModTransformer.getMappingImplementation().getCustomPayLoadPacketName();
        this.handleCustomPayloadName = LabyModTransformer.getMappingImplementation().getHandleCustomPayLoadName();
        this.getChannelNameName = LabyModTransformer.getMappingImplementation().getChannelNameName();
        this.getBufferDataName = LabyModTransformer.getMappingImplementation().getBufferDataName();
        this.packetBufferName = LabyModTransformer.getMappingImplementation().getPacketBufferName();
        this.handleResourcePackName = LabyModTransformer.getMappingImplementation().getHandleResourcePackName();
        this.packetResourcePackSendName = LabyModTransformer.getMappingImplementation().getPacketResourcePackSendName();
    }
    
    @Override
    public void accept(final String name, final ClassNode node) {
        for (final MethodNode methodNode : node.methods) {
            if (methodNode.name.equals(this.handleCustomPayloadName) && methodNode.desc.equals("(L" + this.packetCustomPayloadName + ";)V")) {
                AbstractInsnNode returnNode = null;
                AbstractInsnNode[] array;
                for (int length = (array = methodNode.instructions.toArray()).length, i = 0; i < length; ++i) {
                    final AbstractInsnNode insnNode = array[i];
                    if (insnNode instanceof InsnNode && insnNode.getOpcode() == 177) {
                        returnNode = insnNode;
                        break;
                    }
                }
                if (returnNode == null) {
                    continue;
                }
                final InsnList insnList = new InsnList();
                insnList.add(new VarInsnNode(25, 1));
                insnList.add(new MethodInsnNode(182, this.packetCustomPayloadName, this.getChannelNameName, "()Ljava/lang/String;", false));
                insnList.add(new VarInsnNode(25, 1));
                insnList.add(new MethodInsnNode(182, this.packetCustomPayloadName, this.getBufferDataName, "()L" + this.packetBufferName + ";", false));
                insnList.add(new MethodInsnNode(184, "BytecodeMethods", "onReceivePluginMessage", "(Ljava/lang/String;L" + this.packetBufferName + ";)V", false));
                methodNode.instructions.insertBefore(returnNode, insnList);
            }
            if (NetHandlerPlayClientVisitor.MC_18 && methodNode.name.equals(this.handleResourcePackName) && methodNode.desc.equals("(L" + this.packetResourcePackSendName + ";)V")) {
                MethodInsnNode methodInsnNode = null;
                AbstractInsnNode[] array2;
                for (int length2 = (array2 = methodNode.instructions.toArray()).length, j = 0; j < length2; ++j) {
                    final AbstractInsnNode insnNode = array2[j];
                    if (insnNode instanceof MethodInsnNode && insnNode.getOpcode() == 182) {
                        methodInsnNode = (MethodInsnNode)insnNode;
                        break;
                    }
                }
                if (methodInsnNode == null) {
                    continue;
                }
                methodNode.instructions.insert(methodInsnNode, new MethodInsnNode(184, "BytecodeMethods", "modifyResourcePackURL", "(Ljava/lang/String;)Ljava/lang/String;", false));
            }
        }
    }
}
