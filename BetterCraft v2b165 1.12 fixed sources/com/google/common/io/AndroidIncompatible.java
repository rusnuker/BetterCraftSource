// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.io;

import com.google.common.annotations.GwtIncompatible;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE })
@GwtIncompatible
@interface AndroidIncompatible {
}
