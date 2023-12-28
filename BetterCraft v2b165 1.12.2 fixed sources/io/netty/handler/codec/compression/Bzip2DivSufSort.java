// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.compression;

final class Bzip2DivSufSort
{
    private static final int STACK_SIZE = 64;
    private static final int BUCKET_A_SIZE = 256;
    private static final int BUCKET_B_SIZE = 65536;
    private static final int SS_BLOCKSIZE = 1024;
    private static final int INSERTIONSORT_THRESHOLD = 8;
    private static final int[] LOG_2_TABLE;
    private final int[] SA;
    private final byte[] T;
    private final int n;
    
    Bzip2DivSufSort(final byte[] block, final int[] bwtBlock, final int blockLength) {
        this.T = block;
        this.SA = bwtBlock;
        this.n = blockLength;
    }
    
    private static void swapElements(final int[] array1, final int idx1, final int[] array2, final int idx2) {
        final int temp = array1[idx1];
        array1[idx1] = array2[idx2];
        array2[idx2] = temp;
    }
    
    private int ssCompare(final int p1, final int p2, final int depth) {
        final int[] SA = this.SA;
        byte[] T;
        int U1n;
        int U2n;
        int U1;
        int U2;
        for (T = this.T, U1n = SA[p1 + 1] + 2, U2n = SA[p2 + 1] + 2, U1 = depth + SA[p1], U2 = depth + SA[p2]; U1 < U1n && U2 < U2n && T[U1] == T[U2]; ++U1, ++U2) {}
        return (U1 < U1n) ? ((U2 < U2n) ? ((T[U1] & 0xFF) - (T[U2] & 0xFF)) : 1) : ((U2 < U2n) ? -1 : 0);
    }
    
    private int ssCompareLast(final int pa, final int p1, final int p2, final int depth, final int size) {
        final int[] SA = this.SA;
        byte[] T;
        int U1;
        int U2;
        int U1n;
        int U2n;
        for (T = this.T, U1 = depth + SA[p1], U2 = depth + SA[p2], U1n = size, U2n = SA[p2 + 1] + 2; U1 < U1n && U2 < U2n && T[U1] == T[U2]; ++U1, ++U2) {}
        if (U1 < U1n) {
            return (U2 < U2n) ? ((T[U1] & 0xFF) - (T[U2] & 0xFF)) : 1;
        }
        if (U2 == U2n) {
            return 1;
        }
        for (U1 %= size, U1n = SA[pa] + 2; U1 < U1n && U2 < U2n && T[U1] == T[U2]; ++U1, ++U2) {}
        return (U1 < U1n) ? ((U2 < U2n) ? ((T[U1] & 0xFF) - (T[U2] & 0xFF)) : 1) : ((U2 < U2n) ? -1 : 0);
    }
    
    private void ssInsertionSort(final int pa, final int first, final int last, final int depth) {
        final int[] SA = this.SA;
        for (int i = last - 2; first <= i; --i) {
            final int t = SA[i];
            int j = i + 1;
            int r;
            while (0 < (r = this.ssCompare(pa + t, pa + SA[j], depth))) {
                do {
                    SA[j - 1] = SA[j];
                } while (++j < last && SA[j] < 0);
                if (last <= j) {
                    break;
                }
            }
            if (r == 0) {
                SA[j] ^= -1;
            }
            SA[j - 1] = t;
        }
    }
    
    private void ssFixdown(final int td, final int pa, final int sa, int i, final int size) {
        final int[] SA = this.SA;
        final byte[] T = this.T;
        final int v = SA[sa + i];
        final int c = T[td + SA[pa + v]] & 0xFF;
        int j;
        while ((j = 2 * i + 1) < size) {
            int k;
            int d = T[td + SA[pa + SA[sa + (k = j++)]]] & 0xFF;
            final int e;
            if (d < (e = (T[td + SA[pa + SA[sa + j]]] & 0xFF))) {
                k = j;
                d = e;
            }
            if (d <= c) {
                break;
            }
            SA[sa + i] = SA[sa + k];
            i = k;
        }
        SA[sa + i] = v;
    }
    
    private void ssHeapSort(final int td, final int pa, final int sa, final int size) {
        final int[] SA = this.SA;
        final byte[] T = this.T;
        int m = size;
        if (size % 2 == 0) {
            --m;
            if ((T[td + SA[pa + SA[sa + m / 2]]] & 0xFF) < (T[td + SA[pa + SA[sa + m]]] & 0xFF)) {
                swapElements(SA, sa + m, SA, sa + m / 2);
            }
        }
        for (int i = m / 2 - 1; 0 <= i; --i) {
            this.ssFixdown(td, pa, sa, i, m);
        }
        if (size % 2 == 0) {
            swapElements(SA, sa, SA, sa + m);
            this.ssFixdown(td, pa, sa, 0, m);
        }
        for (int i = m - 1; 0 < i; --i) {
            final int t = SA[sa];
            SA[sa] = SA[sa + i];
            this.ssFixdown(td, pa, sa, 0, i);
            SA[sa + i] = t;
        }
    }
    
    private int ssMedian3(final int td, final int pa, int v1, int v2, final int v3) {
        final int[] SA = this.SA;
        final byte[] T = this.T;
        int T_v1 = T[td + SA[pa + SA[v1]]] & 0xFF;
        int T_v2 = T[td + SA[pa + SA[v2]]] & 0xFF;
        final int T_v3 = T[td + SA[pa + SA[v3]]] & 0xFF;
        if (T_v1 > T_v2) {
            final int temp = v1;
            v1 = v2;
            v2 = temp;
            final int T_vtemp = T_v1;
            T_v1 = T_v2;
            T_v2 = T_vtemp;
        }
        if (T_v2 <= T_v3) {
            return v2;
        }
        if (T_v1 > T_v3) {
            return v1;
        }
        return v3;
    }
    
    private int ssMedian5(final int td, final int pa, int v1, int v2, int v3, int v4, int v5) {
        final int[] SA = this.SA;
        final byte[] T = this.T;
        int T_v1 = T[td + SA[pa + SA[v1]]] & 0xFF;
        int T_v2 = T[td + SA[pa + SA[v2]]] & 0xFF;
        int T_v3 = T[td + SA[pa + SA[v3]]] & 0xFF;
        int T_v4 = T[td + SA[pa + SA[v4]]] & 0xFF;
        int T_v5 = T[td + SA[pa + SA[v5]]] & 0xFF;
        if (T_v2 > T_v3) {
            final int temp = v2;
            v2 = v3;
            v3 = temp;
            final int T_vtemp = T_v2;
            T_v2 = T_v3;
            T_v3 = T_vtemp;
        }
        if (T_v4 > T_v5) {
            final int temp = v4;
            v4 = v5;
            v5 = temp;
            final int T_vtemp = T_v4;
            T_v4 = T_v5;
            T_v5 = T_vtemp;
        }
        if (T_v2 > T_v4) {
            int temp = v4 = v2;
            int T_vtemp = T_v4 = T_v2;
            temp = v3;
            v3 = v5;
            v5 = temp;
            T_vtemp = T_v3;
            T_v3 = T_v5;
            T_v5 = T_vtemp;
        }
        if (T_v1 > T_v3) {
            final int temp = v1;
            v1 = v3;
            v3 = temp;
            final int T_vtemp = T_v1;
            T_v1 = T_v3;
            T_v3 = T_vtemp;
        }
        if (T_v1 > T_v4) {
            final int temp = v4 = v1;
            final int T_vtemp = T_v4 = T_v1;
            v3 = v5;
            T_v3 = T_v5;
        }
        if (T_v3 > T_v4) {
            return v4;
        }
        return v3;
    }
    
    private int ssPivot(final int td, final int pa, final int first, final int last) {
        int t = last - first;
        final int middle = first + t / 2;
        if (t > 512) {
            t >>= 3;
            return this.ssMedian3(td, pa, this.ssMedian3(td, pa, first, first + t, first + (t << 1)), this.ssMedian3(td, pa, middle - t, middle, middle + t), this.ssMedian3(td, pa, last - 1 - (t << 1), last - 1 - t, last - 1));
        }
        if (t <= 32) {
            return this.ssMedian3(td, pa, first, middle, last - 1);
        }
        t >>= 2;
        return this.ssMedian5(td, pa, first, first + t, middle, last - 1 - t, last - 1);
    }
    
