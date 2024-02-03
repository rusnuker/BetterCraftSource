// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

final class Processor$ZipEntryElement implements Processor$EntryElement
{
    private ZipOutputStream zos;
    
    Processor$ZipEntryElement(final ZipOutputStream zos) {
        this.zos = zos;
    }
    
    public OutputStream openEntry(final String s) throws IOException {
        this.zos.putNextEntry(new ZipEntry(s));
        return this.zos;
    }
    
    public void closeEntry() throws IOException {
        this.zos.flush();
        this.zos.closeEntry();
    }
}
