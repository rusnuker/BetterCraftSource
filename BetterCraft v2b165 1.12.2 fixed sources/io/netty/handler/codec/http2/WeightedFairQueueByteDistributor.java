// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import java.util.Iterator;
import io.netty.util.internal.PriorityQueueNode;
import io.netty.util.internal.MathUtil;
import io.netty.util.internal.SystemPropertyUtil;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import io.netty.util.internal.DefaultPriorityQueue;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.internal.EmptyPriorityQueue;
import io.netty.util.collection.IntCollections;
import io.netty.util.internal.PriorityQueue;
import io.netty.util.collection.IntObjectMap;

public final class WeightedFairQueueByteDistributor implements StreamByteDistributor
{
    static final int INITIAL_CHILDREN_MAP_SIZE;
    private static final int DEFAULT_MAX_STATE_ONLY_SIZE = 5;
    private final Http2Connection.PropertyKey stateKey;
    private final IntObjectMap<State> stateOnlyMap;
    private final PriorityQueue<State> stateOnlyRemovalQueue;
    private final Http2Connection connection;
    private final State connectionState;
    private int allocationQuantum;
    private final int maxStateOnlySize;
    
    public WeightedFairQueueByteDistributor(final Http2Connection connection) {
        this(connection, 5);
    }
    
    public WeightedFairQueueByteDistributor(final Http2Connection connection, final int maxStateOnlySize) {
        this.allocationQuantum = 1024;
        if (maxStateOnlySize < 0) {
            throw new IllegalArgumentException("maxStateOnlySize: " + maxStateOnlySize + " (expected: >0)");
        }
        if (maxStateOnlySize == 0) {
            this.stateOnlyMap = IntCollections.emptyMap();
            this.stateOnlyRemovalQueue = (PriorityQueue<State>)EmptyPriorityQueue.instance();
        }
        else {
            this.stateOnlyMap = new IntObjectHashMap<State>(maxStateOnlySize);
            this.stateOnlyRemovalQueue = new DefaultPriorityQueue<State>(StateOnlyComparator.INSTANCE, maxStateOnlySize + 2);
        }
        this.maxStateOnlySize = maxStateOnlySize;
        this.connection = connection;
        this.stateKey = connection.newKey();
        final Http2Stream connectionStream = connection.connectionStream();
        connectionStream.setProperty(this.stateKey, this.connectionState = new State(connectionStream, 16));
        connection.addListener(new Http2ConnectionAdapter() {
            @Override
            public void onStreamAdded(final Http2Stream stream) {
                State state = WeightedFairQueueByteDistributor.this.stateOnlyMap.remove(stream.id());
                if (state == null) {
                    state = new State(stream);
                    final List<ParentChangedEvent> events = new ArrayList<ParentChangedEvent>(1);
                    WeightedFairQueueByteDistributor.this.connectionState.takeChild(state, false, events);
                    WeightedFairQueueByteDistributor.this.notifyParentChanged(events);
                }
                else {
                    WeightedFairQueueByteDistributor.this.stateOnlyRemovalQueue.removeTyped(state);
                    state.stream = stream;
                }
                switch (stream.state()) {
                    case RESERVED_REMOTE:
                    case RESERVED_LOCAL: {
                        state.setStreamReservedOrActivated();
                        break;
                    }
                }
                stream.setProperty(WeightedFairQueueByteDistributor.this.stateKey, state);
            }
            
            @Override
            public void onStreamActive(final Http2Stream stream) {
                WeightedFairQueueByteDistributor.this.state(stream).setStreamReservedOrActivated();
            }
            
            @Override
            public void onStreamClosed(final Http2Stream stream) {
                WeightedFairQueueByteDistributor.this.state(stream).close();
            }
            
            @Override
            public void onStreamRemoved(final Http2Stream stream) {
                final State state = WeightedFairQueueByteDistributor.this.state(stream);
                state.stream = null;
                if (WeightedFairQueueByteDistributor.this.maxStateOnlySize == 0) {
                    state.parent.removeChild(state);
                    return;
                }
                if (WeightedFairQueueByteDistributor.this.stateOnlyRemovalQueue.size() == WeightedFairQueueByteDistributor.this.maxStateOnlySize) {
                    final State stateToRemove = (State)WeightedFairQueueByteDistributor.this.stateOnlyRemovalQueue.peek();
                    if (StateOnlyComparator.INSTANCE.compare(stateToRemove, state) >= 0) {
                        state.parent.removeChild(state);
                        return;
                    }
                    WeightedFairQueueByteDistributor.this.stateOnlyRemovalQueue.poll();
                    stateToRemove.parent.removeChild(stateToRemove);
                    WeightedFairQueueByteDistributor.this.stateOnlyMap.remove(stateToRemove.streamId);
                }
                WeightedFairQueueByteDistributor.this.stateOnlyRemovalQueue.add(state);
                WeightedFairQueueByteDistributor.this.stateOnlyMap.put(state.streamId, state);
            }
        });
    }
    