    private static int ssLog(final int n) {
        return ((n & 0xFF00) != 0x0) ? (8 + Bzip2DivSufSort.LOG_2_TABLE[n >> 8 & 0xFF]) : Bzip2DivSufSort.LOG_2_TABLE[n & 0xFF];
    }
    
    private int ssSubstringPartition(final int pa, final int first, final int last, final int depth) {
        final int[] SA = this.SA;
        int a = first - 1;
        int b = last;
        while (true) {
            if (++a < b && SA[pa + SA[a]] + depth >= SA[pa + SA[a] + 1] + 1) {
                SA[a] ^= -1;
            }
            else {
                --b;
                while (a < b && SA[pa + SA[b]] + depth < SA[pa + SA[b] + 1] + 1) {
                    --b;
                }
                if (b <= a) {
                    break;
                }
                final int t = ~SA[b];
                SA[b] = SA[a];
                SA[a] = t;
            }
        }
        if (first < a) {
            SA[first] ^= -1;
        }
        return a;
    }
    
    private void ssMultiKeyIntroSort(final int pa, int first, int last, int depth) {
        final int[] SA = this.SA;
        final byte[] T = this.T;
        final StackEntry[] stack = new StackEntry[64];
        int x = 0;
        int ssize = 0;
        int limit = ssLog(last - first);
        while (true) {
            if (last - first <= 8) {
                if (1 < last - first) {
                    this.ssInsertionSort(pa, first, last, depth);
                }
                if (ssize == 0) {
                    break;
                }
                final StackEntry entry = stack[--ssize];
                first = entry.a;
                last = entry.b;
                depth = entry.c;
                limit = entry.d;
            }
            else {
                final int Td = depth;
                if (limit-- == 0) {
                    this.ssHeapSort(Td, pa, first, last - first);
                }
                if (limit < 0) {
                    int a = first + 1;
                    int v = T[Td + SA[pa + SA[first]]] & 0xFF;
                    while (a < last) {
                        if ((x = (T[Td + SA[pa + SA[a]]] & 0xFF)) != v) {
                            if (1 < a - first) {
                                break;
                            }
                            v = x;
                            first = a;
                        }
                        ++a;
                    }
                    if ((T[Td + SA[pa + SA[first]] - 1] & 0xFF) < v) {
                        first = this.ssSubstringPartition(pa, first, a, depth);
                    }
                    if (a - first <= last - a) {
                        if (1 < a - first) {
                            stack[ssize++] = new StackEntry(a, last, depth, -1);
                            last = a;
                            ++depth;
                            limit = ssLog(a - first);
                        }
                        else {
                            first = a;
                            limit = -1;
                        }
                    }
                    else if (1 < last - a) {
                        stack[ssize++] = new StackEntry(first, a, depth + 1, ssLog(a - first));
                        first = a;
                        limit = -1;
                    }
                    else {
                        last = a;
                        ++depth;
                        limit = ssLog(a - first);
                    }
                }
                else {
                    int a = this.ssPivot(Td, pa, first, last);
                    final int v = T[Td + SA[pa + SA[a]]] & 0xFF;
                    swapElements(SA, first, SA, a);
                    int b;
                    for (b = first + 1; b < last && (x = (T[Td + SA[pa + SA[b]]] & 0xFF)) == v; ++b) {}
                    if ((a = b) < last && x < v) {
                        while (++b < last && (x = (T[Td + SA[pa + SA[b]]] & 0xFF)) <= v) {
                            if (x == v) {
                                swapElements(SA, b, SA, a);
                                ++a;
                            }
                        }
                    }
                    int c;
                    for (c = last - 1; b < c && (x = (T[Td + SA[pa + SA[c]]] & 0xFF)) == v; --c) {}
                    int d;
                    if (b < (d = c) && x > v) {
                        while (b < --c && (x = (T[Td + SA[pa + SA[c]]] & 0xFF)) >= v) {
                            if (x == v) {
                                swapElements(SA, c, SA, d);
                                --d;
                            }
                        }
                    }
                    while (b < c) {
                        swapElements(SA, b, SA, c);
                        while (++b < c && (x = (T[Td + SA[pa + SA[b]]] & 0xFF)) <= v) {
                            if (x == v) {
                                swapElements(SA, b, SA, a);
                                ++a;
                            }
                        }
                        while (b < --c && (x = (T[Td + SA[pa + SA[c]]] & 0xFF)) >= v) {
                            if (x == v) {
                                swapElements(SA, c, SA, d);
                                --d;
                            }
                        }
                    }
                    if (a <= d) {
                        c = b - 1;
                        int s;
                        int t;
                        if ((s = a - first) > (t = b - a)) {
                            s = t;
                        }
                        for (int e = first, f = b - s; 0 < s; --s, ++e, ++f) {
                            swapElements(SA, e, SA, f);
                        }
                        if ((s = d - c) > (t = last - d - 1)) {
                            s = t;
                        }
                        for (int e = b, f = last - s; 0 < s; --s, ++e, ++f) {
                            swapElements(SA, e, SA, f);
                        }
                        a = first + (b - a);
                        c = last - (d - c);
                        b = ((v <= (T[Td + SA[pa + SA[a]] - 1] & 0xFF)) ? a : this.ssSubstringPartition(pa, a, c, depth));
                        if (a - first <= last - c) {
                            if (last - c <= c - b) {
                                stack[ssize++] = new StackEntry(b, c, depth + 1, ssLog(c - b));
                                stack[ssize++] = new StackEntry(c, last, depth, limit);
                                last = a;
                            }
                            else if (a - first <= c - b) {
                                stack[ssize++] = new StackEntry(c, last, depth, limit);
                                stack[ssize++] = new StackEntry(b, c, depth + 1, ssLog(c - b));
                                last = a;
                            }
                            else {
                                stack[ssize++] = new StackEntry(c, last, depth, limit);
                                stack[ssize++] = new StackEntry(first, a, depth, limit);
                                first = b;
                                last = c;
                                ++depth;
                                limit = ssLog(c - b);
                            }
                        }
                        else if (a - first <= c - b) {
                            stack[ssize++] = new StackEntry(b, c, depth + 1, ssLog(c - b));
                            stack[ssize++] = new StackEntry(first, a, depth, limit);
                            first = c;
                        }
                        else if (last - c <= c - b) {
                            stack[ssize++] = new StackEntry(first, a, depth, limit);
                            stack[ssize++] = new StackEntry(b, c, depth + 1, ssLog(c - b));
                            first = c;
                        }
                        else {
                            stack[ssize++] = new StackEntry(first, a, depth, limit);
                            stack[ssize++] = new StackEntry(c, last, depth, limit);
                            first = b;
                            last = c;
                            ++depth;
                            limit = ssLog(c - b);
                        }
                    }
                    else {
                        ++limit;
                        if ((T[Td + SA[pa + SA[first]] - 1] & 0xFF) < v) {
                            first = this.ssSubstringPartition(pa, first, last, depth);
                            limit = ssLog(last - first);
                        }
                        ++depth;
                    }
                }
            }
        }
    }
    
    private static void ssBlockSwap(final int[] array1, final int first1, final int[] array2, final int first2, final int size) {
        for (int i = size, a = first1, b = first2; 0 < i; --i, ++a, ++b) {
            swapElements(array1, a, array2, b);
        }
    }
    
