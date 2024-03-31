// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm;

import net.labymod.core.asm.global.MinecraftVisitor;
import org.apache.commons.io.FileUtils;
import java.io.File;
import org.objectweb.asm.commons.RemappingClassAdapter;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import net.labymod.support.util.Debug;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import net.labymod.core.asm.global.MainVisitor;
import net.labymod.core.asm.global.ItemRendererVisitor;
import net.labymod.core.asm.global.ModelBipedVisitor;
import net.labymod.core.asm.global.MessageDeserializerVisitor;
import net.labymod.core.asm.global.ItemVisitor;
import net.labymod.core.asm.global.ServerPingerVisitor;
import net.labymod.core.asm.global.GuiContainerVisitor;
import net.labymod.core.asm.global.RenderGlobalVisitor;
import net.labymod.core.asm.global.NetHandlerPlayClientVisitor;
import net.labymod.core.asm.global.ScaledResolutionVisitor;
import net.labymod.core.asm.global.GuiMultiplayerVisitor;
import net.labymod.core.asm.global.ServerListEntryNormal$1Visitor;
import net.labymod.core.asm.global.ServerListEntryNormalVisitor;
import net.labymod.core.asm.global.EntityRendererVisitor;
import net.labymod.core.asm.global.GuiSlotVisitor;
import net.labymod.core.asm.global.GuiDisconnectedVisitor;
import net.labymod.core.asm.global.GuiScreenVisitor;
import net.labymod.core.asm.mappings.Minecraft112MappingImplementation;
import net.labymod.core.asm.version_18.ItemStackVisitor;
import net.labymod.core.asm.version_18.ChunkPacketVisitor;
import net.labymod.core.asm.version_18.EntityPlayerVisitor;
import net.labymod.core.asm.version_18.CapeUtilsVisitor;
import net.labymod.core.asm.version_18.CapeImageBufferVisitor;
import net.labymod.core.asm.version_18.EntityLivingBaseVisitor;
import net.labymod.core.asm.version_18.EntityPlayerSPVisitor;
import java.util.HashMap;
import net.labymod.core.asm.mappings.Minecraft18MappingImplementation;
import net.labymod.core.asm.mappings.UnobfuscatedImplementation;
import net.labymod.main.Source;
import net.labymod.core.asm.global.ClassEditor;
import java.util.Map;
import net.minecraft.launchwrapper.IClassTransformer;

public class LabyModTransformer implements IClassTransformer
{
    private static LabyModTransformer instance;
    private static MappingAdapter mappingImplementation;
    private boolean debugASM;
    private Map<String, Map<String, Class<? extends ClassEditor>>> visitorsByVersion;
    
    public static MappingAdapter getMappingImplementation() {
        return LabyModTransformer.mappingImplementation;
    }
    
    public static String getVersion() {
        return Source.ABOUT_MC_VERSION;
    }
    
