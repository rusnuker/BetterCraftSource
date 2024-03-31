// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation;

import org.spongepowered.tools.obfuscation.mirror.TypeHandleSimulated;
import org.objectweb.asm.Type;
import javax.lang.model.element.PackageElement;
import javax.lang.model.util.Elements;
import org.spongepowered.tools.obfuscation.mirror.TypeHandleASM;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationMirror;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.Messager;
import org.spongepowered.tools.obfuscation.struct.InjectorRemap;
import javax.lang.model.element.VariableElement;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.gen.Invoker;
import java.lang.annotation.Annotation;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import org.spongepowered.asm.mixin.gen.Accessor;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import java.util.Iterator;
import org.spongepowered.tools.obfuscation.mirror.TypeReference;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Collection;
import javax.lang.model.element.TypeElement;
import java.io.InputStream;
import javax.tools.FileObject;
import javax.annotation.processing.Filer;
import javax.tools.JavaFileManager;
import javax.tools.StandardLocation;
import java.util.regex.Matcher;
import java.util.Locale;
import java.util.regex.Pattern;
import java.io.IOException;
import java.io.File;
import javax.tools.Diagnostic;
import org.spongepowered.asm.util.VersionNumber;
import com.google.common.collect.ImmutableList;
import org.spongepowered.tools.obfuscation.validation.TargetValidator;
import org.spongepowered.tools.obfuscation.validation.ParentValidator;
import org.spongepowered.asm.util.logging.MessageRouter;
import org.spongepowered.tools.obfuscation.interfaces.IOptionProvider;
import org.spongepowered.tools.obfuscation.interfaces.IMessagerEx;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import org.spongepowered.tools.obfuscation.interfaces.IMixinValidator;
import org.spongepowered.tools.obfuscation.interfaces.IObfuscationManager;
import java.util.List;
import javax.annotation.processing.ProcessingEnvironment;
import java.util.Map;
import org.spongepowered.tools.obfuscation.interfaces.IJavadocProvider;
import org.spongepowered.tools.obfuscation.interfaces.ITypeHandleProvider;
import org.spongepowered.asm.util.ITokenProvider;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;

final class AnnotatedMixins implements IMixinAnnotationProcessor, ITokenProvider, ITypeHandleProvider, IJavadocProvider
{
    private static final String MAPID_SYSTEM_PROPERTY = "mixin.target.mapid";
    private static final String RECOMMENDED_MIXINGRADLE_VERSION = "0.7";
    private static Map<ProcessingEnvironment, AnnotatedMixins> instances;
    private final CompilerEnvironment env;
    private final ProcessingEnvironment processingEnv;
    private final Map<String, AnnotatedMixin> mixins;
    private final List<AnnotatedMixin> mixinsForPass;
    private final IObfuscationManager obf;
    private final List<IMixinValidator> validators;
    private final Map<String, Integer> tokenCache;
    private final TargetMap targets;
    private Properties properties;
    
    private AnnotatedMixins(final ProcessingEnvironment processingEnv) {
        this.mixins = new HashMap<String, AnnotatedMixin>();
        this.mixinsForPass = new ArrayList<AnnotatedMixin>();
        this.tokenCache = new HashMap<String, Integer>();
        this.env = CompilerEnvironment.detect(processingEnv);
        this.processingEnv = processingEnv;
        IMessagerEx.MessageType.applyOptions(this.env, this);
        MessageRouter.setMessager(processingEnv.getMessager());
        final String pluginVersion = this.checkPluginVersion(this.getOption("pluginVersion"));
        final String pluginVersionString = (pluginVersion != null) ? String.format(" (MixinGradle Version=%s)", pluginVersion) : "";
        this.printMessage(IMessagerEx.MessageType.INFO, "SpongePowered MIXIN Annotation Processor Version=0.8.5" + pluginVersionString);
        this.targets = this.initTargetMap();
        (this.obf = new ObfuscationManager(this)).init();
        this.validators = (List<IMixinValidator>)ImmutableList.of(new ParentValidator(this), new TargetValidator(this));
        this.initTokenCache(this.getOption("tokens"));
    }
    
    private String checkPluginVersion(final String version) {
        if (version == null) {
            return null;
        }
        final VersionNumber pluginVersion = VersionNumber.parse(version);
        final VersionNumber recommendedVersion = VersionNumber.parse("0.7");
        if (pluginVersion.compareTo(recommendedVersion) < 0) {
            this.printMessage(Diagnostic.Kind.WARNING, String.format("MixinGradle version %s is out of date. Update to the recommended version %s", pluginVersion, recommendedVersion));
        }
        return pluginVersion.toString();
    }
    
