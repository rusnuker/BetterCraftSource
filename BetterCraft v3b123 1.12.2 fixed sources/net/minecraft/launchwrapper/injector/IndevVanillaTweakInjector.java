// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.launchwrapper.injector;

import net.minecraft.launchwrapper.Launch;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ListIterator;
import java.util.Iterator;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import net.minecraft.launchwrapper.IClassTransformer;

public class IndevVanillaTweakInjector implements IClassTransformer
{
    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        final ClassNode classNode = new ClassNode();
        final ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 8);
        if (!classNode.interfaces.contains("java/lang/Runnable")) {
            return bytes;
        }
        MethodNode runMethod = null;
        for (final MethodNode methodNode : classNode.methods) {
            if ("run".equals(methodNode.name)) {
                runMethod = methodNode;
                break;
            }
        }
        if (runMethod == null) {
            return bytes;
        }
        System.out.println("Probably the minecraft class (it has run && is applet!): " + name);
        final ListIterator<AbstractInsnNode> iterator = runMethod.instructions.iterator();
        int firstSwitchJump = -1;
        while (iterator.hasNext()) {
            AbstractInsnNode instruction = iterator.next();
            if (instruction.getOpcode() == 170) {
                final TableSwitchInsnNode tableSwitchInsnNode = (TableSwitchInsnNode)instruction;
                firstSwitchJump = runMethod.instructions.indexOf(tableSwitchInsnNode.labels.get(0));
            }
            else {
                if (firstSwitchJump < 0 || runMethod.instructions.indexOf(instruction) != firstSwitchJump) {
                    continue;
                }
                int endOfSwitch = -1;
                while (iterator.hasNext()) {
                    instruction = iterator.next();
                    if (instruction.getOpcode() == 167) {
                        endOfSwitch = runMethod.instructions.indexOf(((JumpInsnNode)instruction).label);
                        break;
                    }
                }
                if (endOfSwitch < 0) {
                    continue;
                }
                while (runMethod.instructions.indexOf(instruction) != endOfSwitch && iterator.hasNext()) {
                    instruction = iterator.next();
                }
                instruction = iterator.next();
                runMethod.instructions.insertBefore(instruction, new MethodInsnNode(184, "net/minecraft/launchwrapper/injector/IndevVanillaTweakInjector", "inject", "()Ljava/io/File;"));
                runMethod.instructions.insertBefore(instruction, new VarInsnNode(58, 2));
            }
        }
        final ClassWriter writer = new ClassWriter(3);
        classNode.accept(writer);
        return writer.toByteArray();
    }
    
    public static File inject() {
        System.out.println("Turning of ImageIO disk-caching");
        ImageIO.setUseCache(false);
        VanillaTweakInjector.loadIconsOnFrames();
        System.out.println("Setting gameDir to: " + Launch.minecraftHome);
        return Launch.minecraftHome;
    }
}
