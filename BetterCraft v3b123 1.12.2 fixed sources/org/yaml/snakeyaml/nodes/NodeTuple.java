// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.nodes;

public final class NodeTuple
{
    private final Node keyNode;
    private final Node valueNode;
    
    public NodeTuple(final Node keyNode, final Node valueNode) {
        if (keyNode == null || valueNode == null) {
            throw new NullPointerException("Nodes must be provided.");
        }
        this.keyNode = keyNode;
        this.valueNode = valueNode;
    }
    
    public Node getKeyNode() {
        return this.keyNode;
    }
    
    public Node getValueNode() {
        return this.valueNode;
    }
    
    @Override
    public String toString() {
        return "<NodeTuple keyNode=" + this.keyNode + "; valueNode=" + this.valueNode + ">";
    }
}