    protected TargetMap initTargetMap() {
        final TargetMap targets = TargetMap.create(System.getProperty("mixin.target.mapid"));
        System.setProperty("mixin.target.mapid", targets.getSessionId());
        final String targetsFileName = this.getOption("dependencyTargetsFile");
        if (targetsFileName != null) {
            try {
                targets.readImports(new File(targetsFileName));
            }
            catch (final IOException ex) {
                this.printMessage(Diagnostic.Kind.WARNING, "Could not read from specified imports file: " + targetsFileName);
            }
        }
        return targets;
    }
    
    private void initTokenCache(final String tokens) {
        if (tokens != null) {
            final Pattern tokenPattern = Pattern.compile("^([A-Z0-9\\-_\\.]+)=([0-9]+)$");
            final String[] split;
            final String[] tokenValues = split = tokens.replaceAll("\\s", "").toUpperCase(Locale.ROOT).split("[;,]");
            for (final String tokenValue : split) {
                final Matcher tokenMatcher = tokenPattern.matcher(tokenValue);
                if (tokenMatcher.matches()) {
                    this.tokenCache.put(tokenMatcher.group(1), Integer.parseInt(tokenMatcher.group(2)));
                }
            }
        }
    }
    
    @Override
    public ITypeHandleProvider getTypeProvider() {
        return this;
    }
    
    @Override
    public ITokenProvider getTokenProvider() {
        return this;
    }
    
    @Override
    public IObfuscationManager getObfuscationManager() {
        return this.obf;
    }
    
    @Override
    public IJavadocProvider getJavadocProvider() {
        return this;
    }
    
    @Override
    public ProcessingEnvironment getProcessingEnvironment() {
        return this.processingEnv;
    }
    
    @Override
    public CompilerEnvironment getCompilerEnvironment() {
        return this.env;
    }
    
    @Override
    public Integer getToken(final String token) {
        if (this.tokenCache.containsKey(token)) {
            return this.tokenCache.get(token);
        }
        final String option = this.getOption(token);
        Integer value = null;
        try {
            value = Integer.parseInt(option);
        }
        catch (final Exception ex) {}
        this.tokenCache.put(token, value);
        return value;
    }
    
    @Override
    public String getOption(final String option) {
        if (option == null) {
            return null;
        }
        final String value = this.processingEnv.getOptions().get(option);
        if (value != null) {
            return value;
        }
        return this.getProperties().getProperty(option);
    }
    
    @Override
    public String getOption(final String option, final String defaultValue) {
        final String value = this.getOption(option);
        return (value != null) ? value : defaultValue;
    }
    
    @Override
    public boolean getOption(final String option, final boolean defaultValue) {
        final String value = this.getOption(option);
        return (value != null) ? Boolean.parseBoolean(value) : defaultValue;
    }
    
    @Override
    public List<String> getOptions(final String option) {
        final ImmutableList.Builder<String> list = ImmutableList.builder();
        final String value = this.getOption(option);
        if (value != null) {
            for (final String part : value.split(",")) {
                list.add(part);
            }
        }
        return list.build();
    }
    
    public Properties getProperties() {
        if (this.properties == null) {
            this.properties = new Properties();
            try {
                final Filer filer = this.processingEnv.getFiler();
                final FileObject propertyFile = filer.getResource(StandardLocation.SOURCE_PATH, "", "mixin.properties");
                if (propertyFile != null) {
                    final InputStream inputStream = propertyFile.openInputStream();
                    this.properties.load(inputStream);
                    inputStream.close();
                }
            }
            catch (final Exception ex) {}
        }
        return this.properties;
    }
    
    public void writeMappings() {
        this.obf.writeMappings();
    }
    
    public void writeReferences() {
        this.obf.writeReferences();
    }
    
    public void clear() {
        this.mixins.clear();
    }
    
    public void registerMixin(final TypeElement mixinType) {
        final String name = mixinType.getQualifiedName().toString();
        if (!this.mixins.containsKey(name)) {
            final AnnotatedMixin mixin = new AnnotatedMixin(this, mixinType);
            this.targets.registerTargets(mixin);
            mixin.runValidators(IMixinValidator.ValidationPass.EARLY, this.validators);
            this.mixins.put(name, mixin);
            this.mixinsForPass.add(mixin);
        }
    }
    
    public AnnotatedMixin getMixin(final TypeElement mixinType) {
        return this.getMixin(mixinType.getQualifiedName().toString());
    }
    
    public AnnotatedMixin getMixin(final String mixinType) {
        return this.mixins.get(mixinType);
    }
    
