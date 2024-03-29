// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.serializer;

import org.yaml.snakeyaml.events.CommentEvent;
import org.yaml.snakeyaml.comments.CommentLine;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.ImplicitTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.events.AliasEvent;
import java.util.Iterator;
import java.util.List;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.AnchorNode;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.events.DocumentEndEvent;
import org.yaml.snakeyaml.events.DocumentStartEvent;
import org.yaml.snakeyaml.events.StreamEndEvent;
import java.io.IOException;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.events.StreamStartEvent;
import java.util.HashMap;
import java.util.HashSet;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.nodes.Node;
import java.util.Set;
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.resolver.Resolver;
import org.yaml.snakeyaml.emitter.Emitable;

public final class Serializer
{
    private final Emitable emitter;
    private final Resolver resolver;
    private final boolean explicitStart;
    private final boolean explicitEnd;
    private DumperOptions.Version useVersion;
    private final Map<String, String> useTags;
    private final Set<Node> serializedNodes;
    private final Map<Node, String> anchors;
    private final AnchorGenerator anchorGenerator;
    private Boolean closed;
    private final Tag explicitRoot;
    
    public Serializer(final Emitable emitter, final Resolver resolver, final DumperOptions opts, final Tag rootTag) {
        if (emitter == null) {
            throw new NullPointerException("Emitter must  be provided");
        }
        if (resolver == null) {
            throw new NullPointerException("Resolver must  be provided");
        }
        if (opts == null) {
            throw new NullPointerException("DumperOptions must  be provided");
        }
        this.emitter = emitter;
        this.resolver = resolver;
        this.explicitStart = opts.isExplicitStart();
        this.explicitEnd = opts.isExplicitEnd();
        if (opts.getVersion() != null) {
            this.useVersion = opts.getVersion();
        }
        this.useTags = opts.getTags();
        this.serializedNodes = new HashSet<Node>();
        this.anchors = new HashMap<Node, String>();
        this.anchorGenerator = opts.getAnchorGenerator();
        this.closed = null;
        this.explicitRoot = rootTag;
    }
    
    public void open() throws IOException {
        if (this.closed == null) {
            this.emitter.emit(new StreamStartEvent(null, null));
            this.closed = Boolean.FALSE;
            return;
        }
        if (Boolean.TRUE.equals(this.closed)) {
            throw new SerializerException("serializer is closed");
        }
        throw new SerializerException("serializer is already opened");
    }
    
    public void close() throws IOException {
        if (this.closed == null) {
            throw new SerializerException("serializer is not opened");
        }
        if (!Boolean.TRUE.equals(this.closed)) {
            this.emitter.emit(new StreamEndEvent(null, null));
            this.closed = Boolean.TRUE;
            this.serializedNodes.clear();
            this.anchors.clear();
        }
    }
    
    public void serialize(final Node node) throws IOException {
        if (this.closed == null) {
            throw new SerializerException("serializer is not opened");
        }
        if (this.closed) {
            throw new SerializerException("serializer is closed");
        }
        this.emitter.emit(new DocumentStartEvent(null, null, this.explicitStart, this.useVersion, this.useTags));
        this.anchorNode(node);
        if (this.explicitRoot != null) {
            node.setTag(this.explicitRoot);
        }
        this.serializeNode(node, null);
        this.emitter.emit(new DocumentEndEvent(null, null, this.explicitEnd));
        this.serializedNodes.clear();
        this.anchors.clear();
    }
    