    private void ssMergeForward(final int pa, final int[] buf, final int bufoffset, final int first, final int middle, final int last, final int depth) {
        final int[] SA = this.SA;
        final int bufend = bufoffset + (middle - first) - 1;
        ssBlockSwap(buf, bufoffset, SA, first, middle - first);
        final int t = SA[first];
        int i = first;
        int j = bufoffset;
        int k = middle;
        Label_0312: {
        Label_0161:
            while (true) {
                final int r = this.ssCompare(pa + buf[j], pa + SA[k], depth);
                if (r < 0) {
                    do {
                        SA[i++] = buf[j];
                        if (bufend <= j) {
                            buf[j] = t;
                            return;
                        }
                        buf[j++] = SA[i];
                    } while (buf[j] < 0);
                }
                else if (r > 0) {
                    do {
                        SA[i++] = SA[k];
                        SA[k++] = SA[i];
                        if (last <= k) {
                            break Label_0161;
                        }
                    } while (SA[k] < 0);
                }
                else {
                    SA[k] ^= -1;
                    do {
                        SA[i++] = buf[j];
                        if (bufend <= j) {
                            buf[j] = t;
                            return;
                        }
                        buf[j++] = SA[i];
                    } while (buf[j] < 0);
                    do {
                        SA[i++] = SA[k];
                        SA[k++] = SA[i];
                        if (last <= k) {
                            break Label_0312;
                        }
                    } while (SA[k] < 0);
                }
            }
            while (j < bufend) {
                SA[i++] = buf[j];
                buf[j++] = SA[i];
            }
            SA[i] = buf[j];
            buf[j] = t;
            return;
        }
        while (j < bufend) {
            SA[i++] = buf[j];
            buf[j++] = SA[i];
        }
        SA[i] = buf[j];
        buf[j] = t;
    }
    
    private void ssMergeBackward(final int pa, final int[] buf, final int bufoffset, final int first, final int middle, final int last, final int depth) {
        final int[] SA = this.SA;
        final int bufend = bufoffset + (last - middle);
        ssBlockSwap(buf, bufoffset, SA, middle, last - middle);
        int x = 0;
        int p1;
        if (buf[bufend - 1] < 0) {
            x |= 0x1;
            p1 = pa + ~buf[bufend - 1];
        }
        else {
            p1 = pa + buf[bufend - 1];
        }
        int p2;
        if (SA[middle - 1] < 0) {
            x |= 0x2;
            p2 = pa + ~SA[middle - 1];
        }
        else {
            p2 = pa + SA[middle - 1];
        }
        final int t = SA[last - 1];
        int i = last - 1;
        int j = bufend - 1;
        int k = middle - 1;
        Label_0614: {
            while (true) {
                final int r = this.ssCompare(p1, p2, depth);
                if (r > 0) {
                    if ((x & 0x1) != 0x0) {
                        do {
                            SA[i--] = buf[j];
                            buf[j--] = SA[i];
                        } while (buf[j] < 0);
                        x ^= 0x1;
                    }
                    SA[i--] = buf[j];
                    if (j <= bufoffset) {
                        buf[j] = t;
                        return;
                    }
                    buf[j--] = SA[i];
                    if (buf[j] < 0) {
                        x |= 0x1;
                        p1 = pa + ~buf[j];
                    }
                    else {
                        p1 = pa + buf[j];
                    }
                }
                else if (r < 0) {
                    if ((x & 0x2) != 0x0) {
                        do {
                            SA[i--] = SA[k];
                            SA[k--] = SA[i];
                        } while (SA[k] < 0);
                        x ^= 0x2;
                    }
                    SA[i--] = SA[k];
                    SA[k--] = SA[i];
                    if (k < first) {
                        break;
                    }
                    if (SA[k] < 0) {
                        x |= 0x2;
                        p2 = pa + ~SA[k];
                    }
                    else {
                        p2 = pa + SA[k];
                    }
                }
                else {
                    if ((x & 0x1) != 0x0) {
                        do {
                            SA[i--] = buf[j];
                            buf[j--] = SA[i];
                        } while (buf[j] < 0);
                        x ^= 0x1;
                    }
                    SA[i--] = ~buf[j];
                    if (j <= bufoffset) {
                        buf[j] = t;
                        return;
                    }
                    buf[j--] = SA[i];
                    if ((x & 0x2) != 0x0) {
                        do {
                            SA[i--] = SA[k];
                            SA[k--] = SA[i];
                        } while (SA[k] < 0);
                        x ^= 0x2;
                    }
                    SA[i--] = SA[k];
                    SA[k--] = SA[i];
                    if (k < first) {
                        break Label_0614;
                    }
                    if (buf[j] < 0) {
                        x |= 0x1;
                        p1 = pa + ~buf[j];
                    }
                    else {
                        p1 = pa + buf[j];
                    }
                    if (SA[k] < 0) {
                        x |= 0x2;
                        p2 = pa + ~SA[k];
                    }
                    else {
                        p2 = pa + SA[k];
                    }
                }
            }
            while (bufoffset < j) {
                SA[i--] = buf[j];
                buf[j--] = SA[i];
            }
            SA[i] = buf[j];
            buf[j] = t;
            return;
        }
        while (bufoffset < j) {
            SA[i--] = buf[j];
            buf[j--] = SA[i];
        }
        SA[i] = buf[j];
        buf[j] = t;
    }
    
    private static int getIDX(final int a) {
        return (0 <= a) ? a : (~a);
    }
    
    private void ssMergeCheckEqual(final int pa, final int depth, final int a) {
        final int[] SA = this.SA;
        if (0 <= SA[a] && this.ssCompare(pa + getIDX(SA[a - 1]), pa + SA[a], depth) == 0) {
            SA[a] ^= -1;
        }
    }
    
    private void ssMerge(final int pa, int first, int middle, int last, final int[] buf, final int bufoffset, final int bufsize, final int depth) {
        final int[] SA = this.SA;
        final StackEntry[] stack = new StackEntry[64];
        int check = 0;
        int ssize = 0;
        while (true) {
            if (last - middle <= bufsize) {
                if (first < middle && middle < last) {
                    this.ssMergeBackward(pa, buf, bufoffset, first, middle, last, depth);
                }
                if ((check & 0x1) != 0x0) {
                    this.ssMergeCheckEqual(pa, depth, first);
                }
                if ((check & 0x2) != 0x0) {
                    this.ssMergeCheckEqual(pa, depth, last);
                }
                if (ssize == 0) {
                    return;
                }
                final StackEntry entry = stack[--ssize];
                first = entry.a;
                middle = entry.b;
                last = entry.c;
                check = entry.d;
            }
            else if (middle - first <= bufsize) {
                if (first < middle) {
                    this.ssMergeForward(pa, buf, bufoffset, first, middle, last, depth);
                }
                if ((check & 0x1) != 0x0) {
                    this.ssMergeCheckEqual(pa, depth, first);
                }
                if ((check & 0x2) != 0x0) {
                    this.ssMergeCheckEqual(pa, depth, last);
                }
                if (ssize == 0) {
                    return;
                }
                final StackEntry entry = stack[--ssize];
                first = entry.a;
                middle = entry.b;
                last = entry.c;
                check = entry.d;
            }
            else {
                int m = 0;
                for (int len = Math.min(middle - first, last - middle), half = len >> 1; 0 < len; len = half, half >>= 1) {
                    if (this.ssCompare(pa + getIDX(SA[middle + m + half]), pa + getIDX(SA[middle - m - half - 1]), depth) < 0) {
                        m += half + 1;
                        half -= ((len & 0x1) ^ 0x1);
                    }
                }
                if (0 < m) {
                    ssBlockSwap(SA, middle - m, SA, middle, m);
                    int i;
                    int j = i = middle;
                    int next = 0;
                    if (middle + m < last) {
                        if (SA[middle + m] < 0) {
                            while (SA[i - 1] < 0) {
                                --i;
                            }
                            SA[middle + m] ^= -1;
                        }
                        for (j = middle; SA[j] < 0; ++j) {}
                        next = 1;
                    }
                    if (i - first <= last - j) {
                        stack[ssize++] = new StackEntry(j, middle + m, last, (check & 0x2) | (next & 0x1));
                        middle -= m;
                        last = i;
                        check &= 0x1;
                    }
                    else {
                        if (i == middle && middle == j) {
                            next <<= 1;
                        }
                        stack[ssize++] = new StackEntry(first, middle - m, i, (check & 0x1) | (next & 0x2));
                        first = j;
                        middle += m;
                        check = ((check & 0x2) | (next & 0x1));
                    }
                }
                else {
                    if ((check & 0x1) != 0x0) {
                        this.ssMergeCheckEqual(pa, depth, first);
                    }
                    this.ssMergeCheckEqual(pa, depth, middle);
                    if ((check & 0x2) != 0x0) {
                        this.ssMergeCheckEqual(pa, depth, last);
                    }
                    if (ssize == 0) {
                        return;
                    }
                    final StackEntry entry = stack[--ssize];
                    first = entry.a;
                    middle = entry.b;
                    last = entry.c;
                    check = entry.d;
                }
            }
        }
    }
    
