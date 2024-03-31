// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.resources;

import java.util.Locale;
import java.io.FileFilter;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nullable;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import net.minecraft.util.Util;
import com.google.common.base.CharMatcher;

public class FolderResourcePack extends AbstractResourcePack
{
    private static final boolean field_191386_b;
    private static final CharMatcher field_191387_c;
    
    static {
        field_191386_b = (Util.getOSType() == Util.EnumOS.WINDOWS);
        field_191387_c = CharMatcher.is('\\');
    }
    
    public FolderResourcePack(final File resourcePackFileIn) {
        super(resourcePackFileIn);
    }
    
    protected static boolean func_191384_a(final File p_191384_0_, final String p_191384_1_) throws IOException {
        String s = p_191384_0_.getCanonicalPath();
        if (FolderResourcePack.field_191386_b) {
            s = FolderResourcePack.field_191387_c.replaceFrom(s, '/');
        }
        return s.endsWith(p_191384_1_);
    }
    
    @Override
    protected InputStream getInputStreamByName(final String name) throws IOException {
        final File file1 = this.func_191385_d(name);
        if (file1 == null) {
            throw new ResourcePackFileNotFoundException(this.resourcePackFile, name);
        }
        return new BufferedInputStream(new FileInputStream(file1));
    }
    
    @Override
    protected boolean hasResourceName(final String name) {
        return this.func_191385_d(name) != null;
    }
    
    @Nullable
    private File func_191385_d(final String p_191385_1_) {
        try {
            final File file1 = new File(this.resourcePackFile, p_191385_1_);
            if (file1.isFile() && func_191384_a(file1, p_191385_1_)) {
                return file1;
            }
        }
        catch (final IOException ex) {}
        return null;
    }
    
    @Override
    public Set<String> getResourceDomains() {
        final Set<String> set = (Set<String>)Sets.newHashSet();
        final File file1 = new File(this.resourcePackFile, "assets/");
        if (file1.isDirectory()) {
            File[] listFiles;
            for (int length = (listFiles = file1.listFiles((FileFilter)DirectoryFileFilter.DIRECTORY)).length, i = 0; i < length; ++i) {
                final File file2 = listFiles[i];
                final String s = AbstractResourcePack.getRelativeName(file1, file2);
                if (s.equals(s.toLowerCase(Locale.ROOT))) {
                    set.add(s.substring(0, s.length() - 1));
                }
                else {
                    this.logNameNotLowercase(s);
                }
            }
        }
        return set;
    }
}
