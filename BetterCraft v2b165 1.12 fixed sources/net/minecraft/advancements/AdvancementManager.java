// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements;

import java.io.BufferedReader;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystem;
import java.io.Closeable;
import java.net.URISyntaxException;
import org.apache.commons.io.IOUtils;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.FileVisitOption;
import java.nio.file.FileSystems;
import java.util.Collections;
import java.nio.file.Paths;
import net.minecraft.item.crafting.CraftingManager;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.FileUtils;
import com.google.common.collect.Maps;
import java.util.Iterator;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import javax.annotation.Nullable;
import com.google.gson.TypeAdapterFactory;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.ITextComponent;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;

public class AdvancementManager
{
    private static final Logger field_192782_a;
    private static final Gson field_192783_b;
    private static final AdvancementList field_192784_c;
    private final File field_192785_d;
    private boolean field_193768_e;
    
    static {
        field_192782_a = LogManager.getLogger();
        field_192783_b = new GsonBuilder().registerTypeHierarchyAdapter(Advancement.Builder.class, new JsonDeserializer<Advancement.Builder>() {
            @Override
            public Advancement.Builder deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
                final JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "advancement");
                return Advancement.Builder.func_192059_a(jsonobject, p_deserialize_3_);
            }
        }).registerTypeAdapter(AdvancementRewards.class, new AdvancementRewards.Deserializer()).registerTypeHierarchyAdapter(ITextComponent.class, new ITextComponent.Serializer()).registerTypeHierarchyAdapter(Style.class, new Style.Serializer()).registerTypeAdapterFactory(new EnumTypeAdapterFactory()).create();
        field_192784_c = new AdvancementList();
    }
    
    public AdvancementManager(@Nullable final File p_i47421_1_) {
        this.field_192785_d = p_i47421_1_;
        this.func_192779_a();
    }
    
    public void func_192779_a() {
        this.field_193768_e = false;
        AdvancementManager.field_192784_c.func_192087_a();
        final Map<ResourceLocation, Advancement.Builder> map = this.func_192781_c();
        this.func_192777_a(map);
        AdvancementManager.field_192784_c.func_192083_a(map);
        for (final Advancement advancement : AdvancementManager.field_192784_c.func_192088_b()) {
            if (advancement.func_192068_c() != null) {
                AdvancementTreeNode.func_192323_a(advancement);
            }
        }
    }
    
    public boolean func_193767_b() {
        return this.field_193768_e;
    }
    
    private Map<ResourceLocation, Advancement.Builder> func_192781_c() {
        if (this.field_192785_d == null) {
            return (Map<ResourceLocation, Advancement.Builder>)Maps.newHashMap();
        }
        final Map<ResourceLocation, Advancement.Builder> map = (Map<ResourceLocation, Advancement.Builder>)Maps.newHashMap();
        this.field_192785_d.mkdirs();
        for (final File file1 : FileUtils.listFiles(this.field_192785_d, new String[] { "json" }, true)) {
            final String s = FilenameUtils.removeExtension(this.field_192785_d.toURI().relativize(file1.toURI()).toString());
            final String[] astring = s.split("/", 2);
            if (astring.length == 2) {
                final ResourceLocation resourcelocation = new ResourceLocation(astring[0], astring[1]);
                try {
                    final Advancement.Builder advancement$builder = JsonUtils.gsonDeserialize(AdvancementManager.field_192783_b, FileUtils.readFileToString(file1, StandardCharsets.UTF_8), Advancement.Builder.class);
                    if (advancement$builder == null) {
                        AdvancementManager.field_192782_a.error("Couldn't load custom advancement " + resourcelocation + " from " + file1 + " as it's empty or null");
                    }
                    else {
                        map.put(resourcelocation, advancement$builder);
                    }
                }
                catch (final IllegalArgumentException | JsonParseException jsonparseexception) {
                    AdvancementManager.field_192782_a.error("Parsing error loading custom advancement " + resourcelocation, jsonparseexception);
                    this.field_193768_e = true;
                }
                catch (final IOException ioexception) {
                    AdvancementManager.field_192782_a.error("Couldn't read custom advancement " + resourcelocation + " from " + file1, ioexception);
                    this.field_193768_e = true;
                }
            }
        }
        return map;
    }
    
    private void func_192777_a(final Map<ResourceLocation, Advancement.Builder> p_192777_1_) {
        FileSystem filesystem = null;
        try {
            final URL url = AdvancementManager.class.getResource("/assets/.mcassetsroot");
            if (url != null) {
                final URI uri = url.toURI();
                Path path;
                if ("file".equals(uri.getScheme())) {
                    path = Paths.get(CraftingManager.class.getResource("/assets/minecraft/advancements").toURI());
                }
                else {
                    if (!"jar".equals(uri.getScheme())) {
                        AdvancementManager.field_192782_a.error("Unsupported scheme " + uri + " trying to list all built-in advancements (NYI?)");
                        this.field_193768_e = true;
                        return;
                    }
                    filesystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                    path = filesystem.getPath("/assets/minecraft/advancements", new String[0]);
                }
                for (final Path path2 : Files.walk(path, new FileVisitOption[0])) {
                    if ("json".equals(FilenameUtils.getExtension(path2.toString()))) {
                        final Path path3 = path.relativize(path2);
                        final String s = FilenameUtils.removeExtension(path3.toString()).replaceAll("\\\\", "/");
                        final ResourceLocation resourcelocation = new ResourceLocation("minecraft", s);
                        if (p_192777_1_.containsKey(resourcelocation)) {
                            continue;
                        }
                        BufferedReader bufferedreader = null;
                        try {
                            bufferedreader = Files.newBufferedReader(path2);
                            final Advancement.Builder advancement$builder = JsonUtils.func_193839_a(AdvancementManager.field_192783_b, bufferedreader, Advancement.Builder.class);
                            p_192777_1_.put(resourcelocation, advancement$builder);
                        }
                        catch (final JsonParseException jsonparseexception) {
                            AdvancementManager.field_192782_a.error("Parsing error loading built-in advancement " + resourcelocation, jsonparseexception);
                            this.field_193768_e = true;
                        }
                        catch (final IOException ioexception) {
                            AdvancementManager.field_192782_a.error("Couldn't read advancement " + resourcelocation + " from " + path2, ioexception);
                            this.field_193768_e = true;
                        }
                        finally {
                            IOUtils.closeQuietly(bufferedreader);
                        }
                        IOUtils.closeQuietly(bufferedreader);
                    }
                }
                return;
            }
            AdvancementManager.field_192782_a.error("Couldn't find .mcassetsroot");
            this.field_193768_e = true;
        }
        catch (final IOException | URISyntaxException urisyntaxexception) {
            AdvancementManager.field_192782_a.error("Couldn't get a list of all built-in advancement files", urisyntaxexception);
            this.field_193768_e = true;
            return;
        }
        finally {
            IOUtils.closeQuietly(filesystem);
        }
        IOUtils.closeQuietly(filesystem);
    }
    
    @Nullable
    public Advancement func_192778_a(final ResourceLocation p_192778_1_) {
        return AdvancementManager.field_192784_c.func_192084_a(p_192778_1_);
    }
    
    public Iterable<Advancement> func_192780_b() {
        return AdvancementManager.field_192784_c.func_192089_c();
    }
}