    @Override
    public void updateStreamableBytes(final StreamState state) {
        this.state(state.stream()).updateStreamableBytes(Http2CodecUtil.streamableBytes(state), state.hasFrame() && state.windowSize() >= 0);
    }
    
    @Override
    public void updateDependencyTree(final int childStreamId, final int parentStreamId, final short weight, final boolean exclusive) {
        if (weight < 1 || weight > 256) {
            throw new IllegalArgumentException(String.format("Invalid weight: %d. Must be between %d and %d (inclusive).", weight, 1, 256));
        }
        if (childStreamId == parentStreamId) {
            throw new IllegalArgumentException("A stream cannot depend on itself");
        }
        State state = this.state(childStreamId);
        if (state == null) {
            if (this.maxStateOnlySize == 0) {
                return;
            }
            state = new State(childStreamId);
            this.stateOnlyRemovalQueue.add(state);
            this.stateOnlyMap.put(childStreamId, state);
        }
        State newParent = this.state(parentStreamId);
        if (newParent == null) {
            if (this.maxStateOnlySize == 0) {
                return;
            }
            newParent = new State(parentStreamId);
            this.stateOnlyRemovalQueue.add(newParent);
            this.stateOnlyMap.put(parentStreamId, newParent);
        }
        if (state.activeCountForTree != 0 && state.parent != null) {
            final State parent = state.parent;
            parent.totalQueuedWeights += weight - state.weight;
        }
        state.weight = weight;
        if (newParent != state.parent || (exclusive && newParent.children.size() != 1)) {
            List<ParentChangedEvent> events;
            if (newParent.isDescendantOf(state)) {
                events = new ArrayList<ParentChangedEvent>(2 + (exclusive ? newParent.children.size() : 0));
                state.parent.takeChild(newParent, false, events);
            }
            else {
                events = new ArrayList<ParentChangedEvent>(1 + (exclusive ? newParent.children.size() : 0));
            }
            newParent.takeChild(state, exclusive, events);
            this.notifyParentChanged(events);
        }
        while (this.stateOnlyRemovalQueue.size() > this.maxStateOnlySize) {
            final State stateToRemove = this.stateOnlyRemovalQueue.poll();
            stateToRemove.parent.removeChild(stateToRemove);
            this.stateOnlyMap.remove(stateToRemove.streamId);
        }
    }
    
    @Override
    public boolean distribute(int maxBytes, final Writer writer) throws Http2Exception {
        if (this.connectionState.activeCountForTree == 0) {
            return false;
        }
        int oldIsActiveCountForTree;
        do {
            oldIsActiveCountForTree = this.connectionState.activeCountForTree;
            maxBytes -= this.distributeToChildren(maxBytes, writer, this.connectionState);
        } while (this.connectionState.activeCountForTree != 0 && (maxBytes > 0 || oldIsActiveCountForTree != this.connectionState.activeCountForTree));
        return this.connectionState.activeCountForTree != 0;
    }
    
    public void allocationQuantum(final int allocationQuantum) {
        if (allocationQuantum <= 0) {
            throw new IllegalArgumentException("allocationQuantum must be > 0");
        }
        this.allocationQuantum = allocationQuantum;
    }
    
    private int distribute(final int maxBytes, final Writer writer, final State state) throws Http2Exception {
        if (state.isActive()) {
            final int nsent = Math.min(maxBytes, state.streamableBytes);
            state.write(nsent, writer);
            if (nsent == 0 && maxBytes != 0) {
                state.updateStreamableBytes(state.streamableBytes, false);
            }
            return nsent;
        }
        return this.distributeToChildren(maxBytes, writer, state);
    }
    
