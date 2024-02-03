// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation.interfaces;

import org.spongepowered.asm.util.ITokenProvider;
import javax.annotation.processing.ProcessingEnvironment;

public interface IMixinAnnotationProcessor extends IMessagerSuppressible, IOptionProvider
{
    CompilerEnvironment getCompilerEnvironment();
    
    ProcessingEnvironment getProcessingEnvironment();
    
    IObfuscationManager getObfuscationManager();
    
    ITokenProvider getTokenProvider();
    
    ITypeHandleProvider getTypeProvider();
    
    IJavadocProvider getJavadocProvider();
    
    public enum CompilerEnvironment
    {
        JAVAC(false, "Java Compiler"), 
        JDT(true, "Eclipse (JDT)") {
            @Override
            protected boolean isDetected(final ProcessingEnvironment processingEnv) {
                return processingEnv.getClass().getName().contains("jdt");
            }
        }, 
        IDEA(true, "IntelliJ IDEA") {
            @Override
            protected boolean isDetected(final ProcessingEnvironment processingEnv) {
                for (final String ideaSystemProperty : new String[] { "idea.plugins.path", "idea.config.path", "idea.home.path", "idea.paths.selector" }) {
                    if (System.getProperty(ideaSystemProperty) != null) {
                        return true;
                    }
                }
                return false;
            }
        };
        
        private final boolean isDevelopmentEnvironment;
        private final String friendlyName;
        
        private CompilerEnvironment(final boolean isDevelopmentEnvironment, final String friendlyName) {
            this.isDevelopmentEnvironment = isDevelopmentEnvironment;
            this.friendlyName = friendlyName;
        }
        
        public boolean isCompiler() {
            return !this.isDevelopmentEnvironment;
        }
        
        public boolean isDevelopmentEnvironment() {
            return this.isDevelopmentEnvironment;
        }
        
        public String getFriendlyName() {
            return this.friendlyName;
        }
        
        protected boolean isDetected(final ProcessingEnvironment processingEnv) {
            return false;
        }
        
        public static CompilerEnvironment detect(final ProcessingEnvironment processingEnv) {
            for (final CompilerEnvironment environment : values()) {
                if (environment.isDetected(processingEnv)) {
                    return environment;
                }
            }
            return CompilerEnvironment.JAVAC;
        }
    }
}
