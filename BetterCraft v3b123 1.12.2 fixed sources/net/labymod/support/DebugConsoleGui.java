// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.support;

import javax.swing.text.EditorKit;
import javax.swing.text.Document;
import java.io.StringReader;
import net.labymod.user.User;
import java.util.Iterator;
import net.labymod.main.Source;
import net.labymod.core.asm.LabyModCoreMod;
import net.labymod.utils.ModUtils;
import net.labymod.user.cosmetic.util.CosmeticData;
import java.util.Date;
import net.labymod.api.LabyModAddon;
import net.labymod.addon.AddonLoader;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import javax.swing.JScrollPane;
import javax.swing.text.Highlighter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import java.awt.Color;
import javax.swing.text.DefaultHighlighter;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.Font;
import net.minecraft.client.Minecraft;
import java.io.IOException;
import net.labymod.support.util.Debug;
import java.awt.BorderLayout;
import net.labymod.support.util.Hastebin;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import java.awt.LayoutManager;
import javax.swing.BoxLayout;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import net.labymod.main.LabyMod;
import java.awt.Toolkit;
import java.awt.Component;
import javax.swing.UIManager;
import net.labymod.utils.Consumer;
import javax.swing.JTextPane;
import javax.swing.JFrame;

public class DebugConsoleGui extends JFrame
{
    private final JTextPane textPane;
    private long idle;
    
