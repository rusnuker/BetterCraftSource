// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.setup;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Iterator;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;

public class CfgParser
{
    private ArrayList<Line> lines;
    private HashMap<String, HashMap<String, PropertyLine>> data;
    private File location;
    
    public CfgParser(final File loc) {
        this.lines = new ArrayList<Line>();
        this.data = new HashMap<String, HashMap<String, PropertyLine>>();
        this.location = loc;
    }
    
    public boolean load() {
        try {
            this.unsafeRead();
            return true;
        }
        catch (final Throwable t) {
            System.err.println("Could not read config file \"" + this.location.getAbsolutePath() + "\":");
            t.printStackTrace();
            return false;
        }
    }
    
    private void unsafeRead() throws Throwable {
        this.lines.clear();
        this.data.clear();
        final BufferedReader br = new BufferedReader(new FileReader(this.location));
        String currentCategory = null;
        int lineCnt = 0;
        String line;
        while ((line = br.readLine()) != null) {
            final String trimmed = line.trim();
            Line l;
            if (trimmed.isEmpty() || trimmed.charAt(0) == '#') {
                l = new CommentLine(line);
            }
            else {
                l = LineType.parseLine(line);
            }
            if (l == null) {
                throw new RuntimeException("Could not parse line #" + (lineCnt + 1) + ".");
            }
            if (l instanceof BeginCategoryLine) {
                if (currentCategory != null) {
                    throw new RuntimeException("At line #" + (lineCnt + 1) + ": Forgot to close brackets.");
                }
                currentCategory = ((BeginCategoryLine)l).getCategoryName();
                this.data.put(currentCategory, new HashMap<String, PropertyLine>());
            }
            else if (l instanceof EndCategoryLine) {
                if (currentCategory == null) {
                    throw new RuntimeException("At line #" + (lineCnt + 1) + ": Closing non-opened bracket.");
                }
                currentCategory = null;
            }
            else if (l instanceof PropertyLine) {
                if (currentCategory == null) {
                    throw new RuntimeException("At line #" + (lineCnt + 1) + ": Setting property outside brackets.");
                }
                this.data.get(currentCategory).put((Object)((PropertyLine)l).getKey(), (Object)l);
            }
            this.lines.add(l);
            ++lineCnt;
        }
        SetupUtil.silentClose(br);
    }
    
    public boolean save() {
        try {
            this.unsafeWrite();
            return true;
        }
        catch (final Throwable t) {
            System.err.println("Could not write config file \"" + this.location.getAbsolutePath() + "\":");
            t.printStackTrace();
            return false;
        }
    }
    
    private void unsafeWrite() throws Throwable {
        final BufferedWriter bw = new BufferedWriter(new FileWriter(this.location));
        for (final Line l : this.lines) {
            l.write(bw);
        }
        SetupUtil.silentClose(bw);
    }
    
    private int findCategoryBeginning(final String cat) {
        for (int i = 0; i < this.lines.size(); ++i) {
            final Line l = this.lines.get(i);
            if (l instanceof BeginCategoryLine && ((BeginCategoryLine)l).getCategoryName().equals(cat)) {
                return i;
            }
        }
        return -1;
    }
    
    private PropertyLine getValue(final char type, final String category, final String key, final String def) {
        if (!this.data.containsKey(category)) {
            final BeginCategoryLine bcl = new BeginCategoryLine(category);
            final PropertyLine pl = new PropertyLine(type, key, def);
            final EndCategoryLine ecl = new EndCategoryLine();
            this.lines.add(bcl);
            this.lines.add(pl);
            this.lines.add(ecl);
            final HashMap<String, PropertyLine> subdata = new HashMap<String, PropertyLine>();
            subdata.put(key, pl);
            this.data.put(category, subdata);
            return pl;
        }
        final HashMap<String, PropertyLine> subdata = this.data.get(category);
        if (subdata.containsKey(key)) {
            return subdata.get(key);
        }
        final int pos = this.findCategoryBeginning(category);
        if (pos < 0) {
            throw new RuntimeException("Could not find beginning for category \"" + category + "\"! This should NOT happen!");
        }
        final PropertyLine pl = new PropertyLine(type, key, def);
        this.lines.add(pos + 1, pl);
        subdata.put(key, pl);
        return pl;
    }
    
    public String getStringValue(final String category, final String key, final String def) {
        return this.getValue('S', category, key, def).getValue();
    }
    
    public boolean getBooleanValue(final String category, final String key, final boolean def) {
        return this.getValue('B', category, key, def ? "true" : "false").getBooleanValue(def);
    }
    
    public void setStringValue(final String category, final String key, final String val) {
        this.getValue('S', category, key, val).setValue(val);
    }
    
    public void setBooleanValue(final String category, final String key, final boolean val) {
        final String data = val ? "true" : "false";
        this.getValue('B', category, key, data).setValue(data);
    }
    
