// 
// Decompiled by Procyon v0.6.0
// 

package wdl.api;

import java.io.File;

public interface ISaveListener extends IWDLMod
{
    void afterChunksSaved(final File p0);
}
