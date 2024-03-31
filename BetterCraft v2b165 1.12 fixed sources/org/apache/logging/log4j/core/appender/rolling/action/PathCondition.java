// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.rolling.action;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.Path;

public interface PathCondition
{
    void beforeFileTreeWalk();
    
    boolean accept(final Path p0, final Path p1, final BasicFileAttributes p2);
}
