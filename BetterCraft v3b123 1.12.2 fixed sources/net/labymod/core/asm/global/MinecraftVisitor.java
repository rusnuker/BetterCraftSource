// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core.asm.global;

import net.labymod.core.asm.LabyModCoreMod;
import org.objectweb.asm.Label;
import net.labymod.main.Source;
import org.objectweb.asm.MethodVisitor;
import net.labymod.core.asm.LabyModTransformer;

public class MinecraftVisitor extends ClassEditor
{
    private String createDisplayName;
    private String drawSplashScreenName;
    private String startGameName;
    private String minecraftName;
    private String fullscreenName;
    private String toggleFullscreenName;
    private String setInitialDisplayMode;
    private String rightClickMouseName;
    private String bootstrapName;
    private String printToSYSOUTName;
    private String runTickName;
    private String thirdPersonViewName;
    
    public MinecraftVisitor() {
        super(ClassEditorType.CLASS_VISITOR);
        LabyModTransformer.addVisitors();
        this.createDisplayName = LabyModTransformer.getMappingImplementation().getCreateDisplayName();
        this.drawSplashScreenName = LabyModTransformer.getMappingImplementation().getDrawSplashScreenName();
        this.startGameName = LabyModTransformer.getMappingImplementation().getStartGameName();
        this.minecraftName = LabyModTransformer.getMappingImplementation().getMinecraftName();
        this.fullscreenName = LabyModTransformer.getMappingImplementation().getFullscreenName();
        this.toggleFullscreenName = LabyModTransformer.getMappingImplementation().getToggleFullscreenName();
        this.setInitialDisplayMode = LabyModTransformer.getMappingImplementation().getSetInitialDisplayModeName();
        this.rightClickMouseName = LabyModTransformer.getMappingImplementation().getRightClickMouseName();
        this.bootstrapName = LabyModTransformer.getMappingImplementation().getBootstrapName();
        this.printToSYSOUTName = LabyModTransformer.getMappingImplementation().getPrintToSYSOUTName();
        this.runTickName = LabyModTransformer.getMappingImplementation().getRunTickName();
        this.thirdPersonViewName = LabyModTransformer.getMappingImplementation().getThirdPersonViewName();
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals(this.createDisplayName) && desc.equals("()V")) {
            return new MethodVisitor(262144, mv) {
                @Override
                public void visitLdcInsn(Object cst) {
                    if (cst instanceof String) {
                        cst = cst + " | LabyMod " + "3.6.6" + " ";
                    }
                    super.visitLdcInsn(cst);
                }
            };
        }
        if (name.equals("<init>") && Source.ABOUT_MC_VERSION.startsWith("1.8")) {
            return new MethodVisitor(262144, mv) {
                @Override
                public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
                    if (name.equals("info")) {
                        super.visitMethodInsn(opcode, owner, "debug", desc, itf);
                    }
                    else {
                        super.visitMethodInsn(opcode, owner, name, desc, itf);
                    }
                }
            };
        }
        if (name.equals(this.runTickName) && desc.equals("()V")) {
            return new MethodVisitor(262144, mv) {
                private boolean inject = false;
                
                @Override
                public void visitJumpInsn(final int opcode, final Label label) {
                    super.visitJumpInsn(opcode, label);
                    if ((opcode == 154 || opcode == 160) && this.inject) {
                        super.visitMethodInsn(184, "BytecodeMethods", "canSwitchShader", "()Z", false);
                        super.visitJumpInsn(153, label);
                    }
                    this.inject = false;
                }
                
                @Override
                public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
                    super.visitFieldInsn(opcode, owner, name, desc);
                    this.inject = false;
                    if (name.equals(MinecraftVisitor.this.thirdPersonViewName)) {
                        this.inject = true;
                    }
                }
            };
        }
        if (desc.endsWith(";Ljava/lang/String;)V")) {
            return new MethodVisitor(262144, mv) {
                @Override
                public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
                    if (owner.equals("java/lang/System") && name.equals("gc") && desc.equals("()V")) {
                        final Label label = new Label();
                        super.visitMethodInsn(184, "BytecodeMethods", "useGCOnLoadWorld", "()Z", false);
                        super.visitJumpInsn(153, label);
                        super.visitMethodInsn(opcode, owner, name, desc, itf);
                        super.visitLabel(label);
                    }
                    else {
                        super.visitMethodInsn(opcode, owner, name, desc, itf);
                    }
                }
            };
        }
        if (desc.endsWith(";)V") && access == 1) {
            return new MethodVisitor(262144, mv) {
                private int index = 0;
                
                @Override
                public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
                    super.visitMethodInsn(opcode, owner, name, desc, itf);
                    if (opcode == 184 && owner.equals(MinecraftVisitor.this.bootstrapName) && name.equals(MinecraftVisitor.this.printToSYSOUTName) && desc.equals("(Ljava/lang/String;)V")) {
                        if (this.index >= 1 && this.index <= 3) {
                            super.visitIntInsn(25, 3);
                            super.visitIntInsn(25, 1);
                            super.visitMethodInsn(184, "BytecodeMethods", "reportCrash", "(Ljava/io/File;Ljava/lang/Object;)V", false);
                        }
                        ++this.index;
                    }
                }
            };
        }
        if (name.equals(this.startGameName) && desc.equals("()V")) {
            return new MethodVisitor(262144, mv) {
                private boolean renderCustomSplashScreen = false;
                private boolean inserted;
                
                @Override
                public void visitLabel(final Label label) {
                    super.visitLabel(label);
                    if (!this.inserted) {
                        this.inserted = true;
                        this.mv.visitInsn(LabyModCoreMod.isForge() ? 4 : 3);
                        this.mv.visitMethodInsn(184, "net/labymod/main/LabyModForge", "setForge", "(Z)V", false);
                    }
                }
                
                @Override
                public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
                    if (!LabyModCoreMod.isForge() && ((opcode == 183 && name.equals(MinecraftVisitor.this.drawSplashScreenName)) || this.renderCustomSplashScreen)) {
                        this.renderCustomSplashScreen = true;
                        super.visitMethodInsn(opcode, owner, name, desc, itf);
                        this.mv.visitMethodInsn(184, "net/labymod/utils/manager/CustomLoadingScreen", "renderInstance", "()V", false);
                    }
                    else {
                        super.visitMethodInsn(opcode, owner, name, desc, itf);
                    }
                }
            };
        }
        if (name.equals(this.setInitialDisplayMode) && desc.equals("()V")) {
            return new MethodVisitor(262144, mv) {
                private boolean firstMethodAdded = false;
                
                @Override
                public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
                    if (!this.firstMethodAdded && opcode == 180) {
                        this.firstMethodAdded = true;
                        this.mv.visitVarInsn(25, 0);
                        this.mv.visitFieldInsn(180, MinecraftVisitor.this.minecraftName, MinecraftVisitor.this.fullscreenName, "Z");
                        this.mv.visitMethodInsn(184, "BytecodeMethods", "borderlessWindowAtInitialDisplayMode", "(Z)V", false);
                        this.mv.visitVarInsn(25, 0);
                    }
                    if (opcode == 184 && name == "setFullscreen") {
                        return;
                    }
                    super.visitFieldInsn(opcode, owner, name, desc);
                }
            };
        }
        if (name.equals(this.toggleFullscreenName) && desc.equals("()V")) {
            return new MethodVisitor(262144, mv) {
                private boolean firstMethodAdded = false;
                
                @Override
                public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
                    if (!this.firstMethodAdded && opcode == 180) {
                        this.firstMethodAdded = true;
                        super.visitFieldInsn(opcode, owner, name, desc);
                        this.mv.visitInsn(4);
                        this.mv.visitMethodInsn(184, "BytecodeMethods", "borderlessWindowAtToggleFullscreen", "(ZZ)V", false);
                        this.mv.visitVarInsn(25, 0);
                        this.mv.visitFieldInsn(180, MinecraftVisitor.this.minecraftName, MinecraftVisitor.this.fullscreenName, "Z");
                        return;
                    }
                    super.visitFieldInsn(opcode, owner, name, desc);
                }
                
                @Override
                public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
                    if (opcode == 184 && name.equals("setFullscreen")) {
                        this.mv.visitInsn(3);
                        this.mv.visitMethodInsn(184, "BytecodeMethods", "borderlessWindowAtToggleFullscreen", "(ZZ)V", false);
                        return;
                    }
                    super.visitMethodInsn(opcode, owner, name, desc, itf);
                }
            };
        }
        if (name.equals(this.rightClickMouseName) && desc.equals("()V")) {
            return new MethodVisitor(262144, mv) {
                private boolean addedA = false;
                private boolean addedB = false;
                
                @Override
                public void visitIntInsn(final int opcode, final int operand) {
                    super.visitIntInsn(opcode, operand);
                    if (opcode == 25 && !this.addedA) {
                        this.addedA = true;
                        super.visitIntInsn(opcode, operand);
                    }
                }
                
                @Override
                public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
                    super.visitMethodInsn(opcode, owner, name, desc, itf);
                    if (opcode == 182 && !this.addedB) {
                        this.addedB = true;
                        super.visitMethodInsn(184, "BytecodeMethods", "shouldCancelMouseClick", "(Z)Z", false);
                    }
                }
            };
        }
        if (desc.equals("()V") && exceptions == null) {
            return new MethodVisitor(262144, mv) {
                @Override
                public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
                    super.visitMethodInsn(opcode, owner, name, desc, itf);
                    if (opcode == 184 && owner.equals("org/lwjgl/input/Keyboard") && name.equals("getEventCharacter")) {
                        this.mv.visitIntInsn(17, 256);
                        this.mv.visitInsn(96);
                    }
                }
            };
        }
        return mv;
    }
}
