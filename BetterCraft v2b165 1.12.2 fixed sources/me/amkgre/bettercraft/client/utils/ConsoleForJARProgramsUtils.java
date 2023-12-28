// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import java.awt.Component;
import java.util.EventListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import net.minecraft.client.Minecraft;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

public class ConsoleForJARProgramsUtils implements KeyListener, ActionListener
{
    Dimension screenSize;
    int screenWidth;
    int screenHeight;
    String title;
    String text;
    public JFrame jf;
    public JTextArea jta;
    JScrollPane jsp;
    JMenuBar jmb;
    JMenu jm;
    JMenuItem jmi;
    int initialCaretPosition;
    int currentCaretPosition;
    boolean inputAvailable;
    int BACKSPACE;
    int ENTER;
    int PG_UP;
    int PG_DN;
    int END;
    int HOME;
    int LEFT_ARROW;
    int UP_ARROW;
    int DOWN_ARROW;
    int CTRL;
    int A;
    int H;
    private static final Minecraft mc;
    
    static {
        mc = Minecraft.getMinecraft();
    }
    
    public ConsoleForJARProgramsUtils() {
        this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.screenWidth = this.screenSize.width;
        this.screenHeight = this.screenSize.height;
        this.title = null;
        this.text = null;
        this.jf = null;
        this.jta = null;
        this.jsp = null;
        this.jmb = null;
        this.jm = null;
        this.jmi = null;
        this.initialCaretPosition = 0;
        this.currentCaretPosition = 0;
        this.inputAvailable = false;
        this.BACKSPACE = 8;
        this.ENTER = 10;
        this.PG_UP = 33;
        this.PG_DN = 34;
        this.END = 35;
        this.HOME = 36;
        this.LEFT_ARROW = 37;
        this.UP_ARROW = 38;
        this.DOWN_ARROW = 40;
        this.CTRL = 128;
        this.A = 65;
        this.H = 72;
    }
    
    @Override
    public void actionPerformed(final ActionEvent ae) {
        final int cCurrPos = this.jta.getCaretPosition();
        this.jta.selectAll();
        this.jta.copy();
        this.jta.select(cCurrPos, cCurrPos);
    }
    
    @Override
    public void keyTyped(final KeyEvent ke) {
    }
    
    @Override
    public void keyReleased(final KeyEvent ke) {
    }
    
    @Override
    public void keyPressed(final KeyEvent ke) {
        final int keyCode = ke.getKeyCode();
        if (keyCode == this.PG_UP || keyCode == this.PG_DN || keyCode == this.UP_ARROW || keyCode == this.DOWN_ARROW || (keyCode == this.A && ke.getModifiersEx() == this.CTRL)) {
            ke.consume();
        }
        else {
            if (keyCode == this.LEFT_ARROW || keyCode == this.BACKSPACE || (keyCode == this.H && ke.getModifiersEx() == this.CTRL)) {
                final ConsoleForJARProgramsUtils consoleForJARPrograms = this;
                synchronized (consoleForJARPrograms) {
                    if (this.jta.getCaretPosition() <= this.initialCaretPosition) {
                        ke.consume();
                    }
                    monitorexit(consoleForJARPrograms);
                }
            }
            if (keyCode == this.HOME) {
                final ConsoleForJARProgramsUtils consoleForJARPrograms = this;
                synchronized (consoleForJARPrograms) {
                    this.jta.setCaretPosition(this.initialCaretPosition);
                    ke.consume();
                    monitorexit(consoleForJARPrograms);
                }
            }
            if (keyCode == this.END) {
                final ConsoleForJARProgramsUtils consoleForJARPrograms = this;
                synchronized (consoleForJARPrograms) {
                    this.jta.setCaretPosition(this.jta.getDocument().getLength());
                    ke.consume();
                    monitorexit(consoleForJARPrograms);
                }
            }
            if (keyCode == this.ENTER) {
                this.jta.setCaretPosition(this.jta.getDocument().getLength());
                final ConsoleForJARProgramsUtils consoleForJARPrograms = this;
                synchronized (consoleForJARPrograms) {
                    this.currentCaretPosition = this.jta.getCaretPosition();
                    try {
                        final String charAtInitialCaretPosition = this.jta.getText(this.initialCaretPosition, 1);
                        if (charAtInitialCaretPosition.equals("\n")) {
                            ++this.initialCaretPosition;
                        }
                    }
                    catch (final Exception ex) {}
                    if (this.currentCaretPosition - this.initialCaretPosition > 0) {
                        this.inputAvailable = true;
                        this.notifyAll();
                    }
                    monitorexit(consoleForJARPrograms);
                }
            }
        }
    }
    
