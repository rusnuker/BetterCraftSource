// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.shorts.ShortListIterator;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.AbstractShortCollection;
import java.util.SortedSet;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.Map;
import java.util.Comparator;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.io.Serializable;

public class Byte2ShortAVLTreeMap extends AbstractByte2ShortSortedMap implements Serializable, Cloneable
{
    protected transient Entry tree;
    protected int count;
    protected transient Entry firstEntry;
    protected transient Entry lastEntry;
    protected transient ObjectSortedSet<Byte2ShortMap.Entry> entries;
    protected transient ByteSortedSet keys;
    protected transient ShortCollection values;
    protected transient boolean modified;
    protected Comparator<? super Byte> storedComparator;
    protected transient ByteComparator actualComparator;
    private static final long serialVersionUID = -7046029254386353129L;
    private static final boolean ASSERTS = false;
    private transient boolean[] dirPath;
    
    public Byte2ShortAVLTreeMap() {
        this.allocatePaths();
        this.tree = null;
        this.count = 0;
    }
    
    private void setActualComparator() {
        if (this.storedComparator == null || this.storedComparator instanceof ByteComparator) {
            this.actualComparator = (ByteComparator)this.storedComparator;
        }
        else {
            this.actualComparator = new ByteComparator() {
                @Override
                public int compare(final byte k1, final byte k2) {
                    return Byte2ShortAVLTreeMap.this.storedComparator.compare(k1, k2);
                }
                
                @Override
                public int compare(final Byte ok1, final Byte ok2) {
                    return Byte2ShortAVLTreeMap.this.storedComparator.compare(ok1, ok2);
                }
            };
        }
    }
    
    public Byte2ShortAVLTreeMap(final Comparator<? super Byte> c) {
        this();
        this.storedComparator = c;
        this.setActualComparator();
    }
    
    public Byte2ShortAVLTreeMap(final Map<? extends Byte, ? extends Short> m) {
        this();
        this.putAll(m);
    }
    
    public Byte2ShortAVLTreeMap(final SortedMap<Byte, Short> m) {
        this(m.comparator());
        this.putAll(m);
    }
    
    public Byte2ShortAVLTreeMap(final Byte2ShortMap m) {
        this();
        this.putAll(m);
    }
    
    public Byte2ShortAVLTreeMap(final Byte2ShortSortedMap m) {
        this(m.comparator());
        this.putAll(m);
    }
    