    private void subStringSort(final int pa, int first, final int last, final int[] buf, final int bufoffset, final int bufsize, final int depth, final boolean lastsuffix, final int size) {
        final int[] SA = this.SA;
        if (lastsuffix) {
            ++first;
        }
        int a;
        int i;
        for (a = first, i = 0; a + 1024 < last; a += 1024, ++i) {
            this.ssMultiKeyIntroSort(pa, a, a + 1024, depth);
            int[] curbuf = SA;
            int curbufoffset = a + 1024;
            int curbufsize = last - (a + 1024);
            if (curbufsize <= bufsize) {
                curbufsize = bufsize;
                curbuf = buf;
                curbufoffset = bufoffset;
            }
            int b = a;
            int k = 1024;
            for (int j = i; (j & 0x1) != 0x0; j >>>= 1) {
                this.ssMerge(pa, b - k, b, b + k, curbuf, curbufoffset, curbufsize, depth);
                b -= k;
                k <<= 1;
            }
        }
        this.ssMultiKeyIntroSort(pa, a, last, depth);
        int k = 1024;
        while (i != 0) {
            if ((i & 0x1) != 0x0) {
                this.ssMerge(pa, a - k, a, last, buf, bufoffset, bufsize, depth);
                a -= k;
            }
            k <<= 1;
            i >>= 1;
        }
        if (lastsuffix) {
            a = first;
            i = SA[first - 1];
            int r = 1;
            while (a < last && (SA[a] < 0 || 0 < (r = this.ssCompareLast(pa, pa + i, pa + SA[a], depth, size)))) {
                SA[a - 1] = SA[a];
                ++a;
            }
            if (r == 0) {
                SA[a] ^= -1;
            }
            SA[a - 1] = i;
        }
    }
    
    private int trGetC(final int isa, final int isaD, final int isaN, final int p) {
        return (isaD + p < isaN) ? this.SA[isaD + p] : this.SA[isa + (isaD - isa + p) % (isaN - isa)];
    }
    
    private void trFixdown(final int isa, final int isaD, final int isaN, final int sa, int i, final int size) {
        final int[] SA = this.SA;
        final int v = SA[sa + i];
        final int c = this.trGetC(isa, isaD, isaN, v);
        int j;
        while ((j = 2 * i + 1) < size) {
            int k = j++;
            int d = this.trGetC(isa, isaD, isaN, SA[sa + k]);
            final int e;
            if (d < (e = this.trGetC(isa, isaD, isaN, SA[sa + j]))) {
                k = j;
                d = e;
            }
            if (d <= c) {
                break;
            }
            SA[sa + i] = SA[sa + k];
            i = k;
        }
        SA[sa + i] = v;
    }
    
    private void trHeapSort(final int isa, final int isaD, final int isaN, final int sa, final int size) {
        final int[] SA = this.SA;
        int m = size;
        if (size % 2 == 0) {
            --m;
            if (this.trGetC(isa, isaD, isaN, SA[sa + m / 2]) < this.trGetC(isa, isaD, isaN, SA[sa + m])) {
                swapElements(SA, sa + m, SA, sa + m / 2);
            }
        }
        for (int i = m / 2 - 1; 0 <= i; --i) {
            this.trFixdown(isa, isaD, isaN, sa, i, m);
        }
        if (size % 2 == 0) {
            swapElements(SA, sa, SA, sa + m);
            this.trFixdown(isa, isaD, isaN, sa, 0, m);
        }
        for (int i = m - 1; 0 < i; --i) {
            final int t = SA[sa];
            SA[sa] = SA[sa + i];
            this.trFixdown(isa, isaD, isaN, sa, 0, i);
            SA[sa + i] = t;
        }
    }
    
    private void trInsertionSort(final int isa, final int isaD, final int isaN, final int first, final int last) {
        final int[] SA = this.SA;
        for (int a = first + 1; a < last; ++a) {
            final int t = SA[a];
            int b = a - 1;
            int r;
            while (0 > (r = this.trGetC(isa, isaD, isaN, t) - this.trGetC(isa, isaD, isaN, SA[b]))) {
                do {
                    SA[b + 1] = SA[b];
                } while (first <= --b && SA[b] < 0);
                if (b < first) {
                    break;
                }
            }
            if (r == 0) {
                SA[b] ^= -1;
            }
            SA[b + 1] = t;
        }
    }
    
    private static int trLog(final int n) {
        return ((n & 0xFFFF0000) != 0x0) ? (((n & 0xFF000000) != 0x0) ? (24 + Bzip2DivSufSort.LOG_2_TABLE[n >> 24 & 0xFF]) : Bzip2DivSufSort.LOG_2_TABLE[n >> 16 & 0x10F]) : (((n & 0xFF00) != 0x0) ? (8 + Bzip2DivSufSort.LOG_2_TABLE[n >> 8 & 0xFF]) : Bzip2DivSufSort.LOG_2_TABLE[n & 0xFF]);
    }
    
    private int trMedian3(final int isa, final int isaD, final int isaN, int v1, int v2, final int v3) {
        final int[] SA = this.SA;
        int SA_v1 = this.trGetC(isa, isaD, isaN, SA[v1]);
        int SA_v2 = this.trGetC(isa, isaD, isaN, SA[v2]);
        final int SA_v3 = this.trGetC(isa, isaD, isaN, SA[v3]);
        if (SA_v1 > SA_v2) {
            final int temp = v1;
            v1 = v2;
            v2 = temp;
            final int SA_vtemp = SA_v1;
            SA_v1 = SA_v2;
            SA_v2 = SA_vtemp;
        }
        if (SA_v2 <= SA_v3) {
            return v2;
        }
        if (SA_v1 > SA_v3) {
            return v1;
        }
        return v3;
    }
    
    private int trMedian5(final int isa, final int isaD, final int isaN, int v1, int v2, int v3, int v4, int v5) {
        final int[] SA = this.SA;
        int SA_v1 = this.trGetC(isa, isaD, isaN, SA[v1]);
        int SA_v2 = this.trGetC(isa, isaD, isaN, SA[v2]);
        int SA_v3 = this.trGetC(isa, isaD, isaN, SA[v3]);
        int SA_v4 = this.trGetC(isa, isaD, isaN, SA[v4]);
        int SA_v5 = this.trGetC(isa, isaD, isaN, SA[v5]);
        if (SA_v2 > SA_v3) {
            final int temp = v2;
            v2 = v3;
            v3 = temp;
            final int SA_vtemp = SA_v2;
            SA_v2 = SA_v3;
            SA_v3 = SA_vtemp;
        }
        if (SA_v4 > SA_v5) {
            final int temp = v4;
            v4 = v5;
            v5 = temp;
            final int SA_vtemp = SA_v4;
            SA_v4 = SA_v5;
            SA_v5 = SA_vtemp;
        }
        if (SA_v2 > SA_v4) {
            int temp = v4 = v2;
            int SA_vtemp = SA_v4 = SA_v2;
            temp = v3;
            v3 = v5;
            v5 = temp;
            SA_vtemp = SA_v3;
            SA_v3 = SA_v5;
            SA_v5 = SA_vtemp;
        }
        if (SA_v1 > SA_v3) {
            final int temp = v1;
            v1 = v3;
            v3 = temp;
            final int SA_vtemp = SA_v1;
            SA_v1 = SA_v3;
            SA_v3 = SA_vtemp;
        }
        if (SA_v1 > SA_v4) {
            final int temp = v4 = v1;
            final int SA_vtemp = SA_v4 = SA_v1;
            v3 = v5;
            SA_v3 = SA_v5;
        }
        if (SA_v3 > SA_v4) {
            return v4;
        }
        return v3;
    }
    