    String getInputFromJTextArea(final JTextArea jta) {
        int len = 0;
        String inputFromUser = "";
        while (true) {
            final ConsoleForJARProgramsUtils consoleForJARPrograms = this;
            synchronized (consoleForJARPrograms) {
                if (this.inputAvailable) {
                    len = this.currentCaretPosition - this.initialCaretPosition;
                    try {
                        inputFromUser = jta.getText(this.initialCaretPosition, len);
                        this.initialCaretPosition = this.currentCaretPosition;
                    }
                    catch (final Exception e) {
                        final String s;
                        inputFromUser = (s = "");
                        monitorexit(consoleForJARPrograms);
                        return s;
                    }
                    this.inputAvailable = false;
                    final String s2 = inputFromUser;
                    monitorexit(consoleForJARPrograms);
                    return s2;
                }
                try {
                    this.wait();
                }
                catch (final Exception ex) {}
                monitorexit(consoleForJARPrograms);
            }
        }
    }
    
    public void outputToJTextArea(final JTextArea jta, final String text) {
        jta.append(text);
        jta.setCaretPosition(jta.getDocument().getLength());
        final ConsoleForJARProgramsUtils consoleForJARPrograms = this;
        synchronized (consoleForJARPrograms) {
            this.initialCaretPosition = jta.getCaretPosition();
            monitorexit(consoleForJARPrograms);
        }
    }
    
    public void begin() {
        new Thread(() -> {
            while (true) {
                this.outputToJTextArea(this.jta, "\n");
                final String input = this.getInputFromJTextArea(this.jta);
                if (ConsoleForJARProgramsUtils.mc.player == null || ConsoleForJARProgramsUtils.mc.world == null) {
                    this.outputToJTextArea(this.jta, "You have to be ingame for executing commands!");
                }
                this.outputToJTextArea(this.jta, "\n");
            }
        }).start();
    }
    
    void configureJTextAreaForInputOutput(final JTextArea jta) {
        jta.addKeyListener(this);
        MouseListener[] mouseListeners;
        for (int length = (mouseListeners = jta.getMouseListeners()).length, i = 0; i < length; ++i) {
            final MouseListener mouseListener = mouseListeners[i];
            jta.removeMouseListener(mouseListener);
        }
        MouseMotionListener[] mouseMotionListeners;
        for (int length2 = (mouseMotionListeners = jta.getMouseMotionListeners()).length, j = 0; j < length2; ++j) {
            final EventListener eventListener = mouseMotionListeners[j];
            jta.removeMouseMotionListener((MouseMotionListener)eventListener);
        }
        MouseWheelListener[] mouseWheelListeners;
        for (int length3 = (mouseWheelListeners = jta.getMouseWheelListeners()).length, k = 0; k < length3; ++k) {
            final EventListener eventListener = mouseWheelListeners[k];
            jta.removeMouseWheelListener((MouseWheelListener)eventListener);
        }
    }
    
    public void createAndShowGUI() {
        this.title = "Console";
        this.jf = JFrameUtils.setupJFrameAndGet(this.title, this.screenWidth - 150, this.screenHeight - 100);
        this.configureJTextAreaForInputOutput(this.jta = JFrameUtils.setupJTextAreaAndGet("", 1000, 100, true, true, true, false, 0, 0, 0, 0));
        (this.jsp = JFrameUtils.setupScrollableJTextAreaAndGet(this.jta, 10, 10, this.screenWidth - 180, this.screenHeight - 180)).setHorizontalScrollBarPolicy(30);
        this.jsp.setVerticalScrollBarPolicy(22);
        this.jf.add(this.jsp);
        this.jmb = JFrameUtils.setupJMenuBarAndGet();
        this.jf.setJMenuBar(this.jmb);
        this.jf.setVisible(false);
    }
}
