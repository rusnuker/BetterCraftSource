// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.javassist.compiler;

import com.viaversion.viaversion.libs.javassist.compiler.ast.ASTree;

public class NoFieldException extends CompileError
{
    private static final long serialVersionUID = 1L;
    private String fieldName;
    private ASTree expr;
    
    public NoFieldException(final String name, final ASTree e) {
        super("no such field: " + name);
        this.fieldName = name;
        this.expr = e;
    }
    
    public String getField() {
        return this.fieldName;
    }
    
    public ASTree getExpr() {
        return this.expr;
    }
}