    private int trPivot(final int isa, final int isaD, final int isaN, final int first, final int last) {
        int t = last - first;
        final int middle = first + t / 2;
        if (t > 512) {
            t >>= 3;
            return this.trMedian3(isa, isaD, isaN, this.trMedian3(isa, isaD, isaN, first, first + t, first + (t << 1)), this.trMedian3(isa, isaD, isaN, middle - t, middle, middle + t), this.trMedian3(isa, isaD, isaN, last - 1 - (t << 1), last - 1 - t, last - 1));
        }
        if (t <= 32) {
            return this.trMedian3(isa, isaD, isaN, first, middle, last - 1);
        }
        t >>= 2;
        return this.trMedian5(isa, isaD, isaN, first, first + t, middle, last - 1 - t, last - 1);
    }
    
    private void lsUpdateGroup(final int isa, final int first, final int last) {
        final int[] SA = this.SA;
        for (int a = first; a < last; ++a) {
            if (0 <= SA[a]) {
                final int b = a;
                do {
                    SA[isa + SA[a]] = a;
                } while (++a < last && 0 <= SA[a]);
                SA[b] = b - a;
                if (last <= a) {
                    break;
                }
            }
            int b = a;
            do {
                SA[a] ^= -1;
            } while (SA[++a] < 0);
            final int t = a;
            do {
                SA[isa + SA[b]] = t;
            } while (++b <= a);
        }
    }
    
    private void lsIntroSort(final int isa, final int isaD, final int isaN, int first, int last) {
        final int[] SA = this.SA;
        final StackEntry[] stack = new StackEntry[64];
        int x = 0;
        int ssize = 0;
        int limit = trLog(last - first);
        while (true) {
            if (last - first <= 8) {
                if (1 < last - first) {
                    this.trInsertionSort(isa, isaD, isaN, first, last);
                    this.lsUpdateGroup(isa, first, last);
                }
                else if (last - first == 1) {
                    SA[first] = -1;
                }
                if (ssize == 0) {
                    return;
                }
                final StackEntry entry = stack[--ssize];
                first = entry.a;
                last = entry.b;
                limit = entry.c;
            }
            else if (limit-- == 0) {
                this.trHeapSort(isa, isaD, isaN, first, last - first);
                int b;
                for (int a = last - 1; first < a; a = b) {
                    for (x = this.trGetC(isa, isaD, isaN, SA[a]), b = a - 1; first <= b && this.trGetC(isa, isaD, isaN, SA[b]) == x; --b) {
                        SA[b] ^= -1;
                    }
                }
                this.lsUpdateGroup(isa, first, last);
                if (ssize == 0) {
                    return;
                }
                final StackEntry entry = stack[--ssize];
                first = entry.a;
                last = entry.b;
                limit = entry.c;
            }
            else {
                int a = this.trPivot(isa, isaD, isaN, first, last);
                swapElements(SA, first, SA, a);
                int b;
                int v;
                for (v = this.trGetC(isa, isaD, isaN, SA[first]), b = first + 1; b < last && (x = this.trGetC(isa, isaD, isaN, SA[b])) == v; ++b) {}
                if ((a = b) < last && x < v) {
                    while (++b < last && (x = this.trGetC(isa, isaD, isaN, SA[b])) <= v) {
                        if (x == v) {
                            swapElements(SA, b, SA, a);
                            ++a;
                        }
                    }
                }
                int c;
                for (c = last - 1; b < c && (x = this.trGetC(isa, isaD, isaN, SA[c])) == v; --c) {}
                int d;
                if (b < (d = c) && x > v) {
                    while (b < --c && (x = this.trGetC(isa, isaD, isaN, SA[c])) >= v) {
                        if (x == v) {
                            swapElements(SA, c, SA, d);
                            --d;
                        }
                    }
                }
                while (b < c) {
                    swapElements(SA, b, SA, c);
                    while (++b < c && (x = this.trGetC(isa, isaD, isaN, SA[b])) <= v) {
                        if (x == v) {
                            swapElements(SA, b, SA, a);
                            ++a;
                        }
                    }
                    while (b < --c && (x = this.trGetC(isa, isaD, isaN, SA[c])) >= v) {
                        if (x == v) {
                            swapElements(SA, c, SA, d);
                            --d;
                        }
                    }
                }
                if (a <= d) {
                    c = b - 1;
                    int s;
                    int t;
                    if ((s = a - first) > (t = b - a)) {
                        s = t;
                    }
                    for (int e = first, f = b - s; 0 < s; --s, ++e, ++f) {
                        swapElements(SA, e, SA, f);
                    }
                    if ((s = d - c) > (t = last - d - 1)) {
                        s = t;
                    }
                    for (int e = b, f = last - s; 0 < s; --s, ++e, ++f) {
                        swapElements(SA, e, SA, f);
                    }
                    a = first + (b - a);
                    b = last - (d - c);
                    c = first;
                    v = a - 1;
                    while (c < a) {
                        SA[isa + SA[c]] = v;
                        ++c;
                    }
                    if (b < last) {
                        c = a;
                        v = b - 1;
                        while (c < b) {
                            SA[isa + SA[c]] = v;
                            ++c;
                        }
                    }
                    if (b - a == 1) {
                        SA[a] = -1;
                    }
                    if (a - first <= last - b) {
                        if (first < a) {
                            stack[ssize++] = new StackEntry(b, last, limit, 0);
                            last = a;
                        }
                        else {
                            first = b;
                        }
                    }
                    else if (b < last) {
                        stack[ssize++] = new StackEntry(first, a, limit, 0);
                        first = b;
                    }
                    else {
                        last = a;
                    }
                }
                else {
                    if (ssize == 0) {
                        return;
                    }
                    final StackEntry entry = stack[--ssize];
                    first = entry.a;
                    last = entry.b;
                    limit = entry.c;
                }
            }
        }
    }
    
    private void lsSort(final int isa, final int n, final int depth) {
        final int[] SA = this.SA;
        int isaD = isa + depth;
        while (-n < SA[0]) {
            int first = 0;
            int skip = 0;
            do {
                final int t;
                if ((t = SA[first]) < 0) {
                    first -= t;
                    skip += t;
                }
                else {
                    if (skip != 0) {
                        SA[first + skip] = skip;
                        skip = 0;
                    }
                    final int last = SA[isa + t] + 1;
                    this.lsIntroSort(isa, isaD, isa + n, first, last);
                    first = last;
                }
            } while (first < n);
            if (skip != 0) {
                SA[first + skip] = skip;
            }
            if (n < isaD - isa) {
                first = 0;
                do {
                    final int t;
                    if ((t = SA[first]) < 0) {
                        first -= t;
                    }
                    else {
                        final int last = SA[isa + t] + 1;
                        for (int i = first; i < last; ++i) {
                            SA[isa + SA[i]] = i;
                        }
                        first = last;
                    }
                } while (first < n);
                break;
            }
            isaD += isaD - isa;
        }
    }
    
