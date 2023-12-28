// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.HashCommon;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.Collection;
import java.util.Comparator;
import java.io.Serializable;

public class FloatRBTreeSet extends AbstractFloatSortedSet implements Serializable, Cloneable, FloatSortedSet
{
    protected transient Entry tree;
    protected int count;
    protected transient Entry firstEntry;
    protected transient Entry lastEntry;
    protected Comparator<? super Float> storedComparator;
    protected transient FloatComparator actualComparator;
    private static final long serialVersionUID = -7046029254386353130L;
    private static final boolean ASSERTS = false;
    private transient boolean[] dirPath;
    private transient Entry[] nodePath;
    
    public FloatRBTreeSet() {
        this.allocatePaths();
        this.tree = null;
        this.count = 0;
    }
    
    private void setActualComparator() {
        if (this.storedComparator == null || this.storedComparator instanceof FloatComparator) {
            this.actualComparator = (FloatComparator)this.storedComparator;
        }
        else {
            this.actualComparator = new FloatComparator() {
                @Override
                public int compare(final float k1, final float k2) {
                    return FloatRBTreeSet.this.storedComparator.compare(k1, k2);
                }
                
                @Override
                public int compare(final Float ok1, final Float ok2) {
                    return FloatRBTreeSet.this.storedComparator.compare(ok1, ok2);
                }
            };
        }
    }
    
    public FloatRBTreeSet(final Comparator<? super Float> c) {
        this();
        this.storedComparator = c;
        this.setActualComparator();
    }
    
    public FloatRBTreeSet(final Collection<? extends Float> c) {
        this();
        this.addAll(c);
    }
    
    public FloatRBTreeSet(final SortedSet<Float> s) {
        this(s.comparator());
        this.addAll(s);
    }
    
    public FloatRBTreeSet(final FloatCollection c) {
        this();
        this.addAll(c);
    }
    
    public FloatRBTreeSet(final FloatSortedSet s) {
        this(s.comparator());
        this.addAll(s);
    }
    
    public FloatRBTreeSet(final FloatIterator i) {
        this.allocatePaths();
        while (i.hasNext()) {
            this.add(i.nextFloat());
        }
    }
    
    public FloatRBTreeSet(final Iterator<?> i) {
        this(FloatIterators.asFloatIterator(i));
    }
    
