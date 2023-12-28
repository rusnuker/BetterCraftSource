// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils.manager;

import java.io.Writer;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import com.google.common.base.Preconditions;
import com.google.gson.GsonBuilder;
import java.io.File;
import com.google.gson.Gson;

public class ConfigManager<T>
{
    private static final Gson gson;
    private File file;
    private Class<? extends T> configDefaults;
    private T settings;
    
    static {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }
    
    public ConfigManager(final File file, final Class<? extends T> configDefaults) {
        this.file = Preconditions.checkNotNull(file);
        this.configDefaults = Preconditions.checkNotNull(configDefaults);
        this.loadConfig(false);
    }
    
    private void loadConfig(final boolean reload) {
        if (!this.file.getParentFile().exists()) {
            this.file.getParentFile().mkdir();
        }
        boolean flag = false;
        if (reload && this.file.exists()) {
            flag = true;
        }
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
                flag = true;
            }
            catch (final IOException ioexception1) {
                ioexception1.printStackTrace();
            }
        }
        FileInputStream fileinputstream = null;
        try {
            fileinputstream = new FileInputStream(this.file);
        }
        catch (final FileNotFoundException filenotfoundexception) {
            filenotfoundexception.printStackTrace();
        }
        try {
            this.settings = ConfigManager.gson.fromJson(flag ? ConfigManager.gson.toJson(this.configDefaults.newInstance()) : IOUtils.toString(fileinputstream, StandardCharsets.UTF_8), (Class<T>)this.configDefaults);
            if (!reload && this.settings == null) {
                this.loadConfig(true);
            }
            else if (this.settings != null) {
                this.save();
            }
        }
        catch (final Exception exception) {
            exception.printStackTrace();
            if (!reload) {
                this.loadConfig(true);
            }
            try {
                fileinputstream.close();
            }
            catch (final IOException ioexception2) {
                ioexception2.printStackTrace();
            }
            try {
                fileinputstream.close();
            }
            catch (final IOException ioexception2) {
                ioexception2.printStackTrace();
            }
            return;
        }
        finally {
            try {
                fileinputstream.close();
            }
            catch (final IOException ioexception3) {
                ioexception3.printStackTrace();
            }
        }
        try {
            fileinputstream.close();
        }
        catch (final IOException ioexception3) {
            ioexception3.printStackTrace();
        }
        try {
            fileinputstream.close();
        }
        catch (final IOException ioexception4) {
            ioexception4.printStackTrace();
        }
    }
    
    public void save() {
        try {
            final PrintWriter printwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.file), StandardCharsets.UTF_8), true);
            printwriter.print(ConfigManager.gson.toJson(this.settings));
            printwriter.flush();
            printwriter.close();
        }
        catch (final Exception exception) {
            exception.printStackTrace();
        }
    }
    
    public T getSettings() {
        return this.settings;
    }
    
    public File getFile() {
        return this.file;
    }
}