    private int distributeToChildren(final int maxBytes, final Writer writer, final State state) throws Http2Exception {
        final long oldTotalQueuedWeights = state.totalQueuedWeights;
        final State childState = state.pollPseudoTimeQueue();
        final State nextChildState = state.peekPseudoTimeQueue();
        childState.setDistributing();
        try {
            assert nextChildState.pseudoTimeToWrite >= childState.pseudoTimeToWrite : "nextChildState[" + nextChildState.streamId + "].pseudoTime(" + nextChildState.pseudoTimeToWrite + ") <  childState[" + childState.streamId + "].pseudoTime(" + childState.pseudoTimeToWrite + ")";
            final int nsent = this.distribute((nextChildState == null) ? maxBytes : Math.min(maxBytes, (int)Math.min((nextChildState.pseudoTimeToWrite - childState.pseudoTimeToWrite) * childState.weight / oldTotalQueuedWeights + this.allocationQuantum, 2147483647L)), writer, childState);
            state.pseudoTime += nsent;
            childState.updatePseudoTime(state, nsent, oldTotalQueuedWeights);
            return nsent;
        }
        finally {
            childState.unsetDistributing();
            if (childState.activeCountForTree != 0) {
                state.offerPseudoTimeQueue(childState);
            }
        }
    }
    
    private State state(final Http2Stream stream) {
        return stream.getProperty(this.stateKey);
    }
    
    private State state(final int streamId) {
        final Http2Stream stream = this.connection.stream(streamId);
        return (stream != null) ? this.state(stream) : this.stateOnlyMap.get(streamId);
    }
    
    int streamableBytes0(final Http2Stream stream) {
        return this.state(stream).streamableBytes;
    }
    
    boolean isChild(final int childId, final int parentId, final short weight) {
        final State parent = this.state(parentId);
        final State child;
        return parent.children.containsKey(childId) && (child = this.state(childId)).parent == parent && child.weight == weight;
    }
    
    int numChildren(final int streamId) {
        final State state = this.state(streamId);
        return (state == null) ? 0 : state.children.size();
    }
    
    void notifyParentChanged(final List<ParentChangedEvent> events) {
        for (int i = 0; i < events.size(); ++i) {
            final ParentChangedEvent event = events.get(i);
            this.stateOnlyRemovalQueue.priorityChanged(event.state);
            if (event.state.parent != null && event.state.activeCountForTree != 0) {
                event.state.parent.offerAndInitializePseudoTime(event.state);
                event.state.parent.activeCountChangeForTree(event.state.activeCountForTree);
            }
        }
    }
    
    static {
        INITIAL_CHILDREN_MAP_SIZE = Math.max(1, SystemPropertyUtil.getInt("io.netty.http2.childrenMapSize", 2));
    }
    
    private static final class StateOnlyComparator implements Comparator<State>
    {
        static final StateOnlyComparator INSTANCE;
        
        @Override
        public int compare(final State o1, final State o2) {
            final boolean o1Actived = o1.wasStreamReservedOrActivated();
            if (o1Actived != o2.wasStreamReservedOrActivated()) {
                return o1Actived ? -1 : 1;
            }
            final int x = o2.dependencyTreeDepth - o1.dependencyTreeDepth;
            return (x != 0) ? x : (o1.streamId - o2.streamId);
        }
        
        static {
            INSTANCE = new StateOnlyComparator();
        }
    }
    
    private static final class StatePseudoTimeComparator implements Comparator<State>
    {
        static final StatePseudoTimeComparator INSTANCE;
        
        @Override
        public int compare(final State o1, final State o2) {
            return MathUtil.compare(o1.pseudoTimeToWrite, o2.pseudoTimeToWrite);
        }
        
        static {
            INSTANCE = new StatePseudoTimeComparator();
        }
    }
    
    private final class State implements PriorityQueueNode
    {
        private static final byte STATE_IS_ACTIVE = 1;
        private static final byte STATE_IS_DISTRIBUTING = 2;
        private static final byte STATE_STREAM_ACTIVATED = 4;
        Http2Stream stream;
        State parent;
        IntObjectMap<State> children;
        private final PriorityQueue<State> pseudoTimeQueue;
        final int streamId;
        int streamableBytes;
        int dependencyTreeDepth;
        int activeCountForTree;
        private int pseudoTimeQueueIndex;
        private int stateOnlyQueueIndex;
        long pseudoTimeToWrite;
        long pseudoTime;
        long totalQueuedWeights;
        private byte flags;
        short weight;
        
        State(final WeightedFairQueueByteDistributor weightedFairQueueByteDistributor, final int streamId) {
            this(weightedFairQueueByteDistributor, streamId, null, 0);
        }
        
        State(final WeightedFairQueueByteDistributor weightedFairQueueByteDistributor, final Http2Stream stream) {
            this(weightedFairQueueByteDistributor, stream, 0);
        }
        
