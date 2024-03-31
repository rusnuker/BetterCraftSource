// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.tslogs;

import java.io.Reader;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.StringReader;
import javax.swing.text.html.HTMLEditorKit;
import java.util.Iterator;
import java.io.Closeable;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.Tailer;
import java.io.File;
import me.amkgre.bettercraft.client.mods.teamspeak.impl.ServerInfoImpl;
import org.apache.commons.io.input.TailerListenerAdapter;

public abstract class LogFileParser extends TailerListenerAdapter
{
    protected final ServerInfoImpl serverInfo;
    private final File logFile;
    private Tailer tailer;
    
    public LogFileParser(final ServerInfoImpl serverInfo, final File logFile) {
        this.serverInfo = serverInfo;
        this.logFile = logFile;
        this.readTail();
        this.tailer = new Tailer(logFile, this, 500L, true);
        final Thread thread = new Thread(this.tailer, "Tail " + logFile.getName());
        thread.setDaemon(true);
        thread.start();
    }
    
    private void readTail() {
        ReversedLinesFileReader reader = null;
        try {
            reader = new ReversedLinesFileReader(this.logFile, 4096, Charsets.UTF_8);
            final ArrayList<String> lines = new ArrayList<String>();
            String line2;
            for (int i = 0; i < 100 && (line2 = reader.readLine()) != null; ++i) {
                lines.add(line2);
            }
            Collections.reverse(lines);
            for (final String line3 : lines) {
                this.parse(line3);
            }
        }
        catch (final IOException e) {
            e.printStackTrace();
            return;
        }
        finally {
            IOUtils.closeQuietly(reader);
        }
        IOUtils.closeQuietly(reader);
    }
    
    public void stop() {
        this.tailer.stop();
    }
    
    protected abstract HTMLEditorKit.ParserCallback createParserCallback();
    
    @Override
    public void handle(final String line) {
        this.parse(this.rebuildUTF8String(line));
    }
    
    private void parse(final String line) {
        StringReader reader = null;
        try {
            String replace = line.replace("&nbsp;", " ");
            replace = replace.replace("&apos;", "'");
            reader = new StringReader(replace);
            final ParserDelegator parser = new ParserDelegator();
            parser.parse(reader, this.createParserCallback(), true);
        }
        catch (final IOException e) {
            e.printStackTrace();
            return;
        }
        finally {
            IOUtils.closeQuietly(reader);
        }
        IOUtils.closeQuietly(reader);
    }
    
    private String rebuildUTF8String(final String line) {
        final int len = line.length();
        final byte[] bytes = new byte[len];
        for (int i = 0; i < len; ++i) {
            bytes[i] = (byte)line.charAt(i);
        }
        return new String(bytes, Charsets.UTF_8);
    }
}
