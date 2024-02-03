// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.launchwrapper;

import java.io.Closeable;
import java.util.Locale;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.io.OutputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.security.CodeSigner;
import java.net.URLConnection;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import java.security.CodeSource;
import java.net.JarURLConnection;
import org.apache.logging.log4j.Level;
import java.util.Collection;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.io.File;
import java.util.jar.Manifest;
import java.util.Set;
import java.util.Map;
import java.net.URL;
import java.util.List;
import java.net.URLClassLoader;

public class LaunchClassLoader extends URLClassLoader
{
    public static final int BUFFER_SIZE = 4096;
    private List<URL> sources;
    private ClassLoader parent;
    private List<IClassTransformer> transformers;
    private Map<String, Class<?>> cachedClasses;
    private Set<String> invalidClasses;
    private Set<String> classLoaderExceptions;
    private Set<String> transformerExceptions;
    private Map<Package, Manifest> packageManifests;
    private Map<String, byte[]> resourceCache;
    private Set<String> negativeResourceCache;
    private IClassNameTransformer renameTransformer;
    private static final Manifest EMPTY;
    private final ThreadLocal<byte[]> loadBuffer;
    private static final String[] RESERVED_NAMES;
    private static final boolean DEBUG;
    private static final boolean DEBUG_FINER;
    private static final boolean DEBUG_SAVE;
    private static File tempFolder;
    