    private void anchorNode(Node node) {
        if (node.getNodeId() == NodeId.anchor) {
            node = ((AnchorNode)node).getRealNode();
        }
        if (this.anchors.containsKey(node)) {
            String anchor = this.anchors.get(node);
            if (null == anchor) {
                anchor = this.anchorGenerator.nextAnchor(node);
                this.anchors.put(node, anchor);
            }
        }
        else {
            this.anchors.put(node, (node.getAnchor() != null) ? this.anchorGenerator.nextAnchor(node) : null);
            switch (node.getNodeId()) {
                case sequence: {
                    final SequenceNode seqNode = (SequenceNode)node;
                    final List<Node> list = seqNode.getValue();
                    for (final Node item : list) {
                        this.anchorNode(item);
                    }
                    break;
                }
                case mapping: {
                    final MappingNode mnode = (MappingNode)node;
                    final List<NodeTuple> map = mnode.getValue();
                    for (final NodeTuple object : map) {
                        final Node key = object.getKeyNode();
                        final Node value = object.getValueNode();
                        this.anchorNode(key);
                        this.anchorNode(value);
                    }
                    break;
                }
            }
        }
    }
    
    private void serializeNode(Node node, final Node parent) throws IOException {
        if (node.getNodeId() == NodeId.anchor) {
            node = ((AnchorNode)node).getRealNode();
        }
        final String tAlias = this.anchors.get(node);
        if (this.serializedNodes.contains(node)) {
            this.emitter.emit(new AliasEvent(tAlias, null, null));
        }
        else {
            this.serializedNodes.add(node);
            switch (node.getNodeId()) {
                case scalar: {
                    final ScalarNode scalarNode = (ScalarNode)node;
                    this.serializeComments(node.getBlockComments());
                    final Tag detectedTag = this.resolver.resolve(NodeId.scalar, scalarNode.getValue(), true);
                    final Tag defaultTag = this.resolver.resolve(NodeId.scalar, scalarNode.getValue(), false);
                    final ImplicitTuple tuple = new ImplicitTuple(node.getTag().equals(detectedTag), node.getTag().equals(defaultTag));
                    final ScalarEvent event = new ScalarEvent(tAlias, node.getTag().getValue(), tuple, scalarNode.getValue(), null, null, scalarNode.getScalarStyle());
                    this.emitter.emit(event);
                    this.serializeComments(node.getInLineComments());
                    this.serializeComments(node.getEndComments());
                    break;
                }
                case sequence: {
                    final SequenceNode seqNode = (SequenceNode)node;
                    this.serializeComments(node.getBlockComments());
                    final boolean implicitS = node.getTag().equals(this.resolver.resolve(NodeId.sequence, null, true));
                    this.emitter.emit(new SequenceStartEvent(tAlias, node.getTag().getValue(), implicitS, null, null, seqNode.getFlowStyle()));
                    final List<Node> list = seqNode.getValue();
                    for (final Node item : list) {
                        this.serializeNode(item, node);
                    }
                    this.emitter.emit(new SequenceEndEvent(null, null));
                    this.serializeComments(node.getInLineComments());
                    this.serializeComments(node.getEndComments());
                    break;
                }
                default: {
                    this.serializeComments(node.getBlockComments());
                    final Tag implicitTag = this.resolver.resolve(NodeId.mapping, null, true);
                    final boolean implicitM = node.getTag().equals(implicitTag);
                    final MappingNode mnode = (MappingNode)node;
                    final List<NodeTuple> map = mnode.getValue();
                    if (mnode.getTag() != Tag.COMMENT) {
                        this.emitter.emit(new MappingStartEvent(tAlias, mnode.getTag().getValue(), implicitM, null, null, mnode.getFlowStyle()));
                        for (final NodeTuple row : map) {
                            final Node key = row.getKeyNode();
                            final Node value = row.getValueNode();
                            this.serializeNode(key, mnode);
                            this.serializeNode(value, mnode);
                        }
                        this.emitter.emit(new MappingEndEvent(null, null));
                        this.serializeComments(node.getInLineComments());
                        this.serializeComments(node.getEndComments());
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    private void serializeComments(final List<CommentLine> comments) throws IOException {
        if (comments == null) {
            return;
        }
        for (final CommentLine line : comments) {
            final CommentEvent commentEvent = new CommentEvent(line.getCommentType(), line.getValue(), line.getStartMark(), line.getEndMark());
            this.emitter.emit(commentEvent);
        }
    }
}