    public static void addVisitors() {
        final Map<String, Map<String, Class<? extends ClassEditor>>> visitorsByVersion = LabyModTransformer.instance.getVisitorsByVersion();
        if (!LabyModCoreMod.isObfuscated()) {
            LabyModTransformer.mappingImplementation = new UnobfuscatedImplementation();
        }
        if (Source.ABOUT_MC_VERSION.startsWith("1.8")) {
            if (LabyModCoreMod.isObfuscated()) {
                LabyModTransformer.mappingImplementation = new Minecraft18MappingImplementation();
            }
            final Map<String, Class<? extends ClassEditor>> visitors_18 = new HashMap<String, Class<? extends ClassEditor>>();
            addVisitor(visitors_18, LabyModTransformer.mappingImplementation.getEntityPlayerSpName(), EntityPlayerSPVisitor.class);
            addVisitor(visitors_18, LabyModTransformer.mappingImplementation.getEntityLivingBaseName(), EntityLivingBaseVisitor.class);
            addVisitor(visitors_18, "net.labymod.core_implementation.mc18.of.CapeImageBuffer", CapeImageBufferVisitor.class);
            addVisitor(visitors_18, "CapeUtils", CapeUtilsVisitor.class);
            addVisitor(visitors_18, LabyModTransformer.mappingImplementation.getEntityPlayerName(), EntityPlayerVisitor.class);
            addVisitor(visitors_18, LabyModTransformer.mappingImplementation.getS26PacketMapChunkBulkName(), ChunkPacketVisitor.class);
            addVisitor(visitors_18, LabyModTransformer.mappingImplementation.getS21PacketChunkDataName(), ChunkPacketVisitor.class);
            addVisitor(visitors_18, LabyModTransformer.mappingImplementation.getItemStackName(), ItemStackVisitor.class);
            visitorsByVersion.put("1.8", visitors_18);
        }
        if (Source.ABOUT_MC_VERSION.startsWith("1.12")) {
            if (LabyModCoreMod.isObfuscated()) {
                LabyModTransformer.mappingImplementation = new Minecraft112MappingImplementation();
            }
            final Map<String, Class<? extends ClassEditor>> visitors_19 = new HashMap<String, Class<? extends ClassEditor>>();
            addVisitor(visitors_19, LabyModTransformer.mappingImplementation.getEntityLivingBaseName(), net.labymod.core.asm.version_112.EntityLivingBaseVisitor.class);
            visitorsByVersion.put("1.12", visitors_19);
        }
        final Map<String, Class<? extends ClassEditor>> globalVisitors = new HashMap<String, Class<? extends ClassEditor>>();
        addVisitor(globalVisitors, LabyModTransformer.mappingImplementation.getGuiScreenName(), GuiScreenVisitor.class);
        addVisitor(globalVisitors, LabyModTransformer.mappingImplementation.getGuiDisconnectedName(), GuiDisconnectedVisitor.class);
        addVisitor(globalVisitors, LabyModTransformer.mappingImplementation.getGuiSlotName(), GuiSlotVisitor.class);
        addVisitor(globalVisitors, LabyModTransformer.mappingImplementation.getEntityRendererName(), EntityRendererVisitor.class);
        addVisitor(globalVisitors, LabyModTransformer.mappingImplementation.getServerListEntryNormalName(), ServerListEntryNormalVisitor.class);
        addVisitor(globalVisitors, String.valueOf(LabyModTransformer.mappingImplementation.getServerListEntryNormalName()) + "$1", ServerListEntryNormal$1Visitor.class);
        addVisitor(globalVisitors, LabyModTransformer.mappingImplementation.getGuiMultiplayerName(), GuiMultiplayerVisitor.class);
        addVisitor(globalVisitors, LabyModTransformer.mappingImplementation.getScaledResolutionName(), ScaledResolutionVisitor.class);
        addVisitor(globalVisitors, LabyModTransformer.mappingImplementation.getNetHandlerPlayClientName(), NetHandlerPlayClientVisitor.class);
        addVisitor(globalVisitors, LabyModTransformer.mappingImplementation.getRenderGlobalName(), RenderGlobalVisitor.class);
        addVisitor(globalVisitors, LabyModTransformer.mappingImplementation.getGuiContainerName(), GuiContainerVisitor.class);
        addVisitor(globalVisitors, LabyModTransformer.mappingImplementation.getServerPingerName(), ServerPingerVisitor.class);
        addVisitor(globalVisitors, LabyModTransformer.mappingImplementation.getItemName(), ItemVisitor.class);
        addVisitor(globalVisitors, LabyModTransformer.mappingImplementation.getMessageDeserializerName(), MessageDeserializerVisitor.class);
        addVisitor(globalVisitors, LabyModTransformer.mappingImplementation.getModelBipedName(), ModelBipedVisitor.class);
        addVisitor(globalVisitors, LabyModTransformer.mappingImplementation.getItemRendererName(), ItemRendererVisitor.class);
        visitorsByVersion.put("global", globalVisitors);
    }
    
    private static void addVisitor(final Map<String, Class<? extends ClassEditor>> globalVisitors, final String name, final Class<? extends ClassEditor> clazz) {
        globalVisitors.put(name.replace("/", "."), clazz);
    }
    
    public LabyModTransformer() {
        this.debugASM = (System.getProperty("debugASM") != null);
        this.visitorsByVersion = new HashMap<String, Map<String, Class<? extends ClassEditor>>>();
        LabyModTransformer.instance = this;
        final Map<String, Class<? extends ClassEditor>> globalVisitors = new HashMap<String, Class<? extends ClassEditor>>();
        globalVisitors.put("net.minecraft.client.main.Main", MainVisitor.class);
        LabyModTransformer.instance.getVisitorsByVersion().put("global", globalVisitors);
    }
    
