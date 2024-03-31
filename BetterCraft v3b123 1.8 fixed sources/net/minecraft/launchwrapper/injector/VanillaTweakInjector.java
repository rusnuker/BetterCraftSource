// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.launchwrapper.injector;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.awt.Image;
import java.awt.Frame;
import org.lwjgl.opengl.Display;
import java.nio.ByteBuffer;
import net.minecraft.launchwrapper.Launch;
import javax.imageio.ImageIO;
import java.util.Iterator;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import java.io.File;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import net.minecraft.launchwrapper.IClassTransformer;

public class VanillaTweakInjector implements IClassTransformer
{
    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        if (!"net.minecraft.client.Minecraft".equals(name)) {
            return bytes;
        }
        final ClassNode classNode = new ClassNode();
        final ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 8);
        MethodNode mainMethod = null;
        for (final MethodNode methodNode : classNode.methods) {
            if ("main".equals(methodNode.name)) {
                mainMethod = methodNode;
                break;
            }
        }
        if (mainMethod == null) {
            return bytes;
        }
        FieldNode workDirNode = null;
        for (final FieldNode fieldNode : classNode.fields) {
            final String fileTypeDescriptor = Type.getDescriptor(File.class);
            if (fileTypeDescriptor.equals(fieldNode.desc) && (fieldNode.access & 0x8) == 0x8) {
                workDirNode = fieldNode;
                break;
            }
        }
        final MethodNode injectedMethod = new MethodNode();
        final Label label = new Label();
        injectedMethod.visitLabel(label);
        injectedMethod.visitLineNumber(9001, label);
        injectedMethod.visitMethodInsn(184, "net/minecraft/launchwrapper/injector/VanillaTweakInjector", "inject", "()Ljava/io/File;");
        injectedMethod.visitFieldInsn(179, "net/minecraft/client/Minecraft", workDirNode.name, "Ljava/io/File;");
        mainMethod.instructions.insert(injectedMethod.instructions);
        final ClassWriter writer = new ClassWriter(3);
        classNode.accept(writer);
        return writer.toByteArray();
    }
    
    public static File inject() {
        System.out.println("Turning of ImageIO disk-caching");
        ImageIO.setUseCache(false);
        loadIconsOnFrames();
        System.out.println("Setting gameDir to: " + Launch.minecraftHome);
        return Launch.minecraftHome;
    }
    
    public static void loadIconsOnFrames() {
        try {
            final File smallIcon = new File(Launch.assetsDir, "icons/icon_16x16.png");
            final File bigIcon = new File(Launch.assetsDir, "icons/icon_32x32.png");
            System.out.println("Loading current icons for window from: " + smallIcon + " and " + bigIcon);
            Display.setIcon(new ByteBuffer[] { loadIcon(smallIcon), loadIcon(bigIcon) });
            final Frame[] frames = Frame.getFrames();
            if (frames != null) {
                final List<Image> icons = Arrays.asList(ImageIO.read(smallIcon), ImageIO.read(bigIcon));
                Frame[] array;
                for (int length = (array = frames).length, i = 0; i < length; ++i) {
                    final Frame frame = array[i];
                    try {
                        frame.setIconImages(icons);
                    }
                    catch (final Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    private static ByteBuffer loadIcon(final File iconFile) throws IOException {
        final BufferedImage icon = ImageIO.read(iconFile);
        final int[] rgb = icon.getRGB(0, 0, icon.getWidth(), icon.getHeight(), null, 0, icon.getWidth());
        final ByteBuffer buffer = ByteBuffer.allocate(4 * rgb.length);
        int[] array;
        for (int length = (array = rgb).length, i = 0; i < length; ++i) {
            final int color = array[i];
            buffer.putInt(color << 8 | (color >> 24 & 0xFF));
        }
        buffer.flip();
        return buffer;
    }
}
