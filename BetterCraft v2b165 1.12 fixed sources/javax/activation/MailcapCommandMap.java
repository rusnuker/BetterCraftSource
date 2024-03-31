// 
// Decompiled by Procyon v0.6.0
// 

package javax.activation;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.FileReader;
import java.util.Enumeration;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.net.URL;
import java.util.Map;

public class MailcapCommandMap extends CommandMap
{
    private final Map mimeTypes;
    private final Map preferredCommands;
    private final Map allCommands;
    private final Map nativeCommands;
    private final Map fallbackCommands;
    private URL url;
    
    public MailcapCommandMap() {
        this.mimeTypes = new HashMap();
        this.preferredCommands = new HashMap();
        this.allCommands = new HashMap();
        this.nativeCommands = new HashMap();
        this.fallbackCommands = new HashMap();
        final ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        try {
            final InputStream is = MailcapCommandMap.class.getResourceAsStream("/META-INF/mailcap.default");
            if (is != null) {
                try {
                    this.parseMailcap(is);
                }
                finally {
                    is.close();
                }
                is.close();
            }
        }
        catch (final IOException ex) {}
        try {
            final Enumeration e = contextLoader.getResources("META-INF/mailcap");
            while (e.hasMoreElements()) {
                this.url = e.nextElement();
                try {
                    final InputStream is2 = this.url.openStream();
                    try {
                        this.parseMailcap(is2);
                    }
                    finally {
                        is2.close();
                    }
                    is2.close();
                }
                catch (final IOException ex2) {}
            }
        }
        catch (final SecurityException ex3) {}
        catch (final IOException ex4) {}
        try {
            final File file = new File(System.getProperty("java.home"), "lib/mailcap");
            final InputStream is2 = new FileInputStream(file);
            try {
                this.parseMailcap(is2);
            }
            finally {
                is2.close();
            }
            is2.close();
        }
        catch (final SecurityException ex5) {}
        catch (final IOException ex6) {}
        try {
            final File file = new File(System.getProperty("user.home"), ".mailcap");
            final InputStream is2 = new FileInputStream(file);
            try {
                this.parseMailcap(is2);
            }
            finally {
                is2.close();
            }
            is2.close();
        }
        catch (final SecurityException ex7) {}
        catch (final IOException ex8) {}
    }
    
    public MailcapCommandMap(final String fileName) throws IOException {
        this();
        final FileReader reader = new FileReader(fileName);
        try {
            this.parseMailcap(reader);
        }
        finally {
            reader.close();
        }
        reader.close();
    }
    
    public MailcapCommandMap(final InputStream is) {
        this();
        this.parseMailcap(is);
    }
    
    private void parseMailcap(final InputStream is) {
        try {
            this.parseMailcap(new InputStreamReader(is));
        }
        catch (final IOException ex) {}
    }
    
    void parseMailcap(final Reader reader) throws IOException {
        final BufferedReader br = new BufferedReader(reader);
        String line;
        while ((line = br.readLine()) != null) {
            this.addMailcap(line);
        }
    }
    
