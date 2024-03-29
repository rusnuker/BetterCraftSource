// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.parser;

import java.util.LinkedList;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.tokens.BlockEntryToken;
import org.yaml.snakeyaml.events.DocumentEndEvent;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.events.StreamEndEvent;
import org.yaml.snakeyaml.tokens.StreamEndToken;
import org.yaml.snakeyaml.events.DocumentStartEvent;
import org.yaml.snakeyaml.events.StreamStartEvent;
import org.yaml.snakeyaml.tokens.StreamStartToken;
import org.yaml.snakeyaml.tokens.TagTuple;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.ImplicitTuple;
import org.yaml.snakeyaml.tokens.ScalarToken;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.tokens.TagToken;
import org.yaml.snakeyaml.tokens.AnchorToken;
import org.yaml.snakeyaml.events.AliasEvent;
import org.yaml.snakeyaml.tokens.AliasToken;
import java.util.List;
import java.util.Iterator;
import org.yaml.snakeyaml.tokens.DirectiveToken;
import org.yaml.snakeyaml.tokens.Token;
import org.yaml.snakeyaml.comments.CommentType;
import org.yaml.snakeyaml.events.CommentEvent;
import org.yaml.snakeyaml.tokens.CommentToken;
import org.yaml.snakeyaml.DumperOptions;
import java.util.HashMap;
import org.yaml.snakeyaml.scanner.ScannerImpl;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.util.ArrayStack;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.scanner.Scanner;
import java.util.Map;

public class ParserImpl implements Parser
{
    private static final Map<String, String> DEFAULT_TAGS;
    protected final Scanner scanner;
    private Event currentEvent;
    private final ArrayStack<Production> states;
    private final ArrayStack<Mark> marks;
    private Production state;
    private VersionTagsTuple directives;
    
    public ParserImpl(final StreamReader reader, final LoaderOptions options) {
        this(new ScannerImpl(reader, options));
    }
    
    public ParserImpl(final Scanner scanner) {
        this.scanner = scanner;
        this.currentEvent = null;
        this.directives = new VersionTagsTuple(null, new HashMap<String, String>(ParserImpl.DEFAULT_TAGS));
        this.states = new ArrayStack<Production>(100);
        this.marks = new ArrayStack<Mark>(10);
        this.state = new ParseStreamStart();
    }
    
    @Override
    public boolean checkEvent(final Event.ID choice) {
        this.peekEvent();
        return this.currentEvent != null && this.currentEvent.is(choice);
    }
    
    @Override
    public Event peekEvent() {
        if (this.currentEvent == null && this.state != null) {
            this.currentEvent = this.state.produce();
        }
        return this.currentEvent;
    }
    
    @Override
    public Event getEvent() {
        this.peekEvent();
        final Event value = this.currentEvent;
        this.currentEvent = null;
        return value;
    }
    
    private CommentEvent produceCommentEvent(final CommentToken token) {
        final Mark startMark = token.getStartMark();
        final Mark endMark = token.getEndMark();
        final String value = token.getValue();
        final CommentType type = token.getCommentType();
        return new CommentEvent(type, value, startMark, endMark);
    }
    
