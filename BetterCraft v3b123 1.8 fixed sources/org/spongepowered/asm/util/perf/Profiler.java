// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.util.perf;

import java.util.Arrays;
import java.util.HashMap;
import com.google.common.collect.ImmutableList;
import java.lang.reflect.Method;
import org.spongepowered.asm.service.MixinService;
import java.util.Set;
import java.util.LinkedHashSet;
import java.text.DecimalFormat;
import org.spongepowered.asm.util.PrettyPrinter;
import java.util.Collections;
import java.util.Collection;
import java.util.NoSuchElementException;
import com.google.common.base.Joiner;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Deque;
import java.util.List;
import java.util.Map;

public final class Profiler
{
    private static final String METRONOME_AGENT_CLASS = "org.spongepowered.metronome.Agent";
    public static final int ROOT = 1;
    public static final int FINE = 2;
    private static final Map<String, Profiler> profilers;
    private static boolean active;
    private final String id;
    private final Map<String, Section> sections;
    private final List<String> phases;
    private final Deque<Section> stack;
    
    public Profiler(final String id) {
        this.sections = new TreeMap<String, Section>();
        this.phases = new ArrayList<String>();
        this.stack = new LinkedList<Section>();
        this.id = id;
        this.phases.add("Initial");
    }
    
    @Override
    public String toString() {
        return this.id;
    }
    
    public static void setActive(final boolean active) {
        Profiler.active = active;
    }
    
    public synchronized void reset() {
        for (final Section section : this.sections.values()) {
            section.invalidate();
        }
        this.sections.clear();
        this.phases.clear();
        this.phases.add("Initial");
        this.stack.clear();
    }
    
    public synchronized Section get(final String name) {
        Section section = this.sections.get(name);
        if (section == null) {
            section = (Profiler.active ? new LiveSection(name, this.phases.size() - 1) : new DisabledSection(name));
            this.sections.put(name, section);
        }
        return section;
    }
    
    private synchronized Section getSubSection(final String name, final String baseName, final Section root) {
        Section section = this.sections.get(name);
        if (section == null) {
            section = new SubSection(name, this.phases.size() - 1, baseName, root);
            this.sections.put(name, section);
        }
        return section;
    }
    
    boolean isHead(final Section section) {
        return this.stack.peek() == section;
    }
    
    public Section begin(final String... path) {
        return this.begin(0, path);
    }
    
    public Section begin(final int flags, final String... path) {
        return this.begin(flags, Joiner.on('.').join(path));
    }
    
    public Section begin(final String name) {
        return this.begin(0, name);
    }
    
    public synchronized Section begin(final int flags, String name) {
        boolean root = (flags & 0x1) != 0x0;
        final boolean fine = (flags & 0x2) != 0x0;
        String path = name;
        final Section head = this.stack.peek();
        if (head != null) {
            path = head.getName() + (root ? " -> " : ".") + path;
            if (head.isRoot() && !root) {
                final int pos = head.getName().lastIndexOf(" -> ");
                name = ((pos > -1) ? head.getName().substring(pos + 4) : head.getName()) + "." + name;
                root = true;
            }
        }
        Section section = this.get(root ? name : path);
        if (root && head != null && Profiler.active) {
            section = this.getSubSection(path, head.getName(), section);
        }
        section.setFine(fine).setRoot(root);
        this.stack.push(section);
        return section.start();
    }
    
    synchronized void end(final Section section) {
        try {
            Section next;
            final Section head = next = this.stack.pop();
            while (next != section) {
                if (next == null && Profiler.active) {
                    if (head == null) {
                        throw new IllegalStateException("Attempted to pop " + section + " but the stack is empty");
                    }
                    throw new IllegalStateException("Attempted to pop " + section + " which was not in the stack, head was " + head);
                }
                else {
                    next = this.stack.pop();
                }
            }
        }
        catch (final NoSuchElementException ex) {
            if (Profiler.active) {
                throw new IllegalStateException("Attempted to pop " + section + " but the stack is empty");
            }
        }
    }
    
    public synchronized void mark(final String phase) {
        long currentPhaseTime = 0L;
        for (final Section section : this.sections.values()) {
            currentPhaseTime += section.getTime();
        }
        if (currentPhaseTime == 0L) {
            final int size = this.phases.size();
            this.phases.set(size - 1, phase);
            return;
        }
        this.phases.add(phase);
        for (final Section section : this.sections.values()) {
            section.mark();
        }
    }
    
