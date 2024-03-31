// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item.crafting;

import net.minecraft.util.NonNullList;
import javax.annotation.Nullable;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.util.Iterator;
import java.net.URI;
import java.net.URL;
import com.google.gson.Gson;
import java.nio.file.FileSystem;
import java.io.Closeable;
import java.net.URISyntaxException;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import com.google.gson.JsonParseException;
import java.io.Reader;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonObject;
import org.apache.commons.io.FilenameUtils;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.FileVisitOption;
import java.nio.file.FileSystems;
import java.util.Collections;
import java.nio.file.Paths;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import org.apache.logging.log4j.Logger;

public class CraftingManager
{
    private static final Logger field_192422_a;
    private static int field_193381_c;
    public static final RegistryNamespaced<ResourceLocation, IRecipe> field_193380_a;
    
    static {
        field_192422_a = LogManager.getLogger();
        field_193380_a = new RegistryNamespaced<ResourceLocation, IRecipe>();
    }
    
    public static boolean func_193377_a() {
        try {
            func_193379_a("armordye", new RecipesArmorDyes());
            func_193379_a("bookcloning", new RecipeBookCloning());
            func_193379_a("mapcloning", new RecipesMapCloning());
            func_193379_a("mapextending", new RecipesMapExtending());
            func_193379_a("fireworks", new RecipeFireworks());
            func_193379_a("repairitem", new RecipeRepairItem());
            func_193379_a("tippedarrow", new RecipeTippedArrow());
            func_193379_a("bannerduplicate", new RecipesBanners.RecipeDuplicatePattern());
            func_193379_a("banneraddpattern", new RecipesBanners.RecipeAddPattern());
            func_193379_a("shielddecoration", new ShieldRecipes.Decoration());
            func_193379_a("shulkerboxcoloring", new ShulkerBoxRecipes.ShulkerBoxColoring());
            return func_192420_c();
        }
        catch (final Throwable var1) {
            return false;
        }
    }
    
    private static boolean func_192420_c() {
        FileSystem filesystem = null;
        final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        boolean flag4;
        try {
            final URL url = CraftingManager.class.getResource("/assets/.mcassetsroot");
            if (url != null) {
                final URI uri = url.toURI();
                Path path;
                if ("file".equals(uri.getScheme())) {
                    path = Paths.get(CraftingManager.class.getResource("/assets/minecraft/recipes").toURI());
                }
                else {
                    if (!"jar".equals(uri.getScheme())) {
                        CraftingManager.field_192422_a.error("Unsupported scheme " + uri + " trying to list all recipes");
                        final boolean flag2 = false;
                        return flag2;
                    }
                    filesystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                    path = filesystem.getPath("/assets/minecraft/recipes", new String[0]);
                }
                for (final Path path2 : Files.walk(path, new FileVisitOption[0])) {
                    if ("json".equals(FilenameUtils.getExtension(path2.toString()))) {
                        final Path path3 = path.relativize(path2);
                        final String s = FilenameUtils.removeExtension(path3.toString()).replaceAll("\\\\", "/");
                        final ResourceLocation resourcelocation = new ResourceLocation(s);
                        BufferedReader bufferedreader = null;
                        try {
                            bufferedreader = Files.newBufferedReader(path2);
                            func_193379_a(s, func_193376_a(JsonUtils.func_193839_a(gson, bufferedreader, JsonObject.class)));
                        }
                        catch (final JsonParseException jsonparseexception) {
                            CraftingManager.field_192422_a.error("Parsing error loading recipe " + resourcelocation, jsonparseexception);
                            final boolean flag3 = false;
                            return flag3;
                        }
                        catch (final IOException ioexception) {
                            CraftingManager.field_192422_a.error("Couldn't read recipe " + resourcelocation + " from " + path2, ioexception);
                            final boolean flag3 = false;
                            return flag3;
                        }
                        finally {
                            IOUtils.closeQuietly(bufferedreader);
                        }
                        IOUtils.closeQuietly(bufferedreader);
                    }
                }
                return true;
            }
            CraftingManager.field_192422_a.error("Couldn't find .mcassetsroot");
            flag4 = false;
        }
        catch (final IOException | URISyntaxException urisyntaxexception) {
            CraftingManager.field_192422_a.error("Couldn't get a list of all recipe files", urisyntaxexception);
            flag4 = false;
            return flag4;
        }
        finally {
            IOUtils.closeQuietly(filesystem);
        }
        IOUtils.closeQuietly(filesystem);
        return flag4;
    }
    
    private static IRecipe func_193376_a(final JsonObject p_193376_0_) {
        final String s = JsonUtils.getString(p_193376_0_, "type");
        if ("crafting_shaped".equals(s)) {
            return ShapedRecipes.func_193362_a(p_193376_0_);
        }
        if ("crafting_shapeless".equals(s)) {
            return ShapelessRecipes.func_193363_a(p_193376_0_);
        }
        throw new JsonSyntaxException("Invalid or unsupported recipe type '" + s + "'");
    }
    
    public static void func_193379_a(final String p_193379_0_, final IRecipe p_193379_1_) {
        func_193372_a(new ResourceLocation(p_193379_0_), p_193379_1_);
    }
    
    public static void func_193372_a(final ResourceLocation p_193372_0_, final IRecipe p_193372_1_) {
        if (CraftingManager.field_193380_a.containsKey(p_193372_0_)) {
            throw new IllegalStateException("Duplicate recipe ignored with ID " + p_193372_0_);
        }
        CraftingManager.field_193380_a.register(CraftingManager.field_193381_c++, p_193372_0_, p_193372_1_);
    }
    
    public static ItemStack findMatchingRecipe(final InventoryCrafting p_82787_0_, final World craftMatrix) {
        for (final IRecipe irecipe : CraftingManager.field_193380_a) {
            if (irecipe.matches(p_82787_0_, craftMatrix)) {
                return irecipe.getCraftingResult(p_82787_0_);
            }
        }
        return ItemStack.field_190927_a;
    }
    
    @Nullable
    public static IRecipe func_192413_b(final InventoryCrafting p_192413_0_, final World p_192413_1_) {
        for (final IRecipe irecipe : CraftingManager.field_193380_a) {
            if (irecipe.matches(p_192413_0_, p_192413_1_)) {
                return irecipe;
            }
        }
        return null;
    }
    
    public static NonNullList<ItemStack> getRemainingItems(final InventoryCrafting p_180303_0_, final World craftMatrix) {
        for (final IRecipe irecipe : CraftingManager.field_193380_a) {
            if (irecipe.matches(p_180303_0_, craftMatrix)) {
                return irecipe.getRemainingItems(p_180303_0_);
            }
        }
        final NonNullList<ItemStack> nonnulllist = NonNullList.func_191197_a(p_180303_0_.getSizeInventory(), ItemStack.field_190927_a);
        for (int i = 0; i < nonnulllist.size(); ++i) {
            nonnulllist.set(i, p_180303_0_.getStackInSlot(i));
        }
        return nonnulllist;
    }
    
    @Nullable
    public static IRecipe func_193373_a(final ResourceLocation p_193373_0_) {
        return CraftingManager.field_193380_a.getObject(p_193373_0_);
    }
    
    public static int func_193375_a(final IRecipe p_193375_0_) {
        return CraftingManager.field_193380_a.getIDForObject(p_193375_0_);
    }
    
    @Nullable
    public static IRecipe func_193374_a(final int p_193374_0_) {
        return CraftingManager.field_193380_a.getObjectById(p_193374_0_);
    }
}