    private VersionTagsTuple processDirectives() {
        final HashMap<String, String> tagHandles = new HashMap<String, String>(this.directives.getTags());
        for (final String key : ParserImpl.DEFAULT_TAGS.keySet()) {
            tagHandles.remove(key);
        }
        this.directives = new VersionTagsTuple(null, tagHandles);
        while (this.scanner.checkToken(Token.ID.Directive)) {
            final DirectiveToken token = (DirectiveToken)this.scanner.getToken();
            if (token.getName().equals("YAML")) {
                if (this.directives.getVersion() != null) {
                    throw new ParserException(null, null, "found duplicate YAML directive", token.getStartMark());
                }
                final List<Integer> value = token.getValue();
                final Integer major = value.get(0);
                if (major != 1) {
                    throw new ParserException(null, null, "found incompatible YAML document (version 1.* is required)", token.getStartMark());
                }
                final Integer minor = value.get(1);
                if (minor == 0) {
                    this.directives = new VersionTagsTuple(DumperOptions.Version.V1_0, tagHandles);
                }
                else {
                    this.directives = new VersionTagsTuple(DumperOptions.Version.V1_1, tagHandles);
                }
            }
            else {
                if (!token.getName().equals("TAG")) {
                    continue;
                }
                final List<String> value2 = token.getValue();
                final String handle = value2.get(0);
                final String prefix = value2.get(1);
                if (tagHandles.containsKey(handle)) {
                    throw new ParserException(null, null, "duplicate tag handle " + handle, token.getStartMark());
                }
                tagHandles.put(handle, prefix);
            }
        }
        HashMap<String, String> detectedTagHandles = new HashMap<String, String>();
        if (!tagHandles.isEmpty()) {
            detectedTagHandles = new HashMap<String, String>(tagHandles);
        }
        for (final String key2 : ParserImpl.DEFAULT_TAGS.keySet()) {
            if (!tagHandles.containsKey(key2)) {
                tagHandles.put(key2, ParserImpl.DEFAULT_TAGS.get(key2));
            }
        }
        return new VersionTagsTuple(this.directives.getVersion(), detectedTagHandles);
    }
    
    private Event parseFlowNode() {
        return this.parseNode(false, false);
    }
    
    private Event parseBlockNodeOrIndentlessSequence() {
        return this.parseNode(true, true);
    }
    
    private Event parseNode(final boolean block, final boolean indentlessSequence) {
        Mark startMark = null;
        Mark endMark = null;
        Mark tagMark = null;
        Event event;
        if (this.scanner.checkToken(Token.ID.Alias)) {
            final AliasToken token = (AliasToken)this.scanner.getToken();
            event = new AliasEvent(token.getValue(), token.getStartMark(), token.getEndMark());
            this.state = this.states.pop();
        }
        else {
            String anchor = null;
            TagTuple tagTokenTag = null;
            if (this.scanner.checkToken(Token.ID.Anchor)) {
                final AnchorToken token2 = (AnchorToken)this.scanner.getToken();
                startMark = token2.getStartMark();
                endMark = token2.getEndMark();
                anchor = token2.getValue();
                if (this.scanner.checkToken(Token.ID.Tag)) {
                    final TagToken tagToken = (TagToken)this.scanner.getToken();
                    tagMark = tagToken.getStartMark();
                    endMark = tagToken.getEndMark();
                    tagTokenTag = tagToken.getValue();
                }
            }
            else if (this.scanner.checkToken(Token.ID.Tag)) {
                final TagToken tagToken2 = (TagToken)this.scanner.getToken();
                startMark = (tagMark = tagToken2.getStartMark());
                endMark = tagToken2.getEndMark();
                tagTokenTag = tagToken2.getValue();
                if (this.scanner.checkToken(Token.ID.Anchor)) {
                    final AnchorToken token3 = (AnchorToken)this.scanner.getToken();
                    endMark = token3.getEndMark();
                    anchor = token3.getValue();
                }
            }
            String tag = null;
            if (tagTokenTag != null) {
                final String handle = tagTokenTag.getHandle();
                final String suffix = tagTokenTag.getSuffix();
                if (handle != null) {
                    if (!this.directives.getTags().containsKey(handle)) {
                        throw new ParserException("while parsing a node", startMark, "found undefined tag handle " + handle, tagMark);
                    }
                    tag = this.directives.getTags().get(handle) + suffix;
                }
                else {
                    tag = suffix;
                }
            }
            if (startMark == null) {
                startMark = (endMark = this.scanner.peekToken().getStartMark());
            }
            event = null;
            final boolean implicit = tag == null || tag.equals("!");
            if (indentlessSequence && this.scanner.checkToken(Token.ID.BlockEntry)) {
                endMark = this.scanner.peekToken().getEndMark();
                event = new SequenceStartEvent(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.BLOCK);
                this.state = new ParseIndentlessSequenceEntryKey();
            }
            else if (this.scanner.checkToken(Token.ID.Scalar)) {
                final ScalarToken token4 = (ScalarToken)this.scanner.getToken();
                endMark = token4.getEndMark();
                ImplicitTuple implicitValues;
                if ((token4.getPlain() && tag == null) || "!".equals(tag)) {
                    implicitValues = new ImplicitTuple(true, false);
                }
                else if (tag == null) {
                    implicitValues = new ImplicitTuple(false, true);
                }
                else {
                    implicitValues = new ImplicitTuple(false, false);
                }
                event = new ScalarEvent(anchor, tag, implicitValues, token4.getValue(), startMark, endMark, token4.getStyle());
                this.state = this.states.pop();
            }
            else if (this.scanner.checkToken(Token.ID.FlowSequenceStart)) {
                endMark = this.scanner.peekToken().getEndMark();
                event = new SequenceStartEvent(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.FLOW);
                this.state = new ParseFlowSequenceFirstEntry();
            }
            else if (this.scanner.checkToken(Token.ID.FlowMappingStart)) {
                endMark = this.scanner.peekToken().getEndMark();
                event = new MappingStartEvent(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.FLOW);
                this.state = new ParseFlowMappingFirstKey();
            }
            else if (block && this.scanner.checkToken(Token.ID.BlockSequenceStart)) {
                endMark = this.scanner.peekToken().getStartMark();
                event = new SequenceStartEvent(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.BLOCK);
                this.state = new ParseBlockSequenceFirstEntry();
            }
            else if (block && this.scanner.checkToken(Token.ID.BlockMappingStart)) {
                endMark = this.scanner.peekToken().getStartMark();
                event = new MappingStartEvent(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.BLOCK);
                this.state = new ParseBlockMappingFirstKey();
            }
            else {
                if (anchor == null && tag == null) {
                    final Token token5 = this.scanner.peekToken();
                    throw new ParserException("while parsing a " + (block ? "block" : "flow") + " node", startMark, "expected the node content, but found '" + token5.getTokenId() + "'", token5.getStartMark());
                }
                event = new ScalarEvent(anchor, tag, new ImplicitTuple(implicit, false), "", startMark, endMark, DumperOptions.ScalarStyle.PLAIN);
                this.state = this.states.pop();
            }
        }
        return event;
    }
    
