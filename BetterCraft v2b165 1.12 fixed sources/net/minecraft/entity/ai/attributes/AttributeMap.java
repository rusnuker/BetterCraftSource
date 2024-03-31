// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.ai.attributes;

import java.util.Collection;
import java.util.Iterator;
import net.minecraft.util.LowerStringMap;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;

public class AttributeMap extends AbstractAttributeMap
{
    private final Set<IAttributeInstance> attributeInstanceSet;
    protected final Map<String, IAttributeInstance> descriptionToAttributeInstanceMap;
    
    public AttributeMap() {
        this.attributeInstanceSet = (Set<IAttributeInstance>)Sets.newHashSet();
        this.descriptionToAttributeInstanceMap = new LowerStringMap<IAttributeInstance>();
    }
    
    @Override
    public ModifiableAttributeInstance getAttributeInstance(final IAttribute attribute) {
        return (ModifiableAttributeInstance)super.getAttributeInstance(attribute);
    }
    
    @Override
    public ModifiableAttributeInstance getAttributeInstanceByName(final String attributeName) {
        IAttributeInstance iattributeinstance = super.getAttributeInstanceByName(attributeName);
        if (iattributeinstance == null) {
            iattributeinstance = this.descriptionToAttributeInstanceMap.get(attributeName);
        }
        return (ModifiableAttributeInstance)iattributeinstance;
    }
    
    @Override
    public IAttributeInstance registerAttribute(final IAttribute attribute) {
        final IAttributeInstance iattributeinstance = super.registerAttribute(attribute);
        if (attribute instanceof RangedAttribute && ((RangedAttribute)attribute).getDescription() != null) {
            this.descriptionToAttributeInstanceMap.put(((RangedAttribute)attribute).getDescription(), iattributeinstance);
        }
        return iattributeinstance;
    }
    
    @Override
    protected IAttributeInstance createInstance(final IAttribute attribute) {
        return new ModifiableAttributeInstance(this, attribute);
    }
    
    @Override
    public void onAttributeModified(final IAttributeInstance instance) {
        if (instance.getAttribute().getShouldWatch()) {
            this.attributeInstanceSet.add(instance);
        }
        for (final IAttribute iattribute : this.descendantsByParent.get(instance.getAttribute())) {
            final ModifiableAttributeInstance modifiableattributeinstance = this.getAttributeInstance(iattribute);
            if (modifiableattributeinstance != null) {
                modifiableattributeinstance.flagForUpdate();
            }
        }
    }
    
    public Set<IAttributeInstance> getAttributeInstanceSet() {
        return this.attributeInstanceSet;
    }
    
    public Collection<IAttributeInstance> getWatchedAttributes() {
        final Set<IAttributeInstance> set = (Set<IAttributeInstance>)Sets.newHashSet();
        for (final IAttributeInstance iattributeinstance : this.getAllAttributes()) {
            if (iattributeinstance.getAttribute().getShouldWatch()) {
                set.add(iattributeinstance);
            }
        }
        return set;
    }
}