    private enum LineType
    {
        CATEGORY_BEGIN("^(\\s*)([a-z]+)(\\s+)\\{(\\s*)$", (Class<? extends Line>)BeginCategoryLine.class), 
        CATEGORY_END("^(\\s*)\\}(\\s*)$", (Class<? extends Line>)EndCategoryLine.class), 
        PROPERTY("^(\\s*)([A-Z])\\:([A-Za-z]+)=(.*)$", (Class<? extends Line>)PropertyLine.class);
        
        private final Pattern pattern;
        private final Class<? extends Line> cls;
        
        private LineType(final String regex, final Class<? extends Line> cls) {
            this.pattern = Pattern.compile(regex);
            this.cls = cls;
        }
        
        public static Line parseLine(final String l) {
            LineType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final LineType lt = values[i];
                final Matcher m = lt.pattern.matcher(l);
                if (m.matches()) {
                    try {
                        final Line ret = (Line)lt.cls.newInstance();
                        ret.read(l, m);
                        return ret;
                    }
                    catch (final Throwable t) {
                        System.err.println("Could not instantiate line class \"" + lt.cls.getCanonicalName() + "\":");
                        t.printStackTrace();
                        return null;
                    }
                }
            }
            return null;
        }
    }
    
    private abstract static class Line
    {
        public abstract void write(final BufferedWriter p0) throws IOException;
        
        public abstract void read(final String p0, final Matcher p1);
    }
    
    private static class CommentLine extends Line
    {
        private String data;
        
        public CommentLine(final String d) {
            super(null);
            this.data = d;
        }
        
        @Override
        public void write(final BufferedWriter bw) throws IOException {
            bw.write(String.valueOf(this.data) + "\n");
        }
        
        @Override
        public void read(final String content, final Matcher m) {
            this.data = content;
        }
    }
    
    private static class BeginCategoryLine extends Line
    {
        public static final String REGEX = "^(\\s*)([a-z]+)(\\s+)\\{(\\s*)$";
        private String prefix;
        private String category;
        private String inBetween;
        private String suffix;
        
        public BeginCategoryLine() {
            super(null);
        }
        
        public BeginCategoryLine(final String name) {
            super(null);
            this.prefix = "";
            this.category = name;
            this.inBetween = " ";
            this.suffix = "";
        }
        
        @Override
        public void write(final BufferedWriter bw) throws IOException {
            bw.write(this.prefix);
            bw.write(this.category);
            bw.write(this.inBetween);
            bw.write("{");
            bw.write(String.valueOf(this.suffix) + "\n");
        }
        
        @Override
        public void read(final String content, final Matcher m) {
            this.prefix = m.group(1);
            this.category = m.group(2);
            this.inBetween = m.group(3);
            this.suffix = m.group(4);
        }
        
        public String getCategoryName() {
            return this.category;
        }
    }
    
    private static class EndCategoryLine extends Line
    {
        public static final String REGEX = "^(\\s*)\\}(\\s*)$";
        private String prefix;
        private String suffix;
        
        public EndCategoryLine() {
            super(null);
            this.prefix = "";
            this.suffix = "";
        }
        
        @Override
        public void write(final BufferedWriter bw) throws IOException {
            bw.write(this.prefix);
            bw.write("}");
            bw.write(String.valueOf(this.suffix) + "\n");
        }
        
        @Override
        public void read(final String content, final Matcher m) {
            this.prefix = m.group(1);
            this.suffix = m.group(2);
        }
    }
    
    private static class PropertyLine extends Line
    {
        public static final String REGEX = "^(\\s*)([A-Z])\\:([A-Za-z]+)=(.*)$";
        private String prefix;
        private char type;
        private String key;
        private String value;
        
        public PropertyLine() {
            super(null);
        }
        
        public PropertyLine(final char t, final String key, final String val) {
            super(null);
            this.prefix = "    ";
            this.type = t;
            this.key = key;
            this.value = val;
        }
        
        @Override
        public void write(final BufferedWriter bw) throws IOException {
            bw.write(this.prefix);
            bw.write(String.valueOf(this.type) + ":" + this.key);
            bw.write("=" + this.value + "\n");
        }
        
        @Override
        public void read(final String content, final Matcher m) {
            this.prefix = m.group(1);
            this.type = m.group(2).charAt(0);
            this.key = m.group(3);
            this.value = m.group(4);
        }
        
        public String getKey() {
            return this.key;
        }
        
        public String getValue() {
            return this.value;
        }
        
        public boolean getBooleanValue(final boolean def) {
            final String data = this.value.trim().toLowerCase();
            if (data.equals("false")) {
                return false;
            }
            if (data.equals("true")) {
                return true;
            }
            this.value = (def ? "true" : "false");
            return def;
        }
        
        public void setValue(final String v) {
            this.value = v;
        }
    }
}