    private Event processEmptyScalar(final Mark mark) {
        return new ScalarEvent(null, null, new ImplicitTuple(true, false), "", mark, mark, DumperOptions.ScalarStyle.PLAIN);
    }
    
    static {
        (DEFAULT_TAGS = new HashMap<String, String>()).put("!", "!");
        ParserImpl.DEFAULT_TAGS.put("!!", "tag:yaml.org,2002:");
    }
    
    private class ParseStreamStart implements Production
    {
        @Override
        public Event produce() {
            final StreamStartToken token = (StreamStartToken)ParserImpl.this.scanner.getToken();
            final Event event = new StreamStartEvent(token.getStartMark(), token.getEndMark());
            ParserImpl.this.state = new ParseImplicitDocumentStart();
            return event;
        }
    }
    
    private class ParseImplicitDocumentStart implements Production
    {
        @Override
        public Event produce() {
            if (ParserImpl.this.scanner.checkToken(Token.ID.Comment)) {
                ParserImpl.this.state = new ParseImplicitDocumentStart();
                return ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
            }
            if (!ParserImpl.this.scanner.checkToken(Token.ID.Directive, Token.ID.DocumentStart, Token.ID.StreamEnd)) {
                final Token token = ParserImpl.this.scanner.peekToken();
                final Mark endMark;
                final Mark startMark = endMark = token.getStartMark();
                final Event event = new DocumentStartEvent(startMark, endMark, false, null, null);
                ParserImpl.this.states.push(new ParseDocumentEnd());
                ParserImpl.this.state = new ParseBlockNode();
                return event;
            }
            return new ParseDocumentStart().produce();
        }
    }
    
