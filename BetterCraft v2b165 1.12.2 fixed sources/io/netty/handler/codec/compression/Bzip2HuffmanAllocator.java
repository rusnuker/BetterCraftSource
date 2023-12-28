// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.compression;

final class Bzip2HuffmanAllocator
{
    private static int first(final int[] array, int i, final int nodesToMove) {
        final int length = array.length;
        final int limit = i;
        int k = array.length - 2;
        while (i >= nodesToMove && array[i] % length > limit) {
            k = i;
            i -= limit - i + 1;
        }
        i = Math.max(nodesToMove - 1, i);
        while (k > i + 1) {
            final int temp = i + k >>> 1;
            if (array[temp] % length > limit) {
                k = temp;
            }
            else {
                i = temp;
            }
        }
        return k;
    }
    
    private static void setExtendedParentPointers(final int[] array) {
        final int length = array.length;
        final int n = 0;
        array[n] += array[1];
        int headNode = 0;
        int tailNode = 1;
        int topNode = 2;
        while (tailNode < length - 1) {
            int temp;
            if (topNode >= length || array[headNode] < array[topNode]) {
                temp = array[headNode];
                array[headNode++] = tailNode;
            }
            else {
                temp = array[topNode++];
            }
            if (topNode >= length || (headNode < tailNode && array[headNode] < array[topNode])) {
                temp += array[headNode];
                array[headNode++] = tailNode + length;
            }
            else {
                temp += array[topNode++];
            }
            array[tailNode] = temp;
            ++tailNode;
        }
    }
    
    private static int findNodesToRelocate(final int[] array, final int maximumLength) {
        int currentNode = array.length - 2;
        for (int currentDepth = 1; currentDepth < maximumLength - 1 && currentNode > 1; currentNode = first(array, currentNode - 1, 0), ++currentDepth) {}
        return currentNode;
    }
    
    private static void allocateNodeLengths(final int[] array) {
        int firstNode = array.length - 2;
        int nextNode = array.length - 1;
        int lastNode;
        for (int currentDepth = 1, availableNodes = 2; availableNodes > 0; availableNodes = lastNode - firstNode << 1, ++currentDepth) {
            lastNode = firstNode;
            firstNode = first(array, lastNode - 1, 0);
            for (int i = availableNodes - (lastNode - firstNode); i > 0; --i) {
                array[nextNode--] = currentDepth;
            }
        }
    }
    
    private static void allocateNodeLengthsWithRelocation(final int[] array, final int nodesToMove, final int insertDepth) {
        int firstNode = array.length - 2;
        int nextNode = array.length - 1;
        int currentDepth = (insertDepth == 1) ? 2 : 1;
        int nodesLeftToMove = (insertDepth == 1) ? (nodesToMove - 2) : nodesToMove;
        int lastNode;
        int offset;
        for (int availableNodes = currentDepth << 1; availableNodes > 0; availableNodes = lastNode - firstNode + offset << 1, ++currentDepth) {
            lastNode = firstNode;
            firstNode = ((firstNode <= nodesToMove) ? firstNode : first(array, lastNode - 1, nodesToMove));
            offset = 0;
            if (currentDepth >= insertDepth) {
                offset = Math.min(nodesLeftToMove, 1 << currentDepth - insertDepth);
            }
            else if (currentDepth == insertDepth - 1) {
                offset = 1;
                if (array[firstNode] == lastNode) {
                    ++firstNode;
                }
            }
            for (int i = availableNodes - (lastNode - firstNode + offset); i > 0; --i) {
                array[nextNode--] = currentDepth;
            }
            nodesLeftToMove -= offset;
        }
    }
    
    static void allocateHuffmanCodeLengths(final int[] array, final int maximumLength) {
        switch (array.length) {
            case 2: {
                array[1] = 1;
            }
            case 1: {
                array[0] = 1;
                return;
            }
            default: {
                setExtendedParentPointers(array);
                final int nodesToRelocate = findNodesToRelocate(array, maximumLength);
                if (array[0] % array.length >= nodesToRelocate) {
                    allocateNodeLengths(array);
                }
                else {
                    final int insertDepth = maximumLength - (32 - Integer.numberOfLeadingZeros(nodesToRelocate - 1));
                    allocateNodeLengthsWithRelocation(array, nodesToRelocate, insertDepth);
                }
            }
        }
    }
    
    private Bzip2HuffmanAllocator() {
    }
}