    static {
        EMPTY = new Manifest();
        RESERVED_NAMES = new String[] { "CON", "PRN", "AUX", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9" };
        DEBUG = Boolean.parseBoolean(System.getProperty("legacy.debugClassLoading", "false"));
        DEBUG_FINER = (LaunchClassLoader.DEBUG && Boolean.parseBoolean(System.getProperty("legacy.debugClassLoadingFiner", "false")));
        DEBUG_SAVE = (LaunchClassLoader.DEBUG && Boolean.parseBoolean(System.getProperty("legacy.debugClassLoadingSave", "false")));
        LaunchClassLoader.tempFolder = null;
    }
    
    public LaunchClassLoader(final URL[] sources) {
        super(sources, (ClassLoader)null);
        this.parent = this.getClass().getClassLoader();
        this.transformers = new ArrayList<IClassTransformer>(2);
        this.cachedClasses = new ConcurrentHashMap<String, Class<?>>();
        this.invalidClasses = new HashSet<String>(1000);
        this.classLoaderExceptions = new HashSet<String>();
        this.transformerExceptions = new HashSet<String>();
        this.packageManifests = new ConcurrentHashMap<Package, Manifest>();
        this.resourceCache = new ConcurrentHashMap<String, byte[]>(1000);
        this.negativeResourceCache = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
        this.loadBuffer = new ThreadLocal<byte[]>();
        this.sources = new ArrayList<URL>(Arrays.asList(sources));
        this.addClassLoaderExclusion("java.");
        this.addClassLoaderExclusion("sun.");
        this.addClassLoaderExclusion("org.lwjgl.");
        this.addClassLoaderExclusion("org.apache.logging.");
        this.addClassLoaderExclusion("net.minecraft.launchwrapper.");
        this.addTransformerExclusion("javax.");
        this.addTransformerExclusion("argo.");
        this.addTransformerExclusion("org.objectweb.asm.");
        this.addTransformerExclusion("com.google.common.");
        this.addTransformerExclusion("org.bouncycastle.");
        this.addTransformerExclusion("net.minecraft.launchwrapper.injector.");
        if (LaunchClassLoader.DEBUG_SAVE) {
            int x = 1;
            LaunchClassLoader.tempFolder = new File(Launch.minecraftHome, "CLASSLOADER_TEMP");
            while (LaunchClassLoader.tempFolder.exists() && x <= 10) {
                LaunchClassLoader.tempFolder = new File(Launch.minecraftHome, "CLASSLOADER_TEMP" + x++);
            }
            if (LaunchClassLoader.tempFolder.exists()) {
                LogWrapper.info("DEBUG_SAVE enabled, but 10 temp directories already exist, clean them and try again.", new Object[0]);
                LaunchClassLoader.tempFolder = null;
            }
            else {
                LogWrapper.info("DEBUG_SAVE Enabled, saving all classes to \"%s\"", LaunchClassLoader.tempFolder.getAbsolutePath().replace('\\', '/'));
                LaunchClassLoader.tempFolder.mkdirs();
            }
        }
    }
    
    public void registerTransformer(final String transformerClassName) {
        try {
            final IClassTransformer transformer = (IClassTransformer)this.loadClass(transformerClassName).newInstance();
            this.transformers.add(transformer);
            if (transformer instanceof IClassNameTransformer && this.renameTransformer == null) {
                this.renameTransformer = (IClassNameTransformer)transformer;
            }
        }
        catch (final Exception e) {
            LogWrapper.log(Level.ERROR, e, "A critical problem occurred registering the ASM transformer class %s", transformerClassName);
        }
    }
    
    public Class<?> findClass(final String name) throws ClassNotFoundException {
        if (this.invalidClasses.contains(name)) {
            throw new ClassNotFoundException(name);
        }
        for (final String exception : this.classLoaderExceptions) {
            if (name.startsWith(exception)) {
                return this.parent.loadClass(name);
            }
        }
        if (this.cachedClasses.containsKey(name)) {
            return this.cachedClasses.get(name);
        }
        for (final String exception : this.transformerExceptions) {
            if (name.startsWith(exception)) {
                try {
                    final Class<?> clazz = super.findClass(name);
                    this.cachedClasses.put(name, clazz);
                    return clazz;
                }
                catch (final ClassNotFoundException e) {
                    this.invalidClasses.add(name);
                    throw e;
                }
            }
        }
        try {
            final String transformedName = this.transformName(name);
            if (this.cachedClasses.containsKey(transformedName)) {
                return this.cachedClasses.get(transformedName);
            }
            final String untransformedName = this.untransformName(name);
            final int lastDot = untransformedName.lastIndexOf(46);
            final String packageName = (lastDot == -1) ? "" : untransformedName.substring(0, lastDot);
            final String fileName = untransformedName.replace('.', '/').concat(".class");
            final URLConnection urlConnection = this.findCodeSourceConnectionFor(fileName);
            CodeSigner[] signers = null;
            if (lastDot > -1 && !untransformedName.startsWith("net.minecraft.")) {
                if (urlConnection instanceof JarURLConnection) {
                    final JarURLConnection jarURLConnection = (JarURLConnection)urlConnection;
                    final JarFile jarFile = jarURLConnection.getJarFile();
                    if (jarFile != null && jarFile.getManifest() != null) {
                        final Manifest manifest = jarFile.getManifest();
                        final JarEntry entry = jarFile.getJarEntry(fileName);
                        Package pkg = this.getPackage(packageName);
                        this.getClassBytes(untransformedName);
                        signers = entry.getCodeSigners();
                        if (pkg == null) {
                            pkg = this.definePackage(packageName, manifest, jarURLConnection.getJarFileURL());
                            this.packageManifests.put(pkg, manifest);
                        }
                        else if (pkg.isSealed() && !pkg.isSealed(jarURLConnection.getJarFileURL())) {
                            LogWrapper.severe("The jar file %s is trying to seal already secured path %s", jarFile.getName(), packageName);
                        }
                        else if (this.isSealed(packageName, manifest)) {
                            LogWrapper.severe("The jar file %s has a security seal for path %s, but that path is defined and not secure", jarFile.getName(), packageName);
                        }
                    }
                }
                else {
                    Package pkg2 = this.getPackage(packageName);
                    if (pkg2 == null) {
                        pkg2 = this.definePackage(packageName, null, null, null, null, null, null, null);
                        this.packageManifests.put(pkg2, LaunchClassLoader.EMPTY);
                    }
                    else if (pkg2.isSealed()) {
                        LogWrapper.severe("The URL %s is defining elements for sealed path %s", urlConnection.getURL(), packageName);
                    }
                }
            }
            final byte[] transformedClass = this.runTransformers(untransformedName, transformedName, this.getClassBytes(untransformedName));
            if (LaunchClassLoader.DEBUG_SAVE) {
                this.saveTransformedClass(transformedClass, transformedName);
            }
            final CodeSource codeSource = (urlConnection == null) ? null : new CodeSource(urlConnection.getURL(), signers);
            final Class<?> clazz2 = this.defineClass(transformedName, transformedClass, 0, transformedClass.length, codeSource);
            this.cachedClasses.put(transformedName, clazz2);
            return clazz2;
        }
        catch (final Throwable e2) {
            this.invalidClasses.add(name);
            if (LaunchClassLoader.DEBUG) {
                LogWrapper.log(Level.TRACE, e2, "Exception encountered attempting classloading of %s", name);
                LogManager.getLogger("LaunchWrapper").log(Level.ERROR, "Exception encountered attempting classloading of %s", e2);
            }
            throw new ClassNotFoundException(name, e2);
        }
    }
    
    private void saveTransformedClass(final byte[] data, final String transformedName) {
        if (LaunchClassLoader.tempFolder == null) {
            return;
        }
        final File outFile = new File(LaunchClassLoader.tempFolder, String.valueOf(transformedName.replace('.', File.separatorChar)) + ".class");
        final File outDir = outFile.getParentFile();
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        if (outFile.exists()) {
            outFile.delete();
        }
        try {
            LogWrapper.fine("Saving transformed class \"%s\" to \"%s\"", transformedName, outFile.getAbsolutePath().replace('\\', '/'));
            final OutputStream output = new FileOutputStream(outFile);
            output.write(data);
            output.close();
        }
        catch (final IOException ex) {
            LogWrapper.log(Level.WARN, ex, "Could not save transformed class \"%s\"", transformedName);
        }
    }
    
    private String untransformName(final String name) {
        if (this.renameTransformer != null) {
            return this.renameTransformer.unmapClassName(name);
        }
        return name;
    }
    
    private String transformName(final String name) {
        if (this.renameTransformer != null) {
            return this.renameTransformer.remapClassName(name);
        }
        return name;
    }
    
    private boolean isSealed(final String path, final Manifest manifest) {
        Attributes attributes = manifest.getAttributes(path);
        String sealed = null;
        if (attributes != null) {
            sealed = attributes.getValue(Attributes.Name.SEALED);
        }
        if (sealed == null) {
            attributes = manifest.getMainAttributes();
            if (attributes != null) {
                sealed = attributes.getValue(Attributes.Name.SEALED);
            }
        }
        return "true".equalsIgnoreCase(sealed);
    }
    
    private URLConnection findCodeSourceConnectionFor(final String name) {
        final URL resource = this.findResource(name);
        if (resource != null) {
            try {
                return resource.openConnection();
            }
            catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
    
    private byte[] runTransformers(final String name, final String transformedName, byte[] basicClass) {
        if (LaunchClassLoader.DEBUG_FINER) {
            LogWrapper.finest("Beginning transform of {%s (%s)} Start Length: %d", name, transformedName, (basicClass == null) ? 0 : basicClass.length);
            for (final IClassTransformer transformer : this.transformers) {
                final String transName = transformer.getClass().getName();
                LogWrapper.finest("Before Transformer {%s (%s)} %s: %d", name, transformedName, transName, (basicClass == null) ? 0 : basicClass.length);
                basicClass = transformer.transform(name, transformedName, basicClass);
                LogWrapper.finest("After  Transformer {%s (%s)} %s: %d", name, transformedName, transName, (basicClass == null) ? 0 : basicClass.length);
            }
            LogWrapper.finest("Ending transform of {%s (%s)} Start Length: %d", name, transformedName, (basicClass == null) ? 0 : basicClass.length);
        }
        else {
            for (final IClassTransformer transformer : this.transformers) {
                basicClass = transformer.transform(name, transformedName, basicClass);
            }
        }
        return basicClass;
    }
    
    public void addURL(final URL url) {
        super.addURL(url);
        this.sources.add(url);
    }
    
    public List<URL> getSources() {
        return this.sources;
    }
    
    private byte[] readFully(final InputStream stream) {
        try {
            byte[] buffer;
            int totalLength;
            int read;
            byte[] newBuffer = null;
            for (buffer = this.getOrCreateBuffer(), totalLength = 0; (read = stream.read(buffer, totalLength, buffer.length - totalLength)) != -1; buffer = newBuffer) {
                totalLength += read;
                if (totalLength >= buffer.length - 1) {
                    newBuffer = new byte[buffer.length + 4096];
                    System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
                }
            }
            final byte[] result = new byte[totalLength];
            System.arraycopy(buffer, 0, result, 0, totalLength);
            return result;
        }
        catch (final Throwable t) {
            LogWrapper.log(Level.WARN, t, "Problem loading class", new Object[0]);
            return new byte[0];
        }
    }
    
    private byte[] getOrCreateBuffer() {
        byte[] buffer = this.loadBuffer.get();
        if (buffer == null) {
            this.loadBuffer.set(new byte[4096]);
            buffer = this.loadBuffer.get();
        }
        return buffer;
    }
    
    public List<IClassTransformer> getTransformers() {
        return Collections.unmodifiableList((List<? extends IClassTransformer>)this.transformers);
    }
    
    public void addClassLoaderExclusion(final String toExclude) {
        this.classLoaderExceptions.add(toExclude);
    }
    
    public void addTransformerExclusion(final String toExclude) {
        this.transformerExceptions.add(toExclude);
    }
    
    public byte[] getClassBytes(final String name) throws IOException {
        if (this.negativeResourceCache.contains(name)) {
            return null;
        }
        if (this.resourceCache.containsKey(name)) {
            return this.resourceCache.get(name);
        }
        if (name.indexOf(46) == -1) {
            String[] reserved_NAMES;
            for (int length = (reserved_NAMES = LaunchClassLoader.RESERVED_NAMES).length, i = 0; i < length; ++i) {
                final String reservedName = reserved_NAMES[i];
                if (name.toUpperCase(Locale.ENGLISH).startsWith(reservedName)) {
                    final byte[] data = this.getClassBytes("_" + name);
                    if (data != null) {
                        this.resourceCache.put(name, data);
                        return data;
                    }
                }
            }
        }
        InputStream classStream = null;
        try {
            final String resourcePath = name.replace('.', '/').concat(".class");
            final URL classResource = this.findResource(resourcePath);
            if (classResource == null) {
                if (LaunchClassLoader.DEBUG) {
                    LogWrapper.finest("Failed to find class resource %s", resourcePath);
                }
                this.negativeResourceCache.add(name);
                return null;
            }
            classStream = classResource.openStream();
            if (LaunchClassLoader.DEBUG) {
                LogWrapper.finest("Loading class %s from resource %s", name, classResource.toString());
            }
            final byte[] data2 = this.readFully(classStream);
            this.resourceCache.put(name, data2);
            return data2;
        }
        finally {
            closeSilently(classStream);
        }
    }
    
    private static void closeSilently(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (final IOException ex) {}
        }
    }
    
    public void clearNegativeEntries(final Set<String> entriesToClear) {
        this.negativeResourceCache.removeAll(entriesToClear);
    }
}
