// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer.ext.extensions;

import org.spongepowered.asm.service.MixinService;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.FileOutputStream;
import org.objectweb.asm.tree.ClassNode;
import java.util.Set;
import org.spongepowered.asm.util.SignaturePrinter;
import java.util.Collection;
import java.util.HashSet;
import org.spongepowered.asm.util.PrettyPrinter;
import java.util.Iterator;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import org.spongepowered.asm.mixin.MixinEnvironment;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.IOException;
import com.google.common.io.Files;
import com.google.common.base.Charsets;
import org.spongepowered.asm.util.Constants;
import com.google.common.collect.HashMultimap;
import org.spongepowered.asm.mixin.transformer.ClassInfo;
import com.google.common.collect.Multimap;
import java.io.File;
import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;

public class ExtensionCheckInterfaces implements IExtension
{
    private static final String AUDIT_DIR = "audit";
    private static final String IMPL_REPORT_FILENAME = "mixin_implementation_report";
    private static final String IMPL_REPORT_CSV_FILENAME = "mixin_implementation_report.csv";
    private static final String IMPL_REPORT_TXT_FILENAME = "mixin_implementation_report.txt";
    private static final ILogger logger;
    private final File csv;
    private final File report;
    private final Multimap<ClassInfo, ClassInfo.Method> interfaceMethods;
    private boolean strict;
    private boolean started;
    
    public ExtensionCheckInterfaces() {
        this.interfaceMethods = (Multimap<ClassInfo, ClassInfo.Method>)HashMultimap.create();
        this.started = false;
        final File debugOutputFolder = new File(Constants.DEBUG_OUTPUT_DIR, "audit");
        this.csv = new File(debugOutputFolder, "mixin_implementation_report.csv");
        this.report = new File(debugOutputFolder, "mixin_implementation_report.txt");
    }
    
    private void start() {
        if (this.started) {
            return;
        }
        this.started = true;
        this.csv.getParentFile().mkdirs();
        try {
            Files.write("Class,Method,Signature,Interface\n", this.csv, Charsets.ISO_8859_1);
        }
        catch (final IOException ex) {}
        try {
            final String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            Files.write("Mixin Implementation Report generated on " + dateTime + "\n", this.report, Charsets.ISO_8859_1);
        }
        catch (final IOException ex2) {}
    }
    
    @Override
    public boolean checkActive(final MixinEnvironment environment) {
        this.strict = environment.getOption(MixinEnvironment.Option.CHECK_IMPLEMENTS_STRICT);
        return environment.getOption(MixinEnvironment.Option.CHECK_IMPLEMENTS);
    }
    
    @Override
    public void preApply(final ITargetClassContext context) {
        final ClassInfo targetClassInfo = context.getClassInfo();
        for (final ClassInfo.Method m : targetClassInfo.getInterfaceMethods(false)) {
            this.interfaceMethods.put(targetClassInfo, m);
        }
    }
    
    @Override
    public void postApply(final ITargetClassContext context) {
        this.start();
        final ClassInfo targetClassInfo = context.getClassInfo();
        if (targetClassInfo.isAbstract() && !this.strict) {
            ExtensionCheckInterfaces.logger.info("{} is skipping abstract target {}", this.getClass().getSimpleName(), context);
            return;
        }
        final String className = targetClassInfo.getName().replace('/', '.');
        int missingMethodCount = 0;
        final PrettyPrinter printer = new PrettyPrinter();
        printer.add("Class: %s", className).hr();
        printer.add("%-32s %-47s  %s", "Return Type", "Missing Method", "From Interface").hr();
        final Set<ClassInfo.Method> interfaceMethods = targetClassInfo.getInterfaceMethods(true);
        final Set<ClassInfo.Method> implementedMethods = new HashSet<ClassInfo.Method>(targetClassInfo.getSuperClass().getInterfaceMethods(true));
        implementedMethods.addAll(this.interfaceMethods.removeAll(targetClassInfo));
        for (final ClassInfo.Method method : interfaceMethods) {
            final ClassInfo.Method found = targetClassInfo.findMethodInHierarchy(method.getName(), method.getDesc(), ClassInfo.SearchType.ALL_CLASSES, ClassInfo.Traversal.ALL);
            if (found != null && !found.isAbstract()) {
                continue;
            }
            if (implementedMethods.contains(method)) {
                continue;
            }
            if (missingMethodCount > 0) {
                printer.add();
            }
            final SignaturePrinter signaturePrinter = new SignaturePrinter(method.getName(), method.getDesc()).setModifiers("");
            final String iface = method.getOwner().getName().replace('/', '.');
            ++missingMethodCount;
            printer.add("%-32s%s", signaturePrinter.getReturnType(), signaturePrinter);
            printer.add("%-80s  %s", "", iface);
            this.appendToCSVReport(className, method, iface);
        }
        if (missingMethodCount > 0) {
            printer.hr().add("%82s%s: %d", "", "Total unimplemented", missingMethodCount);
            printer.print(System.err);
            this.appendToTextReport(printer);
        }
    }
    
    @Override
    public void export(final MixinEnvironment env, final String name, final boolean force, final ClassNode classNode) {
    }
    
    private void appendToCSVReport(final String className, final ClassInfo.Method method, final String iface) {
        try {
            Files.append(String.format("%s,%s,%s,%s\n", className, method.getName(), method.getDesc(), iface), this.csv, Charsets.ISO_8859_1);
        }
        catch (final IOException ex) {}
    }
    
    private void appendToTextReport(final PrettyPrinter printer) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(this.report, true);
            final PrintStream stream = new PrintStream(fos);
            stream.print("\n");
            printer.print(stream);
        }
        catch (final Exception ex3) {
            if (fos != null) {
                try {
                    fos.close();
                }
                catch (final IOException ex) {
                    ExtensionCheckInterfaces.logger.catching(ex);
                }
            }
        }
        finally {
            if (fos != null) {
                try {
                    fos.close();
                }
                catch (final IOException ex2) {
                    ExtensionCheckInterfaces.logger.catching(ex2);
                }
            }
        }
    }
    
    static {
        logger = MixinService.getService().getLogger("mixin");
    }
}