    public DebugConsoleGui(final Consumer<Boolean> consumer) {
        this.textPane = new JTextPane();
        this.idle = 0L;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (final Exception e1) {
            e1.printStackTrace();
        }
        this.setSize(900, 520);
        this.setLocationRelativeTo(null);
        this.setTitle("LabyMod Debug Console");
        try {
            this.setIconImage(Toolkit.getDefaultToolkit().getImage(LabyMod.class.getResource("/assets/minecraft/labymod/textures/labymod_logo.png")));
        }
        catch (final Exception error) {
            error.printStackTrace();
        }
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                consumer.accept(true);
                e.getWindow().dispose();
            }
        });
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), 1));
        final JPanel controlPanel = new JPanel();
        this.getContentPane().add(controlPanel);
        controlPanel.setLayout(new FlowLayout(2, 5, 5));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        final JButton uploadButton = new JButton("Upload");
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final String log = DebugConsoleGui.this.createLog();
                Hastebin.upload(log);
            }
        });
        controlPanel.add(uploadButton);
        final JPanel guiPanel = new JPanel();
        controlPanel.add(guiPanel);
        guiPanel.setLayout(new BorderLayout(0, 0));
        final JButton restartDebugButton = new JButton("Restart in debug");
        restartDebugButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    Debug.DEBUG_FILE.createNewFile();
                }
                catch (final IOException e2) {
                    e2.printStackTrace();
                }
                Minecraft.getMinecraft().shutdown();
            }
        });
        guiPanel.add(restartDebugButton);
        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0));
        this.textPane.setFont(new Font("Courier New", 0, 15));
        this.textPane.setEditable(false);
        this.textPane.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(final CaretEvent evt) {
                if (evt.getDot() == evt.getMark()) {
                    return;
                }
                final JTextPane txtPane = (JTextPane)evt.getSource();
                final DefaultHighlighter highlighter = (DefaultHighlighter)txtPane.getHighlighter();
                highlighter.removeAllHighlights();
                final DefaultHighlighter.DefaultHighlightPainter hPainter = new DefaultHighlighter.DefaultHighlightPainter(new Color(16755200));
                final String selText = txtPane.getSelectedText();
                if (selText.isEmpty() || selText.equals("\n")) {
                    return;
                }
                String contText = "";
                final DefaultStyledDocument document = (DefaultStyledDocument)txtPane.getDocument();
                try {
                    contText = document.getText(0, document.getLength());
                    if (contText.isEmpty() || contText.equals("\n")) {
                        return;
                    }
                }
                catch (final BadLocationException ex) {
                    ex.printStackTrace();
                }
                int index = 0;
                while ((index = contText.indexOf(selText, index)) > -1) {
                    try {
                        highlighter.addHighlight(index, selText.length() + index, hPainter);
                        index += selText.length();
                    }
                    catch (final BadLocationException ex2) {
                        ex2.printStackTrace();
                    }
                }
            }
        });
        final JScrollPane scrollPane = new JScrollPane(this.textPane);
        panel.add(scrollPane);
        this.getContentPane().add(panel);
        this.setVisible(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Debug.log(Debug.EnumDebugMode.GENERAL, "Debug console started");
                try {
                    final BufferedReader br = new BufferedReader(new FileReader(new File("logs/latest.log")));
                    while (DebugConsoleGui.this.isVisible()) {
                        DebugConsoleGui.this.readLogFile(br);
                        if (DebugConsoleGui.this.idle < System.currentTimeMillis()) {
                            try {
                                Thread.sleep(2000L);
                            }
                            catch (final InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                catch (final Exception e2) {
                    e2.printStackTrace();
                }
                Debug.log(Debug.EnumDebugMode.GENERAL, "Debug console closed");
            }
        }).start();
    }
    
    private String createLog() {
        String log = this.textPane.getText();
        final long maxMemory = Runtime.getRuntime().maxMemory();
        final long totalMemory = Runtime.getRuntime().totalMemory();
        final long freeMemory = Runtime.getRuntime().freeMemory();
        final long usedMemory = totalMemory - freeMemory;
        final long percent = usedMemory * 100L / maxMemory;
        String addons = "";
        for (final LabyModAddon addon : AddonLoader.getAddons()) {
            if (addon != null && addon.about != null) {
                addons = String.valueOf(addons) + addon.about.name + ", ";
            }
        }
        final User user = LabyMod.getInstance().getUserManager().getUser(LabyMod.getInstance().getPlayerUUID());
        log = String.valueOf(log) + "\n---------------------------------------";
        log = String.valueOf(log) + "\nTime: " + new Date().toString();
        log = String.valueOf(log) + "\nUsername: " + LabyMod.getInstance().getPlayerName();
        log = String.valueOf(log) + "\nUUID: " + user.getUuid().toString();
        log = String.valueOf(log) + "\nGroup: " + ((user.getGroup() == null) ? "NONE" : user.getGroup().getName());
        log = String.valueOf(log) + "\nWhitelist: " + LabyMod.getInstance().getUserManager().isWhitelisted(user.getUuid());
        log = String.valueOf(log) + "\nCosmetics: " + user.getCosmetics().size();
        for (final CosmeticData cos : user.getCosmetics().values()) {
            log = String.valueOf(log) + "\n- " + cos.getClass().getSimpleName();
        }
        log = String.valueOf(log) + "\nMemory: " + (int)percent + "% (" + ModUtils.humanReadableByteCount(maxMemory, true, true) + ")";
        log = String.valueOf(log) + "\nAddons: [" + AddonLoader.getAddons().size() + "] " + addons;
        log = String.valueOf(log) + "\nForge: " + LabyModCoreMod.isForge();
        log = String.valueOf(log) + "\nVersion: " + Source.getUserAgent();
        log = String.valueOf(log) + "\n---------------------------------------";
        return log;
    }
    
    private void readLogFile(final BufferedReader br) {
        try {
            boolean changed = false;
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("Session ID is token")) {
                    continue;
                }
                this.addString(String.valueOf(line) + "\n");
                changed = true;
            }
            if (changed) {
                this.textPane.repaint();
                this.idle = System.currentTimeMillis() + 5000L;
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    private void addString(final String string) {
        try {
            final Document doc = this.textPane.getDocument();
            final StringReader sr = new StringReader(string);
            final EditorKit ek = this.textPane.getEditorKit();
            ek.read(sr, doc, this.textPane.getDocument().getLength());
            this.textPane.setCaretPosition(doc.getLength());
        }
        catch (final Exception error) {
            error.printStackTrace();
        }
    }
}