    public FloatRBTreeSet(final float[] a, final int offset, final int length, final Comparator<? super Float> c) {
        this(c);
        FloatArrays.ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; ++i) {
            this.add(a[offset + i]);
        }
    }
    
    public FloatRBTreeSet(final float[] a, final int offset, final int length) {
        this(a, offset, length, null);
    }
    
    public FloatRBTreeSet(final float[] a) {
        this();
        int i = a.length;
        while (i-- != 0) {
            this.add(a[i]);
        }
    }
    
    public FloatRBTreeSet(final float[] a, final Comparator<? super Float> c) {
        this(c);
        int i = a.length;
        while (i-- != 0) {
            this.add(a[i]);
        }
    }
    
    final int compare(final float k1, final float k2) {
        return (this.actualComparator == null) ? Float.compare(k1, k2) : this.actualComparator.compare(k1, k2);
    }
    
    private Entry findKey(final float k) {
        Entry e;
        int cmp;
        for (e = this.tree; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {}
        return e;
    }
    
    final Entry locateKey(final float k) {
        Entry e = this.tree;
        Entry last = this.tree;
        int cmp;
        for (cmp = 0; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {
            last = e;
        }
        return (cmp == 0) ? e : last;
    }
    
    private void allocatePaths() {
        this.dirPath = new boolean[64];
        this.nodePath = new Entry[64];
    }
    
    @Override
    public boolean add(final float k) {
        int maxDepth = 0;
        Label_0876: {
            if (this.tree != null) {
                Entry p = this.tree;
                int i = 0;
                int cmp;
                while ((cmp = this.compare(k, p.key)) != 0) {
                    this.nodePath[i] = p;
                    final boolean[] dirPath = this.dirPath;
                    final int n = i++;
                    final boolean b = cmp > 0;
                    dirPath[n] = b;
                    if (b) {
                        if (!p.succ()) {
                            p = p.right;
                            continue;
                        }
                        ++this.count;
                        final Entry e = new Entry(k);
                        if (p.right == null) {
                            this.lastEntry = e;
                        }
                        e.left = p;
                        e.right = p.right;
                        p.right(e);
                    }
                    else {
                        if (!p.pred()) {
                            p = p.left;
                            continue;
                        }
                        ++this.count;
                        final Entry e = new Entry(k);
                        if (p.left == null) {
                            this.firstEntry = e;
                        }
                        e.right = p;
                        e.left = p.left;
                        p.left(e);
                    }
                    maxDepth = i--;
                    while (i > 0 && !this.nodePath[i].black()) {
                        if (!this.dirPath[i - 1]) {
                            Entry y = this.nodePath[i - 1].right;
                            if (!this.nodePath[i - 1].succ() && !y.black()) {
                                this.nodePath[i].black(true);
                                y.black(true);
                                this.nodePath[i - 1].black(false);
                                i -= 2;
                            }
                            else {
                                if (!this.dirPath[i]) {
                                    y = this.nodePath[i];
                                }
                                else {
                                    final Entry x = this.nodePath[i];
                                    y = x.right;
                                    x.right = y.left;
                                    y.left = x;
                                    this.nodePath[i - 1].left = y;
                                    if (y.pred()) {
                                        y.pred(false);
                                        x.succ(y);
                                    }
                                }
                                final Entry x = this.nodePath[i - 1];
                                x.black(false);
                                y.black(true);
                                x.left = y.right;
                                y.right = x;
                                if (i < 2) {
                                    this.tree = y;
                                }
                                else if (this.dirPath[i - 2]) {
                                    this.nodePath[i - 2].right = y;
                                }
                                else {
                                    this.nodePath[i - 2].left = y;
                                }
                                if (y.succ()) {
                                    y.succ(false);
                                    x.pred(y);
                                    break;
                                }
                                break;
                            }
                        }
                        else {
                            Entry y = this.nodePath[i - 1].left;
                            if (!this.nodePath[i - 1].pred() && !y.black()) {
                                this.nodePath[i].black(true);
                                y.black(true);
                                this.nodePath[i - 1].black(false);
                                i -= 2;
                            }
                            else {
                                if (this.dirPath[i]) {
                                    y = this.nodePath[i];
                                }
                                else {
                                    final Entry x = this.nodePath[i];
                                    y = x.left;
                                    x.left = y.right;
                                    y.right = x;
                                    this.nodePath[i - 1].right = y;
                                    if (y.succ()) {
                                        y.succ(false);
                                        x.pred(y);
                                    }
                                }
                                final Entry x = this.nodePath[i - 1];
                                x.black(false);
                                y.black(true);
                                x.right = y.left;
                                y.left = x;
                                if (i < 2) {
                                    this.tree = y;
                                }
                                else if (this.dirPath[i - 2]) {
                                    this.nodePath[i - 2].right = y;
                                }
                                else {
                                    this.nodePath[i - 2].left = y;
                                }
                                if (y.pred()) {
                                    y.pred(false);
                                    x.succ(y);
                                    break;
                                }
                                break;
                            }
                        }
                    }
                    break Label_0876;
                }
                while (i-- != 0) {
                    this.nodePath[i] = null;
                }
                return false;
            }
            ++this.count;
            final Entry tree = new Entry(k);
            this.firstEntry = tree;
            this.lastEntry = tree;
            this.tree = tree;
        }
        this.tree.black(true);
        while (maxDepth-- != 0) {
            this.nodePath[maxDepth] = null;
        }
        return true;
    }
    
    @Override
    public boolean rem(final float k) {
        if (this.tree == null) {
            return false;
        }
        Entry p = this.tree;
        int i = 0;
        final float kk = k;
        int cmp;
        while ((cmp = this.compare(kk, p.key)) != 0) {
            this.dirPath[i] = (cmp > 0);
            this.nodePath[i] = p;
            if (this.dirPath[i++]) {
                if ((p = p.right()) == null) {
                    while (i-- != 0) {
                        this.nodePath[i] = null;
                    }
                    return false;
                }
                continue;
            }
            else {
                if ((p = p.left()) == null) {
                    while (i-- != 0) {
                        this.nodePath[i] = null;
                    }
                    return false;
                }
                continue;
            }
        }
        if (p.left == null) {
            this.firstEntry = p.next();
        }
        if (p.right == null) {
            this.lastEntry = p.prev();
        }
        if (p.succ()) {
            if (p.pred()) {
                if (i == 0) {
                    this.tree = p.left;
                }
                else if (this.dirPath[i - 1]) {
                    this.nodePath[i - 1].succ(p.right);
                }
                else {
                    this.nodePath[i - 1].pred(p.left);
                }
            }
            else {
                p.prev().right = p.right;
                if (i == 0) {
                    this.tree = p.left;
                }
                else if (this.dirPath[i - 1]) {
                    this.nodePath[i - 1].right = p.left;
                }
                else {
                    this.nodePath[i - 1].left = p.left;
                }
            }
        }
        else {
            Entry r = p.right;
            if (r.pred()) {
                r.left = p.left;
                r.pred(p.pred());
                if (!r.pred()) {
                    r.prev().right = r;
                }
                if (i == 0) {
                    this.tree = r;
                }
                else if (this.dirPath[i - 1]) {
                    this.nodePath[i - 1].right = r;
                }
                else {
                    this.nodePath[i - 1].left = r;
                }
                final boolean color = r.black();
                r.black(p.black());
                p.black(color);
                this.dirPath[i] = true;
                this.nodePath[i++] = r;
            }
            else {
                final int j = i++;
                Entry s;
                while (true) {
                    this.dirPath[i] = false;
                    this.nodePath[i++] = r;
                    s = r.left;
                    if (s.pred()) {
                        break;
                    }
                    r = s;
                }
                this.dirPath[j] = true;
                this.nodePath[j] = s;
                if (s.succ()) {
                    r.pred(s);
                }
                else {
                    r.left = s.right;
                }
                s.left = p.left;
                if (!p.pred()) {
                    (p.prev().right = s).pred(false);
                }
                s.right(p.right);
                final boolean color = s.black();
                s.black(p.black());
                p.black(color);
                if (j == 0) {
                    this.tree = s;
                }
                else if (this.dirPath[j - 1]) {
                    this.nodePath[j - 1].right = s;
                }
                else {
                    this.nodePath[j - 1].left = s;
                }
            }
        }
        int maxDepth = i;
        if (p.black()) {
            while (i > 0) {
                if ((this.dirPath[i - 1] && !this.nodePath[i - 1].succ()) || (!this.dirPath[i - 1] && !this.nodePath[i - 1].pred())) {
                    final Entry x = this.dirPath[i - 1] ? this.nodePath[i - 1].right : this.nodePath[i - 1].left;
                    if (!x.black()) {
                        x.black(true);
                        break;
                    }
                }
                if (!this.dirPath[i - 1]) {
                    Entry w = this.nodePath[i - 1].right;
                    if (!w.black()) {
                        w.black(true);
                        this.nodePath[i - 1].black(false);
                        this.nodePath[i - 1].right = w.left;
                        w.left = this.nodePath[i - 1];
                        if (i < 2) {
                            this.tree = w;
                        }
                        else if (this.dirPath[i - 2]) {
                            this.nodePath[i - 2].right = w;
                        }
                        else {
                            this.nodePath[i - 2].left = w;
                        }
                        this.nodePath[i] = this.nodePath[i - 1];
                        this.dirPath[i] = false;
                        this.nodePath[i - 1] = w;
                        if (maxDepth == i++) {
                            ++maxDepth;
                        }
                        w = this.nodePath[i - 1].right;
                    }
                    if ((w.pred() || w.left.black()) && (w.succ() || w.right.black())) {
                        w.black(false);
                    }
                    else {
                        if (w.succ() || w.right.black()) {
                            final Entry y = w.left;
                            y.black(true);
                            w.black(false);
                            w.left = y.right;
                            y.right = w;
                            final Entry entry = this.nodePath[i - 1];
                            final Entry right = y;
                            entry.right = right;
                            w = right;
                            if (w.succ()) {
                                w.succ(false);
                                w.right.pred(w);
                            }
                        }
                        w.black(this.nodePath[i - 1].black());
                        this.nodePath[i - 1].black(true);
                        w.right.black(true);
                        this.nodePath[i - 1].right = w.left;
                        w.left = this.nodePath[i - 1];
                        if (i < 2) {
                            this.tree = w;
                        }
                        else if (this.dirPath[i - 2]) {
                            this.nodePath[i - 2].right = w;
                        }
                        else {
                            this.nodePath[i - 2].left = w;
                        }
                        if (w.pred()) {
                            w.pred(false);
                            this.nodePath[i - 1].succ(w);
                            break;
                        }
                        break;
                    }
                }
                else {
                    Entry w = this.nodePath[i - 1].left;
                    if (!w.black()) {
                        w.black(true);
                        this.nodePath[i - 1].black(false);
                        this.nodePath[i - 1].left = w.right;
                        w.right = this.nodePath[i - 1];
                        if (i < 2) {
                            this.tree = w;
                        }
                        else if (this.dirPath[i - 2]) {
                            this.nodePath[i - 2].right = w;
                        }
                        else {
                            this.nodePath[i - 2].left = w;
                        }
                        this.nodePath[i] = this.nodePath[i - 1];
                        this.dirPath[i] = true;
                        this.nodePath[i - 1] = w;
                        if (maxDepth == i++) {
                            ++maxDepth;
                        }
                        w = this.nodePath[i - 1].left;
                    }
                    if ((w.pred() || w.left.black()) && (w.succ() || w.right.black())) {
                        w.black(false);
                    }
                    else {
                        if (w.pred() || w.left.black()) {
                            final Entry y = w.right;
                            y.black(true);
                            w.black(false);
                            w.right = y.left;
                            y.left = w;
                            final Entry entry2 = this.nodePath[i - 1];
                            final Entry left = y;
                            entry2.left = left;
                            w = left;
                            if (w.pred()) {
                                w.pred(false);
                                w.left.succ(w);
                            }
                        }
                        w.black(this.nodePath[i - 1].black());
                        this.nodePath[i - 1].black(true);
                        w.left.black(true);
                        this.nodePath[i - 1].left = w.right;
                        w.right = this.nodePath[i - 1];
                        if (i < 2) {
                            this.tree = w;
                        }
                        else if (this.dirPath[i - 2]) {
                            this.nodePath[i - 2].right = w;
                        }
                        else {
                            this.nodePath[i - 2].left = w;
                        }
                        if (w.succ()) {
                            w.succ(false);
                            this.nodePath[i - 1].pred(w);
                            break;
                        }
                        break;
                    }
                }
                --i;
            }
            if (this.tree != null) {
                this.tree.black(true);
            }
        }
        --this.count;
        while (maxDepth-- != 0) {
            this.nodePath[maxDepth] = null;
        }
        return true;
    }
    
    @Override
    public boolean contains(final float k) {
        return this.findKey(k) != null;
    }
    
    @Override
    public void clear() {
        this.count = 0;
        this.tree = null;
        final Entry entry = null;
        this.lastEntry = entry;
        this.firstEntry = entry;
    }
    
    @Override
    public int size() {
        return this.count;
    }
    
    @Override
    public boolean isEmpty() {
        return this.count == 0;
    }
    
    @Override
    public float firstFloat() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }
    
    @Override
    public float lastFloat() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }
    
    @Override
    public FloatBidirectionalIterator iterator() {
        return new SetIterator();
    }
    
    @Override
    public FloatBidirectionalIterator iterator(final float from) {
        return new SetIterator(from);
    }
    
    @Override
    public FloatComparator comparator() {
        return this.actualComparator;
    }
    
    @Override
    public FloatSortedSet headSet(final float to) {
        return new Subset(0.0f, true, to, false);
    }
    
    @Override
    public FloatSortedSet tailSet(final float from) {
        return new Subset(from, false, 0.0f, true);
    }
    
    @Override
    public FloatSortedSet subSet(final float from, final float to) {
        return new Subset(from, false, to, false);
    }
    
    public Object clone() {
        FloatRBTreeSet c;
        try {
            c = (FloatRBTreeSet)super.clone();
        }
        catch (final CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.allocatePaths();
        if (this.count != 0) {
            final Entry rp = new Entry();
            final Entry rq = new Entry();
            Entry p = rp;
            rp.left(this.tree);
            Entry q = rq;
            rq.pred(null);
        Block_4:
            while (true) {
                if (!p.pred()) {
                    final Entry e = p.left.clone();
                    e.pred(q.left);
                    e.succ(q);
                    q.left(e);
                    p = p.left;
                    q = q.left;
                }
                else {
                    while (p.succ()) {
                        p = p.right;
                        if (p == null) {
                            break Block_4;
                        }
                        q = q.right;
                    }
                    p = p.right;
                    q = q.right;
                }
                if (!p.succ()) {
                    final Entry e = p.right.clone();
                    e.succ(q.right);
                    e.pred(q);
                    q.right(e);
                }
            }
            q.right = null;
            c.tree = rq.left;
            c.firstEntry = c.tree;
            while (c.firstEntry.left != null) {
                c.firstEntry = c.firstEntry.left;
            }
            c.lastEntry = c.tree;
            while (c.lastEntry.right != null) {
                c.lastEntry = c.lastEntry.right;
            }
            return c;
        }
        return c;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        int n = this.count;
        final SetIterator i = new SetIterator();
        s.defaultWriteObject();
        while (n-- != 0) {
            s.writeFloat(i.nextFloat());
        }
    }
    
    private Entry readTree(final ObjectInputStream s, final int n, final Entry pred, final Entry succ) throws IOException, ClassNotFoundException {
        if (n == 1) {
            final Entry top = new Entry(s.readFloat());
            top.pred(pred);
            top.succ(succ);
            top.black(true);
            return top;
        }
        if (n == 2) {
            final Entry top = new Entry(s.readFloat());
            top.black(true);
            top.right(new Entry(s.readFloat()));
            top.right.pred(top);
            top.pred(pred);
            top.right.succ(succ);
            return top;
        }
        final int rightN = n / 2;
        final int leftN = n - rightN - 1;
        final Entry top2 = new Entry();
        top2.left(this.readTree(s, leftN, pred, top2));
        top2.key = s.readFloat();
        top2.black(true);
        top2.right(this.readTree(s, rightN, top2, succ));
        if (n + 2 == (n + 2 & -(n + 2))) {
            top2.right.black(false);
        }
        return top2;
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.setActualComparator();
        this.allocatePaths();
        if (this.count != 0) {
            this.tree = this.readTree(s, this.count, null, null);
            Entry e;
            for (e = this.tree; e.left() != null; e = e.left()) {}
            this.firstEntry = e;
            for (e = this.tree; e.right() != null; e = e.right()) {}
            this.lastEntry = e;
        }
    }
    
    private void checkNodePath() {
    }
    
    private int checkTree(final Entry e, final int d, final int D) {
        return 0;
    }
    
    private static final class Entry implements Cloneable
    {
        private static final int BLACK_MASK = 1;
        private static final int SUCC_MASK = Integer.MIN_VALUE;
        private static final int PRED_MASK = 1073741824;
        float key;
        Entry left;
        Entry right;
        int info;
        
        Entry() {
        }
        
        Entry(final float k) {
            this.key = k;
            this.info = -1073741824;
        }
        
        Entry left() {
            return ((this.info & 0x40000000) != 0x0) ? null : this.left;
        }
        
        Entry right() {
            return ((this.info & Integer.MIN_VALUE) != 0x0) ? null : this.right;
        }
        
        boolean pred() {
            return (this.info & 0x40000000) != 0x0;
        }
        
        boolean succ() {
            return (this.info & Integer.MIN_VALUE) != 0x0;
        }
        
        void pred(final boolean pred) {
            if (pred) {
                this.info |= 0x40000000;
            }
            else {
                this.info &= 0xBFFFFFFF;
            }
        }
        
        void succ(final boolean succ) {
            if (succ) {
                this.info |= Integer.MIN_VALUE;
            }
            else {
                this.info &= Integer.MAX_VALUE;
            }
        }
        
        void pred(final Entry pred) {
            this.info |= 0x40000000;
            this.left = pred;
        }
        
        void succ(final Entry succ) {
            this.info |= Integer.MIN_VALUE;
            this.right = succ;
        }
        
        void left(final Entry left) {
            this.info &= 0xBFFFFFFF;
            this.left = left;
        }
        
        void right(final Entry right) {
            this.info &= Integer.MAX_VALUE;
            this.right = right;
        }
        
        boolean black() {
            return (this.info & 0x1) != 0x0;
        }
        
        void black(final boolean black) {
            if (black) {
                this.info |= 0x1;
            }
            else {
                this.info &= 0xFFFFFFFE;
            }
        }
        
        Entry next() {
            Entry next = this.right;
            if ((this.info & Integer.MIN_VALUE) == 0x0) {
                while ((next.info & 0x40000000) == 0x0) {
                    next = next.left;
                }
            }
            return next;
        }
        
        Entry prev() {
            Entry prev = this.left;
            if ((this.info & 0x40000000) == 0x0) {
                while ((prev.info & Integer.MIN_VALUE) == 0x0) {
                    prev = prev.right;
                }
            }
            return prev;
        }
        
        public Entry clone() {
            Entry c;
            try {
                c = (Entry)super.clone();
            }
            catch (final CloneNotSupportedException cantHappen) {
                throw new InternalError();
            }
            c.key = this.key;
            c.info = this.info;
            return c;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }
            final Entry e = (Entry)o;
            return Float.floatToIntBits(this.key) == Float.floatToIntBits(e.key);
        }
        
        @Override
        public int hashCode() {
            return HashCommon.float2int(this.key);
        }
        
        @Override
        public String toString() {
            return String.valueOf(this.key);
        }
    }
    
    private class SetIterator extends AbstractFloatListIterator
    {
        Entry prev;
        Entry next;
        Entry curr;
        int index;
        
        SetIterator() {
            this.index = 0;
            this.next = FloatRBTreeSet.this.firstEntry;
        }
        
        SetIterator(final float k) {
            this.index = 0;
            final Entry locateKey = FloatRBTreeSet.this.locateKey(k);
            this.next = locateKey;
            if (locateKey != null) {
                if (FloatRBTreeSet.this.compare(this.next.key, k) <= 0) {
                    this.prev = this.next;
                    this.next = this.next.next();
                }
                else {
                    this.prev = this.next.prev();
                }
            }
        }
        
        @Override
        public boolean hasNext() {
            return this.next != null;
        }
        
        @Override
        public boolean hasPrevious() {
            return this.prev != null;
        }
        
        void updateNext() {
            this.next = this.next.next();
        }
        
        Entry nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final Entry next = this.next;
            this.prev = next;
            this.curr = next;
            ++this.index;
            this.updateNext();
            return this.curr;
        }
        
        @Override
        public float nextFloat() {
            return this.nextEntry().key;
        }
        
        @Override
        public float previousFloat() {
            return this.previousEntry().key;
        }
        
        void updatePrevious() {
            this.prev = this.prev.prev();
        }
        
        Entry previousEntry() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final Entry prev = this.prev;
            this.next = prev;
            this.curr = prev;
            --this.index;
            this.updatePrevious();
            return this.curr;
        }
        
        @Override
        public int nextIndex() {
            return this.index;
        }
        
        @Override
        public int previousIndex() {
            return this.index - 1;
        }
        
        @Override
        public void remove() {
            if (this.curr == null) {
                throw new IllegalStateException();
            }
            if (this.curr == this.prev) {
                --this.index;
            }
            final Entry curr = this.curr;
            this.prev = curr;
            this.next = curr;
            this.updatePrevious();
            this.updateNext();
            FloatRBTreeSet.this.rem(this.curr.key);
            this.curr = null;
        }
    }
    
    private final class Subset extends AbstractFloatSortedSet implements Serializable, FloatSortedSet
    {
        private static final long serialVersionUID = -7046029254386353129L;
        float from;
        float to;
        boolean bottom;
        boolean top;
        final /* synthetic */ FloatRBTreeSet this$0;
        
        public Subset(final float from, final boolean bottom, final float to, final boolean top) {
            if (!bottom && !top && FloatRBTreeSet.this.compare(from, to) > 0) {
                throw new IllegalArgumentException("Start element (" + from + ") is larger than end element (" + to + ")");
            }
            this.from = from;
            this.bottom = bottom;
            this.to = to;
            this.top = top;
        }
        
        @Override
        public void clear() {
            final SubsetIterator i = new SubsetIterator();
            while (i.hasNext()) {
                i.nextFloat();
                i.remove();
            }
        }
        
        final boolean in(final float k) {
            return (this.bottom || FloatRBTreeSet.this.compare(k, this.from) >= 0) && (this.top || FloatRBTreeSet.this.compare(k, this.to) < 0);
        }
        
        @Override
        public boolean contains(final float k) {
            return this.in(k) && FloatRBTreeSet.this.contains(k);
        }
        
        @Override
        public boolean add(final float k) {
            if (!this.in(k)) {
                throw new IllegalArgumentException("Element (" + k + ") out of range [" + (this.bottom ? "-" : String.valueOf(this.from)) + ", " + (this.top ? "-" : String.valueOf(this.to)) + ")");
            }
            return FloatRBTreeSet.this.add(k);
        }
        
        @Override
        public boolean rem(final float k) {
            return this.in(k) && FloatRBTreeSet.this.rem(k);
        }
        
        @Override
        public int size() {
            final SubsetIterator i = new SubsetIterator();
            int n = 0;
            while (i.hasNext()) {
                ++n;
                i.nextFloat();
            }
            return n;
        }
        
        @Override
        public boolean isEmpty() {
            return !new SubsetIterator().hasNext();
        }
        
        @Override
        public FloatComparator comparator() {
            return FloatRBTreeSet.this.actualComparator;
        }
        
        @Override
        public FloatBidirectionalIterator iterator() {
            return new SubsetIterator();
        }
        
        @Override
        public FloatBidirectionalIterator iterator(final float from) {
            return new SubsetIterator(from);
        }
        
        @Override
        public FloatSortedSet headSet(final float to) {
            if (this.top) {
                return new Subset(this.from, this.bottom, to, false);
            }
            return (FloatRBTreeSet.this.compare(to, this.to) < 0) ? new Subset(this.from, this.bottom, to, false) : this;
        }
        
        @Override
        public FloatSortedSet tailSet(final float from) {
            if (this.bottom) {
                return new Subset(from, false, this.to, this.top);
            }
            return (FloatRBTreeSet.this.compare(from, this.from) > 0) ? new Subset(from, false, this.to, this.top) : this;
        }
        
        @Override
        public FloatSortedSet subSet(float from, float to) {
            if (this.top && this.bottom) {
                return new Subset(from, false, to, false);
            }
            if (!this.top) {
                to = ((FloatRBTreeSet.this.compare(to, this.to) < 0) ? to : this.to);
            }
            if (!this.bottom) {
                from = ((FloatRBTreeSet.this.compare(from, this.from) > 0) ? from : this.from);
            }
            if (!this.top && !this.bottom && from == this.from && to == this.to) {
                return this;
            }
            return new Subset(from, false, to, false);
        }
        
        public Entry firstEntry() {
            if (FloatRBTreeSet.this.tree == null) {
                return null;
            }
            Entry e;
            if (this.bottom) {
                e = FloatRBTreeSet.this.firstEntry;
            }
            else {
                e = FloatRBTreeSet.this.locateKey(this.from);
                if (FloatRBTreeSet.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || (!this.top && FloatRBTreeSet.this.compare(e.key, this.to) >= 0)) {
                return null;
            }
            return e;
        }
        
        public Entry lastEntry() {
            if (FloatRBTreeSet.this.tree == null) {
                return null;
            }
            Entry e;
            if (this.top) {
                e = FloatRBTreeSet.this.lastEntry;
            }
            else {
                e = FloatRBTreeSet.this.locateKey(this.to);
                if (FloatRBTreeSet.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || (!this.bottom && FloatRBTreeSet.this.compare(e.key, this.from) < 0)) {
                return null;
            }
            return e;
        }
        
        @Override
        public float firstFloat() {
            final Entry e = this.firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        @Override
        public float lastFloat() {
            final Entry e = this.lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        private final class SubsetIterator extends SetIterator
        {
            SubsetIterator() {
                Subset.this.this$0.super();
                this.next = Subset.this.firstEntry();
            }
            
            SubsetIterator(final Subset subset, final float k) {
                this(subset);
                if (this.next != null) {
                    if (!subset.bottom && subset.this$0.compare(k, this.next.key) < 0) {
                        this.prev = null;
                    }
                    else {
                        if (!subset.top) {
                            final FloatRBTreeSet this$0 = subset.this$0;
                            final Entry lastEntry = subset.lastEntry();
                            this.prev = lastEntry;
                            if (this$0.compare(k, lastEntry.key) >= 0) {
                                this.next = null;
                                return;
                            }
                        }
                        this.next = subset.this$0.locateKey(k);
                        if (subset.this$0.compare(this.next.key, k) <= 0) {
                            this.prev = this.next;
                            this.next = this.next.next();
                        }
                        else {
                            this.prev = this.next.prev();
                        }
                    }
                }
            }
            
            @Override
            void updatePrevious() {
                this.prev = this.prev.prev();
                if (!Subset.this.bottom && this.prev != null && FloatRBTreeSet.this.compare(this.prev.key, Subset.this.from) < 0) {
                    this.prev = null;
                }
            }
            
            @Override
            void updateNext() {
                this.next = this.next.next();
                if (!Subset.this.top && this.next != null && FloatRBTreeSet.this.compare(this.next.key, Subset.this.to) >= 0) {
                    this.next = null;
                }
            }
        }
    }
}
