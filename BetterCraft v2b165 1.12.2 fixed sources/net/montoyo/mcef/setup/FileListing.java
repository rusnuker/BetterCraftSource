// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.setup;

import java.util.zip.ZipEntry;
import java.util.Comparator;
import java.io.InputStream;
import java.util.zip.ZipInputStream;
import java.io.FileInputStream;
import java.util.Iterator;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.ArrayList;

public class FileListing
{
    private ArrayList<String> fileNames;
    private File location;
    
    public FileListing(final File dir) {
        this.fileNames = new ArrayList<String>();
        this.location = new File(dir, "mcefFiles.lst");
        if (this.location.exists()) {
            this.load();
        }
    }
    
    public boolean load() {
        try {
            this.unsafeLoad();
            return true;
        }
        catch (final Throwable t) {
            System.err.println("Coud not read file listing:");
            t.printStackTrace();
            return false;
        }
    }
    
    private void unsafeLoad() throws Throwable {
        final BufferedReader br = new BufferedReader(new FileReader(this.location));
        this.fileNames.clear();
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.length() > 0 && line.charAt(0) != '#' && line.charAt(0) != '.' && line.charAt(0) != '/' && line.charAt(0) != '\\') {
                this.fileNames.add(line);
            }
        }
        SetupUtil.silentClose(br);
    }
    
    public boolean save() {
        try {
            this.unsafeSave();
            return true;
        }
        catch (final Throwable t) {
            System.err.println("Coud not write file listing:");
            t.printStackTrace();
            return false;
        }
    }
    
    private void unsafeSave() throws Throwable {
        if (this.location.exists()) {
            SetupUtil.tryDelete(this.location);
        }
        final BufferedWriter bw = new BufferedWriter(new FileWriter(this.location));
        bw.write("# DO NOT EDIT THIS FILE. IT HAS BEEN AUTOMATICALLY GENERATED.\n");
        bw.write("# This file contains the list of files installed by MCEF.\n");
        bw.write("# If you remove MCEF, they are no longer needed and you can safely remove them,\n");
        bw.write("# or you can let the uninstaller do it for you. Just run the MCEF mod jar using Java.\n\n");
        for (final String f : this.fileNames) {
            bw.write(String.valueOf(f) + "\n");
        }
        SetupUtil.silentClose(bw);
    }
    
    public void addFile(final String f) {
        if (!this.fileNames.contains(f)) {
            this.fileNames.add(f);
        }
    }
    
    public boolean addZip(final String fname) {
        try {
            this.addZipUnsafe(fname);
            return true;
        }
        catch (final Throwable t) {
            System.err.println("Coud not list file in ZIP archive \"" + fname + "\":");
            t.printStackTrace();
            return false;
        }
    }
    
    private void addZipUnsafe(final String fname) throws Throwable {
        final ArrayList<String> files = new ArrayList<String>();
        final ZipInputStream zis = new ZipInputStream(new FileInputStream(fname));
        ZipEntry ze;
        while ((ze = zis.getNextEntry()) != null) {
            final String name = ze.getName();
            if (ze.isDirectory() && (name.endsWith("/") || name.endsWith("\\"))) {
                files.add(name.substring(0, name.length() - 1));
            }
            else {
                files.add(name);
            }
        }
        SetupUtil.silentClose(zis);
        files.sort(new SlashComparator(new DefaultComparator()));
        for (final String t : files) {
            this.addFile(t);
        }
    }
    
    public Iterator<String> iterator() {
        return this.fileNames.iterator();
    }
    
    public boolean selfDestruct() {
        return SetupUtil.tryDelete(this.location);
    }
}