    public synchronized Collection<Section> getSections() {
        return Collections.unmodifiableCollection((Collection<? extends Section>)this.sections.values());
    }
    
    public PrettyPrinter printer(final boolean includeFine, final boolean group) {
        return printer(includeFine, group, this.phases, this.sections);
    }
    
    private static PrettyPrinter printer(final boolean includeFine, final boolean group, final List<String> phases, final Map<String, Section> sections) {
        final PrettyPrinter printer = new PrettyPrinter();
        final int colCount = phases.size() + 4;
        final int[] columns = { 0, 1, 2, colCount - 2, colCount - 1 };
        final Object[] headers = new Object[colCount * 2];
        int col = 0;
        int pos = 0;
        while (col < colCount) {
            headers[pos + 1] = PrettyPrinter.Alignment.RIGHT;
            if (col == columns[0]) {
                headers[pos] = (group ? "" : "  ") + "Section";
                headers[pos + 1] = PrettyPrinter.Alignment.LEFT;
            }
            else if (col == columns[1]) {
                headers[pos] = "    TOTAL";
            }
            else if (col == columns[3]) {
                headers[pos] = "    Count";
            }
            else if (col == columns[4]) {
                headers[pos] = "Avg. ";
            }
            else if (col - columns[2] < phases.size()) {
                headers[pos] = phases.get(col - columns[2]);
            }
            else {
                headers[pos] = "";
            }
            pos = ++col * 2;
        }
        printer.table(headers).th().hr().add();
        for (final Section section : sections.values()) {
            if (!section.isFine() || includeFine) {
                if (group && section.getDelegate() != section) {
                    continue;
                }
                printSectionRow(printer, colCount, columns, section, group);
                if (!group) {
                    continue;
                }
                for (final Section subSection : sections.values()) {
                    final Section delegate = subSection.getDelegate();
                    if ((!subSection.isFine() || includeFine) && delegate == section) {
                        if (delegate == subSection) {
                            continue;
                        }
                        printSectionRow(printer, colCount, columns, subSection, group);
                    }
                }
            }
        }
        return printer.add();
    }
    
    private static void printSectionRow(final PrettyPrinter printer, final int colCount, final int[] columns, final Section section, final boolean group) {
        final boolean isDelegate = section.getDelegate() != section;
        final Object[] values = new Object[colCount];
        int col = 1;
        if (group) {
            values[0] = (isDelegate ? ("  > " + section.getBaseName()) : section.getName());
        }
        else {
            values[0] = (isDelegate ? "+ " : "  ") + section.getName();
        }
        final long[] times2;
        final long[] times = times2 = section.getTimes();
        for (final long time : times2) {
            if (col == columns[1]) {
                values[col++] = section.getTotalTime() + " ms";
            }
            if (col >= columns[2] && col < values.length) {
                values[col++] = time + " ms";
            }
        }
        values[columns[3]] = section.getTotalCount();
        values[columns[4]] = new DecimalFormat("   ###0.000 ms").format(section.getTotalAverageTime());
        for (int i = 0; i < values.length; ++i) {
            if (values[i] == null) {
                values[i] = "-";
            }
        }
        printer.tr(values);
    }
    
    public void printSummary() {
        printSummary(this.id, this.phases, this.sections);
    }
    
    public static void printAuditSummary() {
        final String id;
        final Set<String> allPhases;
        final Map<String, Section> allSections;
        synchronized (Profiler.profilers) {
            id = Joiner.on(',').join(Profiler.profilers.values());
            allPhases = new LinkedHashSet<String>();
            allSections = new TreeMap<String, Section>() {
                private static final long serialVersionUID = 1L;
                
                @Override
                public Section get(final Object name) {
                    Section section = super.get(name);
                    if (section == null) {
                        ((TreeMap<String, ResultSection>)this).put(name.toString(), section = new ResultSection(name.toString()));
                    }
                    return section;
                }
            };
            for (final Profiler profiler : Profiler.profilers.values()) {
                for (final String phase : profiler.phases) {
                    allPhases.add(phase);
                }
                for (final Map.Entry<String, Section> section : profiler.sections.entrySet()) {
                    allSections.get(section.getKey()).add(section.getValue());
                }
            }
        }
        printSummary(id, new ArrayList<String>(allPhases), allSections);
    }
    
