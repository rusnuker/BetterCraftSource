// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation.mapping.common;

import java.io.IOException;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.lang.model.element.Element;
import javax.tools.StandardLocation;
import org.spongepowered.tools.obfuscation.interfaces.IMessagerEx;
import java.io.File;
import java.io.PrintWriter;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import org.spongepowered.tools.obfuscation.mapping.IMappingWriter;

public abstract class MappingWriter implements IMappingWriter
{
    private final Messager messager;
    private final Filer filer;
    
    public MappingWriter(final Messager messager, final Filer filer) {
        this.messager = messager;
        this.filer = filer;
    }
    
    protected PrintWriter openFileWriter(final String fileName, final String description) throws IOException {
        if (fileName.matches("^.*[\\\\/:].*$")) {
            final File outFile = new File(fileName);
            outFile.getParentFile().mkdirs();
            this.printMessage(IMessagerEx.MessageType.INFO, "Writing " + description + " to " + outFile.getAbsolutePath());
            return new PrintWriter(outFile);
        }
        final FileObject outResource = this.filer.createResource(StandardLocation.CLASS_OUTPUT, "", fileName, new Element[0]);
        this.printMessage(IMessagerEx.MessageType.INFO, "Writing " + description + " to " + new File(outResource.toUri()).getAbsolutePath());
        return new PrintWriter(outResource.openWriter());
    }
    
    protected void printMessage(final IMessagerEx.MessageType type, final CharSequence msg) {
        if (this.messager instanceof IMessagerEx) {
            ((IMessagerEx)this.messager).printMessage(type, msg);
        }
        else if (type.isEnabled()) {
            this.messager.printMessage(type.getKind(), type.decorate(msg));
        }
    }
}
