// 
// Decompiled by Procyon v0.6.0
// 

package org.lwjgl.util.glu.tessellation;

class ActiveRegion
{
    GLUhalfEdge eUp;
    DictNode nodeUp;
    int windingNumber;
    boolean inside;
    boolean sentinel;
    boolean dirty;
    boolean fixUpperEdge;
}
