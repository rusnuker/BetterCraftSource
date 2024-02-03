// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.util;

import org.jetbrains.annotations.ApiStatus;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.PACKAGE, ElementType.ANNOTATION_TYPE })
@ApiStatus.Internal
public @interface PlatformAPI {
}
