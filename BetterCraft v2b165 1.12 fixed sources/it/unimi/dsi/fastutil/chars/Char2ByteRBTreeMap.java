// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.bytes.ByteListIterator;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import java.util.SortedSet;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.Map;
import java.util.Comparator;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.io.Serializable;

public class Char2ByteRBTreeMap extends AbstractChar2ByteSortedMap implements Serializable, Cloneable
{
    protected transient Entry tree;
    protected int count;
    protected transient Entry firstEntry;
    protected transient Entry lastEntry;
    protected transient ObjectSortedSet<Char2ByteMap.Entry> entries;
    protected transient CharSortedSet keys;
    protected transient ByteCollection values;
    protected transient boolean modified;
    protected Comparator<? super Character> storedComparator;
    protected transient CharComparator actualComparator;
    private static final long serialVersionUID = -7046029254386353129L;
    private static final boolean ASSERTS = false;
    private transient boolean[] dirPath;
    private transient Entry[] nodePath;
    
    public Char2ByteRBTreeMap() {
        this.allocatePaths();
        this.tree = null;
        this.count = 0;
    }
    
    private void setActualComparator() {
        if (this.storedComparator == null || this.storedComparator instanceof CharComparator) {
            this.actualComparator = (CharComparator)this.storedComparator;
        }
        else {
            this.actualComparator = new CharComparator() {
                @Override
                public int compare(final char k1, final char k2) {
                    return Char2ByteRBTreeMap.this.storedComparator.compare(k1, k2);
                }
                
                @Override
                public int compare(final Character ok1, final Character ok2) {
                    return Char2ByteRBTreeMap.this.storedComparator.compare(ok1, ok2);
                }
            };
        }
    }
    
    public Char2ByteRBTreeMap(final Comparator<? super Character> c) {
        this();
        this.storedComparator = c;
        this.setActualComparator();
    }
    
    public Char2ByteRBTreeMap(final Map<? extends Character, ? extends Byte> m) {
        this();
        this.putAll(m);
    }
    
    public Char2ByteRBTreeMap(final SortedMap<Character, Byte> m) {
        this(m.comparator());
        this.putAll(m);
    }
    
    public Char2ByteRBTreeMap(final Char2ByteMap m) {
        this();
        this.putAll(m);
    }
    
    public Char2ByteRBTreeMap(final Char2ByteSortedMap m) {
        this(m.comparator());
        this.putAll(m);
    }
    