    private class ParseDocumentStart implements Production
    {
        @Override
        public Event produce() {
            while (ParserImpl.this.scanner.checkToken(Token.ID.DocumentEnd)) {
                ParserImpl.this.scanner.getToken();
            }
            if (!ParserImpl.this.scanner.checkToken(Token.ID.StreamEnd)) {
                Token token = ParserImpl.this.scanner.peekToken();
                final Mark startMark = token.getStartMark();
                final VersionTagsTuple tuple = ParserImpl.this.processDirectives();
                while (ParserImpl.this.scanner.checkToken(Token.ID.Comment)) {
                    ParserImpl.this.scanner.getToken();
                }
                if (!ParserImpl.this.scanner.checkToken(Token.ID.StreamEnd)) {
                    if (!ParserImpl.this.scanner.checkToken(Token.ID.DocumentStart)) {
                        throw new ParserException(null, null, "expected '<document start>', but found '" + ParserImpl.this.scanner.peekToken().getTokenId() + "'", ParserImpl.this.scanner.peekToken().getStartMark());
                    }
                    token = ParserImpl.this.scanner.getToken();
                    final Mark endMark = token.getEndMark();
                    final Event event = new DocumentStartEvent(startMark, endMark, true, tuple.getVersion(), tuple.getTags());
                    ParserImpl.this.states.push(new ParseDocumentEnd());
                    ParserImpl.this.state = new ParseDocumentContent();
                    return event;
                }
            }
            final StreamEndToken token2 = (StreamEndToken)ParserImpl.this.scanner.getToken();
            final Event event = new StreamEndEvent(token2.getStartMark(), token2.getEndMark());
            if (!ParserImpl.this.states.isEmpty()) {
                throw new YAMLException("Unexpected end of stream. States left: " + ParserImpl.this.states);
            }
            if (!ParserImpl.this.marks.isEmpty()) {
                throw new YAMLException("Unexpected end of stream. Marks left: " + ParserImpl.this.marks);
            }
            ParserImpl.this.state = null;
            return event;
        }
    }
    
    private class ParseDocumentEnd implements Production
    {
        @Override
        public Event produce() {
            Token token = ParserImpl.this.scanner.peekToken();
            Mark endMark;
            final Mark startMark = endMark = token.getStartMark();
            boolean explicit = false;
            if (ParserImpl.this.scanner.checkToken(Token.ID.DocumentEnd)) {
                token = ParserImpl.this.scanner.getToken();
                endMark = token.getEndMark();
                explicit = true;
            }
            final Event event = new DocumentEndEvent(startMark, endMark, explicit);
            ParserImpl.this.state = new ParseDocumentStart();
            return event;
        }
    }
    
    private class ParseDocumentContent implements Production
    {
        @Override
        public Event produce() {
            if (ParserImpl.this.scanner.checkToken(Token.ID.Comment)) {
                ParserImpl.this.state = new ParseDocumentContent();
                return ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
            }
            if (ParserImpl.this.scanner.checkToken(Token.ID.Directive, Token.ID.DocumentStart, Token.ID.DocumentEnd, Token.ID.StreamEnd)) {
                final Event event = ParserImpl.this.processEmptyScalar(ParserImpl.this.scanner.peekToken().getStartMark());
                ParserImpl.this.state = ParserImpl.this.states.pop();
                return event;
            }
            return new ParseBlockNode().produce();
        }
    }
    
    private class ParseBlockNode implements Production
    {
        @Override
        public Event produce() {
            return ParserImpl.this.parseNode(true, false);
        }
    }
    
    private class ParseBlockSequenceFirstEntry implements Production
    {
        @Override
        public Event produce() {
            final Token token = ParserImpl.this.scanner.getToken();
            ParserImpl.this.marks.push(token.getStartMark());
            return new ParseBlockSequenceEntryKey().produce();
        }
    }
    
