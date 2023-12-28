// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import java.util.Iterator;
import java.util.Set;
import com.google.common.collect.Sets;
import java.util.Collection;
import javax.annotation.Nullable;
import java.io.StringReader;
import net.minecraft.util.JsonUtils;
import java.io.Reader;
import java.lang.reflect.Type;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import java.util.List;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;

public class ModelBlock
{
    private static final Logger LOGGER;
    @VisibleForTesting
    static final Gson SERIALIZER;
    private final List<BlockPart> elements;
    private final boolean gui3d;
    private final boolean ambientOcclusion;
    private final ItemCameraTransforms cameraTransforms;
    private final List<ItemOverride> overrides;
    public String name;
    @VisibleForTesting
    protected final Map<String, String> textures;
    @VisibleForTesting
    protected ModelBlock parent;
    @VisibleForTesting
    protected ResourceLocation parentLocation;
    
    static {
        LOGGER = LogManager.getLogger();
        SERIALIZER = new GsonBuilder().registerTypeAdapter(ModelBlock.class, new Deserializer()).registerTypeAdapter(BlockPart.class, new BlockPart.Deserializer()).registerTypeAdapter(BlockPartFace.class, new BlockPartFace.Deserializer()).registerTypeAdapter(BlockFaceUV.class, new BlockFaceUV.Deserializer()).registerTypeAdapter(ItemTransformVec3f.class, new ItemTransformVec3f.Deserializer()).registerTypeAdapter(ItemCameraTransforms.class, new ItemCameraTransforms.Deserializer()).registerTypeAdapter(ItemOverride.class, new ItemOverride.Deserializer()).create();
    }
    
    public static ModelBlock deserialize(final Reader readerIn) {
        return JsonUtils.gsonDeserialize(ModelBlock.SERIALIZER, readerIn, ModelBlock.class, false);
    }
    
    public static ModelBlock deserialize(final String jsonString) {
        return deserialize(new StringReader(jsonString));
    }
    
    public ModelBlock(@Nullable final ResourceLocation parentLocationIn, final List<BlockPart> elementsIn, final Map<String, String> texturesIn, final boolean ambientOcclusionIn, final boolean gui3dIn, final ItemCameraTransforms cameraTransformsIn, final List<ItemOverride> overridesIn) {
        this.name = "";
        this.elements = elementsIn;
        this.ambientOcclusion = ambientOcclusionIn;
        this.gui3d = gui3dIn;
        this.textures = texturesIn;
        this.parentLocation = parentLocationIn;
        this.cameraTransforms = cameraTransformsIn;
        this.overrides = overridesIn;
    }
    
    public List<BlockPart> getElements() {
        return (this.elements.isEmpty() && this.hasParent()) ? this.parent.getElements() : this.elements;
    }
    
    private boolean hasParent() {
        return this.parent != null;
    }
    
    public boolean isAmbientOcclusion() {
        return this.hasParent() ? this.parent.isAmbientOcclusion() : this.ambientOcclusion;
    }
    
    public boolean isGui3d() {
        return this.gui3d;
    }
    
    public boolean isResolved() {
        return this.parentLocation == null || (this.parent != null && this.parent.isResolved());
    }
    
    public void getParentFromMap(final Map<ResourceLocation, ModelBlock> p_178299_1_) {
        if (this.parentLocation != null) {
            this.parent = p_178299_1_.get(this.parentLocation);
        }
    }
    
    public Collection<ResourceLocation> getOverrideLocations() {
        final Set<ResourceLocation> set = (Set<ResourceLocation>)Sets.newHashSet();
        for (final ItemOverride itemoverride : this.overrides) {
            set.add(itemoverride.getLocation());
        }
        return set;
    }
    
    protected List<ItemOverride> getOverrides() {
        return this.overrides;
    }
    
    public ItemOverrideList createOverrides() {
        return this.overrides.isEmpty() ? ItemOverrideList.NONE : new ItemOverrideList(this.overrides);
    }
    
    public boolean isTexturePresent(final String textureName) {
        return !"missingno".equals(this.resolveTextureName(textureName));
    }
    
    public String resolveTextureName(String textureName) {
        if (!this.startsWithHash(textureName)) {
            textureName = String.valueOf('#') + textureName;
        }
        return this.resolveTextureName(textureName, new Bookkeep(this, null));
    }
    
    private String resolveTextureName(final String textureName, final Bookkeep p_178302_2_) {
        if (!this.startsWithHash(textureName)) {
            return textureName;
        }
        if (this == p_178302_2_.modelExt) {
            ModelBlock.LOGGER.warn("Unable to resolve texture due to upward reference: {} in {}", textureName, this.name);
            return "missingno";
        }
        String s = this.textures.get(textureName.substring(1));
        if (s == null && this.hasParent()) {
            s = this.parent.resolveTextureName(textureName, p_178302_2_);
        }
        p_178302_2_.modelExt = this;
        if (s != null && this.startsWithHash(s)) {
            s = p_178302_2_.model.resolveTextureName(s, p_178302_2_);
        }
        return (s != null && !this.startsWithHash(s)) ? s : "missingno";
    }
    
