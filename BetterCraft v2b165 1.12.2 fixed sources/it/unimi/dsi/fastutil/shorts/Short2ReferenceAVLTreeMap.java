// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.objects.AbstractReferenceCollection;
import java.util.SortedSet;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.Map;
import java.util.Comparator;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.io.Serializable;

public class Short2ReferenceAVLTreeMap<V> extends AbstractShort2ReferenceSortedMap<V> implements Serializable, Cloneable
{
    protected transient Entry<V> tree;
    protected int count;
    protected transient Entry<V> firstEntry;
    protected transient Entry<V> lastEntry;
    protected transient ObjectSortedSet<Short2ReferenceMap.Entry<V>> entries;
    protected transient ShortSortedSet keys;
    protected transient ReferenceCollection<V> values;
    protected transient boolean modified;
    protected Comparator<? super Short> storedComparator;
    protected transient ShortComparator actualComparator;
    private static final long serialVersionUID = -7046029254386353129L;
    private static final boolean ASSERTS = false;
    private transient boolean[] dirPath;
    
    public Short2ReferenceAVLTreeMap() {
        this.allocatePaths();
        this.tree = null;
        this.count = 0;
    }
    
    private void setActualComparator() {
        if (this.storedComparator == null || this.storedComparator instanceof ShortComparator) {
            this.actualComparator = (ShortComparator)this.storedComparator;
        }
        else {
            this.actualComparator = new ShortComparator() {
                @Override
                public int compare(final short k1, final short k2) {
                    return Short2ReferenceAVLTreeMap.this.storedComparator.compare(k1, k2);
                }
                
                @Override
                public int compare(final Short ok1, final Short ok2) {
                    return Short2ReferenceAVLTreeMap.this.storedComparator.compare(ok1, ok2);
                }
            };
        }
    }
    
    public Short2ReferenceAVLTreeMap(final Comparator<? super Short> c) {
        this();
        this.storedComparator = c;
        this.setActualComparator();
    }
    
    public Short2ReferenceAVLTreeMap(final Map<? extends Short, ? extends V> m) {
        this();
        this.putAll(m);
    }
    
    public Short2ReferenceAVLTreeMap(final SortedMap<Short, V> m) {
        this(m.comparator());
        this.putAll((Map<? extends Short, ? extends V>)m);
    }
    
    public Short2ReferenceAVLTreeMap(final Short2ReferenceMap<? extends V> m) {
        this();
        this.putAll(m);
    }
    
    public Short2ReferenceAVLTreeMap(final Short2ReferenceSortedMap<V> m) {
        this(m.comparator());
        this.putAll((Map<? extends Short, ? extends V>)m);
    }
    