        State(final WeightedFairQueueByteDistributor weightedFairQueueByteDistributor, final Http2Stream stream, final int initialSize) {
            this(weightedFairQueueByteDistributor, stream.id(), stream, initialSize);
        }
        
        State(final int streamId, final Http2Stream stream, final int initialSize) {
            this.children = IntCollections.emptyMap();
            this.pseudoTimeQueueIndex = -1;
            this.stateOnlyQueueIndex = -1;
            this.weight = 16;
            this.stream = stream;
            this.streamId = streamId;
            this.pseudoTimeQueue = new DefaultPriorityQueue<State>(StatePseudoTimeComparator.INSTANCE, initialSize);
        }
        
        boolean isDescendantOf(final State state) {
            for (State next = this.parent; next != null; next = next.parent) {
                if (next == state) {
                    return true;
                }
            }
            return false;
        }
        
        void takeChild(final State child, final boolean exclusive, final List<ParentChangedEvent> events) {
            this.takeChild(null, child, exclusive, events);
        }
        
        void takeChild(final Iterator<IntObjectMap.PrimitiveEntry<State>> childItr, final State child, final boolean exclusive, final List<ParentChangedEvent> events) {
            final State oldParent = child.parent;
            if (oldParent != this) {
                events.add(new ParentChangedEvent(child, oldParent));
                child.setParent(this);
                if (childItr != null) {
                    childItr.remove();
                }
                else if (oldParent != null) {
                    oldParent.children.remove(child.streamId);
                }
                this.initChildrenIfEmpty();
                final State oldChild = this.children.put(child.streamId, child);
                assert oldChild == null : "A stream with the same stream ID was already in the child map.";
            }
            if (exclusive && !this.children.isEmpty()) {
                final Iterator<IntObjectMap.PrimitiveEntry<State>> itr = this.removeAllChildrenExcept(child).entries().iterator();
                while (itr.hasNext()) {
                    child.takeChild(itr, itr.next().value(), false, events);
                }
            }
        }
        
        void removeChild(final State child) {
            if (this.children.remove(child.streamId) != null) {
                final List<ParentChangedEvent> events = new ArrayList<ParentChangedEvent>(1 + child.children.size());
                events.add(new ParentChangedEvent(child, child.parent));
                child.setParent(null);
                final Iterator<IntObjectMap.PrimitiveEntry<State>> itr = child.children.entries().iterator();
                while (itr.hasNext()) {
                    this.takeChild(itr, itr.next().value(), false, events);
                }
                WeightedFairQueueByteDistributor.this.notifyParentChanged(events);
            }
        }
        
        private IntObjectMap<State> removeAllChildrenExcept(State stateToRetain) {
            stateToRetain = this.children.remove(stateToRetain.streamId);
            final IntObjectMap<State> prevChildren = this.children;
            this.initChildren();
            if (stateToRetain != null) {
                this.children.put(stateToRetain.streamId, stateToRetain);
            }
            return prevChildren;
        }
        
        private void setParent(final State newParent) {
            if (this.activeCountForTree != 0 && this.parent != null) {
                this.parent.removePseudoTimeQueue(this);
                this.parent.activeCountChangeForTree(-this.activeCountForTree);
            }
            this.dependencyTreeDepth = (((this.parent = newParent) == null) ? Integer.MAX_VALUE : (newParent.dependencyTreeDepth + 1));
        }
        
        private void initChildrenIfEmpty() {
            if (this.children == IntCollections.emptyMap()) {
                this.initChildren();
            }
        }
        
        private void initChildren() {
            this.children = new IntObjectHashMap<State>(WeightedFairQueueByteDistributor.INITIAL_CHILDREN_MAP_SIZE);
        }
        
        void write(final int numBytes, final Writer writer) throws Http2Exception {
            assert this.stream != null;
            try {
                writer.write(this.stream, numBytes);
            }
            catch (final Throwable t) {
                throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, t, "byte distribution write error", new Object[0]);
            }
        }
        
        void activeCountChangeForTree(final int increment) {
            assert this.activeCountForTree + increment >= 0;
            this.activeCountForTree += increment;
            if (this.parent != null) {
                assert !(!this.parent.pseudoTimeQueue.containsTyped(this)) : "State[" + this.streamId + "].activeCountForTree changed from 0 to " + increment + " is in a pseudoTimeQueue, but not in parent[ " + this.parent.streamId + "]'s pseudoTimeQueue";
                if (this.activeCountForTree == 0) {
                    this.parent.removePseudoTimeQueue(this);
                }
                else if (this.activeCountForTree == increment && !this.isDistributing()) {
                    this.parent.offerAndInitializePseudoTime(this);
                }
                this.parent.activeCountChangeForTree(increment);
            }
        }
        
