// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.commons;

import java.security.MessageDigest;
import java.io.IOException;
import java.io.DataOutput;
import java.util.Arrays;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import java.util.ArrayList;
import java.util.Collection;
import org.objectweb.asm.ClassVisitor;

public class SerialVersionUIDAdder extends ClassVisitor
{
    private boolean computeSVUID;
    private boolean hasSVUID;
    private int access;
    private String name;
    private String[] interfaces;
    private Collection svuidFields;
    private boolean hasStaticInitializer;
    private Collection svuidConstructors;
    private Collection svuidMethods;
    static /* synthetic */ Class class$org$objectweb$asm$commons$SerialVersionUIDAdder;
    
    public SerialVersionUIDAdder(final ClassVisitor classVisitor) {
        this(327680, classVisitor);
        if (this.getClass() != SerialVersionUIDAdder.class$org$objectweb$asm$commons$SerialVersionUIDAdder) {
            throw new IllegalStateException();
        }
    }
    
    protected SerialVersionUIDAdder(final int api, final ClassVisitor classVisitor) {
        super(api, classVisitor);
        this.svuidFields = new ArrayList();
        this.svuidConstructors = new ArrayList();
        this.svuidMethods = new ArrayList();
    }
    
    public void visit(final int version, final int n, final String s, final String signature, final String superName, final String[] interfaces) {
        this.computeSVUID = ((n & 0x4000) == 0x0);
        if (this.computeSVUID) {
            this.name = s;
            this.access = n;
            System.arraycopy(interfaces, 0, this.interfaces = new String[interfaces.length], 0, interfaces.length);
        }
        super.visit(version, n, s, signature, superName, interfaces);
    }
    
    public MethodVisitor visitMethod(final int access, final String name, final String descriptor, final String signature, final String[] exceptions) {
        if (this.computeSVUID) {
            if ("<clinit>".equals(name)) {
                this.hasStaticInitializer = true;
            }
            final int n = access & 0xD3F;
            if ((access & 0x2) == 0x0) {
                if ("<init>".equals(name)) {
                    this.svuidConstructors.add(new SerialVersionUIDAdder$Item(name, n, descriptor));
                }
                else if (!"<clinit>".equals(name)) {
                    this.svuidMethods.add(new SerialVersionUIDAdder$Item(name, n, descriptor));
                }
            }
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
    
    public FieldVisitor visitField(final int access, final String name, final String descriptor, final String signature, final Object value) {
        if (this.computeSVUID) {
            if ("serialVersionUID".equals(name)) {
                this.computeSVUID = false;
                this.hasSVUID = true;
            }
            if ((access & 0x2) == 0x0 || (access & 0x88) == 0x0) {
                this.svuidFields.add(new SerialVersionUIDAdder$Item(name, access & 0xDF, descriptor));
            }
        }
        return super.visitField(access, name, descriptor, signature, value);
    }
    
    public void visitInnerClass(final String name, final String outerName, final String innerName, final int n) {
        if (this.name != null && this.name.equals(name)) {
            this.access = n;
        }
        super.visitInnerClass(name, outerName, innerName, n);
    }
    
    public void visitEnd() {
        if (this.computeSVUID && !this.hasSVUID) {
            try {
                this.addSVUID(this.computeSVUID());
            }
            catch (final Throwable t) {
                throw new RuntimeException("Error while computing SVUID for " + this.name, t);
            }
        }
        super.visitEnd();
    }
    
    public boolean hasSVUID() {
        return this.hasSVUID;
    }
    
    protected void addSVUID(final long n) {
        final FieldVisitor visitField = super.visitField(24, "serialVersionUID", "J", null, new Long(n));
        if (visitField != null) {
            visitField.visitEnd();
        }
    }
    
    protected long computeSVUID() throws IOException {
        DataOutputStream dataOutputStream = null;
        long n = 0L;
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeUTF(this.name.replace('/', '.'));
            int access = this.access;
            if ((access & 0x200) != 0x0) {
                access = ((this.svuidMethods.size() > 0) ? (access | 0x400) : (access & 0xFFFFFBFF));
            }
            dataOutputStream.writeInt(access & 0x611);
            Arrays.sort(this.interfaces);
            for (int i = 0; i < this.interfaces.length; ++i) {
                dataOutputStream.writeUTF(this.interfaces[i].replace('/', '.'));
            }
            writeItems(this.svuidFields, dataOutputStream, false);
            if (this.hasStaticInitializer) {
                dataOutputStream.writeUTF("<clinit>");
                dataOutputStream.writeInt(8);
                dataOutputStream.writeUTF("()V");
            }
            writeItems(this.svuidConstructors, dataOutputStream, true);
            writeItems(this.svuidMethods, dataOutputStream, true);
            dataOutputStream.flush();
            final byte[] computeSHAdigest = this.computeSHAdigest(byteArrayOutputStream.toByteArray());
            for (int j = Math.min(computeSHAdigest.length, 8) - 1; j >= 0; --j) {
                n = (n << 8 | (long)(computeSHAdigest[j] & 0xFF));
            }
        }
        finally {
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
        }
        return n;
    }
    
    protected byte[] computeSHAdigest(final byte[] array) {
        try {
            return MessageDigest.getInstance("SHA").digest(array);
        }
        catch (final Exception ex) {
            throw new UnsupportedOperationException(ex.toString());
        }
    }
    
    private static void writeItems(final Collection collection, final DataOutput dataOutput, final boolean b) throws IOException {
        final int size = collection.size();
        final SerialVersionUIDAdder$Item[] array = collection.toArray(new SerialVersionUIDAdder$Item[size]);
        Arrays.sort(array);
        for (int i = 0; i < size; ++i) {
            dataOutput.writeUTF(array[i].name);
            dataOutput.writeInt(array[i].access);
            dataOutput.writeUTF(b ? array[i].desc.replace('/', '.') : array[i].desc);
        }
    }
    
    static /* synthetic */ Class class$(final String s) {
        try {
            return Class.forName(s);
        }
        catch (final ClassNotFoundException ex) {
            throw new NoClassDefFoundError(ex.getMessage());
        }
    }
    
    static {
        SerialVersionUIDAdder.class$org$objectweb$asm$commons$SerialVersionUIDAdder = class$("org.objectweb.asm.commons.SerialVersionUIDAdder");
    }
}