    private class ParseBlockSequenceEntryKey implements Production
    {
        @Override
        public Event produce() {
            if (ParserImpl.this.scanner.checkToken(Token.ID.Comment)) {
                ParserImpl.this.state = new ParseBlockSequenceEntryKey();
                return ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
            }
            if (ParserImpl.this.scanner.checkToken(Token.ID.BlockEntry)) {
                final BlockEntryToken token = (BlockEntryToken)ParserImpl.this.scanner.getToken();
                return new ParseBlockSequenceEntryValue(token).produce();
            }
            if (!ParserImpl.this.scanner.checkToken(Token.ID.BlockEnd)) {
                final Token token2 = ParserImpl.this.scanner.peekToken();
                throw new ParserException("while parsing a block collection", ParserImpl.this.marks.pop(), "expected <block end>, but found '" + token2.getTokenId() + "'", token2.getStartMark());
            }
            final Token token2 = ParserImpl.this.scanner.getToken();
            final Event event = new SequenceEndEvent(token2.getStartMark(), token2.getEndMark());
            ParserImpl.this.state = ParserImpl.this.states.pop();
            ParserImpl.this.marks.pop();
            return event;
        }
    }
    
    private class ParseBlockSequenceEntryValue implements Production
    {
        BlockEntryToken token;
        
        public ParseBlockSequenceEntryValue(final BlockEntryToken token) {
            this.token = token;
        }
        
        @Override
        public Event produce() {
            if (ParserImpl.this.scanner.checkToken(Token.ID.Comment)) {
                ParserImpl.this.state = new ParseBlockSequenceEntryValue(this.token);
                return ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
            }
            if (!ParserImpl.this.scanner.checkToken(Token.ID.BlockEntry, Token.ID.BlockEnd)) {
                ParserImpl.this.states.push(new ParseBlockSequenceEntryKey());
                return new ParseBlockNode().produce();
            }
            ParserImpl.this.state = new ParseBlockSequenceEntryKey();
            return ParserImpl.this.processEmptyScalar(this.token.getEndMark());
        }
    }
    
    private class ParseIndentlessSequenceEntryKey implements Production
    {
        @Override
        public Event produce() {
            if (ParserImpl.this.scanner.checkToken(Token.ID.Comment)) {
                ParserImpl.this.state = new ParseIndentlessSequenceEntryKey();
                return ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
            }
            if (ParserImpl.this.scanner.checkToken(Token.ID.BlockEntry)) {
                final BlockEntryToken token = (BlockEntryToken)ParserImpl.this.scanner.getToken();
                return new ParseIndentlessSequenceEntryValue(token).produce();
            }
            final Token token2 = ParserImpl.this.scanner.peekToken();
            final Event event = new SequenceEndEvent(token2.getStartMark(), token2.getEndMark());
            ParserImpl.this.state = ParserImpl.this.states.pop();
            return event;
        }
    }
    
    private class ParseIndentlessSequenceEntryValue implements Production
    {
        BlockEntryToken token;
        
        public ParseIndentlessSequenceEntryValue(final BlockEntryToken token) {
            this.token = token;
        }
        
        @Override
        public Event produce() {
            if (ParserImpl.this.scanner.checkToken(Token.ID.Comment)) {
                ParserImpl.this.state = new ParseIndentlessSequenceEntryValue(this.token);
                return ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
            }
            if (!ParserImpl.this.scanner.checkToken(Token.ID.BlockEntry, Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd)) {
                ParserImpl.this.states.push(new ParseIndentlessSequenceEntryKey());
                return new ParseBlockNode().produce();
            }
            ParserImpl.this.state = new ParseIndentlessSequenceEntryKey();
            return ParserImpl.this.processEmptyScalar(this.token.getEndMark());
        }
    }
    
    private class ParseBlockMappingFirstKey implements Production
    {
        @Override
        public Event produce() {
            final Token token = ParserImpl.this.scanner.getToken();
            ParserImpl.this.marks.push(token.getStartMark());
            return new ParseBlockMappingKey().produce();
        }
    }
    