        void updateStreamableBytes(final int newStreamableBytes, final boolean isActive) {
            if (this.isActive() != isActive) {
                if (isActive) {
                    this.activeCountChangeForTree(1);
                    this.setActive();
                }
                else {
                    this.activeCountChangeForTree(-1);
                    this.unsetActive();
                }
            }
            this.streamableBytes = newStreamableBytes;
        }
        
        void updatePseudoTime(final State parentState, final int nsent, final long totalQueuedWeights) {
            assert this.streamId != 0 && nsent >= 0;
            this.pseudoTimeToWrite = Math.min(this.pseudoTimeToWrite, parentState.pseudoTime) + nsent * totalQueuedWeights / this.weight;
        }
        
        void offerAndInitializePseudoTime(final State state) {
            state.pseudoTimeToWrite = this.pseudoTime;
            this.offerPseudoTimeQueue(state);
        }
        
        void offerPseudoTimeQueue(final State state) {
            this.pseudoTimeQueue.offer(state);
            this.totalQueuedWeights += state.weight;
        }
        
        State pollPseudoTimeQueue() {
            final State state = this.pseudoTimeQueue.poll();
            this.totalQueuedWeights -= state.weight;
            return state;
        }
        
        void removePseudoTimeQueue(final State state) {
            if (this.pseudoTimeQueue.removeTyped(state)) {
                this.totalQueuedWeights -= state.weight;
            }
        }
        
        State peekPseudoTimeQueue() {
            return this.pseudoTimeQueue.peek();
        }
        
        void close() {
            this.updateStreamableBytes(0, false);
            this.stream = null;
        }
        
        boolean wasStreamReservedOrActivated() {
            return (this.flags & 0x4) != 0x0;
        }
        
        void setStreamReservedOrActivated() {
            this.flags |= 0x4;
        }
        
        boolean isActive() {
            return (this.flags & 0x1) != 0x0;
        }
        
        private void setActive() {
            this.flags |= 0x1;
        }
        
        private void unsetActive() {
            this.flags &= 0xFFFFFFFE;
        }
        
        boolean isDistributing() {
            return (this.flags & 0x2) != 0x0;
        }
        
        void setDistributing() {
            this.flags |= 0x2;
        }
        
        void unsetDistributing() {
            this.flags &= 0xFFFFFFFD;
        }
        
        @Override
        public int priorityQueueIndex(final DefaultPriorityQueue<?> queue) {
            return (queue == WeightedFairQueueByteDistributor.this.stateOnlyRemovalQueue) ? this.stateOnlyQueueIndex : this.pseudoTimeQueueIndex;
        }
        
        @Override
        public void priorityQueueIndex(final DefaultPriorityQueue<?> queue, final int i) {
            if (queue == WeightedFairQueueByteDistributor.this.stateOnlyRemovalQueue) {
                this.stateOnlyQueueIndex = i;
            }
            else {
                this.pseudoTimeQueueIndex = i;
            }
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(256 * ((this.activeCountForTree > 0) ? this.activeCountForTree : 1));
            this.toString(sb);
            return sb.toString();
        }
        
        private void toString(final StringBuilder sb) {
            sb.append("{streamId ").append(this.streamId).append(" streamableBytes ").append(this.streamableBytes).append(" activeCountForTree ").append(this.activeCountForTree).append(" pseudoTimeQueueIndex ").append(this.pseudoTimeQueueIndex).append(" pseudoTimeToWrite ").append(this.pseudoTimeToWrite).append(" pseudoTime ").append(this.pseudoTime).append(" flags ").append(this.flags).append(" pseudoTimeQueue.size() ").append(this.pseudoTimeQueue.size()).append(" stateOnlyQueueIndex ").append(this.stateOnlyQueueIndex).append(" parent ").append(this.parent).append("} [");
            if (!this.pseudoTimeQueue.isEmpty()) {
                for (final State s : this.pseudoTimeQueue) {
                    s.toString(sb);
                    sb.append(", ");
                }
                sb.setLength(sb.length() - 2);
            }
            sb.append(']');
        }
    }
    
    private static final class ParentChangedEvent
    {
        final State state;
        final State oldParent;
        
        ParentChangedEvent(final State state, final State oldParent) {
            this.state = state;
            this.oldParent = oldParent;
        }
    }
}