    @Override
    public byte[] transform(final String name, final String transformedName, byte[] bytes) {
        final boolean isObfuscated = !name.equals(transformedName);
        final Set<Class<? extends ClassEditor>> editors = new HashSet<Class<? extends ClassEditor>>();
        for (final Map.Entry<String, Map<String, Class<? extends ClassEditor>>> visitorEntry : this.visitorsByVersion.entrySet()) {
            if (!visitorEntry.getKey().equals("global") && !Source.ABOUT_MC_VERSION.isEmpty() && !Source.ABOUT_MC_VERSION.startsWith(visitorEntry.getKey())) {
                continue;
            }
            if (!visitorEntry.getValue().containsKey(name)) {
                continue;
            }
            editors.add((Class)visitorEntry.getValue().get(name));
        }
        if (editors.size() != 0) {
            bytes = this.transform(name, transformedName, editors, bytes, isObfuscated);
        }
        return bytes;
    }
    
    private byte[] transform(final String name, final String transformedName, final Set<Class<? extends ClassEditor>> editors, final byte[] bytes, final boolean isObfuscated) {
        Debug.log(Debug.EnumDebugMode.ASM, "Transforming " + transformedName + "...");
        try {
            for (final Class<? extends ClassEditor> editorClass : editors) {
                final ClassEditor editor = (ClassEditor)editorClass.newInstance();
                switch (editor.getType()) {
                    case CLASS_NODE: {
                        final ClassNode node = new ClassNode();
                        final ClassReader reader = new ClassReader(bytes);
                        reader.accept(node, 0);
                        editor.accept(name, node);
                        final ClassWriter writer = new ClassWriter(3);
                        node.accept(writer);
                        return this.debugTransformedClass(transformedName, writer.toByteArray());
                    }
                    case CLASS_VISITOR: {
                        final ClassReader classReader = new ClassReader(bytes);
                        final ClassWriter classWriter = new ClassWriter(classReader, 3);
                        editor.accept(name, classWriter);
                        classReader.accept(editor, 0);
                        return this.debugTransformedClass(transformedName, classWriter.toByteArray());
                    }
                    case CLASS_VISITOR_AND_REMAPPER: {
                        ClassReader classReader = new ClassReader(bytes);
                        ClassWriter classWriter = new ClassWriter(classReader, 3);
                        editor.accept(name, classWriter);
                        classReader.accept(editor, 0);
                        classReader = new ClassReader(classWriter.toByteArray());
                        classWriter = new ClassWriter(classReader, 3);
                        final RemappingClassAdapter adapter = new RemappingClassAdapter(classWriter, new Remapper() {
                            @Override
                            public String map(final String typeName) {
                                return editor.visitMapping(typeName);
                            }
                        });
                        editor.accept(name, classWriter);
                        classReader.accept(adapter, 8);
                        return this.debugTransformedClass(transformedName, classWriter.toByteArray());
                    }
                    default: {
                        continue;
                    }
                }
            }
        }
        catch (final Exception ex) {
            ex.printStackTrace();
        }
        return bytes;
    }
    
    public byte[] debugTransformedClass(final String name, final byte[] bytes) throws Exception {
        if (this.debugASM) {
            final File asmFolder = new File("asm");
            if (!asmFolder.exists()) {
                asmFolder.mkdir();
            }
            final File byteFile = new File(String.valueOf(name) + ".class");
            final File byteFileInAsm = new File(asmFolder, String.valueOf(name) + ".class");
            byteFileInAsm.delete();
            FileUtils.writeByteArrayToFile(byteFile, bytes);
            byteFile.renameTo(byteFileInAsm);
        }
        return bytes;
    }
    
    public Map<String, Map<String, Class<? extends ClassEditor>>> getVisitorsByVersion() {
        return this.visitorsByVersion;
    }
    
    public static void resolveMinecraftClass(final String minecraftClassName) {
        final Map<String, Class<? extends ClassEditor>> globalVisitors = new HashMap<String, Class<? extends ClassEditor>>();
        globalVisitors.put(minecraftClassName.replace("/", "."), MinecraftVisitor.class);
        LabyModTransformer.instance.getVisitorsByVersion().put("global", globalVisitors);
    }
    
    public static LabyModTransformer getInstance() {
        return LabyModTransformer.instance;
    }
}