    private static void printSummary(final String id, final List<String> phases, final Map<String, Section> sections) {
        final DecimalFormat threedp = new DecimalFormat("(###0.000");
        final DecimalFormat onedp = new DecimalFormat("(###0.0");
        final PrettyPrinter printer = printer(false, false, phases, sections);
        final long prepareTime = sections.get("mixin.prepare").getTotalTime();
        final long readTime = sections.get("mixin.read").getTotalTime();
        final long applyTime = sections.get("mixin.apply").getTotalTime();
        final long writeTime = sections.get("mixin.write").getTotalTime();
        final long totalMixinTime = sections.get("mixin").getTotalTime();
        final long loadTime = sections.get("class.load").getTotalTime();
        final long transformTime = sections.get("class.transform").getTotalTime();
        final long exportTime = sections.get("mixin.debug.export").getTotalTime();
        final long actualTime = totalMixinTime - loadTime - transformTime - exportTime;
        final double timeSliceMixin = actualTime / (double)totalMixinTime * 100.0;
        final double timeSliceLoad = loadTime / (double)totalMixinTime * 100.0;
        final double timeSliceTransform = transformTime / (double)totalMixinTime * 100.0;
        final double timeSliceExport = exportTime / (double)totalMixinTime * 100.0;
        long worstTransformerTime = 0L;
        Section worstTransformer = null;
        for (final Section section : sections.values()) {
            final long transformerTime = section.getName().startsWith("class.transform.") ? section.getTotalTime() : 0L;
            if (transformerTime > worstTransformerTime) {
                worstTransformerTime = transformerTime;
                worstTransformer = section;
            }
        }
        printer.hr().add("Summary for Profiler[%s]", id).hr().add();
        final String format = "%9d ms %12s seconds)";
        printer.kv("Total mixin time", format, totalMixinTime, threedp.format(totalMixinTime * 0.001)).add();
        printer.kv("Preparing mixins", format, prepareTime, threedp.format(prepareTime * 0.001));
        printer.kv("Reading input", format, readTime, threedp.format(readTime * 0.001));
        printer.kv("Applying mixins", format, applyTime, threedp.format(applyTime * 0.001));
        printer.kv("Writing output", format, writeTime, threedp.format(writeTime * 0.001)).add();
        printer.kv("of which", (Object)"");
        printer.kv("Time spent loading from disk", format, loadTime, threedp.format(loadTime * 0.001));
        printer.kv("Time spent transforming classes", format, transformTime, threedp.format(transformTime * 0.001)).add();
        if (worstTransformer != null) {
            printer.kv("Worst transformer", (Object)worstTransformer.getName());
            printer.kv("Class", (Object)worstTransformer.getInfo());
            printer.kv("Time spent", "%s seconds", worstTransformer.getTotalSeconds());
            printer.kv("called", "%d times", worstTransformer.getTotalCount()).add();
        }
        printer.kv("   Time allocation:     Processing mixins", "%9d ms %10s%% of total)", actualTime, onedp.format(timeSliceMixin));
        printer.kv("Loading classes", "%9d ms %10s%% of total)", loadTime, onedp.format(timeSliceLoad));
        printer.kv("Running transformers", "%9d ms %10s%% of total)", transformTime, onedp.format(timeSliceTransform));
        if (exportTime > 0L) {
            printer.kv("Exporting classes (debug)", "%9d ms %10s%% of total)", exportTime, onedp.format(timeSliceExport));
        }
        printer.add();
        try {
            final Class<?> agent = MixinService.getService().getClassProvider().findAgentClass("org.spongepowered.metronome.Agent", false);
            final Method mdGetTimes = agent.getDeclaredMethod("getTimes", (Class<?>[])new Class[0]);
            final Map<String, Long> times = (Map<String, Long>)mdGetTimes.invoke(null, new Object[0]);
            printer.hr().add("Transformer Times").hr().add();
            int longest = 10;
            for (final Map.Entry<String, Long> entry : times.entrySet()) {
                longest = Math.max(longest, entry.getKey().length());
            }
            for (final Map.Entry<String, Long> entry : times.entrySet()) {
                final String name = entry.getKey();
                long mixinTime = 0L;
                for (final Section section2 : sections.values()) {
                    if (name.equals(section2.getInfo())) {
                        mixinTime = section2.getTotalTime();
                        break;
                    }
                }
                if (mixinTime > 0L) {
                    printer.add("%-" + longest + "s %8s ms %8s ms in mixin)", name, entry.getValue() + mixinTime, "(" + mixinTime);
                }
                else {
                    printer.add("%-" + longest + "s %8s ms", name, entry.getValue());
                }
            }
            printer.add();
        }
        catch (final Throwable t) {}
        printer.print();
    }
    