    public Byte2ShortAVLTreeMap(final byte[] k, final short[] v, final Comparator<? super Byte> c) {
        this(c);
        if (k.length != v.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Byte2ShortAVLTreeMap(final byte[] k, final short[] v) {
        this(k, v, null);
    }
    
    final int compare(final byte k1, final byte k2) {
        return (this.actualComparator == null) ? Byte.compare(k1, k2) : this.actualComparator.compare(k1, k2);
    }
    
    final Entry findKey(final byte k) {
        Entry e;
        int cmp;
        for (e = this.tree; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {}
        return e;
    }
    
    final Entry locateKey(final byte k) {
        Entry e = this.tree;
        Entry last = this.tree;
        int cmp;
        for (cmp = 0; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {
            last = e;
        }
        return (cmp == 0) ? e : last;
    }
    
    private void allocatePaths() {
        this.dirPath = new boolean[48];
    }
    
    public short addTo(final byte k, final short incr) {
        final Entry e = this.add(k);
        final short oldValue = e.value;
        final Entry entry = e;
        entry.value += incr;
        return oldValue;
    }
    
    @Override
    public short put(final byte k, final short v) {
        final Entry e = this.add(k);
        final short oldValue = e.value;
        e.value = v;
        return oldValue;
    }
    
    private Entry add(final byte k) {
        this.modified = false;
        Entry e = null;
        if (this.tree != null) {
            Entry p = this.tree;
            Entry q = null;
            Entry y = this.tree;
            Entry z = null;
            Entry w = null;
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
                    e = new Entry(k, this.defRetValue);
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
                    e = new Entry(k, this.defRetValue);
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
                    final Entry x = y.left;
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
                    final Entry x = y.right;
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
        final Entry tree = new Entry(k, this.defRetValue);
        this.firstEntry = tree;
        this.lastEntry = tree;
        this.tree = tree;
        e = tree;
        this.modified = true;
        return e;
    }
    
    private Entry parent(final Entry e) {
        if (e == this.tree) {
            return null;
        }
        Entry y = e;
        Entry x = e;
        while (!y.succ()) {
            if (x.pred()) {
                Entry p = x.left;
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
        Entry p = y.right;
        if (p == null || p.left != e) {
            while (!x.pred()) {
                x = x.left;
            }
            p = x.left;
        }
        return p;
    }
    
    @Override
    public short remove(final byte k) {
        this.modified = false;
        if (this.tree == null) {
            return this.defRetValue;
        }
        Entry p = this.tree;
        Entry q = null;
        boolean dir = false;
        final byte kk = k;
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
            Entry r = p.right;
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
                Entry s;
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
            final Entry y = q;
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
                final Entry x = y.right;
                if (x.balance() == -1) {
                    final Entry w = x.left;
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
                final Entry x = y.left;
                if (x.balance() == 1) {
                    final Entry w = x.right;
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
    public Short put(final Byte ok, final Short ov) {
        final short oldValue = this.put((byte)ok, (short)ov);
        return this.modified ? null : Short.valueOf(oldValue);
    }
    
    @Deprecated
    @Override
    public Short remove(final Object ok) {
        final short oldValue = this.remove((byte)ok);
        return this.modified ? Short.valueOf(oldValue) : null;
    }
    
    @Override
    public boolean containsValue(final short v) {
        final ValueIterator i = new ValueIterator();
        int j = this.count;
        while (j-- != 0) {
            final short ev = i.nextShort();
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
        final Entry entry = null;
        this.lastEntry = entry;
        this.firstEntry = entry;
    }
    
    @Override
    public boolean containsKey(final byte k) {
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
    public short get(final byte k) {
        final Entry e = this.findKey(k);
        return (e == null) ? this.defRetValue : e.value;
    }
    
    @Override
    public byte firstByteKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }
    
    @Override
    public byte lastByteKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }
    
    @Override
    public ObjectSortedSet<Byte2ShortMap.Entry> byte2ShortEntrySet() {
        if (this.entries == null) {
            this.entries = new AbstractObjectSortedSet<Byte2ShortMap.Entry>() {
                final Comparator<? super Byte2ShortMap.Entry> comparator = new Comparator<Byte2ShortMap.Entry>() {
                    @Override
                    public int compare(final Byte2ShortMap.Entry x, final Byte2ShortMap.Entry y) {
                        return Byte2ShortAVLTreeMap.this.actualComparator.compare(x.getByteKey(), y.getByteKey());
                    }
                };
                
                @Override
                public Comparator<? super Byte2ShortMap.Entry> comparator() {
                    return this.comparator;
                }
                
                @Override
                public ObjectBidirectionalIterator<Byte2ShortMap.Entry> iterator() {
                    return new EntryIterator();
                }
                
                @Override
                public ObjectBidirectionalIterator<Byte2ShortMap.Entry> iterator(final Byte2ShortMap.Entry from) {
                    return new EntryIterator(from.getByteKey());
                }
                
                @Override
                public boolean contains(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
                    if (e.getKey() == null || !(e.getKey() instanceof Byte)) {
                        return false;
                    }
                    if (e.getValue() == null || !(e.getValue() instanceof Short)) {
                        return false;
                    }
                    final Entry f = Byte2ShortAVLTreeMap.this.findKey((byte)e.getKey());
                    return e.equals(f);
                }
                
                @Override
                public boolean remove(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
                    if (e.getKey() == null || !(e.getKey() instanceof Byte)) {
                        return false;
                    }
                    if (e.getValue() == null || !(e.getValue() instanceof Short)) {
                        return false;
                    }
                    final Entry f = Byte2ShortAVLTreeMap.this.findKey((byte)e.getKey());
                    if (f != null) {
                        Byte2ShortAVLTreeMap.this.remove(f.key);
                    }
                    return f != null;
                }
                
                @Override
                public int size() {
                    return Byte2ShortAVLTreeMap.this.count;
                }
                
                @Override
                public void clear() {
                    Byte2ShortAVLTreeMap.this.clear();
                }
                
                @Override
                public Byte2ShortMap.Entry first() {
                    return Byte2ShortAVLTreeMap.this.firstEntry;
                }
                
                @Override
                public Byte2ShortMap.Entry last() {
                    return Byte2ShortAVLTreeMap.this.lastEntry;
                }
                
                @Override
                public ObjectSortedSet<Byte2ShortMap.Entry> subSet(final Byte2ShortMap.Entry from, final Byte2ShortMap.Entry to) {
                    return Byte2ShortAVLTreeMap.this.subMap(from.getByteKey(), to.getByteKey()).byte2ShortEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Byte2ShortMap.Entry> headSet(final Byte2ShortMap.Entry to) {
                    return Byte2ShortAVLTreeMap.this.headMap(to.getByteKey()).byte2ShortEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Byte2ShortMap.Entry> tailSet(final Byte2ShortMap.Entry from) {
                    return Byte2ShortAVLTreeMap.this.tailMap(from.getByteKey()).byte2ShortEntrySet();
                }
            };
        }
        return this.entries;
    }
    
    @Override
    public ByteSortedSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public ShortCollection values() {
        if (this.values == null) {
            this.values = new AbstractShortCollection() {
                @Override
                public ShortIterator iterator() {
                    return new ValueIterator();
                }
                
                @Override
                public boolean contains(final short k) {
                    return Byte2ShortAVLTreeMap.this.containsValue(k);
                }
                
                @Override
                public int size() {
                    return Byte2ShortAVLTreeMap.this.count;
                }
                
                @Override
                public void clear() {
                    Byte2ShortAVLTreeMap.this.clear();
                }
            };
        }
        return this.values;
    }
    
    @Override
    public ByteComparator comparator() {
        return this.actualComparator;
    }
    
    @Override
    public Byte2ShortSortedMap headMap(final byte to) {
        return new Submap((byte)0, true, to, false);
    }
    
    @Override
    public Byte2ShortSortedMap tailMap(final byte from) {
        return new Submap(from, false, (byte)0, true);
    }
    
    @Override
    public Byte2ShortSortedMap subMap(final byte from, final byte to) {
        return new Submap(from, false, to, false);
    }
    
    public Byte2ShortAVLTreeMap clone() {
        Byte2ShortAVLTreeMap c;
        try {
            c = (Byte2ShortAVLTreeMap)super.clone();
        }
        catch (final CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.keys = null;
        c.values = null;
        c.entries = null;
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
        final EntryIterator i = new EntryIterator();
        s.defaultWriteObject();
        while (n-- != 0) {
            final Entry e = i.nextEntry();
            s.writeByte(e.key);
            s.writeShort(e.value);
        }
    }
    
    private Entry readTree(final ObjectInputStream s, final int n, final Entry pred, final Entry succ) throws IOException, ClassNotFoundException {
        if (n == 1) {
            final Entry top = new Entry(s.readByte(), s.readShort());
            top.pred(pred);
            top.succ(succ);
            return top;
        }
        if (n == 2) {
            final Entry top = new Entry(s.readByte(), s.readShort());
            top.right(new Entry(s.readByte(), s.readShort()));
            top.right.pred(top);
            top.balance(1);
            top.pred(pred);
            top.right.succ(succ);
            return top;
        }
        final int rightN = n / 2;
        final int leftN = n - rightN - 1;
        final Entry top2 = new Entry();
        top2.left(this.readTree(s, leftN, pred, top2));
        top2.key = s.readByte();
        top2.value = s.readShort();
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
            Entry e;
            for (e = this.tree; e.left() != null; e = e.left()) {}
            this.firstEntry = e;
            for (e = this.tree; e.right() != null; e = e.right()) {}
            this.lastEntry = e;
        }
    }
    
    private static int checkTree(final Entry e) {
        return 0;
    }
    
    private static final class Entry implements Cloneable, Byte2ShortMap.Entry
    {
        private static final int SUCC_MASK = Integer.MIN_VALUE;
        private static final int PRED_MASK = 1073741824;
        private static final int BALANCE_MASK = 255;
        byte key;
        short value;
        Entry left;
        Entry right;
        int info;
        
        Entry() {
        }
        
        Entry(final byte k, final short v) {
            this.key = k;
            this.value = v;
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
        
        @Deprecated
        @Override
        public Byte getKey() {
            return this.key;
        }
        
        @Override
        public byte getByteKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Short getValue() {
            return this.value;
        }
        
        @Override
        public short getShortValue() {
            return this.value;
        }
        
        @Override
        public short setValue(final short value) {
            final short oldValue = this.value;
            this.value = value;
            return oldValue;
        }
        
        @Override
        public Short setValue(final Short value) {
            return this.setValue((short)value);
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
            c.value = this.value;
            c.info = this.info;
            return c;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Byte, Short> e = (Map.Entry<Byte, Short>)o;
            return this.key == e.getKey() && this.value == e.getValue();
        }
        
        @Override
        public int hashCode() {
            return this.key ^ this.value;
        }
        
        @Override
        public String toString() {
            return this.key + "=>" + this.value;
        }
    }
    
    private class TreeIterator
    {
        Entry prev;
        Entry next;
        Entry curr;
        int index;
        
        TreeIterator() {
            this.index = 0;
            this.next = Byte2ShortAVLTreeMap.this.firstEntry;
        }
        
        TreeIterator(final byte k) {
            this.index = 0;
            final Entry locateKey = Byte2ShortAVLTreeMap.this.locateKey(k);
            this.next = locateKey;
            if (locateKey != null) {
                if (Byte2ShortAVLTreeMap.this.compare(this.next.key, k) <= 0) {
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
            final Entry curr = this.curr;
            this.prev = curr;
            this.next = curr;
            this.updatePrevious();
            this.updateNext();
            Byte2ShortAVLTreeMap.this.remove(this.curr.key);
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
    
    private class EntryIterator extends TreeIterator implements ObjectListIterator<Byte2ShortMap.Entry>
    {
        EntryIterator() {
        }
        
        EntryIterator(final byte k) {
            super(k);
        }
        
        @Override
        public Byte2ShortMap.Entry next() {
            return this.nextEntry();
        }
        
        @Override
        public Byte2ShortMap.Entry previous() {
            return this.previousEntry();
        }
        
        @Override
        public void set(final Byte2ShortMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final Byte2ShortMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
    }
    
    private final class KeyIterator extends TreeIterator implements ByteListIterator
    {
        public KeyIterator() {
        }
        
        public KeyIterator(final byte k) {
            super(k);
        }
        
        @Override
        public byte nextByte() {
            return this.nextEntry().key;
        }
        
        @Override
        public byte previousByte() {
            return this.previousEntry().key;
        }
        
        @Override
        public void set(final byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Byte next() {
            return this.nextEntry().key;
        }
        
        @Override
        public Byte previous() {
            return this.previousEntry().key;
        }
        
        @Override
        public void set(final Byte ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final Byte ok) {
            throw new UnsupportedOperationException();
        }
    }
    
    private class KeySet extends AbstractByte2ShortSortedMap.KeySet
    {
        @Override
        public ByteBidirectionalIterator iterator() {
            return new KeyIterator();
        }
        
        @Override
        public ByteBidirectionalIterator iterator(final byte from) {
            return new KeyIterator(from);
        }
    }
    
    private final class ValueIterator extends TreeIterator implements ShortListIterator
    {
        @Override
        public short nextShort() {
            return this.nextEntry().value;
        }
        
        @Override
        public short previousShort() {
            return this.previousEntry().value;
        }
        
        @Override
        public void set(final short v) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final short v) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Short next() {
            return this.nextEntry().value;
        }
        
        @Override
        public Short previous() {
            return this.previousEntry().value;
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
    
    private final class Submap extends AbstractByte2ShortSortedMap implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        byte from;
        byte to;
        boolean bottom;
        boolean top;
        protected transient ObjectSortedSet<Byte2ShortMap.Entry> entries;
        protected transient ByteSortedSet keys;
        protected transient ShortCollection values;
        final /* synthetic */ Byte2ShortAVLTreeMap this$0;
        
        public Submap(final byte from, final boolean bottom, final byte to, final boolean top) {
            if (!bottom && !top && Byte2ShortAVLTreeMap.this.compare(from, to) > 0) {
                throw new IllegalArgumentException("Start key (" + from + ") is larger than end key (" + to + ")");
            }
            this.from = from;
            this.bottom = bottom;
            this.to = to;
            this.top = top;
            this.defRetValue = Byte2ShortAVLTreeMap.this.defRetValue;
        }
        
        @Override
        public void clear() {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                i.nextEntry();
                i.remove();
            }
        }
        
        final boolean in(final byte k) {
            return (this.bottom || Byte2ShortAVLTreeMap.this.compare(k, this.from) >= 0) && (this.top || Byte2ShortAVLTreeMap.this.compare(k, this.to) < 0);
        }
        
        @Override
        public ObjectSortedSet<Byte2ShortMap.Entry> byte2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = new AbstractObjectSortedSet<Byte2ShortMap.Entry>() {
                    @Override
                    public ObjectBidirectionalIterator<Byte2ShortMap.Entry> iterator() {
                        return new SubmapEntryIterator();
                    }
                    
                    @Override
                    public ObjectBidirectionalIterator<Byte2ShortMap.Entry> iterator(final Byte2ShortMap.Entry from) {
                        return new SubmapEntryIterator(from.getByteKey());
                    }
                    
                    @Override
                    public Comparator<? super Byte2ShortMap.Entry> comparator() {
                        return Byte2ShortAVLTreeMap.this.entrySet().comparator();
                    }
                    
                    @Override
                    public boolean contains(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
                        if (e.getKey() == null || !(e.getKey() instanceof Byte)) {
                            return false;
                        }
                        if (e.getValue() == null || !(e.getValue() instanceof Short)) {
                            return false;
                        }
                        final Byte2ShortAVLTreeMap.Entry f = Byte2ShortAVLTreeMap.this.findKey((byte)e.getKey());
                        return f != null && Submap.this.in(f.key) && e.equals(f);
                    }
                    
                    @Override
                    public boolean remove(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
                        if (e.getKey() == null || !(e.getKey() instanceof Byte)) {
                            return false;
                        }
                        if (e.getValue() == null || !(e.getValue() instanceof Short)) {
                            return false;
                        }
                        final Byte2ShortAVLTreeMap.Entry f = Byte2ShortAVLTreeMap.this.findKey((byte)e.getKey());
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
                    public Byte2ShortMap.Entry first() {
                        return Submap.this.firstEntry();
                    }
                    
                    @Override
                    public Byte2ShortMap.Entry last() {
                        return Submap.this.lastEntry();
                    }
                    
                    @Override
                    public ObjectSortedSet<Byte2ShortMap.Entry> subSet(final Byte2ShortMap.Entry from, final Byte2ShortMap.Entry to) {
                        return Submap.this.subMap(from.getByteKey(), to.getByteKey()).byte2ShortEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Byte2ShortMap.Entry> headSet(final Byte2ShortMap.Entry to) {
                        return Submap.this.headMap(to.getByteKey()).byte2ShortEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Byte2ShortMap.Entry> tailSet(final Byte2ShortMap.Entry from) {
                        return Submap.this.tailMap(from.getByteKey()).byte2ShortEntrySet();
                    }
                };
            }
            return this.entries;
        }
        
        @Override
        public ByteSortedSet keySet() {
            if (this.keys == null) {
                this.keys = new KeySet();
            }
            return this.keys;
        }
        
        @Override
        public ShortCollection values() {
            if (this.values == null) {
                this.values = new AbstractShortCollection() {
                    @Override
                    public ShortIterator iterator() {
                        return new SubmapValueIterator();
                    }
                    
                    @Override
                    public boolean contains(final short k) {
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
        public boolean containsKey(final byte k) {
            return this.in(k) && Byte2ShortAVLTreeMap.this.containsKey(k);
        }
        
        @Override
        public boolean containsValue(final short v) {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                final short ev = i.nextEntry().value;
                if (ev == v) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public short get(final byte k) {
            final byte kk = k;
            final Byte2ShortAVLTreeMap.Entry e;
            return (this.in(kk) && (e = Byte2ShortAVLTreeMap.this.findKey(kk)) != null) ? e.value : this.defRetValue;
        }
        
        @Override
        public short put(final byte k, final short v) {
            Byte2ShortAVLTreeMap.this.modified = false;
            if (!this.in(k)) {
                throw new IllegalArgumentException("Key (" + k + ") out of range [" + (this.bottom ? "-" : String.valueOf(this.from)) + ", " + (this.top ? "-" : String.valueOf(this.to)) + ")");
            }
            final short oldValue = Byte2ShortAVLTreeMap.this.put(k, v);
            return Byte2ShortAVLTreeMap.this.modified ? this.defRetValue : oldValue;
        }
        
        @Deprecated
        @Override
        public Short put(final Byte ok, final Short ov) {
            final short oldValue = this.put((byte)ok, (short)ov);
            return Byte2ShortAVLTreeMap.this.modified ? null : Short.valueOf(oldValue);
        }
        
        @Override
        public short remove(final byte k) {
            Byte2ShortAVLTreeMap.this.modified = false;
            if (!this.in(k)) {
                return this.defRetValue;
            }
            final short oldValue = Byte2ShortAVLTreeMap.this.remove(k);
            return Byte2ShortAVLTreeMap.this.modified ? oldValue : this.defRetValue;
        }
        
        @Deprecated
        @Override
        public Short remove(final Object ok) {
            final short oldValue = this.remove((byte)ok);
            return Byte2ShortAVLTreeMap.this.modified ? Short.valueOf(oldValue) : null;
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
        public ByteComparator comparator() {
            return Byte2ShortAVLTreeMap.this.actualComparator;
        }
        
        @Override
        public Byte2ShortSortedMap headMap(final byte to) {
            if (this.top) {
                return new Submap(this.from, this.bottom, to, false);
            }
            return (Byte2ShortAVLTreeMap.this.compare(to, this.to) < 0) ? new Submap(this.from, this.bottom, to, false) : this;
        }
        
        @Override
        public Byte2ShortSortedMap tailMap(final byte from) {
            if (this.bottom) {
                return new Submap(from, false, this.to, this.top);
            }
            return (Byte2ShortAVLTreeMap.this.compare(from, this.from) > 0) ? new Submap(from, false, this.to, this.top) : this;
        }
        
        @Override
        public Byte2ShortSortedMap subMap(byte from, byte to) {
            if (this.top && this.bottom) {
                return new Submap(from, false, to, false);
            }
            if (!this.top) {
                to = ((Byte2ShortAVLTreeMap.this.compare(to, this.to) < 0) ? to : this.to);
            }
            if (!this.bottom) {
                from = ((Byte2ShortAVLTreeMap.this.compare(from, this.from) > 0) ? from : this.from);
            }
            if (!this.top && !this.bottom && from == this.from && to == this.to) {
                return this;
            }
            return new Submap(from, false, to, false);
        }
        
        public Byte2ShortAVLTreeMap.Entry firstEntry() {
            if (Byte2ShortAVLTreeMap.this.tree == null) {
                return null;
            }
            Byte2ShortAVLTreeMap.Entry e;
            if (this.bottom) {
                e = Byte2ShortAVLTreeMap.this.firstEntry;
            }
            else {
                e = Byte2ShortAVLTreeMap.this.locateKey(this.from);
                if (Byte2ShortAVLTreeMap.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || (!this.top && Byte2ShortAVLTreeMap.this.compare(e.key, this.to) >= 0)) {
                return null;
            }
            return e;
        }
        
        public Byte2ShortAVLTreeMap.Entry lastEntry() {
            if (Byte2ShortAVLTreeMap.this.tree == null) {
                return null;
            }
            Byte2ShortAVLTreeMap.Entry e;
            if (this.top) {
                e = Byte2ShortAVLTreeMap.this.lastEntry;
            }
            else {
                e = Byte2ShortAVLTreeMap.this.locateKey(this.to);
                if (Byte2ShortAVLTreeMap.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || (!this.bottom && Byte2ShortAVLTreeMap.this.compare(e.key, this.from) < 0)) {
                return null;
            }
            return e;
        }
        
        @Override
        public byte firstByteKey() {
            final Byte2ShortAVLTreeMap.Entry e = this.firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        @Override
        public byte lastByteKey() {
            final Byte2ShortAVLTreeMap.Entry e = this.lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        @Deprecated
        @Override
        public Byte firstKey() {
            final Byte2ShortAVLTreeMap.Entry e = this.firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.getKey();
        }
        
        @Deprecated
        @Override
        public Byte lastKey() {
            final Byte2ShortAVLTreeMap.Entry e = this.lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.getKey();
        }
        
        private class KeySet extends AbstractByte2ShortSortedMap.KeySet
        {
            @Override
            public ByteBidirectionalIterator iterator() {
                return new SubmapKeyIterator();
            }
            
            @Override
            public ByteBidirectionalIterator iterator(final byte from) {
                return new SubmapKeyIterator(from);
            }
        }
        
        private class SubmapIterator extends TreeIterator
        {
            SubmapIterator() {
                Submap.this.this$0.super();
                this.next = Submap.this.firstEntry();
            }
            
            SubmapIterator(final Submap submap, final byte k) {
                this(submap);
                if (this.next != null) {
                    if (!submap.bottom && submap.this$0.compare(k, this.next.key) < 0) {
                        this.prev = null;
                    }
                    else {
                        if (!submap.top) {
                            final Byte2ShortAVLTreeMap this$0 = submap.this$0;
                            final Byte2ShortAVLTreeMap.Entry lastEntry = submap.lastEntry();
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
                if (!Submap.this.bottom && this.prev != null && Byte2ShortAVLTreeMap.this.compare(this.prev.key, Submap.this.from) < 0) {
                    this.prev = null;
                }
            }
            
            @Override
            void updateNext() {
                this.next = this.next.next();
                if (!Submap.this.top && this.next != null && Byte2ShortAVLTreeMap.this.compare(this.next.key, Submap.this.to) >= 0) {
                    this.next = null;
                }
            }
        }
        
        private class SubmapEntryIterator extends SubmapIterator implements ObjectListIterator<Byte2ShortMap.Entry>
        {
            SubmapEntryIterator() {
            }
            
            SubmapEntryIterator(final byte k) {
                super(k);
            }
            
            @Override
            public Byte2ShortMap.Entry next() {
                return this.nextEntry();
            }
            
            @Override
            public Byte2ShortMap.Entry previous() {
                return this.previousEntry();
            }
            
            @Override
            public void set(final Byte2ShortMap.Entry ok) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void add(final Byte2ShortMap.Entry ok) {
                throw new UnsupportedOperationException();
            }
        }
        
        private final class SubmapKeyIterator extends SubmapIterator implements ByteListIterator
        {
            public SubmapKeyIterator() {
            }
            
            public SubmapKeyIterator(final byte from) {
                super(from);
            }
            
            @Override
            public byte nextByte() {
                return this.nextEntry().key;
            }
            
            @Override
            public byte previousByte() {
                return this.previousEntry().key;
            }
            
            @Override
            public void set(final byte k) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void add(final byte k) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public Byte next() {
                return this.nextEntry().key;
            }
            
            @Override
            public Byte previous() {
                return this.previousEntry().key;
            }
            
            @Override
            public void set(final Byte ok) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void add(final Byte ok) {
                throw new UnsupportedOperationException();
            }
        }
        
        private final class SubmapValueIterator extends SubmapIterator implements ShortListIterator
        {
            @Override
            public short nextShort() {
                return this.nextEntry().value;
            }
            
            @Override
            public short previousShort() {
                return this.previousEntry().value;
            }
            
            @Override
            public void set(final short v) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void add(final short v) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public Short next() {
                return this.nextEntry().value;
            }
            
            @Override
            public Short previous() {
                return this.previousEntry().value;
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
    }
}