    private PartitionResult trPartition(final int isa, final int isaD, final int isaN, int first, int last, final int v) {
        final int[] SA = this.SA;
        int x = 0;
        int b;
        for (b = first; b < last && (x = this.trGetC(isa, isaD, isaN, SA[b])) == v; ++b) {}
        int a;
        if ((a = b) < last && x < v) {
            while (++b < last && (x = this.trGetC(isa, isaD, isaN, SA[b])) <= v) {
                if (x == v) {
                    swapElements(SA, b, SA, a);
                    ++a;
                }
            }
        }
        int c;
        for (c = last - 1; b < c && (x = this.trGetC(isa, isaD, isaN, SA[c])) == v; --c) {}
        int d;
        if (b < (d = c) && x > v) {
            while (b < --c && (x = this.trGetC(isa, isaD, isaN, SA[c])) >= v) {
                if (x == v) {
                    swapElements(SA, c, SA, d);
                    --d;
                }
            }
        }
        while (b < c) {
            swapElements(SA, b, SA, c);
            while (++b < c && (x = this.trGetC(isa, isaD, isaN, SA[b])) <= v) {
                if (x == v) {
                    swapElements(SA, b, SA, a);
                    ++a;
                }
            }
            while (b < --c && (x = this.trGetC(isa, isaD, isaN, SA[c])) >= v) {
                if (x == v) {
                    swapElements(SA, c, SA, d);
                    --d;
                }
            }
        }
        if (a <= d) {
            c = b - 1;
            int s;
            int t;
            if ((s = a - first) > (t = b - a)) {
                s = t;
            }
            for (int e = first, f = b - s; 0 < s; --s, ++e, ++f) {
                swapElements(SA, e, SA, f);
            }
            if ((s = d - c) > (t = last - d - 1)) {
                s = t;
            }
            for (int e = b, f = last - s; 0 < s; --s, ++e, ++f) {
                swapElements(SA, e, SA, f);
            }
            first += b - a;
            last -= d - c;
        }
        return new PartitionResult(first, last);
    }
    
    private void trCopy(final int isa, final int isaN, final int first, final int a, final int b, final int last, final int depth) {
        final int[] SA = this.SA;
        final int v = b - 1;
        int c;
        int d;
        for (c = first, d = a - 1; c <= d; ++c) {
            int s;
            if ((s = SA[c] - depth) < 0) {
                s += isaN - isa;
            }
            if (SA[isa + s] == v) {
                SA[++d] = s;
                SA[isa + s] = d;
            }
        }
        c = last - 1;
        final int e = d + 1;
        d = b;
        while (e < d) {
            int s;
            if ((s = SA[c] - depth) < 0) {
                s += isaN - isa;
            }
            if (SA[isa + s] == v) {
                SA[--d] = s;
                SA[isa + s] = d;
            }
            --c;
        }
    }
    
    private void trIntroSort(final int isa, int isaD, final int isaN, int first, int last, final TRBudget budget, final int size) {
        final int[] SA = this.SA;
        final StackEntry[] stack = new StackEntry[64];
        int x = 0;
        int ssize = 0;
        int limit = trLog(last - first);
        while (true) {
            if (limit < 0) {
                if (limit == -1) {
                    if (!budget.update(size, last - first)) {
                        break;
                    }
                    final PartitionResult result = this.trPartition(isa, isaD - 1, isaN, first, last, last - 1);
                    final int a = result.first;
                    final int b = result.last;
                    if (first < a || b < last) {
                        if (a < last) {
                            int c = first;
                            final int v = a - 1;
                            while (c < a) {
                                SA[isa + SA[c]] = v;
                                ++c;
                            }
                        }
                        if (b < last) {
                            int c = a;
                            final int v = b - 1;
                            while (c < b) {
                                SA[isa + SA[c]] = v;
                                ++c;
                            }
                        }
                        stack[ssize++] = new StackEntry(0, a, b, 0);
                        stack[ssize++] = new StackEntry(isaD - 1, first, last, -2);
                        if (a - first <= last - b) {
                            if (1 < a - first) {
                                stack[ssize++] = new StackEntry(isaD, b, last, trLog(last - b));
                                last = a;
                                limit = trLog(a - first);
                            }
                            else if (1 < last - b) {
                                first = b;
                                limit = trLog(last - b);
                            }
                            else {
                                if (ssize == 0) {
                                    return;
                                }
                                final StackEntry entry = stack[--ssize];
                                isaD = entry.a;
                                first = entry.b;
                                last = entry.c;
                                limit = entry.d;
                            }
                        }
                        else if (1 < last - b) {
                            stack[ssize++] = new StackEntry(isaD, first, a, trLog(a - first));
                            first = b;
                            limit = trLog(last - b);
                        }
                        else if (1 < a - first) {
                            last = a;
                            limit = trLog(a - first);
                        }
                        else {
                            if (ssize == 0) {
                                return;
                            }
                            final StackEntry entry = stack[--ssize];
                            isaD = entry.a;
                            first = entry.b;
                            last = entry.c;
                            limit = entry.d;
                        }
                    }
                    else {
                        for (int c = first; c < last; ++c) {
                            SA[isa + SA[c]] = c;
                        }
                        if (ssize == 0) {
                            return;
                        }
                        final StackEntry entry = stack[--ssize];
                        isaD = entry.a;
                        first = entry.b;
                        last = entry.c;
                        limit = entry.d;
                    }
                }
                else if (limit == -2) {
                    final int a = stack[--ssize].b;
                    final int b = stack[ssize].c;
                    this.trCopy(isa, isaN, first, a, b, last, isaD - isa);
                    if (ssize == 0) {
                        return;
                    }
                    final StackEntry entry2 = stack[--ssize];
                    isaD = entry2.a;
                    first = entry2.b;
                    last = entry2.c;
                    limit = entry2.d;
                }
                else {
                    if (0 <= SA[first]) {
                        int a = first;
                        do {
                            SA[isa + SA[a]] = a;
                        } while (++a < last && 0 <= SA[a]);
                        first = a;
                    }
                    if (first < last) {
                        int a = first;
                        do {
                            SA[a] ^= -1;
                        } while (SA[++a] < 0);
                        final int next = (SA[isa + SA[a]] != SA[isaD + SA[a]]) ? trLog(a - first + 1) : -1;
                        if (++a < last) {
                            int b = first;
                            final int v = a - 1;
                            while (b < a) {
                                SA[isa + SA[b]] = v;
                                ++b;
                            }
                        }
                        if (a - first <= last - a) {
                            stack[ssize++] = new StackEntry(isaD, a, last, -3);
                            ++isaD;
                            last = a;
                            limit = next;
                        }
                        else if (1 < last - a) {
                            stack[ssize++] = new StackEntry(isaD + 1, first, a, next);
                            first = a;
                            limit = -3;
                        }
                        else {
                            ++isaD;
                            last = a;
                            limit = next;
                        }
                    }
                    else {
                        if (ssize == 0) {
                            return;
                        }
                        final StackEntry entry2 = stack[--ssize];
                        isaD = entry2.a;
                        first = entry2.b;
                        last = entry2.c;
                        limit = entry2.d;
                    }
                }
            }
            else if (last - first <= 8) {
                if (!budget.update(size, last - first)) {
                    break;
                }
                this.trInsertionSort(isa, isaD, isaN, first, last);
                limit = -3;
            }
            else if (limit-- == 0) {
                if (!budget.update(size, last - first)) {
                    break;
                }
                this.trHeapSort(isa, isaD, isaN, first, last - first);
                int b;
                for (int a = last - 1; first < a; a = b) {
                    for (x = this.trGetC(isa, isaD, isaN, SA[a]), b = a - 1; first <= b && this.trGetC(isa, isaD, isaN, SA[b]) == x; --b) {
                        SA[b] ^= -1;
                    }
                }
                limit = -3;
            }
            else {
                int a = this.trPivot(isa, isaD, isaN, first, last);
                swapElements(SA, first, SA, a);
                int b;
                int v;
                for (v = this.trGetC(isa, isaD, isaN, SA[first]), b = first + 1; b < last && (x = this.trGetC(isa, isaD, isaN, SA[b])) == v; ++b) {}
                if ((a = b) < last && x < v) {
                    while (++b < last && (x = this.trGetC(isa, isaD, isaN, SA[b])) <= v) {
                        if (x == v) {
                            swapElements(SA, b, SA, a);
                            ++a;
                        }
                    }
                }
                int c;
                for (c = last - 1; b < c && (x = this.trGetC(isa, isaD, isaN, SA[c])) == v; --c) {}
                int d;
                if (b < (d = c) && x > v) {
                    while (b < --c && (x = this.trGetC(isa, isaD, isaN, SA[c])) >= v) {
                        if (x == v) {
                            swapElements(SA, c, SA, d);
                            --d;
                        }
                    }
                }
                while (b < c) {
                    swapElements(SA, b, SA, c);
                    while (++b < c && (x = this.trGetC(isa, isaD, isaN, SA[b])) <= v) {
                        if (x == v) {
                            swapElements(SA, b, SA, a);
                            ++a;
                        }
                    }
                    while (b < --c && (x = this.trGetC(isa, isaD, isaN, SA[c])) >= v) {
                        if (x == v) {
                            swapElements(SA, c, SA, d);
                            --d;
                        }
                    }
                }
                if (a <= d) {
                    c = b - 1;
                    int s;
                    int t;
                    if ((s = a - first) > (t = b - a)) {
                        s = t;
                    }
                    for (int e = first, f = b - s; 0 < s; --s, ++e, ++f) {
                        swapElements(SA, e, SA, f);
                    }
                    if ((s = d - c) > (t = last - d - 1)) {
                        s = t;
                    }
                    for (int e = b, f = last - s; 0 < s; --s, ++e, ++f) {
                        swapElements(SA, e, SA, f);
                    }
                    a = first + (b - a);
                    b = last - (d - c);
                    final int next = (SA[isa + SA[a]] != v) ? trLog(b - a) : -1;
                    c = first;
                    v = a - 1;
                    while (c < a) {
                        SA[isa + SA[c]] = v;
                        ++c;
                    }
                    if (b < last) {
                        c = a;
                        v = b - 1;
                        while (c < b) {
                            SA[isa + SA[c]] = v;
                            ++c;
                        }
                    }
                    if (a - first <= last - b) {
                        if (last - b <= b - a) {
                            if (1 < a - first) {
                                stack[ssize++] = new StackEntry(isaD + 1, a, b, next);
                                stack[ssize++] = new StackEntry(isaD, b, last, limit);
                                last = a;
                            }
                            else if (1 < last - b) {
                                stack[ssize++] = new StackEntry(isaD + 1, a, b, next);
                                first = b;
                            }
                            else if (1 < b - a) {
                                ++isaD;
                                first = a;
                                last = b;
                                limit = next;
                            }
                            else {
                                if (ssize == 0) {
                                    return;
                                }
                                final StackEntry entry2 = stack[--ssize];
                                isaD = entry2.a;
                                first = entry2.b;
                                last = entry2.c;
                                limit = entry2.d;
                            }
                        }
                        else if (a - first <= b - a) {
                            if (1 < a - first) {
                                stack[ssize++] = new StackEntry(isaD, b, last, limit);
                                stack[ssize++] = new StackEntry(isaD + 1, a, b, next);
                                last = a;
                            }
                            else if (1 < b - a) {
                                stack[ssize++] = new StackEntry(isaD, b, last, limit);
                                ++isaD;
                                first = a;
                                last = b;
                                limit = next;
                            }
                            else {
                                first = b;
                            }
                        }
                        else if (1 < b - a) {
                            stack[ssize++] = new StackEntry(isaD, b, last, limit);
                            stack[ssize++] = new StackEntry(isaD, first, a, limit);
                            ++isaD;
                            first = a;
                            last = b;
                            limit = next;
                        }
                        else {
                            stack[ssize++] = new StackEntry(isaD, b, last, limit);
                            last = a;
                        }
                    }
                    else if (a - first <= b - a) {
                        if (1 < last - b) {
                            stack[ssize++] = new StackEntry(isaD + 1, a, b, next);
                            stack[ssize++] = new StackEntry(isaD, first, a, limit);
                            first = b;
                        }
                        else if (1 < a - first) {
                            stack[ssize++] = new StackEntry(isaD + 1, a, b, next);
                            last = a;
                        }
                        else if (1 < b - a) {
                            ++isaD;
                            first = a;
                            last = b;
                            limit = next;
                        }
                        else {
                            stack[ssize++] = new StackEntry(isaD, first, last, limit);
                        }
                    }
                    else if (last - b <= b - a) {
                        if (1 < last - b) {
                            stack[ssize++] = new StackEntry(isaD, first, a, limit);
                            stack[ssize++] = new StackEntry(isaD + 1, a, b, next);
                            first = b;
                        }
                        else if (1 < b - a) {
                            stack[ssize++] = new StackEntry(isaD, first, a, limit);
                            ++isaD;
                            first = a;
                            last = b;
                            limit = next;
                        }
                        else {
                            last = a;
                        }
                    }
                    else if (1 < b - a) {
                        stack[ssize++] = new StackEntry(isaD, first, a, limit);
                        stack[ssize++] = new StackEntry(isaD, b, last, limit);
                        ++isaD;
                        first = a;
                        last = b;
                        limit = next;
                    }
                    else {
                        stack[ssize++] = new StackEntry(isaD, first, a, limit);
                        first = b;
                    }
                }
                else {
                    if (!budget.update(size, last - first)) {
                        break;
                    }
                    ++limit;
                    ++isaD;
                }
            }
        }
        for (int s = 0; s < ssize; ++s) {
            if (stack[s].d == -3) {
                this.lsUpdateGroup(isa, stack[s].b, stack[s].c);
            }
        }
    }
    