    public synchronized void addMailcap(final String mail_cap) {
        int index = 0;
        index = this.skipSpace(mail_cap, index);
        if (index == mail_cap.length() || mail_cap.charAt(index) == '#') {
            return;
        }
        int start = index;
        index = this.getToken(mail_cap, index);
        if (start == index) {
            return;
        }
        String mimeType = mail_cap.substring(start, index);
        index = this.skipSpace(mail_cap, index);
        if (index == mail_cap.length() || mail_cap.charAt(index) == '#') {
            return;
        }
        if (mail_cap.charAt(index) == '/') {
            index = (start = this.skipSpace(mail_cap, ++index));
            index = this.getToken(mail_cap, index);
            mimeType = String.valueOf(mimeType) + '/' + mail_cap.substring(start, index);
        }
        else {
            mimeType = String.valueOf(mimeType) + "/*";
        }
        mimeType = mimeType.toLowerCase();
        index = this.skipSpace(mail_cap, index);
        if (index == mail_cap.length() || mail_cap.charAt(index) != ';') {
            return;
        }
        index = this.skipSpace(mail_cap, index + 1);
        if (index == mail_cap.length() || mail_cap.charAt(index) != ';') {
            ArrayList nativeCommandList = this.nativeCommands.get(mimeType);
            if (nativeCommandList == null) {
                nativeCommandList = new ArrayList();
                this.nativeCommands.put(mimeType, nativeCommandList);
            }
            nativeCommandList.add(mail_cap);
            index = this.getMText(mail_cap, index);
        }
        final List commandList = new ArrayList();
        boolean fallback = false;
        final int fieldNumber = 0;
        while (index < mail_cap.length() && mail_cap.charAt(index) == ';') {
            index = (start = this.skipSpace(mail_cap, index + 1));
            index = this.getToken(mail_cap, index);
            final String fieldName = mail_cap.substring(start, index).toLowerCase();
            index = this.skipSpace(mail_cap, index);
            if (index < mail_cap.length() && mail_cap.charAt(index) == '=') {
                index = (start = this.skipSpace(mail_cap, index + 1));
                index = this.getMText(mail_cap, index);
                String value = mail_cap.substring(start, index);
                index = this.skipSpace(mail_cap, index);
                if (!fieldName.startsWith("x-java-") || fieldName.length() <= 7) {
                    continue;
                }
                final String command = fieldName.substring(7);
                value = value.trim();
                if (command.equals("fallback-entry")) {
                    if (!value.equals("true")) {
                        continue;
                    }
                    fallback = true;
                }
                else {
                    final CommandInfo info = new CommandInfo(command, value);
                    commandList.add(info);
                }
            }
        }
        this.addCommands(mimeType, commandList, fallback);
    }
    
    private void addCommands(final String mimeType, final List commands, final boolean fallback) {
        this.mimeTypes.put(mimeType, mimeType);
        final Map target = fallback ? this.fallbackCommands : this.preferredCommands;
        for (final CommandInfo info : commands) {
            this.addCommand(target, mimeType, info);
            if (!fallback) {
                List cmdList = this.allCommands.get(mimeType);
                if (cmdList == null) {
                    cmdList = new ArrayList();
                    this.allCommands.put(mimeType, cmdList);
                }
                cmdList.add(info);
            }
        }
    }
    
    private void addCommand(final Map commandList, final String mimeType, final CommandInfo command) {
        Map commands = commandList.get(mimeType);
        if (commands == null) {
            commands = new HashMap();
            commandList.put(mimeType, commands);
        }
        commands.put(command.getCommandName(), command);
    }
    
    private int skipSpace(final String s, int index) {
        while (index < s.length() && Character.isWhitespace(s.charAt(index))) {
            ++index;
        }
        return index;
    }
    
    private int getToken(final String s, int index) {
        while (index < s.length() && s.charAt(index) != '#' && !MimeType.isSpecial(s.charAt(index))) {
            ++index;
        }
        return index;
    }
    
    private int getMText(final String s, int index) {
        while (index < s.length()) {
            final char c = s.charAt(index);
            if (c == '#' || c == ';' || Character.isISOControl(c)) {
                return index;
            }
            if (c == '\\' && ++index == s.length()) {
                return index;
            }
            ++index;
        }
        return index;
    }
    
    @Override
    public synchronized CommandInfo[] getPreferredCommands(String mimeType) {
        mimeType = mimeType.toLowerCase();
        Map commands = this.preferredCommands.get(mimeType);
        if (commands == null) {
            commands = this.preferredCommands.get(this.getWildcardMimeType(mimeType));
        }
        final Map fallbackCommands = this.getFallbackCommands(mimeType);
        if (fallbackCommands != null) {
            if (commands == null) {
                commands = fallbackCommands;
            }
            else {
                commands = this.mergeCommandMaps(commands, fallbackCommands);
            }
        }
        if (commands == null) {
            return new CommandInfo[0];
        }
        return (CommandInfo[])commands.values().toArray(new CommandInfo[commands.size()]);
    }
    