    private class ParseBlockMappingKey implements Production
    {
        @Override
        public Event produce() {
            if (ParserImpl.this.scanner.checkToken(Token.ID.Comment)) {
                ParserImpl.this.state = new ParseBlockMappingKey();
                return ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
            }
            if (ParserImpl.this.scanner.checkToken(Token.ID.Key)) {
                final Token token = ParserImpl.this.scanner.getToken();
                if (!ParserImpl.this.scanner.checkToken(Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd)) {
                    ParserImpl.this.states.push(new ParseBlockMappingValue());
                    return ParserImpl.this.parseBlockNodeOrIndentlessSequence();
                }
                ParserImpl.this.state = new ParseBlockMappingValue();
                return ParserImpl.this.processEmptyScalar(token.getEndMark());
            }
            else {
                if (!ParserImpl.this.scanner.checkToken(Token.ID.BlockEnd)) {
                    final Token token = ParserImpl.this.scanner.peekToken();
                    throw new ParserException("while parsing a block mapping", ParserImpl.this.marks.pop(), "expected <block end>, but found '" + token.getTokenId() + "'", token.getStartMark());
                }
                final Token token = ParserImpl.this.scanner.getToken();
                final Event event = new MappingEndEvent(token.getStartMark(), token.getEndMark());
                ParserImpl.this.state = ParserImpl.this.states.pop();
                ParserImpl.this.marks.pop();
                return event;
            }
        }
    }
    
    private class ParseBlockMappingValue implements Production
    {
        @Override
        public Event produce() {
            if (ParserImpl.this.scanner.checkToken(Token.ID.Value)) {
                final Token token = ParserImpl.this.scanner.getToken();
                if (ParserImpl.this.scanner.checkToken(Token.ID.Comment)) {
                    ParserImpl.this.state = new ParseBlockMappingValueComment();
                    return ParserImpl.this.state.produce();
                }
                if (!ParserImpl.this.scanner.checkToken(Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd)) {
                    ParserImpl.this.states.push(new ParseBlockMappingKey());
                    return ParserImpl.this.parseBlockNodeOrIndentlessSequence();
                }
                ParserImpl.this.state = new ParseBlockMappingKey();
                return ParserImpl.this.processEmptyScalar(token.getEndMark());
            }
            else {
                if (ParserImpl.this.scanner.checkToken(Token.ID.Scalar)) {
                    ParserImpl.this.states.push(new ParseBlockMappingKey());
                    return ParserImpl.this.parseBlockNodeOrIndentlessSequence();
                }
                ParserImpl.this.state = new ParseBlockMappingKey();
                final Token token = ParserImpl.this.scanner.peekToken();
                return ParserImpl.this.processEmptyScalar(token.getStartMark());
            }
        }
    }
    
    private class ParseBlockMappingValueComment implements Production
    {
        List<CommentToken> tokens;
        
        private ParseBlockMappingValueComment() {
            this.tokens = new LinkedList<CommentToken>();
        }
        
        @Override
        public Event produce() {
            if (ParserImpl.this.scanner.checkToken(Token.ID.Comment)) {
                this.tokens.add((CommentToken)ParserImpl.this.scanner.getToken());
                return this.produce();
            }
            if (ParserImpl.this.scanner.checkToken(Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd)) {
                ParserImpl.this.state = new ParseBlockMappingValueCommentList(this.tokens);
                return ParserImpl.this.processEmptyScalar(ParserImpl.this.scanner.peekToken().getStartMark());
            }
            if (!this.tokens.isEmpty()) {
                return ParserImpl.this.produceCommentEvent(this.tokens.remove(0));
            }
            ParserImpl.this.states.push(new ParseBlockMappingKey());
            return ParserImpl.this.parseBlockNodeOrIndentlessSequence();
        }
    }
    
    private class ParseBlockMappingValueCommentList implements Production
    {
        List<CommentToken> tokens;
        
        public ParseBlockMappingValueCommentList(final List<CommentToken> tokens) {
            this.tokens = tokens;
        }
        
        @Override
        public Event produce() {
            if (!this.tokens.isEmpty()) {
                return ParserImpl.this.produceCommentEvent(this.tokens.remove(0));
            }
            return new ParseBlockMappingKey().produce();
        }
    }
    
    private class ParseFlowSequenceFirstEntry implements Production
    {
        @Override
        public Event produce() {
            final Token token = ParserImpl.this.scanner.getToken();
            ParserImpl.this.marks.push(token.getStartMark());
            return new ParseFlowSequenceEntry(true).produce();
        }
    }
    
    private class ParseFlowSequenceEntry implements Production
    {
        private final boolean first;
        
        public ParseFlowSequenceEntry(final boolean first) {
            this.first = first;
        }
        
