// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer;

import java.util.List;
import java.util.LinkedHashSet;
import java.util.Collection;
import java.util.ArrayList;
import org.spongepowered.asm.mixin.transformer.throwables.MixinTransformerError;
import org.spongepowered.asm.util.asm.ClassNodeAdapter;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.objectweb.asm.tree.ClassNode;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;

class MixinCoprocessorNestHost extends MixinCoprocessor
{
    private final Map<String, Set<String>> nestHosts;
    
    MixinCoprocessorNestHost() {
        this.nestHosts = new HashMap<String, Set<String>>();
    }
    
    void registerNestMember(final String hostName, final String memberName) {
        Set<String> nestMembers = this.nestHosts.get(hostName);
        if (nestMembers == null) {
            this.nestHosts.put(hostName, nestMembers = new HashSet<String>());
        }
        nestMembers.add(memberName);
    }
    
    @Override
    String getName() {
        return "nesthost";
    }
    
    @Override
    boolean postProcess(final String className, final ClassNode classNode) {
        if (!this.nestHosts.containsKey(className)) {
            return false;
        }
        final Set<String> newMembers = this.nestHosts.get(className);
        if (!MixinEnvironment.getCompatibilityLevel().supports(8) || newMembers.isEmpty()) {
            return false;
        }
        final String nestHost = ClassNodeAdapter.getNestHostClass(classNode);
        if (nestHost != null) {
            throw new MixinTransformerError(String.format("Nest host candidate %s is a nest member", classNode.name));
        }
        List<String> nestMembers = ClassNodeAdapter.getNestMembers(classNode);
        if (nestMembers == null) {
            nestMembers = new ArrayList<String>(newMembers);
        }
        else {
            final LinkedHashSet<String> combinedMembers = new LinkedHashSet<String>(nestMembers);
            combinedMembers.addAll((Collection<?>)newMembers);
            nestMembers.clear();
            nestMembers.addAll(combinedMembers);
        }
        ClassNodeAdapter.setNestMembers(classNode, nestMembers);
        return true;
    }
}
