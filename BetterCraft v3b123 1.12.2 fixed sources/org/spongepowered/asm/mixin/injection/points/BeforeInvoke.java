// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.points;

import org.objectweb.asm.tree.MethodInsnNode;
import java.util.ListIterator;
import org.spongepowered.asm.mixin.injection.selectors.throwables.SelectorConstraintException;
import org.spongepowered.asm.mixin.injection.selectors.ElementNode;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.mixin.injection.selectors.ITargetSelectorByName;
import org.objectweb.asm.tree.AbstractInsnNode;
import java.util.Collection;
import org.objectweb.asm.tree.InsnList;
import java.util.Locale;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.injection.IInjectionPointContext;
import org.spongepowered.asm.mixin.injection.selectors.ITargetSelector;
import org.spongepowered.asm.mixin.injection.InjectionPoint;

@AtCode("INVOKE")
public class BeforeInvoke extends InjectionPoint
{
    protected final ITargetSelector target;
    protected final boolean allowPermissive;
    protected final int ordinal;
    protected final String className;
    protected final IInjectionPointContext context;
    protected final IMixinContext mixin;
    protected final ILogger logger;
    private boolean log;
    
    public BeforeInvoke(final InjectionPointData data) {
        super(data);
        this.logger = MixinService.getService().getLogger("mixin");
        this.log = false;
        this.target = data.getTarget();
        this.ordinal = data.getOrdinal();
        this.log = data.get("log", false);
        this.className = this.getClassName();
        this.context = data.getContext();
        this.mixin = data.getMixin();
        this.allowPermissive = (this.mixin.getOption(MixinEnvironment.Option.REFMAP_REMAP) && this.mixin.getOption(MixinEnvironment.Option.REFMAP_REMAP_ALLOW_PERMISSIVE) && !this.mixin.getReferenceMapper().isDefault());
    }
    
    private String getClassName() {
        final AtCode atCode = this.getClass().getAnnotation(AtCode.class);
        return String.format("@At(%s)", (atCode != null) ? atCode.value() : this.getClass().getSimpleName().toUpperCase(Locale.ROOT));
    }
    
    public BeforeInvoke setLogging(final boolean logging) {
        this.log = logging;
        return this;
    }
    
    @Override
    public boolean find(final String desc, final InsnList insns, final Collection<AbstractInsnNode> nodes) {
        this.log("{}->{} is searching for an injection point in method with descriptor {}", this.context, this.className, desc);
        final boolean hasDescriptor = this.target instanceof ITargetSelectorByName && ((ITargetSelectorByName)this.target).getDesc() == null;
        boolean found = this.find(desc, insns, nodes, this.target, SearchType.STRICT);
        if (!found && hasDescriptor && this.allowPermissive) {
            this.logger.warn("STRICT match for {} using \"{}\" in {} returned 0 results, attempting permissive search. To inhibit permissive search set mixin.env.allowPermissiveMatch=false", this.className, this.target, this.mixin);
            found = this.find(desc, insns, nodes, this.target, SearchType.PERMISSIVE);
        }
        return found;
    }
    
    protected boolean find(final String desc, final InsnList insns, final Collection<AbstractInsnNode> nodes, final ITargetSelector selector, final SearchType searchType) {
        if (selector == null) {
            return false;
        }
        final ITargetSelector target = ((searchType == SearchType.PERMISSIVE) ? selector.configure(ITargetSelector.Configure.PERMISSIVE, new String[0]) : selector).configure(ITargetSelector.Configure.SELECT_INSTRUCTION, new String[0]);
        int ordinal = 0;
        int found = 0;
        int matchCount = 0;
        for (final AbstractInsnNode insn : insns) {
            if (this.matchesInsn(insn)) {
                final MemberInfo nodeInfo = new MemberInfo(insn);
                this.log("{}->{} is considering {}", this.context, this.className, nodeInfo);
                if (target.match((ElementNode<Object>)ElementNode.of((TNode)insn)).isExactMatch()) {
                    this.log("{}->{} > found a matching insn, checking preconditions...", this.context, this.className);
                    if (++matchCount > target.getMaxMatchCount()) {
                        break;
                    }
                    if (this.matchesOrdinal(ordinal)) {
                        this.log("{}->{} > > > found a matching insn at ordinal {}", this.context, this.className, ordinal);
                        if (this.addInsn(insns, nodes, insn)) {
                            ++found;
                        }
                    }
                    ++ordinal;
                }
            }
            this.inspectInsn(desc, insns, insn);
        }
        if (searchType == SearchType.PERMISSIVE && found > 1) {
            this.logger.warn("A permissive match for {} using \"{}\" in {} matched {} instructions, this may cause unexpected behaviour. To inhibit permissive search set mixin.env.allowPermissiveMatch=false", this.className, selector, this.mixin, found);
        }
        if (matchCount < target.getMinMatchCount()) {
            throw new SelectorConstraintException(target, String.format("%s did not match the required number of targets (required=%d, matched=%d)", target, selector.getMinMatchCount(), matchCount));
        }
        return found > 0;
    }
    
    protected boolean addInsn(final InsnList insns, final Collection<AbstractInsnNode> nodes, final AbstractInsnNode insn) {
        nodes.add(insn);
        return true;
    }
    
    protected boolean matchesInsn(final AbstractInsnNode insn) {
        return insn instanceof MethodInsnNode;
    }
    
    protected void inspectInsn(final String desc, final InsnList insns, final AbstractInsnNode insn) {
    }
    
    protected boolean matchesOrdinal(final int ordinal) {
        this.log("{}->{} > > comparing target ordinal {} with current ordinal {}", this.context, this.className, this.ordinal, ordinal);
        return this.ordinal == -1 || this.ordinal == ordinal;
    }
    
    protected void log(final String message, final Object... params) {
        if (this.log) {
            this.logger.info(message, params);
        }
    }
    
    public enum SearchType
    {
        STRICT, 
        PERMISSIVE;
    }
}