    private Map getFallbackCommands(final String mimeType) {
        final Map commands = this.fallbackCommands.get(mimeType);
        final Map wildcardCommands = this.fallbackCommands.get(this.getWildcardMimeType(mimeType));
        if (wildcardCommands == null) {
            return commands;
        }
        return this.mergeCommandMaps(commands, wildcardCommands);
    }
    
    private Map mergeCommandMaps(final Map main, final Map fallback) {
        final Map result = new HashMap(fallback);
        result.putAll(main);
        return result;
    }
    
    @Override
    public synchronized CommandInfo[] getAllCommands(String mimeType) {
        mimeType = mimeType.toLowerCase();
        List exactCommands = this.allCommands.get(mimeType);
        if (exactCommands == null) {
            exactCommands = Collections.EMPTY_LIST;
        }
        List wildCommands = this.allCommands.get(this.getWildcardMimeType(mimeType));
        if (wildCommands == null) {
            wildCommands = Collections.EMPTY_LIST;
        }
        Map fallbackCommands = this.getFallbackCommands(mimeType);
        if (fallbackCommands == null) {
            fallbackCommands = Collections.EMPTY_MAP;
        }
        final CommandInfo[] result = new CommandInfo[exactCommands.size() + wildCommands.size() + fallbackCommands.size()];
        int j = 0;
        for (int i = 0; i < exactCommands.size(); ++i) {
            result[j++] = exactCommands.get(i);
        }
        for (int i = 0; i < wildCommands.size(); ++i) {
            result[j++] = wildCommands.get(i);
        }
        final Iterator k = fallbackCommands.keySet().iterator();
        while (k.hasNext()) {
            result[j++] = fallbackCommands.get(k.next());
        }
        return result;
    }
    
    @Override
    public synchronized CommandInfo getCommand(String mimeType, final String cmdName) {
        mimeType = mimeType.toLowerCase();
        final int i = mimeType.indexOf(59);
        if (i != -1) {
            mimeType = mimeType.substring(0, i).trim();
        }
        Map commands = this.preferredCommands.get(mimeType);
        if (commands == null) {
            commands = this.preferredCommands.get(this.getWildcardMimeType(mimeType));
            if (commands == null) {
                commands = this.fallbackCommands.get(mimeType);
                if (commands == null) {
                    commands = this.fallbackCommands.get(this.getWildcardMimeType(mimeType));
                }
                if (commands == null) {
                    return null;
                }
            }
        }
        return commands.get(cmdName.toLowerCase());
    }
    
    private String getWildcardMimeType(final String mimeType) {
        final int i = mimeType.indexOf(47);
        if (i == -1) {
            return String.valueOf(mimeType) + "/*";
        }
        return String.valueOf(mimeType.substring(0, i + 1)) + "*";
    }
    
    @Override
    public synchronized DataContentHandler createDataContentHandler(final String mimeType) {
        final CommandInfo info = this.getCommand(mimeType, "content-handler");
        if (info == null) {
            return null;
        }
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = this.getClass().getClassLoader();
        }
        try {
            return (DataContentHandler)cl.loadClass(info.getCommandClass()).newInstance();
        }
        catch (final ClassNotFoundException e) {
            return null;
        }
        catch (final IllegalAccessException e2) {
            return null;
        }
        catch (final InstantiationException e3) {
            return null;
        }
    }
    
    @Override
    public synchronized String[] getMimeTypes() {
        final ArrayList types = new ArrayList(this.mimeTypes.values());
        return types.toArray(new String[types.size()]);
    }
    
    public synchronized String[] getNativeCommands(final String mimeType) {
        final ArrayList commands = this.nativeCommands.get(mimeType.toLowerCase());
        if (commands == null) {
            return new String[0];
        }
        return commands.toArray(new String[commands.size()]);
    }
}