    public Collection<TypeMirror> getMixinsTargeting(final TypeMirror targetType) {
        return this.getMixinsTargeting((TypeElement)((DeclaredType)targetType).asElement());
    }
    
    public Collection<TypeMirror> getMixinsTargeting(final TypeElement targetType) {
        final List<TypeMirror> minions = new ArrayList<TypeMirror>();
        for (final TypeReference mixin : this.targets.getMixinsTargeting(targetType)) {
            final TypeHandle handle = mixin.getHandle(this.processingEnv);
            if (handle != null && handle.hasTypeMirror()) {
                minions.add(handle.getTypeMirror());
            }
        }
        return minions;
    }
    
    public void registerAccessor(final TypeElement mixinType, final ExecutableElement method) {
        final AnnotatedMixin mixinClass = this.getMixin(mixinType);
        if (mixinClass == null) {
            this.printMessage(IMessagerEx.MessageType.ACCESSOR_ON_NON_MIXIN_METHOD, "Found @Accessor annotation on a non-mixin method", method);
            return;
        }
        final AnnotationHandle accessor = AnnotationHandle.of(method, Accessor.class);
        mixinClass.registerAccessor(method, accessor, shouldRemap(mixinClass, accessor));
    }
    
    public void registerInvoker(final TypeElement mixinType, final ExecutableElement method) {
        final AnnotatedMixin mixinClass = this.getMixin(mixinType);
        if (mixinClass == null) {
            this.printMessage(IMessagerEx.MessageType.ACCESSOR_ON_NON_MIXIN_METHOD, "Found @Invoker annotation on a non-mixin method", method);
            return;
        }
        final AnnotationHandle invoker = AnnotationHandle.of(method, Invoker.class);
        mixinClass.registerInvoker(method, invoker, shouldRemap(mixinClass, invoker));
    }
    
    public void registerOverwrite(final TypeElement mixinType, final ExecutableElement method) {
        final AnnotatedMixin mixinClass = this.getMixin(mixinType);
        if (mixinClass == null) {
            this.printMessage(IMessagerEx.MessageType.OVERWRITE_ON_NON_MIXIN_METHOD, "Found @Overwrite annotation on a non-mixin method", method);
            return;
        }
        final AnnotationHandle overwrite = AnnotationHandle.of(method, Overwrite.class);
        mixinClass.registerOverwrite(method, overwrite, shouldRemap(mixinClass, overwrite));
    }
    
    public void registerShadow(final TypeElement mixinType, final VariableElement field, final AnnotationHandle shadow) {
        final AnnotatedMixin mixinClass = this.getMixin(mixinType);
        if (mixinClass == null) {
            this.printMessage(IMessagerEx.MessageType.SHADOW_ON_NON_MIXIN_ELEMENT, "Found @Shadow annotation on a non-mixin field", field);
            return;
        }
        mixinClass.registerShadow(field, shadow, shouldRemap(mixinClass, shadow));
    }
    
    public void registerShadow(final TypeElement mixinType, final ExecutableElement method, final AnnotationHandle shadow) {
        final AnnotatedMixin mixinClass = this.getMixin(mixinType);
        if (mixinClass == null) {
            this.printMessage(IMessagerEx.MessageType.SHADOW_ON_NON_MIXIN_ELEMENT, "Found @Shadow annotation on a non-mixin method", method);
            return;
        }
        mixinClass.registerShadow(method, shadow, shouldRemap(mixinClass, shadow));
    }
    
    public void registerInjector(final TypeElement mixinType, final ExecutableElement method, final AnnotationHandle inject) {
        final AnnotatedMixin mixinClass = this.getMixin(mixinType);
        if (mixinClass == null) {
            this.printMessage(IMessagerEx.MessageType.INJECTOR_ON_NON_MIXIN_METHOD, "Found " + inject + " annotation on a non-mixin method", method);
            return;
        }
        final InjectorRemap remap = new InjectorRemap(shouldRemap(mixinClass, inject));
        mixinClass.registerInjector(method, inject, remap);
        remap.dispatchPendingMessages(this);
    }
    
    public void registerSoftImplements(final TypeElement mixin, final AnnotationHandle implementsAnnotation) {
        final AnnotatedMixin mixinClass = this.getMixin(mixin);
        if (mixinClass == null) {
            this.printMessage(IMessagerEx.MessageType.SOFT_IMPLEMENTS_ON_NON_MIXIN, "Found @Implements annotation on a non-mixin class");
            return;
        }
        mixinClass.registerSoftImplements(implementsAnnotation);
    }
    
    public void onPassStarted() {
        this.mixinsForPass.clear();
    }
    