        @Override
        public Event produce() {
            if (ParserImpl.this.scanner.checkToken(Token.ID.Comment)) {
                ParserImpl.this.state = new ParseFlowSequenceEntry(this.first);
                return ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
            }
            if (!ParserImpl.this.scanner.checkToken(Token.ID.FlowSequenceEnd)) {
                if (!this.first) {
                    if (!ParserImpl.this.scanner.checkToken(Token.ID.FlowEntry)) {
                        final Token token = ParserImpl.this.scanner.peekToken();
                        throw new ParserException("while parsing a flow sequence", ParserImpl.this.marks.pop(), "expected ',' or ']', but got " + token.getTokenId(), token.getStartMark());
                    }
                    ParserImpl.this.scanner.getToken();
                    if (ParserImpl.this.scanner.checkToken(Token.ID.Comment)) {
                        ParserImpl.this.state = new ParseFlowSequenceEntry(true);
                        return ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
                    }
                }
                if (ParserImpl.this.scanner.checkToken(Token.ID.Key)) {
                    final Token token = ParserImpl.this.scanner.peekToken();
                    final Event event = new MappingStartEvent(null, null, true, token.getStartMark(), token.getEndMark(), DumperOptions.FlowStyle.FLOW);
                    ParserImpl.this.state = new ParseFlowSequenceEntryMappingKey();
                    return event;
                }
                if (!ParserImpl.this.scanner.checkToken(Token.ID.FlowSequenceEnd)) {
                    ParserImpl.this.states.push(new ParseFlowSequenceEntry(false));
                    return ParserImpl.this.parseFlowNode();
                }
            }
            final Token token = ParserImpl.this.scanner.getToken();
            final Event event = new SequenceEndEvent(token.getStartMark(), token.getEndMark());
            if (!ParserImpl.this.scanner.checkToken(Token.ID.Comment)) {
                ParserImpl.this.state = ParserImpl.this.states.pop();
            }
            else {
                ParserImpl.this.state = new ParseFlowEndComment();
            }
            ParserImpl.this.marks.pop();
            return event;
        }
    }
    
    private class ParseFlowEndComment implements Production
    {
        @Override
        public Event produce() {
            final Event event = ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
            if (!ParserImpl.this.scanner.checkToken(Token.ID.Comment)) {
                ParserImpl.this.state = ParserImpl.this.states.pop();
            }
            return event;
        }
    }
    
    private class ParseFlowSequenceEntryMappingKey implements Production
    {
        @Override
        public Event produce() {
            final Token token = ParserImpl.this.scanner.getToken();
            if (!ParserImpl.this.scanner.checkToken(Token.ID.Value, Token.ID.FlowEntry, Token.ID.FlowSequenceEnd)) {
                ParserImpl.this.states.push(new ParseFlowSequenceEntryMappingValue());
                return ParserImpl.this.parseFlowNode();
            }
            ParserImpl.this.state = new ParseFlowSequenceEntryMappingValue();
            return ParserImpl.this.processEmptyScalar(token.getEndMark());
        }
    }
    
    private class ParseFlowSequenceEntryMappingValue implements Production
    {
        @Override
        public Event produce() {
            if (!ParserImpl.this.scanner.checkToken(Token.ID.Value)) {
                ParserImpl.this.state = new ParseFlowSequenceEntryMappingEnd();
                final Token token = ParserImpl.this.scanner.peekToken();
                return ParserImpl.this.processEmptyScalar(token.getStartMark());
            }
            final Token token = ParserImpl.this.scanner.getToken();
            if (!ParserImpl.this.scanner.checkToken(Token.ID.FlowEntry, Token.ID.FlowSequenceEnd)) {
                ParserImpl.this.states.push(new ParseFlowSequenceEntryMappingEnd());
                return ParserImpl.this.parseFlowNode();
            }
            ParserImpl.this.state = new ParseFlowSequenceEntryMappingEnd();
            return ParserImpl.this.processEmptyScalar(token.getEndMark());
        }
    }
    