    private boolean startsWithHash(final String hash) {
        return hash.charAt(0) == '#';
    }
    
    @Nullable
    public ResourceLocation getParentLocation() {
        return this.parentLocation;
    }
    
    public ModelBlock getRootModel() {
        return this.hasParent() ? this.parent.getRootModel() : this;
    }
    
    public ItemCameraTransforms getAllTransforms() {
        final ItemTransformVec3f itemtransformvec3f = this.getTransform(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND);
        final ItemTransformVec3f itemtransformvec3f2 = this.getTransform(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
        final ItemTransformVec3f itemtransformvec3f3 = this.getTransform(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND);
        final ItemTransformVec3f itemtransformvec3f4 = this.getTransform(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND);
        final ItemTransformVec3f itemtransformvec3f5 = this.getTransform(ItemCameraTransforms.TransformType.HEAD);
        final ItemTransformVec3f itemtransformvec3f6 = this.getTransform(ItemCameraTransforms.TransformType.GUI);
        final ItemTransformVec3f itemtransformvec3f7 = this.getTransform(ItemCameraTransforms.TransformType.GROUND);
        final ItemTransformVec3f itemtransformvec3f8 = this.getTransform(ItemCameraTransforms.TransformType.FIXED);
        return new ItemCameraTransforms(itemtransformvec3f, itemtransformvec3f2, itemtransformvec3f3, itemtransformvec3f4, itemtransformvec3f5, itemtransformvec3f6, itemtransformvec3f7, itemtransformvec3f8);
    }
    
    private ItemTransformVec3f getTransform(final ItemCameraTransforms.TransformType type) {
        return (this.parent != null && !this.cameraTransforms.hasCustomTransform(type)) ? this.parent.getTransform(type) : this.cameraTransforms.getTransform(type);
    }
    
    public static void checkModelHierarchy(final Map<ResourceLocation, ModelBlock> p_178312_0_) {
        for (final ModelBlock modelblock : p_178312_0_.values()) {
            try {
                for (ModelBlock modelblock2 = modelblock.parent, modelblock3 = modelblock2.parent; modelblock2 != modelblock3; modelblock2 = modelblock2.parent, modelblock3 = modelblock3.parent.parent) {}
                throw new LoopException();
            }
            catch (final NullPointerException ex) {}
        }
    }
    
    public static class Deserializer implements JsonDeserializer<ModelBlock>
    {
        @Override
        public ModelBlock deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            final JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            final List<BlockPart> list = this.getModelElements(p_deserialize_3_, jsonobject);
            final String s = this.getParent(jsonobject);
            final Map<String, String> map = this.getTextures(jsonobject);
            final boolean flag = this.getAmbientOcclusionEnabled(jsonobject);
            ItemCameraTransforms itemcameratransforms = ItemCameraTransforms.DEFAULT;
            if (jsonobject.has("display")) {
                final JsonObject jsonobject2 = JsonUtils.getJsonObject(jsonobject, "display");
                itemcameratransforms = p_deserialize_3_.deserialize(jsonobject2, ItemCameraTransforms.class);
            }
            final List<ItemOverride> list2 = this.getItemOverrides(p_deserialize_3_, jsonobject);
            final ResourceLocation resourcelocation = s.isEmpty() ? null : new ResourceLocation(s);
            return new ModelBlock(resourcelocation, list, map, flag, true, itemcameratransforms, list2);
        }
        
        protected List<ItemOverride> getItemOverrides(final JsonDeserializationContext deserializationContext, final JsonObject object) {
            final List<ItemOverride> list = (List<ItemOverride>)Lists.newArrayList();
            if (object.has("overrides")) {
                for (final JsonElement jsonelement : JsonUtils.getJsonArray(object, "overrides")) {
                    list.add(deserializationContext.deserialize(jsonelement, ItemOverride.class));
                }
            }
            return list;
        }
        
        private Map<String, String> getTextures(final JsonObject object) {
            final Map<String, String> map = (Map<String, String>)Maps.newHashMap();
            if (object.has("textures")) {
                final JsonObject jsonobject = object.getAsJsonObject("textures");
                for (final Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
                    map.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
            return map;
        }
        
        private String getParent(final JsonObject object) {
            return JsonUtils.getString(object, "parent", "");
        }
        
        protected boolean getAmbientOcclusionEnabled(final JsonObject object) {
            return JsonUtils.getBoolean(object, "ambientocclusion", true);
        }
        
        protected List<BlockPart> getModelElements(final JsonDeserializationContext deserializationContext, final JsonObject object) {
            final List<BlockPart> list = (List<BlockPart>)Lists.newArrayList();
            if (object.has("elements")) {
                for (final JsonElement jsonelement : JsonUtils.getJsonArray(object, "elements")) {
                    list.add(deserializationContext.deserialize(jsonelement, BlockPart.class));
                }
            }
            return list;
        }
    }
    
    static final class Bookkeep
    {
        public final ModelBlock model;
        public ModelBlock modelExt;
        
        private Bookkeep(final ModelBlock modelIn) {
            this.model = modelIn;
        }
    }
    
    public static class LoopException extends RuntimeException
    {
    }
}
