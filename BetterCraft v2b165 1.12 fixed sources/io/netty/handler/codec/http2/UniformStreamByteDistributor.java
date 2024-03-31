// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.util.internal.ObjectUtil;
import java.util.ArrayDeque;
import java.util.Deque;

public final class UniformStreamByteDistributor implements StreamByteDistributor
{
    private final Http2Connection.PropertyKey stateKey;
    private final Deque<State> queue;
    private int minAllocationChunk;
    private long totalStreamableBytes;
    
    public UniformStreamByteDistributor(final Http2Connection connection) {
        this.queue = new ArrayDeque<State>(4);
        this.minAllocationChunk = 1024;
        this.stateKey = connection.newKey();
        final Http2Stream connectionStream = connection.connectionStream();
        connectionStream.setProperty(this.stateKey, new State(connectionStream));
        connection.addListener(new Http2ConnectionAdapter() {
            @Override
            public void onStreamAdded(final Http2Stream stream) {
                stream.setProperty(UniformStreamByteDistributor.this.stateKey, new State(stream));
            }
            
            @Override
            public void onStreamClosed(final Http2Stream stream) {
                UniformStreamByteDistributor.this.state(stream).close();
            }
        });
    }
    
    public void minAllocationChunk(final int minAllocationChunk) {
        if (minAllocationChunk <= 0) {
            throw new IllegalArgumentException("minAllocationChunk must be > 0");
        }
        this.minAllocationChunk = minAllocationChunk;
    }
    
    @Override
    public void updateStreamableBytes(final StreamState streamState) {
        this.state(streamState.stream()).updateStreamableBytes(Http2CodecUtil.streamableBytes(streamState), streamState.hasFrame(), streamState.windowSize());
    }
    
    @Override
    public void updateDependencyTree(final int childStreamId, final int parentStreamId, final short weight, final boolean exclusive) {
    }
    
    @Override
    public boolean distribute(int maxBytes, final Writer writer) throws Http2Exception {
        final int size = this.queue.size();
        if (size == 0) {
            return this.totalStreamableBytes > 0L;
        }
        final int chunkSize = Math.max(this.minAllocationChunk, maxBytes / size);
        State state = this.queue.pollFirst();
        do {
            state.enqueued = false;
            if (state.windowNegative) {
                continue;
            }
            if (maxBytes == 0 && state.streamableBytes > 0) {
                this.queue.addFirst(state);
                state.enqueued = true;
                break;
            }
            final int chunk = Math.min(chunkSize, Math.min(maxBytes, state.streamableBytes));
            maxBytes -= chunk;
            state.write(chunk, writer);
        } while ((state = this.queue.pollFirst()) != null);
        return this.totalStreamableBytes > 0L;
    }
    
    private State state(final Http2Stream stream) {
        return ObjectUtil.checkNotNull(stream, "stream").getProperty(this.stateKey);
    }
    
    int streamableBytes0(final Http2Stream stream) {
        return this.state(stream).streamableBytes;
    }
    
    private final class State
    {
        final Http2Stream stream;
        int streamableBytes;
        boolean windowNegative;
        boolean enqueued;
        boolean writing;
        
        State(final Http2Stream stream) {
            this.stream = stream;
        }
        
        void updateStreamableBytes(final int newStreamableBytes, final boolean hasFrame, final int windowSize) {
            assert newStreamableBytes == 0 : "hasFrame: " + hasFrame + " newStreamableBytes: " + newStreamableBytes;
            final int delta = newStreamableBytes - this.streamableBytes;
            if (delta != 0) {
                this.streamableBytes = newStreamableBytes;
                UniformStreamByteDistributor.this.totalStreamableBytes += delta;
            }
            this.windowNegative = (windowSize < 0);
            if (hasFrame && (windowSize > 0 || (windowSize == 0 && !this.writing))) {
                this.addToQueue();
            }
        }
        
        void write(final int numBytes, final Writer writer) throws Http2Exception {
            this.writing = true;
            try {
                writer.write(this.stream, numBytes);
            }
            catch (final Throwable t) {
                throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, t, "byte distribution write error", new Object[0]);
            }
            finally {
                this.writing = false;
            }
        }
        
        void addToQueue() {
            if (!this.enqueued) {
                this.enqueued = true;
                UniformStreamByteDistributor.this.queue.addLast(this);
            }
        }
        
        void removeFromQueue() {
            if (this.enqueued) {
                this.enqueued = false;
                UniformStreamByteDistributor.this.queue.remove(this);
            }
        }
        
        void close() {
            this.removeFromQueue();
            this.updateStreamableBytes(0, false, 0);
        }
    }
}