    private void trSort(final int isa, final int n, final int depth) {
        final int[] SA = this.SA;
        int first = 0;
        if (-n < SA[0]) {
            final TRBudget budget = new TRBudget(n, trLog(n) * 2 / 3 + 1);
            do {
                final int t;
                if ((t = SA[first]) < 0) {
                    first -= t;
                }
                else {
                    final int last = SA[isa + t] + 1;
                    if (1 < last - first) {
                        this.trIntroSort(isa, isa + depth, isa + n, first, last, budget, n);
                        if (budget.chance == 0) {
                            if (0 < first) {
                                SA[0] = -first;
                            }
                            this.lsSort(isa, n, depth);
                            break;
                        }
                    }
                    first = last;
                }
            } while (first < n);
        }
    }
    
    private static int BUCKET_B(final int c0, final int c1) {
        return c1 << 8 | c0;
    }
    
    private static int BUCKET_BSTAR(final int c0, final int c1) {
        return c0 << 8 | c1;
    }
    
    private int sortTypeBstar(final int[] bucketA, final int[] bucketB) {
        final byte[] T = this.T;
        final int[] SA = this.SA;
        final int n = this.n;
        final int[] tempbuf = new int[256];
        int i = 1;
        int flag = 1;
        while (i < n) {
            if (T[i - 1] != T[i]) {
                if ((T[i - 1] & 0xFF) > (T[i] & 0xFF)) {
                    flag = 0;
                    break;
                }
                break;
            }
            else {
                ++i;
            }
        }
        i = n - 1;
        int m = n;
        int ti;
        final int t0;
        if ((ti = (T[i] & 0xFF)) < (t0 = (T[0] & 0xFF)) || (T[i] == T[0] && flag != 0)) {
            if (flag == 0) {
                final int bucket_BSTAR = BUCKET_BSTAR(ti, t0);
                ++bucketB[bucket_BSTAR];
                SA[--m] = i;
            }
            else {
                final int bucket_B = BUCKET_B(ti, t0);
                ++bucketB[bucket_B];
            }
            --i;
            int ti2;
            while (0 <= i && (ti = (T[i] & 0xFF)) <= (ti2 = (T[i + 1] & 0xFF))) {
                final int bucket_B2 = BUCKET_B(ti, ti2);
                ++bucketB[bucket_B2];
                --i;
            }
        }
        while (0 <= i) {
            do {
                final int n2 = T[i] & 0xFF;
                ++bucketA[n2];
            } while (0 <= --i && (T[i] & 0xFF) >= (T[i + 1] & 0xFF));
            if (0 <= i) {
                final int bucket_BSTAR2 = BUCKET_BSTAR(T[i] & 0xFF, T[i + 1] & 0xFF);
                ++bucketB[bucket_BSTAR2];
                SA[--m] = i;
                --i;
                int ti2;
                while (0 <= i && (ti = (T[i] & 0xFF)) <= (ti2 = (T[i + 1] & 0xFF))) {
                    final int bucket_B3 = BUCKET_B(ti, ti2);
                    ++bucketB[bucket_B3];
                    --i;
                }
            }
        }
        m = n - m;
        if (m == 0) {
            for (i = 0; i < n; ++i) {
                SA[i] = i;
            }
            return 0;
        }
        int c0 = 0;
        i = -1;
        int j = 0;
        while (c0 < 256) {
            final int t2 = i + bucketA[c0];
            bucketA[c0] = i + j;
            i = t2 + bucketB[BUCKET_B(c0, c0)];
            for (int c2 = c0 + 1; c2 < 256; ++c2) {
                j += bucketB[BUCKET_BSTAR(c0, c2)];
                bucketB[c0 << 8 | c2] = j;
                i += bucketB[BUCKET_B(c0, c2)];
            }
            ++c0;
        }
        final int PAb = n - m;
        final int ISAb = m;
        for (i = m - 2; 0 <= i; --i) {
            final int t2 = SA[PAb + i];
            c0 = (T[t2] & 0xFF);
            final int c2 = T[t2 + 1] & 0xFF;
            final int[] array = SA;
            final int bucket_BSTAR3 = BUCKET_BSTAR(c0, c2);
            array[--bucketB[bucket_BSTAR3]] = i;
        }
        int t2 = SA[PAb + m - 1];
        c0 = (T[t2] & 0xFF);
        int c2 = T[t2 + 1] & 0xFF;
        final int[] array2 = SA;
        final int bucket_BSTAR4 = BUCKET_BSTAR(c0, c2);
        array2[--bucketB[bucket_BSTAR4]] = m - 1;
        int[] buf = SA;
        int bufoffset = m;
        int bufsize = n - 2 * m;
        if (bufsize <= 256) {
            buf = tempbuf;
            bufoffset = 0;
            bufsize = 256;
        }
        c0 = 255;
        j = m;
        while (0 < j) {
            for (int c2 = 255; c0 < c2; --c2) {
                i = bucketB[BUCKET_BSTAR(c0, c2)];
                if (1 < j - i) {
                    this.subStringSort(PAb, i, j, buf, bufoffset, bufsize, 2, SA[i] == m - 1, n);
                }
                j = i;
            }
            --c0;
        }
        for (i = m - 1; 0 <= i; --i) {
            if (0 <= SA[i]) {
                j = i;
                do {
                    SA[ISAb + SA[i]] = i;
                } while (0 <= --i && 0 <= SA[i]);
                SA[i + 1] = i - j;
                if (i <= 0) {
                    break;
                }
            }
            j = i;
            do {
                SA[ISAb + (SA[i] ^= -1)] = j;
            } while (SA[--i] < 0);
            SA[ISAb + SA[i]] = j;
        }
        this.trSort(ISAb, m, 1);
        i = n - 1;
        j = m;
        if ((T[i] & 0xFF) < (T[0] & 0xFF) || (T[i] == T[0] && flag != 0)) {
            if (flag == 0) {
                SA[SA[ISAb + --j]] = i;
            }
            --i;
            while (0 <= i && (T[i] & 0xFF) <= (T[i + 1] & 0xFF)) {
                --i;
            }
        }
        while (0 <= i) {
            --i;
            while (0 <= i && (T[i] & 0xFF) >= (T[i + 1] & 0xFF)) {
                --i;
            }
            if (0 <= i) {
                SA[SA[ISAb + --j]] = i;
                --i;
                while (0 <= i && (T[i] & 0xFF) <= (T[i + 1] & 0xFF)) {
                    --i;
                }
            }
        }
        c0 = 255;
        i = n - 1;
        int k = m - 1;
        while (0 <= c0) {
            for (int c2 = 255; c0 < c2; --c2) {
                t2 = i - bucketB[BUCKET_B(c0, c2)];
                bucketB[BUCKET_B(c0, c2)] = i + 1;
                i = t2;
                for (j = bucketB[BUCKET_BSTAR(c0, c2)]; j <= k; --k) {
                    SA[i] = SA[k];
                    --i;
                }
            }
            t2 = i - bucketB[BUCKET_B(c0, c0)];
            bucketB[BUCKET_B(c0, c0)] = i + 1;
            if (c0 < 255) {
                bucketB[BUCKET_BSTAR(c0, c0 + 1)] = t2 + 1;
            }
            i = bucketA[c0];
            --c0;
        }
        return m;
    }
    
