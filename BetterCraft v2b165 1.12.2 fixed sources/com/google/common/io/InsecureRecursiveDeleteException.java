// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.io;

import javax.annotation.Nullable;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;
import java.nio.file.FileSystemException;

@Beta
@AndroidIncompatible
@GwtIncompatible
public final class InsecureRecursiveDeleteException extends FileSystemException
{
    public InsecureRecursiveDeleteException(@Nullable final String file) {
        super(file, null, "unable to guarantee security of recursive delete");
    }
}
