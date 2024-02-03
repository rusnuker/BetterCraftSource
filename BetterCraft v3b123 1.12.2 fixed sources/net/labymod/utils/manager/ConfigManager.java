// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils.manager;

import java.io.Writer;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import net.labymod.support.util.Debug;
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
        boolean createdNewFile = false;
        if (reload && this.file.exists()) {
            createdNewFile = true;
        }
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
                createdNewFile = true;
            }
            catch (final IOException ex) {
                ex.printStackTrace();
            }
        }
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(this.file);
        }
        catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            this.settings = ConfigManager.gson.fromJson(createdNewFile ? ConfigManager.gson.toJson(this.configDefaults.newInstance()) : IOUtils.toString(stream, StandardCharsets.UTF_8), (Class<T>)this.configDefaults);
            Debug.log(Debug.EnumDebugMode.CONFIG_MANAGER, (this.settings != null) ? ("Loaded " + this.file.getName() + "!") : "Loaded file but settings is null");
            if (!reload && this.settings == null) {
                this.loadConfig(true);
            }
            else if (this.settings != null) {
                this.save();
            }
        }
        catch (final Exception ex2) {
            ex2.printStackTrace();
            Debug.log(Debug.EnumDebugMode.CONFIG_MANAGER, "Failed to load " + this.file.getName() + " config!");
            if (!reload) {
                this.loadConfig(true);
            }
            try {
                stream.close();
            }
            catch (final IOException e2) {
                e2.printStackTrace();
            }
            try {
                stream.close();
            }
            catch (final IOException e3) {
                e3.printStackTrace();
            }
            return;
        }
        finally {
            try {
                stream.close();
            }
            catch (final IOException e3) {
                e3.printStackTrace();
            }
        }
        try {
            stream.close();
        }
        catch (final IOException e3) {
            e3.printStackTrace();
        }
    }
    
    public void save() {
        try {
            final PrintWriter w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.file), StandardCharsets.UTF_8), true);
            w.print(ConfigManager.gson.toJson(this.settings));
            w.flush();
            w.close();
        }
        catch (final Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public T getSettings() {
        return this.settings;
    }
    
    public File getFile() {
        return this.file;
    }
}