    public Char2ByteRBTreeMap(final char[] k, final byte[] v, final Comparator<? super Character> c) {
        this(c);
        if (k.length != v.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Char2ByteRBTreeMap(final char[] k, final byte[] v) {
        this(k, v, null);
    }
    
    final int compare(final char k1, final char k2) {
        return (this.actualComparator == null) ? Character.compare(k1, k2) : this.actualComparator.compare(k1, k2);
    }
    
    final Entry findKey(final char k) {
        Entry e;
        int cmp;
        for (e = this.tree; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {}
        return e;
    }
    
    final Entry locateKey(final char k) {
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
    
    public byte addTo(final char k, final byte incr) {
        final Entry e = this.add(k);
        final byte oldValue = e.value;
        final Entry entry = e;
        entry.value += incr;
        return oldValue;
    }
    
    @Override
    public byte put(final char k, final byte v) {
        final Entry e = this.add(k);
        final byte oldValue = e.value;
        e.value = v;
        return oldValue;
    }
    
    private Entry add(final char k) {
        this.modified = false;
        int maxDepth = 0;
        Entry e = null;
        Label_0908: {
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
                        e = new Entry(k, this.defRetValue);
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
                        e = new Entry(k, this.defRetValue);
                        if (p.left == null) {
                            this.firstEntry = e;
                        }
                        e.right = p;
                        e.left = p.left;
                        p.left(e);
                    }
                    this.modified = true;
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
                    break Label_0908;
                }
                while (i-- != 0) {
                    this.nodePath[i] = null;
                }
                return p;
            }
            ++this.count;
            final Entry tree = new Entry(k, this.defRetValue);
            this.firstEntry = tree;
            this.lastEntry = tree;
            this.tree = tree;
            e = tree;
        }
        this.tree.black(true);
        while (maxDepth-- != 0) {
            this.nodePath[maxDepth] = null;
        }
        return e;
    }
    
    @Override
    public byte remove(final char k) {
        this.modified = false;
        if (this.tree == null) {
            return this.defRetValue;
        }
        Entry p = this.tree;
        int i = 0;
        final char kk = k;
        int cmp;
        while ((cmp = this.compare(kk, p.key)) != 0) {
            this.dirPath[i] = (cmp > 0);
            this.nodePath[i] = p;
            if (this.dirPath[i++]) {
                if ((p = p.right()) == null) {
                    while (i-- != 0) {
                        this.nodePath[i] = null;
                    }
                    return this.defRetValue;
                }
                continue;
            }
            else {
                if ((p = p.left()) == null) {
                    while (i-- != 0) {
                        this.nodePath[i] = null;
                    }
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
        this.modified = true;
        --this.count;
        while (maxDepth-- != 0) {
            this.nodePath[maxDepth] = null;
        }
        return p.value;
    }
    
    @Deprecated
    @Override
    public Byte put(final Character ok, final Byte ov) {
        final byte oldValue = this.put((char)ok, (byte)ov);
        return this.modified ? null : Byte.valueOf(oldValue);
    }
    
    @Deprecated
    @Override
    public Byte remove(final Object ok) {
        final byte oldValue = this.remove((char)ok);
        return this.modified ? Byte.valueOf(oldValue) : null;
    }
    
    @Override
    public boolean containsValue(final byte v) {
        final ValueIterator i = new ValueIterator();
        int j = this.count;
        while (j-- != 0) {
            final byte ev = i.nextByte();
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
    public boolean containsKey(final char k) {
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
    public byte get(final char k) {
        final Entry e = this.findKey(k);
        return (e == null) ? this.defRetValue : e.value;
    }
    
    @Override
    public char firstCharKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }
    
    @Override
    public char lastCharKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }
    
    @Override
    public ObjectSortedSet<Char2ByteMap.Entry> char2ByteEntrySet() {
        if (this.entries == null) {
            this.entries = new AbstractObjectSortedSet<Char2ByteMap.Entry>() {
                final Comparator<? super Char2ByteMap.Entry> comparator = new Comparator<Char2ByteMap.Entry>() {
                    @Override
                    public int compare(final Char2ByteMap.Entry x, final Char2ByteMap.Entry y) {
                        return Char2ByteRBTreeMap.this.actualComparator.compare(x.getCharKey(), y.getCharKey());
                    }
                };
                
                @Override
                public Comparator<? super Char2ByteMap.Entry> comparator() {
                    return this.comparator;
                }
                
                @Override
                public ObjectBidirectionalIterator<Char2ByteMap.Entry> iterator() {
                    return new EntryIterator();
                }
                
                @Override
                public ObjectBidirectionalIterator<Char2ByteMap.Entry> iterator(final Char2ByteMap.Entry from) {
                    return new EntryIterator(from.getCharKey());
                }
                
                @Override
                public boolean contains(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
                    if (e.getKey() == null || !(e.getKey() instanceof Character)) {
                        return false;
                    }
                    if (e.getValue() == null || !(e.getValue() instanceof Byte)) {
                        return false;
                    }
                    final Entry f = Char2ByteRBTreeMap.this.findKey((char)e.getKey());
                    return e.equals(f);
                }
                
                @Override
                public boolean remove(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
                    if (e.getKey() == null || !(e.getKey() instanceof Character)) {
                        return false;
                    }
                    if (e.getValue() == null || !(e.getValue() instanceof Byte)) {
                        return false;
                    }
                    final Entry f = Char2ByteRBTreeMap.this.findKey((char)e.getKey());
                    if (f != null) {
                        Char2ByteRBTreeMap.this.remove(f.key);
                    }
                    return f != null;
                }
                
                @Override
                public int size() {
                    return Char2ByteRBTreeMap.this.count;
                }
                
                @Override
                public void clear() {
                    Char2ByteRBTreeMap.this.clear();
                }
                
                @Override
                public Char2ByteMap.Entry first() {
                    return Char2ByteRBTreeMap.this.firstEntry;
                }
                
                @Override
                public Char2ByteMap.Entry last() {
                    return Char2ByteRBTreeMap.this.lastEntry;
                }
                
                @Override
                public ObjectSortedSet<Char2ByteMap.Entry> subSet(final Char2ByteMap.Entry from, final Char2ByteMap.Entry to) {
                    return Char2ByteRBTreeMap.this.subMap(from.getCharKey(), to.getCharKey()).char2ByteEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Char2ByteMap.Entry> headSet(final Char2ByteMap.Entry to) {
                    return Char2ByteRBTreeMap.this.headMap(to.getCharKey()).char2ByteEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Char2ByteMap.Entry> tailSet(final Char2ByteMap.Entry from) {
                    return Char2ByteRBTreeMap.this.tailMap(from.getCharKey()).char2ByteEntrySet();
                }
            };
        }
        return this.entries;
    }
    
    @Override
    public CharSortedSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public ByteCollection values() {
        if (this.values == null) {
            this.values = new AbstractByteCollection() {
                @Override
                public ByteIterator iterator() {
                    return new ValueIterator();
                }
                
                @Override
                public boolean contains(final byte k) {
                    return Char2ByteRBTreeMap.this.containsValue(k);
                }
                
                @Override
                public int size() {
                    return Char2ByteRBTreeMap.this.count;
                }
                
                @Override
                public void clear() {
                    Char2ByteRBTreeMap.this.clear();
                }
            };
        }
        return this.values;
    }
    
    @Override
    public CharComparator comparator() {
        return this.actualComparator;
    }
    
    @Override
    public Char2ByteSortedMap headMap(final char to) {
        return new Submap('\0', true, to, false);
    }
    
    @Override
    public Char2ByteSortedMap tailMap(final char from) {
        return new Submap(from, false, '\0', true);
    }
    
    @Override
    public Char2ByteSortedMap subMap(final char from, final char to) {
        return new Submap(from, false, to, false);
    }
    
    public Char2ByteRBTreeMap clone() {
        Char2ByteRBTreeMap c;
        try {
            c = (Char2ByteRBTreeMap)super.clone();
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
            s.writeChar(e.key);
            s.writeByte(e.value);
        }
    }
    
    private Entry readTree(final ObjectInputStream s, final int n, final Entry pred, final Entry succ) throws IOException, ClassNotFoundException {
        if (n == 1) {
            final Entry top = new Entry(s.readChar(), s.readByte());
            top.pred(pred);
            top.succ(succ);
            top.black(true);
            return top;
        }
        if (n == 2) {
            final Entry top = new Entry(s.readChar(), s.readByte());
            top.black(true);
            top.right(new Entry(s.readChar(), s.readByte()));
            top.right.pred(top);
            top.pred(pred);
            top.right.succ(succ);
            return top;
        }
        final int rightN = n / 2;
        final int leftN = n - rightN - 1;
        final Entry top2 = new Entry();
        top2.left(this.readTree(s, leftN, pred, top2));
        top2.key = s.readChar();
        top2.value = s.readByte();
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
    
    private static int checkTree(final Entry e, final int d, final int D) {
        return 0;
    }
    
    private static final class Entry implements Cloneable, Char2ByteMap.Entry
    {
        private static final int BLACK_MASK = 1;
        private static final int SUCC_MASK = Integer.MIN_VALUE;
        private static final int PRED_MASK = 1073741824;
        char key;
        byte value;
        Entry left;
        Entry right;
        int info;
        
        Entry() {
        }
        
        Entry(final char k, final byte v) {
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
        
        @Deprecated
        @Override
        public Character getKey() {
            return this.key;
        }
        
        @Override
        public char getCharKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Byte getValue() {
            return this.value;
        }
        
        @Override
        public byte getByteValue() {
            return this.value;
        }
        
        @Override
        public byte setValue(final byte value) {
            final byte oldValue = this.value;
            this.value = value;
            return oldValue;
        }
        
        @Override
        public Byte setValue(final Byte value) {
            return this.setValue((byte)value);
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
            final Map.Entry<Character, Byte> e = (Map.Entry<Character, Byte>)o;
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
            this.next = Char2ByteRBTreeMap.this.firstEntry;
        }
        
        TreeIterator(final char k) {
            this.index = 0;
            final Entry locateKey = Char2ByteRBTreeMap.this.locateKey(k);
            this.next = locateKey;
            if (locateKey != null) {
                if (Char2ByteRBTreeMap.this.compare(this.next.key, k) <= 0) {
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
            Char2ByteRBTreeMap.this.remove(this.curr.key);
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
    
    private class EntryIterator extends TreeIterator implements ObjectListIterator<Char2ByteMap.Entry>
    {
        EntryIterator() {
        }
        
        EntryIterator(final char k) {
            super(k);
        }
        
        @Override
        public Char2ByteMap.Entry next() {
            return this.nextEntry();
        }
        
        @Override
        public Char2ByteMap.Entry previous() {
            return this.previousEntry();
        }
        
        @Override
        public void set(final Char2ByteMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final Char2ByteMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
    }
    
    private final class KeyIterator extends TreeIterator implements CharListIterator
    {
        public KeyIterator() {
        }
        
        public KeyIterator(final char k) {
            super(k);
        }
        
        @Override
        public char nextChar() {
            return this.nextEntry().key;
        }
        
        @Override
        public char previousChar() {
            return this.previousEntry().key;
        }
        
        @Override
        public void set(final char k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final char k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Character next() {
            return this.nextEntry().key;
        }
        
        @Override
        public Character previous() {
            return this.previousEntry().key;
        }
        
        @Override
        public void set(final Character ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final Character ok) {
            throw new UnsupportedOperationException();
        }
    }
    
    private class KeySet extends AbstractChar2ByteSortedMap.KeySet
    {
        @Override
        public CharBidirectionalIterator iterator() {
            return new KeyIterator();
        }
        
        @Override
        public CharBidirectionalIterator iterator(final char from) {
            return new KeyIterator(from);
        }
    }
    
    private final class ValueIterator extends TreeIterator implements ByteListIterator
    {
        @Override
        public byte nextByte() {
            return this.nextEntry().value;
        }
        
        @Override
        public byte previousByte() {
            return this.previousEntry().value;
        }
        
        @Override
        public void set(final byte v) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final byte v) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Byte next() {
            return this.nextEntry().value;
        }
        
        @Override
        public Byte previous() {
            return this.previousEntry().value;
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
    
    private final class Submap extends AbstractChar2ByteSortedMap implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        char from;
        char to;
        boolean bottom;
        boolean top;
        protected transient ObjectSortedSet<Char2ByteMap.Entry> entries;
        protected transient CharSortedSet keys;
        protected transient ByteCollection values;
        final /* synthetic */ Char2ByteRBTreeMap this$0;
        
        public Submap(final char from, final boolean bottom, final char to, final boolean top) {
            if (!bottom && !top && Char2ByteRBTreeMap.this.compare(from, to) > 0) {
                throw new IllegalArgumentException("Start key (" + from + ") is larger than end key (" + to + ")");
            }
            this.from = from;
            this.bottom = bottom;
            this.to = to;
            this.top = top;
            this.defRetValue = Char2ByteRBTreeMap.this.defRetValue;
        }
        
        @Override
        public void clear() {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                i.nextEntry();
                i.remove();
            }
        }
        
        final boolean in(final char k) {
            return (this.bottom || Char2ByteRBTreeMap.this.compare(k, this.from) >= 0) && (this.top || Char2ByteRBTreeMap.this.compare(k, this.to) < 0);
        }
        
        @Override
        public ObjectSortedSet<Char2ByteMap.Entry> char2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = new AbstractObjectSortedSet<Char2ByteMap.Entry>() {
                    @Override
                    public ObjectBidirectionalIterator<Char2ByteMap.Entry> iterator() {
                        return new SubmapEntryIterator();
                    }
                    
                    @Override
                    public ObjectBidirectionalIterator<Char2ByteMap.Entry> iterator(final Char2ByteMap.Entry from) {
                        return new SubmapEntryIterator(from.getCharKey());
                    }
                    
                    @Override
                    public Comparator<? super Char2ByteMap.Entry> comparator() {
                        return Char2ByteRBTreeMap.this.char2ByteEntrySet().comparator();
                    }
                    
                    @Override
                    public boolean contains(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
                        if (e.getKey() == null || !(e.getKey() instanceof Character)) {
                            return false;
                        }
                        if (e.getValue() == null || !(e.getValue() instanceof Byte)) {
                            return false;
                        }
                        final Char2ByteRBTreeMap.Entry f = Char2ByteRBTreeMap.this.findKey((char)e.getKey());
                        return f != null && Submap.this.in(f.key) && e.equals(f);
                    }
                    
                    @Override
                    public boolean remove(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
                        if (e.getKey() == null || !(e.getKey() instanceof Character)) {
                            return false;
                        }
                        if (e.getValue() == null || !(e.getValue() instanceof Byte)) {
                            return false;
                        }
                        final Char2ByteRBTreeMap.Entry f = Char2ByteRBTreeMap.this.findKey((char)e.getKey());
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
                    public Char2ByteMap.Entry first() {
                        return Submap.this.firstEntry();
                    }
                    
                    @Override
                    public Char2ByteMap.Entry last() {
                        return Submap.this.lastEntry();
                    }
                    
                    @Override
                    public ObjectSortedSet<Char2ByteMap.Entry> subSet(final Char2ByteMap.Entry from, final Char2ByteMap.Entry to) {
                        return Submap.this.subMap(from.getCharKey(), to.getCharKey()).char2ByteEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Char2ByteMap.Entry> headSet(final Char2ByteMap.Entry to) {
                        return Submap.this.headMap(to.getCharKey()).char2ByteEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Char2ByteMap.Entry> tailSet(final Char2ByteMap.Entry from) {
                        return Submap.this.tailMap(from.getCharKey()).char2ByteEntrySet();
                    }
                };
            }
            return this.entries;
        }
        
        @Override
        public CharSortedSet keySet() {
            if (this.keys == null) {
                this.keys = new KeySet();
            }
            return this.keys;
        }
        
        @Override
        public ByteCollection values() {
            if (this.values == null) {
                this.values = new AbstractByteCollection() {
                    @Override
                    public ByteIterator iterator() {
                        return new SubmapValueIterator();
                    }
                    
                    @Override
                    public boolean contains(final byte k) {
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
        public boolean containsKey(final char k) {
            return this.in(k) && Char2ByteRBTreeMap.this.containsKey(k);
        }
        
        @Override
        public boolean containsValue(final byte v) {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                final byte ev = i.nextEntry().value;
                if (ev == v) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public byte get(final char k) {
            final char kk = k;
            final Char2ByteRBTreeMap.Entry e;
            return (this.in(kk) && (e = Char2ByteRBTreeMap.this.findKey(kk)) != null) ? e.value : this.defRetValue;
        }
        
        @Override
        public byte put(final char k, final byte v) {
            Char2ByteRBTreeMap.this.modified = false;
            if (!this.in(k)) {
                throw new IllegalArgumentException("Key (" + k + ") out of range [" + (this.bottom ? "-" : String.valueOf(this.from)) + ", " + (this.top ? "-" : String.valueOf(this.to)) + ")");
            }
            final byte oldValue = Char2ByteRBTreeMap.this.put(k, v);
            return Char2ByteRBTreeMap.this.modified ? this.defRetValue : oldValue;
        }
        
        @Deprecated
        @Override
        public Byte put(final Character ok, final Byte ov) {
            final byte oldValue = this.put((char)ok, (byte)ov);
            return Char2ByteRBTreeMap.this.modified ? null : Byte.valueOf(oldValue);
        }
        
        @Override
        public byte remove(final char k) {
            Char2ByteRBTreeMap.this.modified = false;
            if (!this.in(k)) {
                return this.defRetValue;
            }
            final byte oldValue = Char2ByteRBTreeMap.this.remove(k);
            return Char2ByteRBTreeMap.this.modified ? oldValue : this.defRetValue;
        }
        
        @Deprecated
        @Override
        public Byte remove(final Object ok) {
            final byte oldValue = this.remove((char)ok);
            return Char2ByteRBTreeMap.this.modified ? Byte.valueOf(oldValue) : null;
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
        public CharComparator comparator() {
            return Char2ByteRBTreeMap.this.actualComparator;
        }
        
        @Override
        public Char2ByteSortedMap headMap(final char to) {
            if (this.top) {
                return new Submap(this.from, this.bottom, to, false);
            }
            return (Char2ByteRBTreeMap.this.compare(to, this.to) < 0) ? new Submap(this.from, this.bottom, to, false) : this;
        }
        
        @Override
        public Char2ByteSortedMap tailMap(final char from) {
            if (this.bottom) {
                return new Submap(from, false, this.to, this.top);
            }
            return (Char2ByteRBTreeMap.this.compare(from, this.from) > 0) ? new Submap(from, false, this.to, this.top) : this;
        }
        
        @Override
        public Char2ByteSortedMap subMap(char from, char to) {
            if (this.top && this.bottom) {
                return new Submap(from, false, to, false);
            }
            if (!this.top) {
                to = ((Char2ByteRBTreeMap.this.compare(to, this.to) < 0) ? to : this.to);
            }
            if (!this.bottom) {
                from = ((Char2ByteRBTreeMap.this.compare(from, this.from) > 0) ? from : this.from);
            }
            if (!this.top && !this.bottom && from == this.from && to == this.to) {
                return this;
            }
            return new Submap(from, false, to, false);
        }
        
        public Char2ByteRBTreeMap.Entry firstEntry() {
            if (Char2ByteRBTreeMap.this.tree == null) {
                return null;
            }
            Char2ByteRBTreeMap.Entry e;
            if (this.bottom) {
                e = Char2ByteRBTreeMap.this.firstEntry;
            }
            else {
                e = Char2ByteRBTreeMap.this.locateKey(this.from);
                if (Char2ByteRBTreeMap.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || (!this.top && Char2ByteRBTreeMap.this.compare(e.key, this.to) >= 0)) {
                return null;
            }
            return e;
        }
        
        public Char2ByteRBTreeMap.Entry lastEntry() {
            if (Char2ByteRBTreeMap.this.tree == null) {
                return null;
            }
            Char2ByteRBTreeMap.Entry e;
            if (this.top) {
                e = Char2ByteRBTreeMap.this.lastEntry;
            }
            else {
                e = Char2ByteRBTreeMap.this.locateKey(this.to);
                if (Char2ByteRBTreeMap.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || (!this.bottom && Char2ByteRBTreeMap.this.compare(e.key, this.from) < 0)) {
                return null;
            }
            return e;
        }
        
        @Override
        public char firstCharKey() {
            final Char2ByteRBTreeMap.Entry e = this.firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        @Override
        public char lastCharKey() {
            final Char2ByteRBTreeMap.Entry e = this.lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        @Deprecated
        @Override
        public Character firstKey() {
            final Char2ByteRBTreeMap.Entry e = this.firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.getKey();
        }
        
        @Deprecated
        @Override
        public Character lastKey() {
            final Char2ByteRBTreeMap.Entry e = this.lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.getKey();
        }
        
        private class KeySet extends AbstractChar2ByteSortedMap.KeySet
        {
            @Override
            public CharBidirectionalIterator iterator() {
                return new SubmapKeyIterator();
            }
            
            @Override
            public CharBidirectionalIterator iterator(final char from) {
                return new SubmapKeyIterator(from);
            }
        }
        
        private class SubmapIterator extends TreeIterator
        {
            SubmapIterator() {
                Submap.this.this$0.super();
                this.next = Submap.this.firstEntry();
            }
            
            SubmapIterator(final Submap submap, final char k) {
                this(submap);
                if (this.next != null) {
                    if (!submap.bottom && submap.this$0.compare(k, this.next.key) < 0) {
                        this.prev = null;
                    }
                    else {
                        if (!submap.top) {
                            final Char2ByteRBTreeMap this$0 = submap.this$0;
                            final Char2ByteRBTreeMap.Entry lastEntry = submap.lastEntry();
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
                if (!Submap.this.bottom && this.prev != null && Char2ByteRBTreeMap.this.compare(this.prev.key, Submap.this.from) < 0) {
                    this.prev = null;
                }
            }
            
            @Override
            void updateNext() {
                this.next = this.next.next();
                if (!Submap.this.top && this.next != null && Char2ByteRBTreeMap.this.compare(this.next.key, Submap.this.to) >= 0) {
                    this.next = null;
                }
            }
        }
        
        private class SubmapEntryIterator extends SubmapIterator implements ObjectListIterator<Char2ByteMap.Entry>
        {
            SubmapEntryIterator() {
            }
            
            SubmapEntryIterator(final char k) {
                super(k);
            }
            
            @Override
            public Char2ByteMap.Entry next() {
                return this.nextEntry();
            }
            
            @Override
            public Char2ByteMap.Entry previous() {
                return this.previousEntry();
            }
            
            @Override
            public void set(final Char2ByteMap.Entry ok) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void add(final Char2ByteMap.Entry ok) {
                throw new UnsupportedOperationException();
            }
        }
        
        private final class SubmapKeyIterator extends SubmapIterator implements CharListIterator
        {
            public SubmapKeyIterator() {
            }
            
            public SubmapKeyIterator(final char from) {
                super(from);
            }
            
            @Override
            public char nextChar() {
                return this.nextEntry().key;
            }
            
            @Override
            public char previousChar() {
                return this.previousEntry().key;
            }
            
            @Override
            public void set(final char k) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void add(final char k) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public Character next() {
                return this.nextEntry().key;
            }
            
            @Override
            public Character previous() {
                return this.previousEntry().key;
            }
            
            @Override
            public void set(final Character ok) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void add(final Character ok) {
                throw new UnsupportedOperationException();
            }
        }
        
        private final class SubmapValueIterator extends SubmapIterator implements ByteListIterator
        {
            @Override
            public byte nextByte() {
                return this.nextEntry().value;
            }
            
            @Override
            public byte previousByte() {
                return this.previousEntry().value;
            }
            
            @Override
            public void set(final byte v) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void add(final byte v) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public Byte next() {
                return this.nextEntry().value;
            }
            
            @Override
            public Byte previous() {
                return this.previousEntry().value;
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
    }
}