    private int constructBWT(final int[] bucketA, final int[] bucketB) {
        final byte[] T = this.T;
        final int[] SA = this.SA;
        final int n = this.n;
        int t = 0;
        int c2 = 0;
        int orig = -1;
        for (int c3 = 254; 0 <= c3; --c3) {
            final int i = bucketB[BUCKET_BSTAR(c3, c3 + 1)];
            int j = bucketA[c3 + 1];
            t = 0;
            c2 = -1;
            while (i <= j) {
                int s2;
                final int s1;
                if (0 <= (s1 = (s2 = SA[j]))) {
                    if (--s2 < 0) {
                        s2 = n - 1;
                    }
                    final int c4;
                    if ((c4 = (T[s2] & 0xFF)) <= c3) {
                        SA[j] = ~s1;
                        if (0 < s2 && (T[s2 - 1] & 0xFF) > c4) {
                            s2 ^= -1;
                        }
                        if (c2 == c4) {
                            SA[--t] = s2;
                        }
                        else {
                            if (0 <= c2) {
                                bucketB[BUCKET_B(c2, c3)] = t;
                            }
                            SA[t = bucketB[BUCKET_B(c2 = c4, c3)] - 1] = s2;
                        }
                    }
                }
                else {
                    SA[j] = ~s2;
                }
                --j;
            }
        }
        for (int i = 0; i < n; ++i) {
            int s2;
            int s1;
            if (0 <= (s1 = (s2 = SA[i]))) {
                if (--s2 < 0) {
                    s2 = n - 1;
                }
                final int c4;
                if ((c4 = (T[s2] & 0xFF)) >= (T[s2 + 1] & 0xFF)) {
                    if (0 < s2 && (T[s2 - 1] & 0xFF) < c4) {
                        s2 ^= -1;
                    }
                    if (c4 == c2) {
                        SA[++t] = s2;
                    }
                    else {
                        if (c2 != -1) {
                            bucketA[c2] = t;
                        }
                        SA[t = bucketA[c2 = c4] + 1] = s2;
                    }
                }
            }
            else {
                s1 ^= -1;
            }
            if (s1 == 0) {
                SA[i] = T[n - 1];
                orig = i;
            }
            else {
                SA[i] = T[s1 - 1];
            }
        }
        return orig;
    }
    
    public int bwt() {
        final int[] SA = this.SA;
        final byte[] T = this.T;
        final int n = this.n;
        final int[] bucketA = new int[256];
        final int[] bucketB = new int[65536];
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            SA[0] = T[0];
            return 0;
        }
        final int m = this.sortTypeBstar(bucketA, bucketB);
        if (0 < m) {
            return this.constructBWT(bucketA, bucketB);
        }
        return 0;
    }
    
    static {
        LOG_2_TABLE = new int[] { -1, 0, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 };
    }
    
    private static class StackEntry
    {
        final int a;
        final int b;
        final int c;
        final int d;
        
        StackEntry(final int a, final int b, final int c, final int d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }
    }
    
    private static class PartitionResult
    {
        final int first;
        final int last;
        
        PartitionResult(final int first, final int last) {
            this.first = first;
            this.last = last;
        }
    }
    
    private static class TRBudget
    {
        int budget;
        int chance;
        
        TRBudget(final int budget, final int chance) {
            this.budget = budget;
            this.chance = chance;
        }
        
        boolean update(final int size, final int n) {
            this.budget -= n;
            if (this.budget <= 0) {
                if (--this.chance == 0) {
                    return false;
                }
                this.budget += size;
            }
            return true;
        }
    }
}
