// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.selectors;

import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;
import java.util.regex.Pattern;

public final class MemberMatcher implements ITargetSelector
{
    private static final Pattern PATTERN;
    private static final String[] PATTERN_SOURCE_NAMES;
    private static final int SOURCE_OWNER = 0;
    private static final int SOURCE_NAME = 1;
    private static final int SOURCE_DESC = 2;
    private final Pattern[] patterns;
    private final Exception parseException;
    private final String input;
    
    private MemberMatcher(final Pattern[] patterns, final Exception parseException, final String input) {
        this.patterns = patterns;
        this.parseException = parseException;
        this.input = input;
    }
    
    public static MemberMatcher parse(final String input, final ISelectorContext context) {
        final Matcher matcher = MemberMatcher.PATTERN.matcher(input);
        final Pattern[] patterns = new Pattern[3];
        Exception parseException = null;
        while (matcher.find()) {
            Pattern pattern;
            try {
                pattern = Pattern.compile(matcher.group(3));
            }
            catch (final PatternSyntaxException ex) {
                parseException = ex;
                pattern = Pattern.compile(".*");
                ex.printStackTrace();
            }
            final int patternId = "owner".equals(matcher.group(2)) ? 0 : ("desc".equals(matcher.group(2)) ? 2 : 1);
            if (patterns[patternId] != null) {
                parseException = new InvalidSelectorException("Pattern for '" + MemberMatcher.PATTERN_SOURCE_NAMES[patternId] + "' specified multiple times: Old=/" + patterns[patternId].pattern() + "/ New=/" + pattern.pattern() + "/");
            }
            patterns[patternId] = pattern;
        }
        return new MemberMatcher(patterns, parseException, input);
    }
    
    @Override
    public ITargetSelector validate() throws InvalidSelectorException {
        if (this.parseException != null) {
            if (this.parseException instanceof InvalidSelectorException) {
                throw (InvalidSelectorException)this.parseException;
            }
            throw new InvalidSelectorException("Error parsing regex selector", this.parseException);
        }
        else {
            boolean validPattern = false;
            for (final Pattern pattern : this.patterns) {
                validPattern |= (pattern != null);
            }
            if (!validPattern) {
                throw new InvalidSelectorException("Error parsing regex selector, the input was in an unexpected format: " + this.input);
            }
            return this;
        }
    }
    
    @Override
    public String toString() {
        return this.input;
    }
    
    @Override
    public ITargetSelector next() {
        return this;
    }
    
    @Override
    public ITargetSelector configure(final Configure request, final String... args) {
        request.checkArgs(args);
        return this;
    }
    
    @Override
    public ITargetSelector attach(final ISelectorContext context) throws InvalidSelectorException {
        return this;
    }
    
    @Override
    public int getMinMatchCount() {
        return 0;
    }
    
    @Override
    public int getMaxMatchCount() {
        return Integer.MAX_VALUE;
    }
    
    @Override
    public <TNode> MatchResult match(final ElementNode<TNode> node) {
        return (node == null) ? MatchResult.NONE : this.matches(node.getOwner(), node.getName(), node.getDesc());
    }
    
    private MatchResult matches(final String... args) {
        MatchResult result = MatchResult.NONE;
        for (int i = 0; i < this.patterns.length; ++i) {
            if (this.patterns[i] != null) {
                if (args[i] != null) {
                    if (this.patterns[i].matcher(args[i]).find()) {
                        result = MatchResult.EXACT_MATCH;
                    }
                    else {
                        result = MatchResult.NONE;
                    }
                }
            }
        }
        return result;
    }
    
    static {
        PATTERN = Pattern.compile("((owner|name|desc)\\s*=\\s*)?/(.*?)(?<!\\\\)/");
        PATTERN_SOURCE_NAMES = new String[] { "owner", "name", "desc" };
    }
}