    public void onPassCompleted(final RoundEnvironment roundEnv) {
        if (!"true".equalsIgnoreCase(this.getOption("disableTargetExport"))) {
            this.targets.write(true);
        }
        for (final AnnotatedMixin mixin : roundEnv.processingOver() ? this.mixins.values() : this.mixinsForPass) {
            mixin.runValidators(roundEnv.processingOver() ? IMixinValidator.ValidationPass.FINAL : IMixinValidator.ValidationPass.LATE, this.validators);
        }
    }
    
    private static boolean shouldRemap(final AnnotatedMixin mixinClass, final AnnotationHandle annotation) {
        return annotation.getBoolean("remap", mixinClass.remap());
    }
    
    private static boolean shouldSuppress(final Element element, final SuppressedBy suppressedBy) {
        return element != null && suppressedBy != null && (AnnotationHandle.of(element, SuppressWarnings.class).getList().contains(suppressedBy.getToken()) || shouldSuppress(element.getEnclosingElement(), suppressedBy));
    }
    
    @Override
    public void printMessage(final IMessagerEx.MessageType type, final CharSequence msg) {
        if (type.isEnabled()) {
            this.printMessage(type.getKind(), type.decorate(msg));
        }
    }
    
    @Override
    public void printMessage(final Diagnostic.Kind kind, final CharSequence msg) {
        this.processingEnv.getMessager().printMessage(kind, msg);
    }
    
    @Override
    public void printMessage(final IMessagerEx.MessageType type, final CharSequence msg, final Element element) {
        if (type.isEnabled()) {
            this.printMessage(type.getKind(), type.decorate(msg), element);
        }
    }
    
    @Override
    public void printMessage(final Diagnostic.Kind kind, final CharSequence msg, final Element element) {
        this.processingEnv.getMessager().printMessage(kind, msg, element);
    }
    
    @Override
    public void printMessage(final IMessagerEx.MessageType type, final CharSequence msg, final Element element, final SuppressedBy suppressedBy) {
        if (type.isEnabled()) {
            this.printMessage(type.getKind(), type.decorate(msg), element, suppressedBy);
        }
    }
    
    @Override
    public void printMessage(final Diagnostic.Kind kind, final CharSequence msg, final Element element, final SuppressedBy suppressedBy) {
        if (kind != Diagnostic.Kind.WARNING || !shouldSuppress(element, suppressedBy)) {
            this.processingEnv.getMessager().printMessage(kind, msg, element);
        }
    }
    
    @Override
    public void printMessage(final IMessagerEx.MessageType type, final CharSequence msg, final Element element, final AnnotationMirror annotation) {
        if (type.isEnabled()) {
            this.printMessage(type.getKind(), type.decorate(msg), element, annotation);
        }
    }
    
    @Override
    public void printMessage(final Diagnostic.Kind kind, final CharSequence msg, final Element element, final AnnotationMirror annotation) {
        this.processingEnv.getMessager().printMessage(kind, msg, element, annotation);
    }
    
    @Override
    public void printMessage(final IMessagerEx.MessageType type, final CharSequence msg, final Element element, final AnnotationMirror annotation, final SuppressedBy suppressedBy) {
        if (type.isEnabled()) {
            this.printMessage(type.getKind(), type.decorate(msg), element, annotation, suppressedBy);
        }
    }
    
    @Override
    public void printMessage(final Diagnostic.Kind kind, final CharSequence msg, final Element element, final AnnotationMirror annotation, final SuppressedBy suppressedBy) {
        if (kind != Diagnostic.Kind.WARNING || !shouldSuppress(element, suppressedBy)) {
            this.processingEnv.getMessager().printMessage(kind, msg, element, annotation);
        }
    }
    
    @Override
    public void printMessage(final IMessagerEx.MessageType type, final CharSequence msg, final Element element, final AnnotationMirror annotation, final AnnotationValue value) {
        if (type.isEnabled()) {
            this.printMessage(type.getKind(), type.decorate(msg), element, annotation, value);
        }
    }
    
    @Override
    public void printMessage(final Diagnostic.Kind kind, final CharSequence msg, final Element element, final AnnotationMirror annotation, final AnnotationValue value) {
        this.processingEnv.getMessager().printMessage(kind, msg, element, annotation, value);
    }
    
    @Override
    public void printMessage(final IMessagerEx.MessageType type, final CharSequence msg, final Element element, final AnnotationMirror annotation, final AnnotationValue value, final SuppressedBy suppressedBy) {
        if (type.isEnabled()) {
            this.printMessage(type.getKind(), type.decorate(msg), element, annotation, value, suppressedBy);
        }
    }
    