    public static Profiler getProfiler(final String id) {
        synchronized (Profiler.profilers) {
            Profiler profiler = Profiler.profilers.get(id);
            if (profiler == null) {
                Profiler.profilers.put(id, profiler = new Profiler(id));
            }
            return profiler;
        }
    }
    
    public static Collection<Profiler> getProfilers() {
        final ImmutableList.Builder<Profiler> list = ImmutableList.builder();
        synchronized (Profiler.profilers) {
            list.addAll(Profiler.profilers.values());
        }
        return list.build();
    }
    
    static {
        profilers = new HashMap<String, Profiler>();
    }
    
    public abstract static class Section
    {
        static final String SEPARATOR_ROOT = " -> ";
        static final String SEPARATOR_CHILD = ".";
        private final String name;
        private boolean root;
        private boolean fine;
        protected boolean invalidated;
        private String info;
        
        Section(final String name) {
            this.name = name;
            this.info = name;
        }
        
        protected int getCursor() {
            return 0;
        }
        
        Section getDelegate() {
            return this;
        }
        
        Section invalidate() {
            this.invalidated = true;
            return this;
        }
        
        Section setRoot(final boolean root) {
            this.root = root;
            return this;
        }
        
        public boolean isRoot() {
            return this.root;
        }
        
        Section setFine(final boolean fine) {
            this.fine = fine;
            return this;
        }
        
        public boolean isFine() {
            return this.fine;
        }
        
        public String getName() {
            return this.name;
        }
        
        public String getBaseName() {
            return this.name;
        }
        
        public void setInfo(final String info) {
            this.info = info;
        }
        
        public String getInfo() {
            return this.info;
        }
        
        Section start() {
            return this;
        }
        
        protected Section stop() {
            return this;
        }
        
        public Section end() {
            return this;
        }
        
        public Section next(final String name) {
            this.end();
            return this;
        }
        
        void mark() {
        }
        
        public long getTime() {
            return 0L;
        }
        
        public long getTotalTime() {
            return 0L;
        }
        
        public double getSeconds() {
            return 0.0;
        }
        
        public double getTotalSeconds() {
            return 0.0;
        }
        
        public long[] getTimes() {
            return new long[1];
        }
        
        public int getCount() {
            return 0;
        }
        
        public int getTotalCount() {
            return 0;
        }
        
        public double getAverageTime() {
            return 0.0;
        }
        
        public double getTotalAverageTime() {
            return 0.0;
        }
        
        @Override
        public final String toString() {
            return this.name;
        }
        
        protected long getMarkedTime() {
            return 0L;
        }
        
        protected int getMarkedCount() {
            return 0;
        }
    }
    
    class DisabledSection extends Section
    {
        DisabledSection(final String name) {
            super(name);
        }
        
        @Override
        public Section end() {
            if (!this.invalidated) {
                Profiler.this.end(this);
            }
            return this;
        }
        
        @Override
        public Section next(final String name) {
            this.end();
            return Profiler.this.begin(name);
        }
    }
    
    class LiveSection extends DisabledSection
    {
        private int cursor;
        private long[] times;
        private long start;
        private long time;
        private long markedTime;
        private int count;
        private int markedCount;
        
        LiveSection(final String name, final int cursor) {
            super(name);
            this.cursor = 0;
            this.times = new long[0];
            this.start = 0L;
            this.cursor = cursor;
        }
        
        @Override
        protected int getCursor() {
            return this.cursor;
        }
        
        @Override
        Section start() {
            this.start = System.currentTimeMillis();
            return this;
        }
        
        @Override
        protected Section stop() {
            if (this.start > 0L) {
                this.time += System.currentTimeMillis() - this.start;
            }
            this.start = 0L;
            ++this.count;
            return this;
        }
        
        @Override
        public Section end() {
            this.stop();
            if (!this.invalidated) {
                Profiler.this.end(this);
            }
            return this;
        }
        
        @Override
        void mark() {
            if (this.cursor >= this.times.length) {
                this.times = Arrays.copyOf(this.times, this.cursor + 4);
            }
            this.times[this.cursor] = this.time;
            this.markedTime += this.time;
            this.markedCount += this.count;
            this.time = 0L;
            this.count = 0;
            ++this.cursor;
        }
        
        @Override
        public long getTime() {
            return this.time;
        }
        
        @Override
        public long getTotalTime() {
            return this.time + this.markedTime;
        }
        
        @Override
        public double getSeconds() {
            return this.time * 0.001;
        }
        
