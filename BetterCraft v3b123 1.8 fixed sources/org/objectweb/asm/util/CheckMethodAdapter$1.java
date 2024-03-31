// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.util;

import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.objectweb.asm.tree.analysis.Interpreter;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.BasicVerifier;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.MethodNode;

class CheckMethodAdapter$1 extends MethodNode
{
    final /* synthetic */ MethodVisitor val$cmv;
    
    CheckMethodAdapter$1(final int api, final int access, final String name, final String descriptor, final String signature, final String[] exceptions, final MethodVisitor val$cmv) {
        this.val$cmv = val$cmv;
        super(api, access, name, descriptor, signature, exceptions);
    }
    
    public void visitEnd() {
        final Analyzer analyzer = new Analyzer(new BasicVerifier());
        try {
            analyzer.analyze("dummy", this);
        }
        catch (final Exception ex) {
            if (ex instanceof IndexOutOfBoundsException && this.maxLocals == 0 && this.maxStack == 0) {
                throw new RuntimeException("Data flow checking option requires valid, non zero maxLocals and maxStack values.");
            }
            ex.printStackTrace();
            final StringWriter stringWriter = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(stringWriter, true);
            CheckClassAdapter.printAnalyzerResult(this, analyzer, printWriter);
            printWriter.close();
            throw new RuntimeException(ex.getMessage() + ' ' + stringWriter.toString());
        }
        this.accept(this.val$cmv);
    }
}