    @Override
    public void printMessage(final Diagnostic.Kind kind, final CharSequence msg, final Element element, final AnnotationMirror annotation, final AnnotationValue value, final SuppressedBy suppressedBy) {
        if (kind != Diagnostic.Kind.WARNING || !shouldSuppress(element, suppressedBy)) {
            this.processingEnv.getMessager().printMessage(kind, msg, element, annotation, value);
        }
    }
    
    @Override
    public TypeHandle getTypeHandle(String name) {
        name = name.replace('/', '.');
        final Elements elements = this.processingEnv.getElementUtils();
        final TypeElement element = this.getTypeElement(name, elements);
        if (element != null) {
            try {
                return new TypeHandle(element);
            }
            catch (final NullPointerException ex) {}
        }
        final int lastDotPos = name.lastIndexOf(46);
        if (lastDotPos > -1) {
            final String pkgName = name.substring(0, lastDotPos);
            final PackageElement pkg = elements.getPackageElement(pkgName);
            if (pkg != null) {
                final TypeHandle asmTypeHandle = TypeHandleASM.of(pkg, name.substring(lastDotPos + 1), this);
                if (asmTypeHandle != null) {
                    return asmTypeHandle;
                }
                return new TypeHandle(pkg, name);
            }
        }
        return null;
    }
    
    @Override
    public TypeHandle getTypeHandle(final Object type) {
        if (type instanceof TypeHandle) {
            return (TypeHandle)type;
        }
        if (type instanceof DeclaredType) {
            return new TypeHandle((DeclaredType)type);
        }
        if (type instanceof Type) {
            return this.getTypeHandle(((Type)type).getClassName());
        }
        if (type instanceof TypeElement) {
            return new TypeHandle((DeclaredType)((TypeElement)type).asType());
        }
        if (type instanceof String) {
            return this.getTypeHandle(type.toString());
        }
        return null;
    }
    
    public TypeElement getTypeElement(final String name) {
        return this.getTypeElement(name.replace('/', '.'), this.processingEnv.getElementUtils());
    }
    
    private TypeElement getTypeElement(String name, final Elements elements) {
        TypeElement element = elements.getTypeElement(name);
        if (element != null || name.indexOf(36) < 0) {
            return element;
        }
        final int lastDotPos = name.lastIndexOf(46);
        final String pkg = (lastDotPos > -1) ? name.substring(0, lastDotPos) : "";
        name = name.substring(pkg.length());
        element = elements.getTypeElement(pkg + name.replace('$', '.'));
        if (element != null) {
            return element;
        }
        final char[] source = name.toCharArray();
        final char[] dest = new char[source.length];
        int occurs = 0;
        for (int offset = 0; offset < source.length; ++offset) {
            if (source[offset] == '$') {
                ++occurs;
            }
        }
        if (occurs > 10 || occurs < 2) {
            return null;
        }
        for (int mask = 1; mask < 1 << occurs && element == null; element = elements.getTypeElement(pkg + new String(dest)), ++mask) {
            int offset2 = source.length - 1;
            int index = 0;
            while (offset2 >= 0) {
                dest[offset2] = ((source[offset2] == '$' && (mask & 1 << index++) != 0x0) ? '.' : source[offset2]);
                --offset2;
            }
        }
        return element;
    }
    
    @Override
    public TypeHandle getSimulatedHandle(String name, final TypeMirror simulatedTarget) {
        name = name.replace('/', '.');
        final int lastDotPos = name.lastIndexOf(46);
        if (lastDotPos > -1) {
            final String pkg = name.substring(0, lastDotPos);
            final PackageElement packageElement = this.processingEnv.getElementUtils().getPackageElement(pkg);
            if (packageElement != null) {
                return new TypeHandleSimulated(packageElement, name, simulatedTarget);
            }
        }
        return new TypeHandleSimulated(name, simulatedTarget);
    }
    
    @Override
    public String getJavadoc(final Element element) {
        final Elements elements = this.processingEnv.getElementUtils();
        return elements.getDocComment(element);
    }
    
    public static AnnotatedMixins getMixinsForEnvironment(final ProcessingEnvironment processingEnv) {
        AnnotatedMixins mixins = AnnotatedMixins.instances.get(processingEnv);
        if (mixins == null) {
            mixins = new AnnotatedMixins(processingEnv);
            AnnotatedMixins.instances.put(processingEnv, mixins);
        }
        return mixins;
    }
    
    static {
        AnnotatedMixins.instances = new HashMap<ProcessingEnvironment, AnnotatedMixins>();
    }
}