        @Override
        public double getTotalSeconds() {
            return (this.time + this.markedTime) * 0.001;
        }
        
        @Override
        public long[] getTimes() {
            final long[] times = new long[this.cursor + 1];
            System.arraycopy(this.times, 0, times, 0, Math.min(this.times.length, this.cursor));
            times[this.cursor] = this.time;
            return times;
        }
        
        @Override
        public int getCount() {
            return this.count;
        }
        
        @Override
        public int getTotalCount() {
            return this.count + this.markedCount;
        }
        
        @Override
        public double getAverageTime() {
            return (this.count > 0) ? (this.time / (double)this.count) : 0.0;
        }
        
        @Override
        public double getTotalAverageTime() {
            return (this.count > 0) ? ((this.time + this.markedTime) / (double)(this.count + this.markedCount)) : 0.0;
        }
        
        @Override
        protected long getMarkedTime() {
            return this.markedTime;
        }
        
        @Override
        protected int getMarkedCount() {
            return this.markedCount;
        }
    }
    
    class SubSection extends LiveSection
    {
        private final String baseName;
        private final Section root;
        
        SubSection(final String name, final int cursor, final String baseName, final Section root) {
            super(name, cursor);
            this.baseName = baseName;
            this.root = root;
        }
        
        @Override
        Section invalidate() {
            this.root.invalidate();
            return super.invalidate();
        }
        
        @Override
        public String getBaseName() {
            return this.baseName;
        }
        
        @Override
        public void setInfo(final String info) {
            this.root.setInfo(info);
            super.setInfo(info);
        }
        
        @Override
        Section getDelegate() {
            return this.root;
        }
        
        @Override
        Section start() {
            this.root.start();
            return super.start();
        }
        
        @Override
        public Section end() {
            this.root.stop();
            return super.end();
        }
        
        @Override
        public Section next(final String name) {
            super.stop();
            return this.root.next(name);
        }
    }
    
    static class ResultSection extends Section
    {
        private List<Section> sections;
        
        ResultSection(final String name) {
            super(name);
            this.sections = new ArrayList<Section>();
        }
        
        void add(final Section section) {
            this.sections.add(section);
        }
        
        @Override
        public long getTime() {
            long time = 0L;
            for (final Section section : this.sections) {
                time += section.getTime();
            }
            return time;
        }
        
        @Override
        public long getTotalTime() {
            long totalTime = 0L;
            for (final Section section : this.sections) {
                totalTime += section.getTotalTime();
            }
            return totalTime;
        }
        
        @Override
        public double getSeconds() {
            double seconds = 0.0;
            for (final Section section : this.sections) {
                seconds += section.getSeconds();
            }
            return seconds;
        }
        
        @Override
        public double getTotalSeconds() {
            double totalSeconds = 0.0;
            for (final Section section : this.sections) {
                totalSeconds += section.getTotalSeconds();
            }
            return totalSeconds;
        }
        
        @Override
        public long[] getTimes() {
            int cursor = 0;
            for (final Section section : this.sections) {
                cursor = Math.max(cursor, section.getCursor());
            }
            final long[] times = new long[cursor + 1];
            for (final Section section2 : this.sections) {
                final long[] sectionTimes = section2.getTimes();
                for (int i = 0; i < sectionTimes.length; ++i) {
                    final long[] array = times;
                    final int n = i;
                    array[n] += sectionTimes[i];
                }
            }
            return times;
        }
        
        @Override
        public int getCount() {
            int count = 0;
            for (final Section section : this.sections) {
                count += section.getCount();
            }
            return count;
        }
        
        @Override
        public int getTotalCount() {
            int totalCount = 0;
            for (final Section section : this.sections) {
                totalCount += section.getTotalCount();
            }
            return totalCount;
        }
        
        @Override
        protected long getMarkedTime() {
            long markedTime = 0L;
            for (final Section section : this.sections) {
                markedTime += section.getMarkedTime();
            }
            return markedTime;
        }
        
        @Override
        protected int getMarkedCount() {
            int markedCount = 0;
            for (final Section section : this.sections) {
                markedCount += section.getMarkedCount();
            }
            return markedCount;
        }
        
        @Override
        public double getAverageTime() {
            final int count = this.getCount();
            return (count > 0) ? (this.getTime() / (double)count) : 0.0;
        }
        
        @Override
        public double getTotalAverageTime() {
            final int count = this.getCount();
            return (count > 0) ? ((this.getTime() + this.getMarkedTime()) / (double)(count + this.getMarkedCount())) : 0.0;
        }
    }
}
