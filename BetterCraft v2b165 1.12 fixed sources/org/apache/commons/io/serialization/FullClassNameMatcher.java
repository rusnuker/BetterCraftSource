// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.io.serialization;

import java.util.Collections;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Set;

final class FullClassNameMatcher implements ClassNameMatcher
{
    private final Set<String> classesSet;
    
    public FullClassNameMatcher(final String... classes) {
        this.classesSet = Collections.unmodifiableSet((Set<? extends String>)new HashSet<String>(Arrays.asList(classes)));
    }
    
    @Override
    public boolean matches(final String className) {
        return this.classesSet.contains(className);
    }
}