    private class ParseFlowSequenceEntryMappingEnd implements Production
    {
        @Override
        public Event produce() {
            ParserImpl.this.state = new ParseFlowSequenceEntry(false);
            final Token token = ParserImpl.this.scanner.peekToken();
            return new MappingEndEvent(token.getStartMark(), token.getEndMark());
        }
    }
    
    private class ParseFlowMappingFirstKey implements Production
    {
        @Override
        public Event produce() {
            final Token token = ParserImpl.this.scanner.getToken();
            ParserImpl.this.marks.push(token.getStartMark());
            return new ParseFlowMappingKey(true).produce();
        }
    }
    
    private class ParseFlowMappingKey implements Production
    {
        private final boolean first;
        
        public ParseFlowMappingKey(final boolean first) {
            this.first = first;
        }
        
        @Override
        public Event produce() {
            if (ParserImpl.this.scanner.checkToken(Token.ID.Comment)) {
                ParserImpl.this.state = new ParseFlowMappingKey(this.first);
                return ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
            }
            if (!ParserImpl.this.scanner.checkToken(Token.ID.FlowMappingEnd)) {
                if (!this.first) {
                    if (!ParserImpl.this.scanner.checkToken(Token.ID.FlowEntry)) {
                        final Token token = ParserImpl.this.scanner.peekToken();
                        throw new ParserException("while parsing a flow mapping", ParserImpl.this.marks.pop(), "expected ',' or '}', but got " + token.getTokenId(), token.getStartMark());
                    }
                    ParserImpl.this.scanner.getToken();
                    if (ParserImpl.this.scanner.checkToken(Token.ID.Comment)) {
                        ParserImpl.this.state = new ParseFlowMappingKey(true);
                        return ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
                    }
                }
                if (ParserImpl.this.scanner.checkToken(Token.ID.Key)) {
                    final Token token = ParserImpl.this.scanner.getToken();
                    if (!ParserImpl.this.scanner.checkToken(Token.ID.Value, Token.ID.FlowEntry, Token.ID.FlowMappingEnd)) {
                        ParserImpl.this.states.push(new ParseFlowMappingValue());
                        return ParserImpl.this.parseFlowNode();
                    }
                    ParserImpl.this.state = new ParseFlowMappingValue();
                    return ParserImpl.this.processEmptyScalar(token.getEndMark());
                }
                else if (!ParserImpl.this.scanner.checkToken(Token.ID.FlowMappingEnd)) {
                    ParserImpl.this.states.push(new ParseFlowMappingEmptyValue());
                    return ParserImpl.this.parseFlowNode();
                }
            }
            final Token token = ParserImpl.this.scanner.getToken();
            final Event event = new MappingEndEvent(token.getStartMark(), token.getEndMark());
            ParserImpl.this.marks.pop();
            if (!ParserImpl.this.scanner.checkToken(Token.ID.Comment)) {
                ParserImpl.this.state = ParserImpl.this.states.pop();
            }
            else {
                ParserImpl.this.state = new ParseFlowEndComment();
            }
            return event;
        }
    }
    
    private class ParseFlowMappingValue implements Production
    {
        @Override
        public Event produce() {
            if (!ParserImpl.this.scanner.checkToken(Token.ID.Value)) {
                ParserImpl.this.state = new ParseFlowMappingKey(false);
                final Token token = ParserImpl.this.scanner.peekToken();
                return ParserImpl.this.processEmptyScalar(token.getStartMark());
            }
            final Token token = ParserImpl.this.scanner.getToken();
            if (!ParserImpl.this.scanner.checkToken(Token.ID.FlowEntry, Token.ID.FlowMappingEnd)) {
                ParserImpl.this.states.push(new ParseFlowMappingKey(false));
                return ParserImpl.this.parseFlowNode();
            }
            ParserImpl.this.state = new ParseFlowMappingKey(false);
            return ParserImpl.this.processEmptyScalar(token.getEndMark());
        }
    }
    
    private class ParseFlowMappingEmptyValue implements Production
    {
        @Override
        public Event produce() {
            ParserImpl.this.state = new ParseFlowMappingKey(false);
            return ParserImpl.this.processEmptyScalar(ParserImpl.this.scanner.peekToken().getStartMark());
        }
    }
}
