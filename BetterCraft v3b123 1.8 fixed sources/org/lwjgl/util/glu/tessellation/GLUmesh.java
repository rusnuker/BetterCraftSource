// 
// Decompiled by Procyon v0.6.0
// 

package org.lwjgl.util.glu.tessellation;

class GLUmesh
{
    GLUvertex vHead;
    GLUface fHead;
    GLUhalfEdge eHead;
    GLUhalfEdge eHeadSym;
    
    GLUmesh() {
        this.vHead = new GLUvertex();
        this.fHead = new GLUface();
        this.eHead = new GLUhalfEdge(true);
        this.eHeadSym = new GLUhalfEdge(false);
    }
}