    public Short2ReferenceAVLTreeMap(final short[] k, final V[] v, final Comparator<? super Short> c) {
        this(c);
        if (k.length != v.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Short2ReferenceAVLTreeMap(final short[] k, final V[] v) {
        this(k, v, null);
    }
    
    final int compare(final short k1, final short k2) {
        return (this.actualComparator == null) ? Short.compare(k1, k2) : this.actualComparator.compare(k1, k2);
    }
    
    final Entry<V> findKey(final short k) {
        Entry<V> e;
        int cmp;
        for (e = this.tree; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {}
        return e;
    }
    
    final Entry<V> locateKey(final short k) {
        Entry<V> e = this.tree;
        Entry<V> last = this.tree;
        int cmp;
        for (cmp = 0; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {
            last = e;
        }
        return (cmp == 0) ? e : last;
    }
    
    private void allocatePaths() {
        this.dirPath = new boolean[48];
    }
    
    @Override
    public V put(final short k, final V v) {
        final Entry<V> e = this.add(k);
        final V oldValue = e.value;
        e.value = v;
        return oldValue;
    }
    
    private Entry<V> add(final short k) {
        this.modified = false;
        Entry<V> e = null;
        if (this.tree != null) {
            Entry<V> p = this.tree;
            Entry<V> q = null;
            Entry<V> y = this.tree;
            Entry<V> z = null;
            Entry<V> w = null;
            int i = 0;
            int cmp;
            while ((cmp = this.compare(k, p.key)) != 0) {
                if (p.balance() != 0) {
                    i = 0;
                    z = q;
                    y = p;
                }
                final boolean[] dirPath = this.dirPath;
                final int n = i++;
                final boolean b = cmp > 0;
                dirPath[n] = b;
                if (b) {
                    if (!p.succ()) {
                        q = p;
                        p = p.right;
                        continue;
                    }
                    ++this.count;
                    e = new Entry<V>(k, this.defRetValue);
                    this.modified = true;
                    if (p.right == null) {
                        this.lastEntry = e;
                    }
                    e.left = p;
                    e.right = p.right;
                    p.right(e);
                }
                else {
                    if (!p.pred()) {
                        q = p;
                        p = p.left;
                        continue;
                    }
                    ++this.count;
                    e = new Entry<V>(k, this.defRetValue);
                    this.modified = true;
                    if (p.left == null) {
                        this.firstEntry = e;
                    }
                    e.right = p;
                    e.left = p.left;
                    p.left(e);
                }
                for (p = y, i = 0; p != e; p = (this.dirPath[i++] ? p.right : p.left)) {
                    if (this.dirPath[i]) {
                        p.incBalance();
                    }
                    else {
                        p.decBalance();
                    }
                }
                if (y.balance() == -2) {
                    final Entry<V> x = y.left;
                    if (x.balance() == -1) {
                        w = x;
                        if (x.succ()) {
                            x.succ(false);
                            y.pred(x);
                        }
                        else {
                            y.left = x.right;
                        }
                        x.right = y;
                        x.balance(0);
                        y.balance(0);
                    }
                    else {
                        w = x.right;
                        x.right = w.left;
                        w.left = x;
                        y.left = w.right;
                        w.right = y;
                        if (w.balance() == -1) {
                            x.balance(0);
                            y.balance(1);
                        }
                        else if (w.balance() == 0) {
                            x.balance(0);
                            y.balance(0);
                        }
                        else {
                            x.balance(-1);
                            y.balance(0);
                        }
                        w.balance(0);
                        if (w.pred()) {
                            x.succ(w);
                            w.pred(false);
                        }
                        if (w.succ()) {
                            y.pred(w);
                            w.succ(false);
                        }
                    }
                }
                else {
                    if (y.balance() != 2) {
                        return e;
                    }
                    final Entry<V> x = y.right;
                    if (x.balance() == 1) {
                        w = x;
                        if (x.pred()) {
                            x.pred(false);
                            y.succ(x);
                        }
                        else {
                            y.right = x.left;
                        }
                        x.left = y;
                        x.balance(0);
                        y.balance(0);
                    }
                    else {
                        w = x.left;
                        x.left = w.right;
                        w.right = x;
                        y.right = w.left;
                        w.left = y;
                        if (w.balance() == 1) {
                            x.balance(0);
                            y.balance(-1);
                        }
                        else if (w.balance() == 0) {
                            x.balance(0);
                            y.balance(0);
                        }
                        else {
                            x.balance(1);
                            y.balance(0);
                        }
                        w.balance(0);
                        if (w.pred()) {
                            y.succ(w);
                            w.pred(false);
                        }
                        if (w.succ()) {
                            x.pred(w);
                            w.succ(false);
                        }
                    }
                }
                if (z == null) {
                    this.tree = w;
                    return e;
                }
                if (z.left == y) {
                    z.left = w;
                    return e;
                }
                z.right = w;
                return e;
            }
            return p;
        }
        ++this.count;
        final Entry<V> tree = new Entry<V>(k, this.defRetValue);
        this.firstEntry = tree;
        this.lastEntry = tree;
        this.tree = tree;
        e = tree;
        this.modified = true;
        return e;
    }
    
    private Entry<V> parent(final Entry<V> e) {
        if (e == this.tree) {
            return null;
        }
        Entry<V> y = e;
        Entry<V> x = e;
        while (!y.succ()) {
            if (x.pred()) {
                Entry<V> p = x.left;
                if (p == null || p.right != e) {
                    while (!y.succ()) {
                        y = y.right;
                    }
                    p = y.right;
                }
                return p;
            }
            x = x.left;
            y = y.right;
        }
        Entry<V> p = y.right;
        if (p == null || p.left != e) {
            while (!x.pred()) {
                x = x.left;
            }
            p = x.left;
        }
        return p;
    }
    
    @Override
    public V remove(final short k) {
        this.modified = false;
        if (this.tree == null) {
            return this.defRetValue;
        }
        Entry<V> p = this.tree;
        Entry<V> q = null;
        boolean dir = false;
        final short kk = k;
        int cmp;
        while ((cmp = this.compare(kk, p.key)) != 0) {
            if (dir = (cmp > 0)) {
                q = p;
                if ((p = p.right()) == null) {
                    return this.defRetValue;
                }
                continue;
            }
            else {
                q = p;
                if ((p = p.left()) == null) {
                    return this.defRetValue;
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
                if (q != null) {
                    if (dir) {
                        q.succ(p.right);
                    }
                    else {
                        q.pred(p.left);
                    }
                }
                else {
                    this.tree = (dir ? p.right : p.left);
                }
            }
            else {
                p.prev().right = p.right;
                if (q != null) {
                    if (dir) {
                        q.right = p.left;
                    }
                    else {
                        q.left = p.left;
                    }
                }
                else {
                    this.tree = p.left;
                }
            }
        }
        else {
            Entry<V> r = p.right;
            if (r.pred()) {
                r.left = p.left;
                r.pred(p.pred());
                if (!r.pred()) {
                    r.prev().right = r;
                }
                if (q != null) {
                    if (dir) {
                        q.right = r;
                    }
                    else {
                        q.left = r;
                    }
                }
                else {
                    this.tree = r;
                }
                r.balance(p.balance());
                q = r;
                dir = true;
            }
            else {
                Entry<V> s;
                while (true) {
                    s = r.left;
                    if (s.pred()) {
                        break;
                    }
                    r = s;
                }
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
                s.right = p.right;
                s.succ(false);
                if (q != null) {
                    if (dir) {
                        q.right = s;
                    }
                    else {
                        q.left = s;
                    }
                }
                else {
                    this.tree = s;
                }
                s.balance(p.balance());
                q = r;
                dir = false;
            }
        }
        while (q != null) {
            final Entry<V> y = q;
            q = this.parent(y);
            if (!dir) {
                dir = (q != null && q.left != y);
                y.incBalance();
                if (y.balance() == 1) {
                    break;
                }
                if (y.balance() != 2) {
                    continue;
                }
                final Entry<V> x = y.right;
                if (x.balance() == -1) {
                    final Entry<V> w = x.left;
                    x.left = w.right;
                    w.right = x;
                    y.right = w.left;
                    w.left = y;
                    if (w.balance() == 1) {
                        x.balance(0);
                        y.balance(-1);
                    }
                    else if (w.balance() == 0) {
                        x.balance(0);
                        y.balance(0);
                    }
                    else {
                        x.balance(1);
                        y.balance(0);
                    }
                    w.balance(0);
                    if (w.pred()) {
                        y.succ(w);
                        w.pred(false);
                    }
                    if (w.succ()) {
                        x.pred(w);
                        w.succ(false);
                    }
                    if (q != null) {
                        if (dir) {
                            q.right = w;
                        }
                        else {
                            q.left = w;
                        }
                    }
                    else {
                        this.tree = w;
                    }
                }
                else {
                    if (q != null) {
                        if (dir) {
                            q.right = x;
                        }
                        else {
                            q.left = x;
                        }
                    }
                    else {
                        this.tree = x;
                    }
                    if (x.balance() == 0) {
                        y.right = x.left;
                        x.left = y;
                        x.balance(-1);
                        y.balance(1);
                        break;
                    }
                    if (x.pred()) {
                        y.succ(true);
                        x.pred(false);
                    }
                    else {
                        y.right = x.left;
                    }
                    (x.left = y).balance(0);
                    x.balance(0);
                }
            }
            else {
                dir = (q != null && q.left != y);
                y.decBalance();
                if (y.balance() == -1) {
                    break;
                }
                if (y.balance() != -2) {
                    continue;
                }
                final Entry<V> x = y.left;
                if (x.balance() == 1) {
                    final Entry<V> w = x.right;
                    x.right = w.left;
                    w.left = x;
                    y.left = w.right;
                    w.right = y;
                    if (w.balance() == -1) {
                        x.balance(0);
                        y.balance(1);
                    }
                    else if (w.balance() == 0) {
                        x.balance(0);
                        y.balance(0);
                    }
                    else {
                        x.balance(-1);
                        y.balance(0);
                    }
                    w.balance(0);
                    if (w.pred()) {
                        x.succ(w);
                        w.pred(false);
                    }
                    if (w.succ()) {
                        y.pred(w);
                        w.succ(false);
                    }
                    if (q != null) {
                        if (dir) {
                            q.right = w;
                        }
                        else {
                            q.left = w;
                        }
                    }
                    else {
                        this.tree = w;
                    }
                }
                else {
                    if (q != null) {
                        if (dir) {
                            q.right = x;
                        }
                        else {
                            q.left = x;
                        }
                    }
                    else {
                        this.tree = x;
                    }
                    if (x.balance() == 0) {
                        y.left = x.right;
                        x.right = y;
                        x.balance(1);
                        y.balance(-1);
                        break;
                    }
                    if (x.succ()) {
                        y.pred(true);
                        x.succ(false);
                    }
                    else {
                        y.left = x.right;
                    }
                    (x.right = y).balance(0);
                    x.balance(0);
                }
            }
        }
        this.modified = true;
        --this.count;
        return p.value;
    }
    
    @Deprecated
    @Override
    public V put(final Short ok, final V ov) {
        final V oldValue = this.put((short)ok, ov);
        return this.modified ? this.defRetValue : oldValue;
    }
    
    @Deprecated
    @Override
    public V remove(final Object ok) {
        final V oldValue = this.remove((short)ok);
        return this.modified ? oldValue : this.defRetValue;
    }
    
    @Override
    public boolean containsValue(final Object v) {
        final ValueIterator i = new ValueIterator();
        int j = this.count;
        while (j-- != 0) {
            final V ev = i.next();
            if (ev == v) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void clear() {
        this.count = 0;
        this.tree = null;
        this.entries = null;
        this.values = null;
        this.keys = null;
        final Entry<V> entry = null;
        this.lastEntry = entry;
        this.firstEntry = entry;
    }
    
    @Override
    public boolean containsKey(final short k) {
        return this.findKey(k) != null;
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
    public V get(final short k) {
        final Entry<V> e = this.findKey(k);
        return (e == null) ? this.defRetValue : e.value;
    }
    
    @Override
    public short firstShortKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }
    
    @Override
    public short lastShortKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }
    
    @Override
    public ObjectSortedSet<Short2ReferenceMap.Entry<V>> short2ReferenceEntrySet() {
        if (this.entries == null) {
            this.entries = new AbstractObjectSortedSet<Short2ReferenceMap.Entry<V>>() {
                final Comparator<? super Short2ReferenceMap.Entry<V>> comparator = new Comparator<Short2ReferenceMap.Entry<V>>() {
                    @Override
                    public int compare(final Short2ReferenceMap.Entry<V> x, final Short2ReferenceMap.Entry<V> y) {
                        return Short2ReferenceAVLTreeMap.this.actualComparator.compare(x.getShortKey(), y.getShortKey());
                    }
                };
                
                @Override
                public Comparator<? super Short2ReferenceMap.Entry<V>> comparator() {
                    return this.comparator;
                }
                
                @Override
                public ObjectBidirectionalIterator<Short2ReferenceMap.Entry<V>> iterator() {
                    return new EntryIterator();
                }
                
                @Override
                public ObjectBidirectionalIterator<Short2ReferenceMap.Entry<V>> iterator(final Short2ReferenceMap.Entry<V> from) {
                    return new EntryIterator(from.getShortKey());
                }
                
                @Override
                public boolean contains(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
                    if (e.getKey() == null || !(e.getKey() instanceof Short)) {
                        return false;
                    }
                    final Entry<V> f = Short2ReferenceAVLTreeMap.this.findKey((short)e.getKey());
                    return e.equals(f);
                }
                
                @Override
                public boolean remove(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
                    if (e.getKey() == null || !(e.getKey() instanceof Short)) {
                        return false;
                    }
                    final Entry<V> f = Short2ReferenceAVLTreeMap.this.findKey((short)e.getKey());
                    if (f != null) {
                        Short2ReferenceAVLTreeMap.this.remove(f.key);
                    }
                    return f != null;
                }
                
                @Override
                public int size() {
                    return Short2ReferenceAVLTreeMap.this.count;
                }
                
                @Override
                public void clear() {
                    Short2ReferenceAVLTreeMap.this.clear();
                }
                
                @Override
                public Short2ReferenceMap.Entry<V> first() {
                    return Short2ReferenceAVLTreeMap.this.firstEntry;
                }
                
                @Override
                public Short2ReferenceMap.Entry<V> last() {
                    return Short2ReferenceAVLTreeMap.this.lastEntry;
                }
                
                @Override
                public ObjectSortedSet<Short2ReferenceMap.Entry<V>> subSet(final Short2ReferenceMap.Entry<V> from, final Short2ReferenceMap.Entry<V> to) {
                    return Short2ReferenceAVLTreeMap.this.subMap(from.getShortKey(), to.getShortKey()).short2ReferenceEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Short2ReferenceMap.Entry<V>> headSet(final Short2ReferenceMap.Entry<V> to) {
                    return Short2ReferenceAVLTreeMap.this.headMap(to.getShortKey()).short2ReferenceEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Short2ReferenceMap.Entry<V>> tailSet(final Short2ReferenceMap.Entry<V> from) {
                    return Short2ReferenceAVLTreeMap.this.tailMap(from.getShortKey()).short2ReferenceEntrySet();
                }
            };
        }
        return this.entries;
    }
    
    @Override
    public ShortSortedSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public ReferenceCollection<V> values() {
        if (this.values == null) {
            this.values = new AbstractReferenceCollection<V>() {
                @Override
                public ObjectIterator<V> iterator() {
                    return new ValueIterator();
                }
                
                @Override
                public boolean contains(final Object k) {
                    return Short2ReferenceAVLTreeMap.this.containsValue(k);
                }
                
                @Override
                public int size() {
                    return Short2ReferenceAVLTreeMap.this.count;
                }
                
                @Override
                public void clear() {
                    Short2ReferenceAVLTreeMap.this.clear();
                }
            };
        }
        return this.values;
    }
    
    @Override
    public ShortComparator comparator() {
        return this.actualComparator;
    }
    
    @Override
    public Short2ReferenceSortedMap<V> headMap(final short to) {
        return new Submap((short)0, true, to, false);
    }
    
    @Override
    public Short2ReferenceSortedMap<V> tailMap(final short from) {
        return new Submap(from, false, (short)0, true);
    }
    
    @Override
    public Short2ReferenceSortedMap<V> subMap(final short from, final short to) {
        return new Submap(from, false, to, false);
    }
    
    public Short2ReferenceAVLTreeMap<V> clone() {
        Short2ReferenceAVLTreeMap<V> c;
        try {
            c = (Short2ReferenceAVLTreeMap)super.clone();
        }
        catch (final CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.keys = null;
        c.values = null;
        c.entries = null;
        c.allocatePaths();
        if (this.count != 0) {
            final Entry<V> rp = new Entry<V>();
            final Entry<V> rq = new Entry<V>();
            Entry<V> p = rp;
            rp.left(this.tree);
            Entry<V> q = rq;
            rq.pred(null);
        Block_4:
            while (true) {
                if (!p.pred()) {
                    final Entry<V> e = p.left.clone();
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
                    final Entry<V> e = p.right.clone();
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
        final EntryIterator i = new EntryIterator();
        s.defaultWriteObject();
        while (n-- != 0) {
            final Entry<V> e = i.nextEntry();
            s.writeShort(e.key);
            s.writeObject(e.value);
        }
    }
    
    private Entry<V> readTree(final ObjectInputStream s, final int n, final Entry<V> pred, final Entry<V> succ) throws IOException, ClassNotFoundException {
        if (n == 1) {
            final Entry<V> top = new Entry<V>(s.readShort(), (V)s.readObject());
            top.pred(pred);
            top.succ(succ);
            return top;
        }
        if (n == 2) {
            final Entry<V> top = new Entry<V>(s.readShort(), (V)s.readObject());
            top.right(new Entry<V>(s.readShort(), (V)s.readObject()));
            top.right.pred(top);
            top.balance(1);
            top.pred(pred);
            top.right.succ(succ);
            return top;
        }
        final int rightN = n / 2;
        final int leftN = n - rightN - 1;
        final Entry<V> top2 = new Entry<V>();
        top2.left(this.readTree(s, leftN, pred, top2));
        top2.key = s.readShort();
        top2.value = (V)s.readObject();
        top2.right(this.readTree(s, rightN, top2, succ));
        if (n == (n & -n)) {
            top2.balance(1);
        }
        return top2;
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.setActualComparator();
        this.allocatePaths();
        if (this.count != 0) {
            this.tree = this.readTree(s, this.count, null, null);
            Entry<V> e;
            for (e = this.tree; e.left() != null; e = e.left()) {}
            this.firstEntry = e;
            for (e = this.tree; e.right() != null; e = e.right()) {}
            this.lastEntry = e;
        }
    }
    
    private static <V> int checkTree(final Entry<V> e) {
        return 0;
    }
    
    private static final class Entry<V> implements Cloneable, Short2ReferenceMap.Entry<V>
    {
        private static final int SUCC_MASK = Integer.MIN_VALUE;
        private static final int PRED_MASK = 1073741824;
        private static final int BALANCE_MASK = 255;
        short key;
        V value;
        Entry<V> left;
        Entry<V> right;
        int info;
        
        Entry() {
        }
        
        Entry(final short k, final V v) {
            this.key = k;
            this.value = v;
            this.info = -1073741824;
        }
        
        Entry<V> left() {
            return ((this.info & 0x40000000) != 0x0) ? null : this.left;
        }
        
        Entry<V> right() {
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
        
        void pred(final Entry<V> pred) {
            this.info |= 0x40000000;
            this.left = pred;
        }
        
        void succ(final Entry<V> succ) {
            this.info |= Integer.MIN_VALUE;
            this.right = succ;
        }
        
        void left(final Entry<V> left) {
            this.info &= 0xBFFFFFFF;
            this.left = left;
        }
        
        void right(final Entry<V> right) {
            this.info &= Integer.MAX_VALUE;
            this.right = right;
        }
        
        int balance() {
            return (byte)this.info;
        }
        
        void balance(final int level) {
            this.info &= 0xFFFFFF00;
            this.info |= (level & 0xFF);
        }
        
        void incBalance() {
            this.info = ((this.info & 0xFFFFFF00) | ((byte)this.info + 1 & 0xFF));
        }
        
        protected void decBalance() {
            this.info = ((this.info & 0xFFFFFF00) | ((byte)this.info - 1 & 0xFF));
        }
        
        Entry<V> next() {
            Entry<V> next = this.right;
            if ((this.info & Integer.MIN_VALUE) == 0x0) {
                while ((next.info & 0x40000000) == 0x0) {
                    next = next.left;
                }
            }
            return next;
        }
        
        Entry<V> prev() {
            Entry<V> prev = this.left;
            if ((this.info & 0x40000000) == 0x0) {
                while ((prev.info & Integer.MIN_VALUE) == 0x0) {
                    prev = prev.right;
                }
            }
            return prev;
        }
        
        @Deprecated
        @Override
        public Short getKey() {
            return this.key;
        }
        
        @Override
        public short getShortKey() {
            return this.key;
        }
        
        @Override
        public V getValue() {
            return this.value;
        }
        
        @Override
        public V setValue(final V value) {
            final V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
        
        public Entry<V> clone() {
            Entry<V> c;
            try {
                c = (Entry)super.clone();
            }
            catch (final CloneNotSupportedException cantHappen) {
                throw new InternalError();
            }
            c.key = this.key;
            c.value = this.value;
            c.info = this.info;
            return c;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Short, V> e = (Map.Entry<Short, V>)o;
            return this.key == e.getKey() && this.value == e.getValue();
        }
        
        @Override
        public int hashCode() {
            return this.key ^ ((this.value == null) ? 0 : System.identityHashCode(this.value));
        }
        
        @Override
        public String toString() {
            return this.key + "=>" + this.value;
        }
    }
    
    private class TreeIterator
    {
        Entry<V> prev;
        Entry<V> next;
        Entry<V> curr;
        int index;
        
        TreeIterator() {
            this.index = 0;
            this.next = Short2ReferenceAVLTreeMap.this.firstEntry;
        }
        
        TreeIterator(final short k) {
            this.index = 0;
            final Entry<V> locateKey = Short2ReferenceAVLTreeMap.this.locateKey(k);
            this.next = locateKey;
            if (locateKey != null) {
                if (Short2ReferenceAVLTreeMap.this.compare(this.next.key, k) <= 0) {
                    this.prev = this.next;
                    this.next = this.next.next();
                }
                else {
                    this.prev = this.next.prev();
                }
            }
        }
        
        public boolean hasNext() {
            return this.next != null;
        }
        
        public boolean hasPrevious() {
            return this.prev != null;
        }
        
        void updateNext() {
            this.next = this.next.next();
        }
        
        Entry<V> nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final Entry<V> next = this.next;
            this.prev = next;
            this.curr = next;
            ++this.index;
            this.updateNext();
            return this.curr;
        }
        
        void updatePrevious() {
            this.prev = this.prev.prev();
        }
        
        Entry<V> previousEntry() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final Entry<V> prev = this.prev;
            this.next = prev;
            this.curr = prev;
            --this.index;
            this.updatePrevious();
            return this.curr;
        }
        
        public int nextIndex() {
            return this.index;
        }
        
        public int previousIndex() {
            return this.index - 1;
        }
        
        public void remove() {
            if (this.curr == null) {
                throw new IllegalStateException();
            }
            if (this.curr == this.prev) {
                --this.index;
            }
            final Entry<V> curr = this.curr;
            this.prev = curr;
            this.next = curr;
            this.updatePrevious();
            this.updateNext();
            Short2ReferenceAVLTreeMap.this.remove(this.curr.key);
            this.curr = null;
        }
        
        public int skip(final int n) {
            int i = n;
            while (i-- != 0 && this.hasNext()) {
                this.nextEntry();
            }
            return n - i - 1;
        }
        
        public int back(final int n) {
            int i = n;
            while (i-- != 0 && this.hasPrevious()) {
                this.previousEntry();
            }
            return n - i - 1;
        }
    }
    
    private class EntryIterator extends TreeIterator implements ObjectListIterator<Short2ReferenceMap.Entry<V>>
    {
        EntryIterator() {
        }
        
        EntryIterator(final short k) {
            super(k);
        }
        
        @Override
        public Short2ReferenceMap.Entry<V> next() {
            return this.nextEntry();
        }
        
        @Override
        public Short2ReferenceMap.Entry<V> previous() {
            return this.previousEntry();
        }
        
        @Override
        public void set(final Short2ReferenceMap.Entry<V> ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final Short2ReferenceMap.Entry<V> ok) {
            throw new UnsupportedOperationException();
        }
    }
    
    private final class KeyIterator extends TreeIterator implements ShortListIterator
    {
        public KeyIterator() {
        }
        
        public KeyIterator(final short k) {
            super(k);
        }
        
        @Override
        public short nextShort() {
            return this.nextEntry().key;
        }
        
        @Override
        public short previousShort() {
            return this.previousEntry().key;
        }
        
        @Override
        public void set(final short k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final short k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Short next() {
            return this.nextEntry().key;
        }
        
        @Override
        public Short previous() {
            return this.previousEntry().key;
        }
        
        @Override
        public void set(final Short ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final Short ok) {
            throw new UnsupportedOperationException();
        }
    }
    
    private class KeySet extends AbstractShort2ReferenceSortedMap.KeySet
    {
        @Override
        public ShortBidirectionalIterator iterator() {
            return new KeyIterator();
        }
        
        @Override
        public ShortBidirectionalIterator iterator(final short from) {
            return new KeyIterator(from);
        }
    }
    
    private final class ValueIterator extends TreeIterator implements ObjectListIterator<V>
    {
        @Override
        public V next() {
            return this.nextEntry().value;
        }
        
        @Override
        public V previous() {
            return this.previousEntry().value;
        }
        
        @Override
        public void set(final V v) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final V v) {
            throw new UnsupportedOperationException();
        }
    }
    
    private final class Submap extends AbstractShort2ReferenceSortedMap<V> implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        short from;
        short to;
        boolean bottom;
        boolean top;
        protected transient ObjectSortedSet<Short2ReferenceMap.Entry<V>> entries;
        protected transient ShortSortedSet keys;
        protected transient ReferenceCollection<V> values;
        final /* synthetic */ Short2ReferenceAVLTreeMap this$0;
        
        public Submap(final short from, final boolean bottom, final short to, final boolean top) {
            if (!bottom && !top && Short2ReferenceAVLTreeMap.this.compare(from, to) > 0) {
                throw new IllegalArgumentException("Start key (" + from + ") is larger than end key (" + to + ")");
            }
            this.from = from;
            this.bottom = bottom;
            this.to = to;
            this.top = top;
            this.defRetValue = Short2ReferenceAVLTreeMap.this.defRetValue;
        }
        
        @Override
        public void clear() {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                i.nextEntry();
                i.remove();
            }
        }
        
        final boolean in(final short k) {
            return (this.bottom || Short2ReferenceAVLTreeMap.this.compare(k, this.from) >= 0) && (this.top || Short2ReferenceAVLTreeMap.this.compare(k, this.to) < 0);
        }
        
        @Override
        public ObjectSortedSet<Short2ReferenceMap.Entry<V>> short2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = new AbstractObjectSortedSet<Short2ReferenceMap.Entry<V>>() {
                    @Override
                    public ObjectBidirectionalIterator<Short2ReferenceMap.Entry<V>> iterator() {
                        return new SubmapEntryIterator();
                    }
                    
                    @Override
                    public ObjectBidirectionalIterator<Short2ReferenceMap.Entry<V>> iterator(final Short2ReferenceMap.Entry<V> from) {
                        return new SubmapEntryIterator(from.getShortKey());
                    }
                    
                    @Override
                    public Comparator<? super Short2ReferenceMap.Entry<V>> comparator() {
                        return Short2ReferenceAVLTreeMap.this.entrySet().comparator();
                    }
                    
                    @Override
                    public boolean contains(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
                        if (e.getKey() == null || !(e.getKey() instanceof Short)) {
                            return false;
                        }
                        final Short2ReferenceAVLTreeMap.Entry<V> f = Short2ReferenceAVLTreeMap.this.findKey((short)e.getKey());
                        return f != null && Submap.this.in(f.key) && e.equals(f);
                    }
                    
                    @Override
                    public boolean remove(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
                        if (e.getKey() == null || !(e.getKey() instanceof Short)) {
                            return false;
                        }
                        final Short2ReferenceAVLTreeMap.Entry<V> f = Short2ReferenceAVLTreeMap.this.findKey((short)e.getKey());
                        if (f != null && Submap.this.in(f.key)) {
                            Submap.this.remove(f.key);
                        }
                        return f != null;
                    }
                    
                    @Override
                    public int size() {
                        int c = 0;
                        final Iterator<?> i = this.iterator();
                        while (i.hasNext()) {
                            ++c;
                            i.next();
                        }
                        return c;
                    }
                    
                    @Override
                    public boolean isEmpty() {
                        return !new SubmapIterator().hasNext();
                    }
                    
                    @Override
                    public void clear() {
                        Submap.this.clear();
                    }
                    
                    @Override
                    public Short2ReferenceMap.Entry<V> first() {
                        return Submap.this.firstEntry();
                    }
                    
                    @Override
                    public Short2ReferenceMap.Entry<V> last() {
                        return Submap.this.lastEntry();
                    }
                    
                    @Override
                    public ObjectSortedSet<Short2ReferenceMap.Entry<V>> subSet(final Short2ReferenceMap.Entry<V> from, final Short2ReferenceMap.Entry<V> to) {
                        return Submap.this.subMap(from.getShortKey(), to.getShortKey()).short2ReferenceEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Short2ReferenceMap.Entry<V>> headSet(final Short2ReferenceMap.Entry<V> to) {
                        return Submap.this.headMap(to.getShortKey()).short2ReferenceEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Short2ReferenceMap.Entry<V>> tailSet(final Short2ReferenceMap.Entry<V> from) {
                        return Submap.this.tailMap(from.getShortKey()).short2ReferenceEntrySet();
                    }
                };
            }
            return this.entries;
        }
        
        @Override
        public ShortSortedSet keySet() {
            if (this.keys == null) {
                this.keys = new KeySet();
            }
            return this.keys;
        }
        
        @Override
        public ReferenceCollection<V> values() {
            if (this.values == null) {
                this.values = new AbstractReferenceCollection<V>() {
                    @Override
                    public ObjectIterator<V> iterator() {
                        return new SubmapValueIterator();
                    }
                    
                    @Override
                    public boolean contains(final Object k) {
                        return Submap.this.containsValue(k);
                    }
                    
                    @Override
                    public int size() {
                        return Submap.this.size();
                    }
                    
                    @Override
                    public void clear() {
                        Submap.this.clear();
                    }
                };
            }
            return this.values;
        }
        
        @Override
        public boolean containsKey(final short k) {
            return this.in(k) && Short2ReferenceAVLTreeMap.this.containsKey(k);
        }
        
        @Override
        public boolean containsValue(final Object v) {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                final Object ev = i.nextEntry().value;
                if (ev == v) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public V get(final short k) {
            final short kk = k;
            final Short2ReferenceAVLTreeMap.Entry<V> e;
            return (V)((this.in(kk) && (e = Short2ReferenceAVLTreeMap.this.findKey(kk)) != null) ? e.value : this.defRetValue);
        }
        
        @Override
        public V put(final short k, final V v) {
            Short2ReferenceAVLTreeMap.this.modified = false;
            if (!this.in(k)) {
                throw new IllegalArgumentException("Key (" + k + ") out of range [" + (this.bottom ? "-" : String.valueOf(this.from)) + ", " + (this.top ? "-" : String.valueOf(this.to)) + ")");
            }
            final V oldValue = Short2ReferenceAVLTreeMap.this.put(k, v);
            return (V)(Short2ReferenceAVLTreeMap.this.modified ? this.defRetValue : oldValue);
        }
        
        @Deprecated
        @Override
        public V put(final Short ok, final V ov) {
            final V oldValue = this.put((short)ok, ov);
            return (V)(Short2ReferenceAVLTreeMap.this.modified ? this.defRetValue : oldValue);
        }
        
        @Override
        public V remove(final short k) {
            Short2ReferenceAVLTreeMap.this.modified = false;
            if (!this.in(k)) {
                return (V)this.defRetValue;
            }
            final V oldValue = Short2ReferenceAVLTreeMap.this.remove(k);
            return (V)(Short2ReferenceAVLTreeMap.this.modified ? oldValue : this.defRetValue);
        }
        
        @Deprecated
        @Override
        public V remove(final Object ok) {
            final V oldValue = this.remove((short)ok);
            return (V)(Short2ReferenceAVLTreeMap.this.modified ? oldValue : this.defRetValue);
        }
        
        @Override
        public int size() {
            final SubmapIterator i = new SubmapIterator();
            int n = 0;
            while (i.hasNext()) {
                ++n;
                i.nextEntry();
            }
            return n;
        }
        
        @Override
        public boolean isEmpty() {
            return !new SubmapIterator().hasNext();
        }
        
        @Override
        public ShortComparator comparator() {
            return Short2ReferenceAVLTreeMap.this.actualComparator;
        }
        
        @Override
        public Short2ReferenceSortedMap<V> headMap(final short to) {
            if (this.top) {
                return new Submap(this.from, this.bottom, to, false);
            }
            return (Short2ReferenceAVLTreeMap.this.compare(to, this.to) < 0) ? new Submap(this.from, this.bottom, to, false) : this;
        }
        
        @Override
        public Short2ReferenceSortedMap<V> tailMap(final short from) {
            if (this.bottom) {
                return new Submap(from, false, this.to, this.top);
            }
            return (Short2ReferenceAVLTreeMap.this.compare(from, this.from) > 0) ? new Submap(from, false, this.to, this.top) : this;
        }
        
        @Override
        public Short2ReferenceSortedMap<V> subMap(short from, short to) {
            if (this.top && this.bottom) {
                return new Submap(from, false, to, false);
            }
            if (!this.top) {
                to = ((Short2ReferenceAVLTreeMap.this.compare(to, this.to) < 0) ? to : this.to);
            }
            if (!this.bottom) {
                from = ((Short2ReferenceAVLTreeMap.this.compare(from, this.from) > 0) ? from : this.from);
            }
            if (!this.top && !this.bottom && from == this.from && to == this.to) {
                return this;
            }
            return new Submap(from, false, to, false);
        }
        
        public Short2ReferenceAVLTreeMap.Entry<V> firstEntry() {
            if (Short2ReferenceAVLTreeMap.this.tree == null) {
                return null;
            }
            Short2ReferenceAVLTreeMap.Entry<V> e;
            if (this.bottom) {
                e = Short2ReferenceAVLTreeMap.this.firstEntry;
            }
            else {
                e = Short2ReferenceAVLTreeMap.this.locateKey(this.from);
                if (Short2ReferenceAVLTreeMap.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || (!this.top && Short2ReferenceAVLTreeMap.this.compare(e.key, this.to) >= 0)) {
                return null;
            }
            return e;
        }
        
        public Short2ReferenceAVLTreeMap.Entry<V> lastEntry() {
            if (Short2ReferenceAVLTreeMap.this.tree == null) {
                return null;
            }
            Short2ReferenceAVLTreeMap.Entry<V> e;
            if (this.top) {
                e = Short2ReferenceAVLTreeMap.this.lastEntry;
            }
            else {
                e = Short2ReferenceAVLTreeMap.this.locateKey(this.to);
                if (Short2ReferenceAVLTreeMap.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || (!this.bottom && Short2ReferenceAVLTreeMap.this.compare(e.key, this.from) < 0)) {
                return null;
            }
            return e;
        }
        
        @Override
        public short firstShortKey() {
            final Short2ReferenceAVLTreeMap.Entry<V> e = this.firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        @Override
        public short lastShortKey() {
            final Short2ReferenceAVLTreeMap.Entry<V> e = this.lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        @Deprecated
        @Override
        public Short firstKey() {
            final Short2ReferenceAVLTreeMap.Entry<V> e = this.firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.getKey();
        }
        
        @Deprecated
        @Override
        public Short lastKey() {
            final Short2ReferenceAVLTreeMap.Entry<V> e = this.lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.getKey();
        }
        
        private class KeySet extends AbstractShort2ReferenceSortedMap.KeySet
        {
            @Override
            public ShortBidirectionalIterator iterator() {
                return new SubmapKeyIterator();
            }
            
            @Override
            public ShortBidirectionalIterator iterator(final short from) {
                return new SubmapKeyIterator(from);
            }
        }
        
        private class SubmapIterator extends TreeIterator
        {
            SubmapIterator() {
                Submap.this.this$0.super();
                this.next = Submap.this.firstEntry();
            }
            
            SubmapIterator(final Submap submap, final short k) {
                this(submap);
                if (this.next != null) {
                    if (!submap.bottom && submap.this$0.compare(k, this.next.key) < 0) {
                        this.prev = null;
                    }
                    else {
                        if (!submap.top) {
                            final Short2ReferenceAVLTreeMap this$0 = submap.this$0;
                            final Short2ReferenceAVLTreeMap.Entry<V> lastEntry = submap.lastEntry();
                            this.prev = lastEntry;
                            if (this$0.compare(k, lastEntry.key) >= 0) {
                                this.next = null;
                                return;
                            }
                        }
                        this.next = submap.this$0.locateKey(k);
                        if (submap.this$0.compare(this.next.key, k) <= 0) {
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
                if (!Submap.this.bottom && this.prev != null && Short2ReferenceAVLTreeMap.this.compare(this.prev.key, Submap.this.from) < 0) {
                    this.prev = null;
                }
            }
            
            @Override
            void updateNext() {
                this.next = this.next.next();
                if (!Submap.this.top && this.next != null && Short2ReferenceAVLTreeMap.this.compare(this.next.key, Submap.this.to) >= 0) {
                    this.next = null;
                }
            }
        }
        
        private class SubmapEntryIterator extends SubmapIterator implements ObjectListIterator<Short2ReferenceMap.Entry<V>>
        {
            SubmapEntryIterator() {
            }
            
            SubmapEntryIterator(final short k) {
                super(k);
            }
            
            @Override
            public Short2ReferenceMap.Entry<V> next() {
                return this.nextEntry();
            }
            
            @Override
            public Short2ReferenceMap.Entry<V> previous() {
                return this.previousEntry();
            }
            
            @Override
            public void set(final Short2ReferenceMap.Entry<V> ok) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void add(final Short2ReferenceMap.Entry<V> ok) {
                throw new UnsupportedOperationException();
            }
        }
        
        private final class SubmapKeyIterator extends SubmapIterator implements ShortListIterator
        {
            public SubmapKeyIterator() {
            }
            
            public SubmapKeyIterator(final short from) {
                super(from);
            }
            
            @Override
            public short nextShort() {
                return this.nextEntry().key;
            }
            
            @Override
            public short previousShort() {
                return this.previousEntry().key;
            }
            
            @Override
            public void set(final short k) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void add(final short k) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public Short next() {
                return this.nextEntry().key;
            }
            
            @Override
            public Short previous() {
                return this.previousEntry().key;
            }
            
            @Override
            public void set(final Short ok) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void add(final Short ok) {
                throw new UnsupportedOperationException();
            }
        }
        
        private final class SubmapValueIterator extends SubmapIterator implements ObjectListIterator<V>
        {
            @Override
            public V next() {
                return this.nextEntry().value;
            }
            
            @Override
            public V previous() {
                return this.previousEntry().value;
            }
            
            @Override
            public void set(final V v) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void add(final V v) {
                throw new UnsupportedOperationException();
            }
        }
    }
}
